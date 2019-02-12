package ir.fallahpoor.enlightened.presentation.app

import androidx.multidex.MultiDexApplication

class App : MultiDexApplication() {

    val appComponent: AppComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .build()
    }

}