package kpfu.itis.homework

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import kpfu.itis.homework.data.CalculateApi
import kpfu.itis.homework.store.CalculateAction
import kpfu.itis.homework.store.CalculateStore
import kpfu.itis.homework.store.side_effects.CalculateSideEffectsImpl

class MainViewModel : ViewModel() {

    private var disposable = CompositeDisposable()
    private val store = CalculateStore( listOf( CalculateSideEffectsImpl(CalculateApi())))

    private val _liveDataOne = MutableLiveData<Int>()
    val liveDataOne: LiveData<Int> = _liveDataOne

    private val _liveDataTwo = MutableLiveData<Int>()
    val liveDataTwo: LiveData<Int> = _liveDataTwo

    private val _liveDataResult = MutableLiveData<Int>()
    val liveDataResult: LiveData<Int> = _liveDataResult

    private val _liveDataLodding = MutableLiveData<Boolean>()
    val liveDataLodding: LiveData<Boolean> = _liveDataLodding

    private val _liveDataError = MutableLiveData<String>()
    val liveDataError: LiveData<String> = _liveDataError

    init {
        disposable += store.state
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy (
                 onNext = {
                    Log.e("state", it.toString())
                     _liveDataLodding.value = it.isCalculate
                     _liveDataOne.value = it.number1
                     _liveDataTwo.value = it.number2
                     _liveDataResult.value = it.result
                },
                onError = {
                    Log.e("error", it.message.toString())
                }
            )
    }

    fun wroteOne(one: Int) {
        store.actionRelay.onNext(CalculateAction.WroteOne(one))
    }

    fun wroteTwo(two: Int) {
        store.actionRelay.onNext(CalculateAction.WroteTwo(two))
    }

    fun wroteResult(result: Int) {
        store.actionRelay.onNext(CalculateAction.WroteResult(result))
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}