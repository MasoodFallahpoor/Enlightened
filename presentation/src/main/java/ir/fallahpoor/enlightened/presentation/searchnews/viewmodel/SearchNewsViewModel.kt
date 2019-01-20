package ir.fallahpoor.enlightened.presentation.searchnews.viewmodel

import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.Disposable
import ir.fallahpoor.enlightened.domain.interactor.SearchNewsUseCase
import ir.fallahpoor.enlightened.presentation.BaseViewModel
import ir.fallahpoor.enlightened.presentation.common.ExceptionParser
import ir.fallahpoor.enlightened.presentation.common.ViewState
import ir.fallahpoor.enlightened.presentation.newslist.model.NewsListDataMapper
import ir.fallahpoor.enlightened.presentation.newslist.model.NewsModel
import ir.fallahpoor.enlightened.presentation.searchnews.view.state.*

class SearchNewsViewModel(
    private val searchNewsUseCase: SearchNewsUseCase,
    private val newsListDataMapper: NewsListDataMapper,
    private val exceptionParser: ExceptionParser
) : BaseViewModel() {

    val viewStateLiveData = MutableLiveData<ViewState>()
    private var pageNumber = 1
    private val PAGE_SIZE = 20
    private lateinit var searchQuery: String
    private val newsList = arrayListOf<NewsModel>()

    fun searchNews(searchQuery: String) {

        if (shouldSearchNews()) {

            pageNumber = 1
            this.searchQuery = searchQuery

            viewStateLiveData.value = LoadingState()

            val params = SearchNewsUseCase.Params.forParams(searchQuery, pageNumber, PAGE_SIZE)
            val d: Disposable = searchNewsUseCase.execute(params)
                .subscribe(
                    { news ->
                        newsList.apply {
                            clear()
                            addAll(newsListDataMapper.transform(news))
                        }
                        viewStateLiveData.value = DataLoadedState(newsList)
                    },
                    { throwable ->
                        viewStateLiveData.value =
                                LoadDataErrorState(exceptionParser.parseException(throwable))
                    }
                )

            addDisposable(d)

        }

    }

    private fun shouldSearchNews() =
        (viewStateLiveData.value == null || viewStateLiveData.value is LoadDataErrorState)

    fun searchMoreNews() {

        viewStateLiveData.value = LoadingState()

        val params = SearchNewsUseCase.Params.forParams(searchQuery, pageNumber + 1, PAGE_SIZE)
        val d: Disposable = searchNewsUseCase.execute(params)
            .subscribe(
                { news ->
                    pageNumber++
                    newsList.addAll(newsListDataMapper.transform(news))
                    viewStateLiveData.value = MoreDataLoadedState(newsList)
                },
                { throwable ->
                    viewStateLiveData.value = LoadMoreDataErrorState(
                        exceptionParser.parseException(throwable)
                    )
                }
            )

        addDisposable(d)

    }

    fun initializeState() {
        if (viewStateLiveData.value is LoadMoreDataErrorState) {
            viewStateLiveData.value = DataLoadedState(newsList)
        }
    }

}