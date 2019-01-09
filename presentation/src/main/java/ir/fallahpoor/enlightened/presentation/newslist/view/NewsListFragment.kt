package ir.fallahpoor.enlightened.presentation.newslist.view

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import ir.fallahpoor.enlightened.R
import ir.fallahpoor.enlightened.data.PreferencesManager
import ir.fallahpoor.enlightened.presentation.app.App
import ir.fallahpoor.enlightened.presentation.common.EndlessOnScrollListener
import ir.fallahpoor.enlightened.presentation.newslist.di.DaggerNewsListComponent
import ir.fallahpoor.enlightened.presentation.newslist.model.NewsModel
import ir.fallahpoor.enlightened.presentation.newslist.viewmodel.NewsListViewModel
import ir.fallahpoor.enlightened.presentation.newslist.viewmodel.NewsListViewModelFactory
import kotlinx.android.synthetic.main.fragment_news_list.*
import kotlinx.android.synthetic.main.try_again_layout.*
import javax.inject.Inject

class NewsListFragment : Fragment() {

    @Inject
    lateinit var newsListViewModelFactory: NewsListViewModelFactory
    @Inject
    lateinit var preferencesManager: PreferencesManager

    private lateinit var newsListViewModel: NewsListViewModel
    private lateinit var newsAdapter: NewsAdapter
    private var isLoadingMoreNews = false
    private val COUNTRY = "us"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
        return inflater.inflate(R.layout.fragment_news_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        injectViewModel()

        super.onActivityCreated(savedInstanceState)

        setupViewModel()
        subscribeToViewModel()

        getNews(false)

    }

    private fun injectViewModel() {
        DaggerNewsListComponent.builder()
            .appComponent((activity?.application as App).appComponent)
            .build()
            .inject(this)
    }

    private fun setupViewModel() {
        newsListViewModel = ViewModelProviders.of(this, newsListViewModelFactory)
            .get(NewsListViewModel::class.java)
    }

    private fun subscribeToViewModel() {

        newsListViewModel.getLoadingLiveData().observe(
            this,
            Observer { show -> if (show) showLoading() else hideLoading() })

        newsListViewModel.getErrorLiveData().observe(
            this,
            Observer { errorMessage -> showError(errorMessage) }
        )

        newsListViewModel.newsListLiveData.observe(
            this,
            Observer { newsList -> showNews(newsList) })

    }

    private fun getNews(loadMore: Boolean) {
        if (loadMore) {
            isLoadingMoreNews = true
            newsListViewModel.getMoreNews()
        } else {
            isLoadingMoreNews = false
            newsListViewModel.getNews(COUNTRY, getNewsCategory())
        }
    }

    private fun showLoading() {
        tryAgain.visibility = View.GONE
        loading.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loading.visibility = View.INVISIBLE
    }

    private fun showError(errorMessage: String) {
        if (isLoadingMoreNews) {
            showLoadMoreError()
        } else {
            setupTryAgainLayout(errorMessage)
            newsListRecyclerView.visibility = View.GONE
        }
    }

    private fun showLoadMoreError() {
        Toast.makeText(activity, R.string.internet_not_connected, Toast.LENGTH_SHORT).show()
    }

    private fun setupTryAgainLayout(errorMessage: String) {
        errorMessageTextView.text = errorMessage
        tryAgainButton.setOnClickListener {
            getNews(false)
        }
        tryAgain.visibility = View.VISIBLE
    }

    private fun showNews(newsList: List<NewsModel>) {
        tryAgain.visibility = View.GONE
        if (isLoadingMoreNews) {
            newsAdapter.appendNews(newsList)
        } else {
            initializeRecyclerViewWith(newsList)
        }
    }

    private fun initializeRecyclerViewWith(newsList: List<NewsModel>) {

        with(newsListRecyclerView) {
            layoutManager = LinearLayoutManager(activity!!)
            newsAdapter = NewsAdapter(
                activity!!,
                newsList
            ) { news: NewsModel -> openNewsInBrowser(news.url) }
            adapter = newsAdapter
            visibility = View.VISIBLE
            addOnScrollListener(object :
                EndlessOnScrollListener(layoutManager as LinearLayoutManager) {
                override fun onLoadMore() {
                    getNews(true)
                }
            })
        }

    }

    private fun openNewsInBrowser(newsUrl: String) {
        CustomTabsIntent.Builder()
            .setToolbarColor(
                ContextCompat.getColor(
                    activity!!,
                    R.color.colorPrimary
                )
            )
            .setStartAnimations(
                activity!!,
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            .setExitAnimations(
                activity!!,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
            .build()
            .launchUrl(activity, Uri.parse(newsUrl))
    }

    private fun getNewsCategory() =
        preferencesManager.getString(getString(R.string.preference_key_news_category))

}
