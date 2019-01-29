package ir.fallahpoor.enlightened.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import ir.fallahpoor.enlightened.presentation.common.ViewState

open class BaseViewModel : ViewModel() {

    private val viewStateLiveData = MutableLiveData<ViewState>()
    private val disposables = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    fun addDisposable(d: Disposable) {
        disposables.add(d)
    }

    fun setViewState(viewState: ViewState) {
        viewStateLiveData.value = viewState
    }

    fun getViewStateLiveData() = viewStateLiveData

}