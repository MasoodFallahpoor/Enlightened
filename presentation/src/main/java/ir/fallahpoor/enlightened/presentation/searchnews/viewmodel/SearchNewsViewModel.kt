package ir.fallahpoor.enlightened.presentation.searchnews.viewmodel

import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.Disposable
import ir.fallahpoor.enlightened.domain.interactor.SearchNewsUseCase
import ir.fallahpoor.enlightened.presentation.BaseViewModel
import ir.fallahpoor.enlightened.presentation.common.ExceptionParser
import ir.fallahpoor.enlightened.presentation.newslist.model.NewsListDataMapper
import ir.fallahpoor.enlightened.presentation.newslist.model.NewsModel

class SearchNewsViewModel(
    private val searchNewsUseCase: SearchNewsUseCase,
    private val newsListDataMapper: NewsListDataMapper,
    private val exceptionParser: ExceptionParser
) : BaseViewModel() {

    val newsListLiveData = MutableLiveData<List<NewsModel>>()
    private var pageNumber = 1
    private val PAGE_SIZE = 20
    private lateinit var searchQuery: String

    fun searchNews(searchQuery: String) {

        pageNumber = 1
        this.searchQuery = searchQuery

        setLoadingLiveData(true)

        val params = SearchNewsUseCase.Params.forParams(searchQuery, pageNumber, PAGE_SIZE)
        val d: Disposable = searchNewsUseCase.execute(params)
            .doFinally { setLoadingLiveData(false) }
            .subscribe(
                { newsList ->
                    newsListLiveData.value = newsListDataMapper.transformNewsList(newsList)
                },
                { throwable ->
                    setErrorLiveData(exceptionParser.parseException(throwable))
                }
            )

        addDisposable(d)

    }

    fun searchMoreNews() {

        setLoadingLiveData(true)

        val params = SearchNewsUseCase.Params.forParams(searchQuery, pageNumber + 1, PAGE_SIZE)
        val d: Disposable = searchNewsUseCase.execute(params)
            .doFinally { setLoadingLiveData(false) }
            .subscribe(
                { newsList ->
                    pageNumber++
                    newsListLiveData.value = newsListDataMapper.transformNewsList(newsList)
                },
                { throwable ->
                    setErrorLiveData(exceptionParser.parseException(throwable))
                }
            )

        addDisposable(d)

    }

}