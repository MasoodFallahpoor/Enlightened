package ir.fallahpoor.enlightened.presentation.newslist.view.state

import ir.fallahpoor.enlightened.presentation.common.ViewState
import ir.fallahpoor.enlightened.presentation.newslist.model.NewsModel

data class MoreDataLoadedState(val news: ArrayList<NewsModel>) : ViewState