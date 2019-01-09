package ir.fallahpoor.enlightened.presentation.searchnews.view

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import ir.fallahpoor.enlightened.R
import ir.fallahpoor.enlightened.R.*
import ir.fallahpoor.enlightened.presentation.app.App
import ir.fallahpoor.enlightened.presentation.common.EndlessOnScrollListener
import ir.fallahpoor.enlightened.presentation.newslist.model.NewsModel
import ir.fallahpoor.enlightened.presentation.searchnews.di.DaggerSearchNewsComponent
import ir.fallahpoor.enlightened.presentation.searchnews.viewmodel.SearchNewsViewModel
import ir.fallahpoor.enlightened.presentation.searchnews.viewmodel.SearchNewsViewModelFactory
import kotlinx.android.synthetic.main.fragment_search_news.*
import kotlinx.android.synthetic.main.try_again_layout.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SearchNewsFragment : Fragment() {

    @Inject
    lateinit var searchNewsViewModelFactory: SearchNewsViewModelFactory

    private lateinit var searchNewsViewModel: SearchNewsViewModel
    private var disposable: Disposable? = null
    private lateinit var newsAdapter: NewsAdapter
    private var isLoadingMoreNews = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(layout.fragment_search_news, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        injectViewModel()

        super.onActivityCreated(savedInstanceState)

        setupViewModel()
        subscribeToViewModel()
        setupSearchView()

    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

    private fun injectViewModel() {
        DaggerSearchNewsComponent.builder()
            .appComponent((activity?.application as App).appComponent)
            .build()
            .inject(this)
    }

    private fun setupViewModel() {
        searchNewsViewModel = ViewModelProviders.of(this, searchNewsViewModelFactory)
            .get(SearchNewsViewModel::class.java)
    }

    private fun subscribeToViewModel() {

        searchNewsViewModel.getLoadingLiveData().observe(
            this,
            Observer { show -> if (show) showLoading() else hideLoading() })

        searchNewsViewModel.getErrorLiveData().observe(
            this,
            Observer { errorMessage -> showError(errorMessage) }
        )

        searchNewsViewModel.newsListLiveData.observe(
            this,
            Observer { newsList -> showNews(newsList) })

    }

    private fun setupSearchView() {
        disposable = createSearchObservable()
            .map { searchQuery -> searchQuery.toLowerCase().trim() }
            .debounce(500, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .filter { searchQuery -> searchQuery.isNotBlank() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { searchQuery -> searchNews(searchQuery, false) }
    }

    private fun createSearchObservable(): Observable<String> =
        Observable.create { subscriber ->
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newQuery: String?): Boolean {
                    subscriber.onNext(newQuery!!)
                    return false
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    subscriber.onNext(query!!)
                    return false
                }
            })
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
            searchResultsRecyclerView.visibility = View.GONE
            noResultTextView.visibility = View.GONE
        }
    }

    private fun showLoadMoreError() {
        Toast.makeText(activity, R.string.internet_not_connected, Toast.LENGTH_SHORT).show()
    }

    private fun setupTryAgainLayout(errorMessage: String) {
        errorMessageTextView.text = errorMessage
        tryAgainButton.setOnClickListener {
            searchNews(searchView.query.toString(), false)
        }
        tryAgain.visibility = View.VISIBLE
    }

    private fun searchNews(searchQuery: String, loadMore: Boolean) {
        if (loadMore) {
            isLoadingMoreNews = true
            searchNewsViewModel.searchMoreNews()
        } else {
            isLoadingMoreNews = false
            searchNewsViewModel.searchNews(searchQuery)
        }
    }

    private fun showNews(newsList: List<NewsModel>) {

        tryAgain.visibility = View.GONE

        if (newsList.isEmpty()) {
            noResultTextView.visibility = View.VISIBLE
            noResultTextView.text = "No results for " + "\"" + searchView.query.toString() + "\""
        } else {
            noResultTextView.visibility = View.GONE
            if (isLoadingMoreNews) {
                newsAdapter.appendNews(newsList)
            } else {
                initializeRecyclerViewWith(newsList)
            }
        }

    }

    private fun initializeRecyclerViewWith(newsList: List<NewsModel>) {
        with(searchResultsRecyclerView) {
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
                    searchNews(searchView.query.toString(), true)
                }
            })
        }
    }

    private fun openNewsInBrowser(newsUrl: String) {
        CustomTabsIntent.Builder()
            .setToolbarColor(
                ContextCompat.getColor(
                    activity!!,
                    color.colorPrimary
                )
            )
            .setStartAnimations(
                activity!!,
                anim.slide_in_right,
                anim.slide_out_left
            )
            .setExitAnimations(
                activity!!,
                anim.slide_in_left,
                anim.slide_out_right
            )
            .build()
            .launchUrl(activity, Uri.parse(newsUrl))
    }

}
