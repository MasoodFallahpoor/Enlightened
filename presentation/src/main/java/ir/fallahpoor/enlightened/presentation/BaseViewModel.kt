package ir.fallahpoor.enlightened.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseViewModel : ViewModel() {

    private val loadingLiveData = MutableLiveData<Boolean>()
    private val errorLiveData = MutableLiveData<String>()
    private val disposables = CompositeDisposable()

    init {
        setLoadingLiveData(false)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    fun setLoadingLiveData(isLoading: Boolean) {
        loadingLiveData.value = isLoading
    }

    fun getLoadingLiveData() = loadingLiveData

    fun setErrorLiveData(errorMessage: String) {
        errorLiveData.value = errorMessage
    }

    fun getErrorLiveData() = errorLiveData

    fun addDisposable(d: Disposable) {
        disposables.add(d)
    }

}