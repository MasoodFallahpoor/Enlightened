package ir.fallahpoor.enlightened.presentation.settings.di

import dagger.Component
import ir.fallahpoor.enlightened.presentation.app.AppComponent
import ir.fallahpoor.enlightened.presentation.settings.SettingsFragment

@Component(dependencies = [AppComponent::class], modules = [SettingsModule::class])
interface SettingsComponent {
    fun inject(settingsFragment: SettingsFragment)
}