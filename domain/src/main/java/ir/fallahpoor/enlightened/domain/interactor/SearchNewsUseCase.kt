package ir.fallahpoor.enlightened.domain.interactor

import io.reactivex.Single
import ir.fallahpoor.enlightened.domain.executor.PostExecutionThread
import ir.fallahpoor.enlightened.domain.executor.ThreadExecutor
import ir.fallahpoor.enlightened.domain.interactor.type.UseCase
import ir.fallahpoor.enlightened.domain.model.News
import ir.fallahpoor.enlightened.domain.repository.NewsRepository
import javax.inject.Inject

class SearchNewsUseCase @Inject constructor(
    private val newsRepository: NewsRepository,
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) : UseCase<List<News>, SearchNewsUseCase.Params>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(inputs: Params): Single<List<News>> =
        newsRepository.searchNews(inputs.searchQuery, inputs.pageNumber, inputs.pageSize)

    class Params private constructor(
        val searchQuery: String,
        val pageNumber: Int,
        val pageSize: Int
    ) {
        companion object {
            fun forParams(searchQuery: String, pageNumber: Int, pageSize: Int) =
                Params(searchQuery, pageNumber, pageSize)
        }
    }

}