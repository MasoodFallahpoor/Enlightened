package ir.fallahpoor.enlightened.presentation

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import ir.fallahpoor.enlightened.domain.executor.PostExecutionThread
import javax.inject.Inject

class UiThread @Inject
constructor() : PostExecutionThread {

    override fun getScheduler(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

}