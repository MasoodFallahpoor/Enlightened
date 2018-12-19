package ir.fallahpoor.enlightened.presentation.searchnews.di

import dagger.Component
import ir.fallahpoor.enlightened.presentation.app.AppComponent
import ir.fallahpoor.enlightened.presentation.searchnews.view.SearchNewsFragment

@Component(dependencies = [AppComponent::class], modules = [SearchNewsModule::class])
interface SearchNewsComponent {
    fun inject(searchNewsFragment: SearchNewsFragment)
}