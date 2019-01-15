package ir.fallahpoor.enlightened.presentation.newslist.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ir.fallahpoor.enlightened.R
import ir.fallahpoor.enlightened.presentation.newslist.model.NewsModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.recycler_item_news.*

class NewsAdapter() : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    constructor(
        context: Context,
        news: List<NewsModel>,
        clickListener: ((NewsModel) -> Unit)?
    ) : this() {
        newsList.addAll(news)
//        this.context = context
        this.clickListener = clickListener
    }

    private val newsList = ArrayList<NewsModel>()
    private var lastPosition = -1
//    private lateinit var context: Context
    private var clickListener: ((NewsModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        NewsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_news, parent, false)
        )

    override fun getItemCount(): Int = newsList.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news: NewsModel = newsList[position]
        holder.bindData(news)
        holder.itemView.setOnClickListener { clickListener?.invoke(news) }
        animateItemView(holder.itemView, position)
    }

    private fun animateItemView(itemView: View, position: Int) {
        if (itemAlreadyNotAnimated(position)) {
//            val animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
//            itemView.startAnimation(animation)
            lastPosition = position
        }
    }

    private fun itemAlreadyNotAnimated(position: Int) = (position > lastPosition)

    fun addNews(news: List<NewsModel>) {
        newsList.addAll(news)
        notifyItemRangeChanged(itemCount, news.size)
    }

    fun getNews() = newsList

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), LayoutContainer {

        override val containerView: View?
            get() = itemView

        fun bindData(news: NewsModel) {
            newsTitleTextView.text = news.title
            Picasso.get()
                .load(news.urlToImage)
                .placeholder(R.drawable.ic_error_outline_24dp)
                .fit()
                .into(newsImageImageView)
            newsSourceTextView.text = news.source.name
            newsPublishDateTextView.text = news.publishedAt
        }

    }

}