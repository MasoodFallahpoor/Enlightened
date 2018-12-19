package ir.fallahpoor.enlightened.presentation.common

import android.content.Context
import ir.fallahpoor.enlightened.R
import java.io.IOException
import javax.inject.Inject

class ExceptionParser @Inject
constructor(private val context: Context) {

    fun parseException(throwable: Throwable): String {
        return when (throwable) {
            is IOException -> {
                context.resources.getString(R.string.internet_not_connected)
            }
            else -> context.resources.getString(R.string.unknown_error)
        }
    }
}