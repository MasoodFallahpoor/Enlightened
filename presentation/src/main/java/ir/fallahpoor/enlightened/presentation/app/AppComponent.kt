package ir.fallahpoor.enlightened.presentation.app

import android.content.Context
import dagger.Component
import ir.fallahpoor.enlightened.domain.executor.PostExecutionThread
import ir.fallahpoor.enlightened.domain.executor.ThreadExecutor

@Component(modules = [AppModule::class])
interface AppComponent {
    fun context(): Context

    fun threadExecutor(): ThreadExecutor

    fun postExecutionThread(): PostExecutionThread
}