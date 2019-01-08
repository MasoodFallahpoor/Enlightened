package ir.fallahpoor.enlightened.presentation.newslist.viewmodel

import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.Disposable
import ir.fallahpoor.enlightened.domain.interactor.GetNewsUseCase
import ir.fallahpoor.enlightened.presentation.BaseViewModel
import ir.fallahpoor.enlightened.presentation.common.ExceptionParser
import ir.fallahpoor.enlightened.presentation.newslist.model.NewsListDataMapper
import ir.fallahpoor.enlightened.presentation.newslist.model.NewsModel

class NewsListViewModel(
    private val getNewsUseCase: GetNewsUseCase,
    private val newsListDataMapper: NewsListDataMapper,
    private val exceptionParser: ExceptionParser
) : BaseViewModel() {

    val newsListLiveData = MutableLiveData<List<NewsModel>>()
    private var pageNumber = 1
    private val PAGE_SIZE = 20
    private lateinit var country: String
    private lateinit var category: String

    fun getNews(country: String, category: String) {

        pageNumber = 1
        this.country = country
        this.category = category

        setLoadingLiveData(true)

        val params = GetNewsUseCase.Params.forParams(country, category, pageNumber, PAGE_SIZE)
        val d: Disposable =
            getNewsUseCase.execute(params)
                .doFinally { setLoadingLiveData(false) }
                .subscribe(
                    { newsList ->
                        newsListLiveData.value = newsListDataMapper.transform(newsList)
                    },
                    { throwable ->
                        setErrorLiveData(exceptionParser.parseException(throwable))
                    }
                )

        addDisposable(d)

    }

    fun getMoreNews() {

        setLoadingLiveData(true)

        val params = GetNewsUseCase.Params.forParams(country, category, pageNumber + 1, PAGE_SIZE)
        val d: Disposable =
            getNewsUseCase.execute(params)
                .doFinally { setLoadingLiveData(false) }
                .subscribe(
                    { newsList ->
                        pageNumber++
                        newsListLiveData.value = newsListDataMapper.transform(newsList)
                    },
                    { throwable ->
                        setErrorLiveData(exceptionParser.parseException(throwable))
                    }
                )

        addDisposable(d)

    }

}