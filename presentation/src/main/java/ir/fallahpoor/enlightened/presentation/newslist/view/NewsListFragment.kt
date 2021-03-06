package ir.fallahpoor.enlightened.presentation.newslist.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ir.fallahpoor.enlightened.R
import ir.fallahpoor.enlightened.data.PreferencesManager
import ir.fallahpoor.enlightened.presentation.app.App
import ir.fallahpoor.enlightened.presentation.common.EndlessOnScrollListener
import ir.fallahpoor.enlightened.presentation.newslist.di.DaggerNewsListComponent
import ir.fallahpoor.enlightened.presentation.newslist.model.NewsModel
import ir.fallahpoor.enlightened.presentation.newslist.view.state.*
import ir.fallahpoor.enlightened.presentation.newslist.viewmodel.NewsListViewModel
import ir.fallahpoor.enlightened.presentation.newslist.viewmodel.NewsListViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_news_list.*
import javax.inject.Inject

class NewsListFragment : Fragment() {

    @Inject
    lateinit var newsListViewModelFactory: NewsListViewModelFactory
    @Inject
    lateinit var preferencesManager: PreferencesManager

    private lateinit var newsListViewModel: NewsListViewModel
    private var newsAdapter = NewsAdapter()
    private val COUNTRY = "us"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
        return inflater.inflate(R.layout.fragment_news_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        injectViewModel()

        super.onActivityCreated(savedInstanceState)

        setupRecyclerView()
        setupViewModel(savedInstanceState != null)
        subscribeToViewModel()

        newsListViewModel.getNews(COUNTRY, getNewsCategory())

    }

    private fun injectViewModel() {
        DaggerNewsListComponent.builder()
            .appComponent((activity?.application as App).appComponent)
            .build()
            .inject(this)
    }

    private fun setupRecyclerView() {
        with(newsListRecyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = newsAdapter
            addOnScrollListener(object :
                EndlessOnScrollListener(layoutManager as LinearLayoutManager) {
                override fun onLoadMore() {
                    newsListViewModel.getMoreNews()
                }
            })
        }
    }

    private fun setupViewModel(isActivityRestored: Boolean) {
        newsListViewModel = ViewModelProviders.of(this, newsListViewModelFactory)
            .get(NewsListViewModel::class.java)
        if (isActivityRestored) {
            newsListViewModel.adjustState()
        }
    }

    private fun subscribeToViewModel() {

        newsListViewModel.getViewStateLiveData().observe(
            this,
            Observer { viewState ->
                run {
                    hideLoading()
                    when (viewState) {
                        is LoadingState -> showLoading()
                        is DataLoadedState -> renderNews(viewState.news)
                        is LoadDataErrorState -> renderLoadNewsError(viewState.errorMessage)
                        is MoreDataLoadedState -> renderMoreNews(viewState.news)
                        is LoadMoreDataErrorState -> renderLoadMoreNewsError(
                            viewState.errorMessage
                        )
                    }
                }
            })

    }

    private fun showLoading() {
        loading.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loading.visibility = View.INVISIBLE
    }

    private fun renderNews(newsList: List<NewsModel>) {
        tryAgainView.visibility = View.GONE
        newsAdapter = createNewsAdapter(newsList)
        newsListRecyclerView.adapter = newsAdapter
    }

    private fun renderLoadNewsError(errorMessage: String) {
        tryAgainView.setErrorMessage(errorMessage)
        tryAgainView.setTryAgainButtonClickListener(
            View.OnClickListener { newsListViewModel.getNews(COUNTRY, getNewsCategory()) })
        tryAgainView.visibility = View.VISIBLE
    }

    private fun renderMoreNews(news: List<NewsModel>) {
        newsAdapter.addNews(news.minus(newsAdapter.getNews()))
    }

    private fun renderLoadMoreNewsError(errorMessage: String) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun createNewsAdapter(newsList: List<NewsModel>) =
        NewsAdapter(newsList) { news: NewsModel ->
            navHostFragment.findNavController().navigate(
                NewsListFragmentDirections.actionNewsListFragmentToNewsDetailsFragment(news.url)
            )
        }

    private fun getNewsCategory() =
        preferencesManager.getString(getString(R.string.preference_key_news_category))

}
