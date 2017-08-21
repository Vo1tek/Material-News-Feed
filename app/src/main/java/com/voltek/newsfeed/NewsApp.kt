package com.voltek.newsfeed

import android.app.Application
import com.orhanobut.hawk.Hawk
import com.voltek.newsfeed.dagger.component.*
import com.voltek.newsfeed.dagger.module.*
import com.voltek.newsfeed.data.network.BASE_URL
import com.voltek.newsfeed.navigation.RouterHolder
import com.voltek.newsfeed.navigation.proxy.NavigatorBinder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import timber.log.Timber
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

class NewsApp : Application() {

    companion object {

        // Const
        private const val FONT_PATH = "fonts/Roboto-Regular.ttf"

        // Navigation
        private val routerHolder: RouterHolder = RouterHolder()

        fun getNavigatorBinder(): NavigatorBinder = routerHolder

        // DI
        lateinit var presenterComponent: PresenterComponent

        lateinit var interactorComponent: InteractorComponent

        lateinit var repositoryComponent: RepositoryComponent

        lateinit var dataComponent: DataComponent
    }

    override fun onCreate() {
        super.onCreate()

        // Dependency injection
        val appModule = AppModule(this)
        val interactorModule = InteractorModule(
                AndroidSchedulers.mainThread(),
                Schedulers.io(),
                Schedulers.computation()
        )
        val networkModule = NetworkModule(BASE_URL)
        val repositoryModule = RepositoryModule()
        val routerModule = RouterModule(routerHolder)
        val platformModule = PlatformModule()
        val storageModule = StorageModule()

        presenterComponent = DaggerPresenterComponent.builder()
                .routerModule(routerModule)
                .interactorModule(interactorModule)
                .build()

        interactorComponent = DaggerInteractorComponent.builder()
                .repositoryModule(repositoryModule)
                .build()

        repositoryComponent = DaggerRepositoryComponent.builder()
                .platformModule(platformModule)
                .storageModule(storageModule)
                .networkModule(networkModule)
                .appModule(appModule)
                .build()

        dataComponent = DaggerDataComponent.builder()
                .appModule(appModule)
                .build()

        // Libs init
        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFontPath(FONT_PATH)
                .setFontAttrId(R.attr.fontPath)
                .build())
        Realm.init(this)
        Hawk.init(this).build()
        Timber.plant(Timber.DebugTree())
    }
}
