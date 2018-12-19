package ir.fallahpoor.enlightened.domain.interactor

import io.reactivex.Single
import ir.fallahpoor.enlightened.domain.executor.PostExecutionThread
import ir.fallahpoor.enlightened.domain.executor.ThreadExecutor
import ir.fallahpoor.enlightened.domain.interactor.type.UseCase
import ir.fallahpoor.enlightened.domain.model.News
import ir.fallahpoor.enlightened.domain.repository.NewsRepository
import javax.inject.Inject

class GetNewsUseCase @Inject constructor(
    private val newsRepository: NewsRepository,
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) : UseCase<List<News>, GetNewsUseCase.Params>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(inputs: Params): Single<List<News>> =
        newsRepository.getNews(inputs.country, inputs.category, inputs.pageNumber, inputs.pageSize)

    class Params private constructor(
        val country: String,
        val category: String,
        val pageNumber: Int,
        val pageSize: Int
    ) {
        companion object {
            fun forParams(
                country: String,
                category: String,
                pageNumber: Int,
                pageSize: Int
            ) = Params(country, category, pageNumber, pageSize)
        }
    }

}