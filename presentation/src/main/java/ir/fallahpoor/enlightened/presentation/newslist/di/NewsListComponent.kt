package ir.fallahpoor.enlightened.presentation.newslist.di

import dagger.Component
import ir.fallahpoor.enlightened.presentation.app.AppComponent
import ir.fallahpoor.enlightened.presentation.newslist.view.NewsListFragment

@Component(dependencies = [AppComponent::class], modules = [NewsListModule::class])
interface NewsListComponent {
    fun inject(newsListFragment: NewsListFragment)
}