package ir.fallahpoor.enlightened.presentation.searchnews.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ir.fallahpoor.enlightened.domain.interactor.SearchNewsUseCase
import ir.fallahpoor.enlightened.presentation.common.ExceptionParser
import ir.fallahpoor.enlightened.presentation.newslist.model.NewsListDataMapper
import javax.inject.Inject

class SearchNewsViewModelFactory @Inject
constructor(
    private val searchNewsUseCase: SearchNewsUseCase,
    private val newsListDataMapper: NewsListDataMapper,
    private val exceptionParser: ExceptionParser
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(viewModelClass: Class<T>): T {

        if (viewModelClass.isAssignableFrom(SearchNewsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchNewsViewModel(searchNewsUseCase, newsListDataMapper, exceptionParser) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class");
        }

    }

}