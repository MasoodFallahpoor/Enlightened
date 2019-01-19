package ir.fallahpoor.enlightened.presentation.newslist.viewmodel

import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.Disposable
import ir.fallahpoor.enlightened.domain.interactor.GetNewsUseCase
import ir.fallahpoor.enlightened.presentation.BaseViewModel
import ir.fallahpoor.enlightened.presentation.common.ExceptionParser
import ir.fallahpoor.enlightened.presentation.common.ViewState
import ir.fallahpoor.enlightened.presentation.newslist.model.NewsListDataMapper
import ir.fallahpoor.enlightened.presentation.newslist.model.NewsModel
import ir.fallahpoor.enlightened.presentation.newslist.view.state.*

class NewsListViewModel(
    private val getNewsUseCase: GetNewsUseCase,
    private val newsListDataMapper: NewsListDataMapper,
    private val exceptionParser: ExceptionParser
) : BaseViewModel() {

    val viewStateLiveData = MutableLiveData<ViewState>()
    private val newsList = arrayListOf<NewsModel>()
    private var pageNumber = 1
    private val PAGE_SIZE = 20
    private lateinit var country: String
    private lateinit var category: String

    fun getNews(country: String, category: String) {

        if (shouldFetchNews()) {

            pageNumber = 1
            this.country = country
            this.category = category

            viewStateLiveData.value = LoadingState()

            val params = GetNewsUseCase.Params.forParams(country, category, pageNumber, PAGE_SIZE)
            val d: Disposable =
                getNewsUseCase.execute(params)
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

    private fun shouldFetchNews() =
        (viewStateLiveData.value == null || viewStateLiveData.value is LoadDataErrorState)

    fun getMoreNews() {

        viewStateLiveData.value = LoadingState()

        val params = GetNewsUseCase.Params.forParams(country, category, pageNumber + 1, PAGE_SIZE)
        val d: Disposable =
            getNewsUseCase.execute(params)
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