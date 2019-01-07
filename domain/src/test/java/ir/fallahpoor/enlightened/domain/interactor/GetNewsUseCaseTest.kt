package ir.fallahpoor.enlightened.domain.interactor

import io.reactivex.Single
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers
import ir.fallahpoor.enlightened.domain.executor.PostExecutionThread
import ir.fallahpoor.enlightened.domain.executor.ThreadExecutor
import ir.fallahpoor.enlightened.domain.model.News
import ir.fallahpoor.enlightened.domain.repository.NewsRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class GetNewsUseCaseTest {

    private companion object {
        const val COUNTRY = "us"
        const val CATEGORY = "all"
        const val PAGE_NUMBER = 1
        const val PAGE_SIZE = 20
    }

    @Mock
    private lateinit var mockedNewsRepository: NewsRepository
    @Mock
    private lateinit var mockedPostExecutionThread: PostExecutionThread

    @Test
    fun getNews_should_return_list_of_news() {

        // Given
        Mockito.`when`(mockedPostExecutionThread.getScheduler())
            .thenReturn(Schedulers.trampoline())
        Mockito.`when`(mockedNewsRepository.getNews(anyString(), anyString(), anyInt(), anyInt()))
            .thenReturn(Single.just(emptyList()))
        val testObserver = TestObserver<List<News>>()

        // When
        val params = GetNewsUseCase.Params.forParams(COUNTRY, CATEGORY, PAGE_NUMBER, PAGE_SIZE)
        GetNewsUseCase(mockedNewsRepository, TestThreadExecutor(), mockedPostExecutionThread)
            .execute(params)
            .subscribe(testObserver)

        // Then
        Mockito.verify(mockedNewsRepository).getNews(COUNTRY, CATEGORY, PAGE_NUMBER, PAGE_SIZE)
        Mockito.verifyNoMoreInteractions(mockedNewsRepository)
        testObserver.assertNoErrors()
        testObserver.assertComplete()

    }

    @Test
    fun getNews_should_fail() {

        // Given
        val throwable = Throwable()
        Mockito.`when`(mockedPostExecutionThread.getScheduler())
            .thenReturn(Schedulers.trampoline())
        Mockito.`when`(mockedNewsRepository.getNews(anyString(), anyString(), anyInt(), anyInt()))
            .thenReturn(Single.error(throwable))
        val testObserver = TestObserver<List<News>>()

        // When
        val params = GetNewsUseCase.Params.forParams(COUNTRY, CATEGORY, PAGE_NUMBER, PAGE_SIZE)
        GetNewsUseCase(mockedNewsRepository, TestThreadExecutor(), mockedPostExecutionThread)
            .execute(params)
            .subscribe(testObserver)

        // Then
        Mockito.verify(mockedNewsRepository).getNews(COUNTRY, CATEGORY, PAGE_NUMBER, PAGE_SIZE)
        Mockito.verifyNoMoreInteractions(mockedNewsRepository)
        testObserver.assertError(throwable)

    }

    inner class TestThreadExecutor : ThreadExecutor {
        override fun execute(command: Runnable) {
            command.run()
        }
    }

}