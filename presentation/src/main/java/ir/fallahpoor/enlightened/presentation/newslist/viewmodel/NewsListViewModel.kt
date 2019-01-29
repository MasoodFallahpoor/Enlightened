package ir.fallahpoor.enlightened.presentation.newslist.viewmodel

import io.reactivex.disposables.Disposable
import ir.fallahpoor.enlightened.domain.interactor.GetNewsUseCase
import ir.fallahpoor.enlightened.presentation.BaseViewModel
import ir.fallahpoor.enlightened.presentation.common.ExceptionParser
import ir.fallahpoor.enlightened.presentation.newslist.model.NewsListDataMapper
import ir.fallahpoor.enlightened.presentation.newslist.model.NewsModel
import ir.fallahpoor.enlightened.presentation.newslist.view.state.*

class NewsListViewModel(
    private val getNewsUseCase: GetNewsUseCase,
    private val newsListDataMapper: NewsListDataMapper,
    private val exceptionParser: ExceptionParser
) : BaseViewModel() {

    private val newsList = arrayListOf<NewsModel>()
    private lateinit var country: String
    private lateinit var category: String
    private var pageNumber = 1
    private val PAGE_SIZE = 20

    fun getNews(country: String, category: String) {

        if (shouldFetchNews()) {

            pageNumber = 1
            this.country = country
            this.category = category

            setViewState(LoadingState())

            val params = GetNewsUseCase.Params.forParams(country, category, pageNumber, PAGE_SIZE)
            val d: Disposable =
                getNewsUseCase.execute(params)
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

    }

    private fun shouldFetchNews() =
        (getViewStateLiveData().value == null || getViewStateLiveData().value is LoadDataErrorState)

    fun getMoreNews() {

        setViewState(LoadingState())

        val params = GetNewsUseCase.Params.forParams(country, category, pageNumber + 1, PAGE_SIZE)
        val d: Disposable =
            getNewsUseCase.execute(params)
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