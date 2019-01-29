package ir.fallahpoor.enlightened.presentation.searchnews.viewmodel

import io.reactivex.disposables.Disposable
import ir.fallahpoor.enlightened.domain.interactor.SearchNewsUseCase
import ir.fallahpoor.enlightened.presentation.BaseViewModel
import ir.fallahpoor.enlightened.presentation.common.ExceptionParser
import ir.fallahpoor.enlightened.presentation.newslist.model.NewsListDataMapper
import ir.fallahpoor.enlightened.presentation.newslist.model.NewsModel
import ir.fallahpoor.enlightened.presentation.searchnews.view.state.*

class SearchNewsViewModel(
    private val searchNewsUseCase: SearchNewsUseCase,
    private val newsListDataMapper: NewsListDataMapper,
    private val exceptionParser: ExceptionParser
) : BaseViewModel() {

    private val newsList = arrayListOf<NewsModel>()
    private lateinit var searchQuery: String
    private var pageNumber = 1
    private val PAGE_SIZE = 20

    fun searchNews(searchQuery: String) {

        pageNumber = 1
        this.searchQuery = searchQuery

        setViewState(LoadingState())

        val params = SearchNewsUseCase.Params.forParams(searchQuery, pageNumber, PAGE_SIZE)
        val d: Disposable = searchNewsUseCase.execute(params)
            .subscribe(
                { news ->
                    newsList.apply {
                        clear()
                        addAll(newsListDataMapper.transform(news))
                    }
                    setViewState(DataLoadedState(newsList))
                },
                { throwable ->
                    setViewState(
                        LoadDataErrorState(exceptionParser.parseException(throwable))
                    )
                }
            )

        addDisposable(d)

    }

    fun searchMoreNews() {

        setViewState(LoadingState())

        val params = SearchNewsUseCase.Params.forParams(searchQuery, pageNumber + 1, PAGE_SIZE)
        val d: Disposable = searchNewsUseCase.execute(params)
            .subscribe(
                { news ->
                    pageNumber++
                    newsList.addAll(newsListDataMapper.transform(news))
                    setViewState(MoreDataLoadedState(newsList))
                },
                { throwable ->
                    setViewState(
                        LoadMoreDataErrorState(
                            exceptionParser.parseException(throwable)
                        )
                    )
                }
            )

        addDisposable(d)

    }

    fun adjustState() {
        if (isNecessaryToAdjustState()) {
            setViewState(DataLoadedState(newsList))
        }
    }

    private fun isNecessaryToAdjustState() =
        getViewStateLiveData().value is LoadMoreDataErrorState

}