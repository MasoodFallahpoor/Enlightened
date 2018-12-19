package ir.fallahpoor.enlightened.data.executor

import androidx.annotation.NonNull
import ir.fallahpoor.enlightened.domain.executor.ThreadExecutor
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class JobExecutor @Inject
constructor() : ThreadExecutor {

    private val threadPoolExecutor = ThreadPoolExecutor(
        3, 5, 10, TimeUnit.SECONDS,
        LinkedBlockingQueue(), JobThreadFactory()
    )

    override fun execute(@NonNull runnable: Runnable) {
        threadPoolExecutor.execute(runnable)
    }

    private class JobThreadFactory : ThreadFactory {

        private var counter = 0

        override fun newThread(@NonNull runnable: Runnable) =
            Thread(runnable, "android_" + counter++)

    }

}
