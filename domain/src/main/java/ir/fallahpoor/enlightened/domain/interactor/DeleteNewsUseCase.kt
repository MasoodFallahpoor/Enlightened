package ir.fallahpoor.enlightened.domain.interactor

import io.reactivex.Completable
import ir.fallahpoor.enlightened.domain.executor.PostExecutionThread
import ir.fallahpoor.enlightened.domain.executor.ThreadExecutor
import ir.fallahpoor.enlightened.domain.interactor.type.CompletableUseCase
import ir.fallahpoor.enlightened.domain.repository.NewsRepository
import javax.inject.Inject

class DeleteNewsUseCase @Inject
constructor(
    private val newsRepository: NewsRepository,
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) : CompletableUseCase<Unit>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(inputs: Unit): Completable = newsRepository.deleteNews()

}