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
class SearchNewsUseCaseTest {

    private companion object {
        const val SEARCH_QUERY = "trump"
        const val PAGE_NUMBER = 1
        const val PAGE_SIZE = 20
    }

    @Mock
    private lateinit var mockedNewsRepository: NewsRepository
    @Mock
    private lateinit var mockedPostExecutionThread: PostExecutionThread

    @Test
    fun searchNews_should_return_list_of_news() {

        // Given
        Mockito.`when`(mockedPostExecutionThread.getScheduler())
            .thenReturn(Schedulers.trampoline())
        Mockito.`when`(mockedNewsRepository.searchNews(anyString(), anyInt(), anyInt()))
            .thenReturn(Single.just(emptyList()))
        val testObserver = TestObserver<List<News>>()

        // When
        val params = SearchNewsUseCase.Params.forParams(SEARCH_QUERY, PAGE_NUMBER, PAGE_SIZE)
        SearchNewsUseCase(mockedNewsRepository, TestThreadExecutor(), mockedPostExecutionThread)
            .execute(params)
            .subscribe(testObserver)

        // Then
        Mockito.verify(mockedNewsRepository).searchNews(SEARCH_QUERY, PAGE_NUMBER, PAGE_SIZE)
        Mockito.verifyNoMoreInteractions(mockedNewsRepository)
        testObserver.assertNoErrors()
        testObserver.assertComplete()

    }

    @Test
    fun searchNews_should_fail() {

        // Given
        Mockito.`when`(mockedPostExecutionThread.getScheduler())
            .thenReturn(Schedulers.trampoline())
        Mockito.`when`(mockedNewsRepository.searchNews(anyString(), anyInt(), anyInt()))
            .thenReturn(Single.error(Throwable()))
        val testObserver = TestObserver<List<News>>()

        // When
        val params = SearchNewsUseCase.Params.forParams(SEARCH_QUERY, PAGE_NUMBER, PAGE_SIZE)
        SearchNewsUseCase(mockedNewsRepository, TestThreadExecutor(), mockedPostExecutionThread)
            .execute(params)
            .subscribe(testObserver)

        // Then
        Mockito.verify(mockedNewsRepository).searchNews(SEARCH_QUERY, PAGE_NUMBER, PAGE_SIZE)
        Mockito.verifyNoMoreInteractions(mockedNewsRepository)
        testObserver.assertError(Throwable::class.java)

    }

    inner class TestThreadExecutor : ThreadExecutor {
        override fun execute(command: Runnable) {
            command.run()
        }
    }


}