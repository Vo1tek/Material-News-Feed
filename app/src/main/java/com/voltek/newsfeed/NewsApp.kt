package com.voltek.newsfeed

import android.app.Application
import com.orhanobut.hawk.Hawk
import com.voltek.newsfeed.dagger.*
import com.voltek.newsfeed.data.network.BASE_URL
import com.voltek.newsfeed.presentation.navigation.RouterHolder
import com.voltek.newsfeed.presentation.navigation.proxy.NavigatorBinder
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
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        // Dependency injection
        val appModule = AppModule(this)
        val interactorModule = UseCaseModule(
                AndroidSchedulers.mainThread(),
                Schedulers.io(),
                Schedulers.computation())
        val networkModule = NetworkModule(BASE_URL)
        val routerModule = RouterModule(routerHolder)

        appComponent = DaggerAppComponent.builder()
                .routerModule(routerModule)
                .useCaseModule(interactorModule)
                .networkModule(networkModule)
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
