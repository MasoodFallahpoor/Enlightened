package ir.fallahpoor.enlightened.presentation.searchnews.view.state

import ir.fallahpoor.enlightened.presentation.common.ViewState
import ir.fallahpoor.enlightened.presentation.newslist.model.NewsModel

data class MoreDataLoadedState(val news: ArrayList<NewsModel>) : ViewState