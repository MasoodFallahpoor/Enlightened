package ir.fallahpoor.enlightened.presentation

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseViewModel : ViewModel() {

    private val disposables = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    fun addDisposable(d: Disposable) {
        disposables.add(d)
    }

}