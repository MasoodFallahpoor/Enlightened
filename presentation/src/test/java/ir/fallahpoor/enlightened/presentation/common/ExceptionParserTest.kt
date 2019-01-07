package ir.fallahpoor.enlightened.presentation.common

import android.content.Context
import android.content.res.Resources
import com.google.common.truth.Truth.assertThat
import ir.fallahpoor.enlightened.R
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
class ExceptionParserTest {

    @Mock
    private lateinit var mockedContext: Context
    @Mock
    private lateinit var mockedResources: Resources

    @Before
    fun runBeforeEachTest() {
        Mockito.`when`(mockedContext.resources).thenReturn(mockedResources)
        Mockito.`when`(mockedContext.resources.getString(R.string.internet_not_connected))
            .thenReturn("Internet not connected")
        Mockito.`when`(mockedContext.resources.getString(R.string.unknown_error))
            .thenReturn("Oops, something went wrong")
    }

    @Test
    fun parseException_returns_proper_message_when_exception_is_ioException() {

        val exceptionParser = ExceptionParser(mockedContext)
        val errorMessage = exceptionParser.parseException(IOException())

        assertThat(errorMessage).isEqualTo("Internet not connected")

    }

    @Test
    fun parseException_returns_proper_message_when_exception_is_anything_but_ioException() {

        val exceptionParser = ExceptionParser(mockedContext)
        val errorMessage = exceptionParser.parseException(Exception())

        assertThat(errorMessage).isEqualTo("Oops, something went wrong")

    }

}