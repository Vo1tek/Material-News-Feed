package com.voltek.newsfeed.presentation.ui.list

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import com.jakewharton.rxbinding2.view.RxView
import com.voltek.newsfeed.R
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_article.view.*
import kotlinx.android.synthetic.main.item_title.view.*
import android.view.animation.AnimationUtils
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.target.Target
import com.voltek.newsfeed.presentation.entity.ArticleUI
import java.lang.Exception

class ListAdapter(private val mContext: Context, private var mItems: MutableList<ArticleUI>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TITLE = 0
        private const val ARTICLE = 1
    }

    private var mOnItemClickSubject: PublishSubject<ArticleUI> = PublishSubject.create()

    fun getOnItemClickObservable(): Observable<ArticleUI> = mOnItemClickSubject

    class ViewHolderTitle(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.tv_source_title
    }

    class ViewHolderArticle(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.im_image
        val title: TextView = view.tv_title
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        when (viewType) {
            TITLE -> {
                val view = layoutInflater.inflate(R.layout.item_title, parent, false)
                return ViewHolderTitle(view)
            }
            else -> {
                val view = layoutInflater.inflate(R.layout.item_article, parent, false)
                return ViewHolderArticle(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (holder?.itemViewType) {
            TITLE -> bindTitle(holder as ViewHolderTitle, position)
            ARTICLE -> bindArticle(holder as ViewHolderArticle, position)
        }
    }

    private fun bindTitle(holder: ViewHolderTitle, position: Int) {
        val item = mItems[position]
        holder.title.text = item.source
    }

    private fun bindArticle(holder: ViewHolderArticle, position: Int) {
        val item = mItems[position]

        RxView.clicks(holder.itemView).subscribe({ mOnItemClickSubject.onNext(item) })

        Glide
                .with(mContext)
                .load(item.urlToImage)
                .placeholder(R.drawable.im_news_placeholder)
                .centerCrop()
                .dontAnimate()
                .listener(object : RequestListener<String, GlideDrawable> {
                    override fun onException(
                            e: Exception?,
                            model: String?,
                            target: Target<GlideDrawable>?,
                            isFirstResource: Boolean
                    ): Boolean = false

                    override fun onResourceReady(
                            resource: GlideDrawable?,
                            model: String?,
                            target: Target<GlideDrawable>?,
                            isFromMemoryCache: Boolean,
                            isFirstResource: Boolean
                    ): Boolean {
                        val fadeIn = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in)
                        holder.image.startAnimation(fadeIn)
                        return false
                    }
                })
                .into(holder.image)

        holder.title.text = item.title
    }

    override fun getItemCount(): Int = mItems.size

    override fun getItemViewType(position: Int): Int =
            if (mItems[position].isEmpty()) TITLE else ARTICLE

    fun replace(items: List<ArticleUI>) {
        val currentSize = mItems.size
        val newSize = items.size

        if (currentSize == 0 && newSize > 0) {
            mItems.addAll(items)
            notifyItemRangeInserted(0, newSize)
        } else if (currentSize > 0 && newSize == 0) {
            mItems.clear()
            notifyDataSetChanged()
        } else if (currentSize > 0 && newSize > 0) {
            mItems.clear()
            mItems.addAll(items)
            notifyDataSetChanged()
        }
    }
}
