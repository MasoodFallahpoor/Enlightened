package ir.fallahpoor.enlightened.domain.interactor.type

import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import ir.fallahpoor.enlightened.domain.executor.PostExecutionThread
import ir.fallahpoor.enlightened.domain.executor.ThreadExecutor

abstract class CompletableUseCase<Inputs>(
    private val threadExecutor: ThreadExecutor,
    private val postExecutionThread: PostExecutionThread
) {
    protected abstract fun buildUseCaseObservable(inputs: Inputs): Completable

    fun execute(inputs: Inputs): Completable =
        buildUseCaseObservable(inputs)
            .subscribeOn(Schedulers.from(threadExecutor))
            .observeOn(postExecutionThread.getScheduler())
}