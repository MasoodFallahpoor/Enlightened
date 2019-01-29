package ir.fallahpoor.enlightened.presentation.searchnews.view

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import ir.fallahpoor.enlightened.R
import ir.fallahpoor.enlightened.presentation.app.App
import ir.fallahpoor.enlightened.presentation.common.EndlessOnScrollListener
import ir.fallahpoor.enlightened.presentation.newslist.model.NewsModel
import ir.fallahpoor.enlightened.presentation.searchnews.di.DaggerSearchNewsComponent
import ir.fallahpoor.enlightened.presentation.searchnews.view.state.*
import ir.fallahpoor.enlightened.presentation.searchnews.viewmodel.SearchNewsViewModel
import ir.fallahpoor.enlightened.presentation.searchnews.viewmodel.SearchNewsViewModelFactory
import kotlinx.android.synthetic.main.fragment_search_news.*
import kotlinx.android.synthetic.main.try_again_layout.*
import javax.inject.Inject

class SearchNewsFragment : Fragment() {

    @Inject
    lateinit var searchNewsViewModelFactory: SearchNewsViewModelFactory

    private lateinit var searchNewsViewModel: SearchNewsViewModel
    private var newsAdapter = NewsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_search_news, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        injectViewModel()

        super.onActivityCreated(savedInstanceState)

        setupRecyclerView()
        setupViewModel(savedInstanceState != null)
        subscribeToViewModel()
        setupSearchView()

    }

    private fun injectViewModel() {
        DaggerSearchNewsComponent.builder()
            .appComponent((activity?.application as App).appComponent)
            .build()
            .inject(this)
    }

    private fun setupRecyclerView() {
        with(searchResultsRecyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = newsAdapter
            addOnScrollListener(object :
                EndlessOnScrollListener(layoutManager as LinearLayoutManager) {
                override fun onLoadMore() {
                    searchNewsViewModel.searchMoreNews()
                }
            })
        }
    }

    private fun setupViewModel(isActivityRestored: Boolean) {
        searchNewsViewModel = ViewModelProviders.of(this, searchNewsViewModelFactory)
            .get(SearchNewsViewModel::class.java)
        if (isActivityRestored) {
            searchNewsViewModel.adjustState()
        }
    }

    private fun subscribeToViewModel() {

        searchNewsViewModel.getViewStateLiveData().observe(
            this,
            Observer { viewState ->
                run {
                    hideLoading()
                    when (viewState) {
                        is LoadingState -> showLoading()
                        is DataLoadedState -> renderNews(viewState.news)
                        is LoadDataErrorState -> renderLoadNewsError(viewState.errorMessage)
                        is MoreDataLoadedState -> renderMoreNews(viewState.news)
                        is LoadMoreDataErrorState -> renderLoadMoreNewsError(viewState.errorMessage)
                    }
                }
            })

    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchNewsViewModel.searchNews(query!!)
                hideKeyboard()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun hideKeyboard() {
        val imm: InputMethodManager =
            context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.rootView?.windowToken, 0)
        view?.clearFocus()
    }

    private fun showLoading() {
        loading.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loading.visibility = View.INVISIBLE
    }

    private fun renderNews(newsList: List<NewsModel>) {

        if (newsList.isEmpty()) {
            noResultTextView.visibility = View.VISIBLE
            noResultTextView.text = getString(R.string.no_search_result_formatted, searchView.query)
        } else {
            noResultTextView.visibility = View.GONE
            newsAdapter = createNewsAdapter(newsList)
            searchResultsRecyclerView.adapter = newsAdapter
        }

        tryAgain.visibility = View.GONE

    }

    private fun renderLoadNewsError(errorMessage: String) {
        noResultTextView.visibility = View.GONE
        errorMessageTextView.text = errorMessage
        tryAgainButton.setOnClickListener {
            searchNewsViewModel.searchNews(searchView.query.toString())
        }
        tryAgain.visibility = View.VISIBLE
    }

    private fun renderMoreNews(news: List<NewsModel>) {
        newsAdapter.addNews(news.minus(newsAdapter.getNews()))
    }

    private fun renderLoadMoreNewsError(errorMessage: String) {
        Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun createNewsAdapter(newsList: List<NewsModel>) =
        NewsAdapter(newsList) { news: NewsModel -> openNewsInBrowser(news.url) }

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

}
