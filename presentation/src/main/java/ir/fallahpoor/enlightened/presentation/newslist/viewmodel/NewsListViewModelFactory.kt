package ir.fallahpoor.enlightened.presentation.newslist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ir.fallahpoor.enlightened.domain.interactor.GetNewsUseCase
import ir.fallahpoor.enlightened.presentation.common.ExceptionParser
import ir.fallahpoor.enlightened.presentation.newslist.model.NewsListDataMapper
import javax.inject.Inject

class NewsListViewModelFactory @Inject
constructor(
    private val getNewsUseCase: GetNewsUseCase,
    private val newsListDataMapper: NewsListDataMapper,
    private val exceptionParser: ExceptionParser
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(viewModelClass: Class<T>): T {

        if (viewModelClass.isAssignableFrom(NewsListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NewsListViewModel(getNewsUseCase, newsListDataMapper, exceptionParser) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }

}