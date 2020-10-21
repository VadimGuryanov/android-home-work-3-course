package kpfu.itis.homework.store.side_effects

import com.freeletics.rxredux.StateAccessor
import io.reactivex.Observable
import io.reactivex.Single
import kpfu.itis.homework.data.CalculateApi
import kpfu.itis.homework.store.CalculateAction
import kpfu.itis.homework.store.CalculateAction.WroteOne
import kpfu.itis.homework.store.CalculateAction.WroteTwo
import kpfu.itis.homework.store.CalculateAction.WroteResult
import kpfu.itis.homework.store.CalculateState
import java.lang.IllegalStateException

class CalculateSideEffectsImpl(
    private val calculateApi: CalculateApi
): CalculateSideEffects {

    private val queueAction = mutableListOf<Pair<Int, Int>>()

    override fun invoke(
        actions: Observable<CalculateAction>,
        state: StateAccessor<CalculateState>
    ): Observable<out CalculateAction> {
        return actions.filter { it is WroteOne || it is WroteTwo || it is WroteResult }
            .switchMap { action ->
                    getResult(action)
                        .map<CalculateAction> { CalculateAction.CalculateSuccess(it) }
                        .onErrorReturnItem(CalculateAction.CalculateError)
                        .toObservable()
                        .startWith(CalculateAction.CalculateStarted)
            }
    }

    private fun getResult(action: CalculateAction): Single<Triple<Int, Int, Int>> {
        return when(action) {
            is WroteOne -> {
                if (queueAction.isEmpty()) {
                    queueAction.add(Pair(1, action.one))
                    calculateApi.getResult(action.one, 0)
                } else {
                    val pair = queueAction.last()
                    when (pair.first) {
                        1 -> {
                            queueAction.removeAt(queueAction.lastIndex)
                            queueAction.add(pair.copy(second = action.one))
                            if (queueAction.size == 1) {
                                calculateApi.getResult(action.one, 0)
                            } else {
                                val prePair = queueAction[queueAction.lastIndex - 1]
                                if (prePair.first == 2) {
                                    calculateApi.getResult(action.one, prePair.second)
                                } else {
                                    calculateApi.getTwo(action.one, prePair.second)
                                }
                            }
                        }
                        2 -> {
                            queueAction.add(Pair(1, action.one))
                            calculateApi.getResult(action.one, pair.second)
                        }
                        else -> {
                            queueAction.add(Pair(1, action.one))
                            calculateApi.getTwo(action.one, pair.second)
                        }
                    }
                }
            }
            is WroteTwo -> {
                if (queueAction.isEmpty()) {
                    queueAction.add(Pair(2, action.two))
                    calculateApi.getResult(0, action.two)
                } else {
                    val pair = queueAction.last()
                    when (pair.first) {
                        2 -> {
                            queueAction.removeAt(queueAction.lastIndex)
                            queueAction.add(pair.copy(second = action.two))
                            if (queueAction.size == 1) {
                                calculateApi.getResult(0, action.two)
                            } else {
                                val prePair = queueAction[queueAction.lastIndex - 1]
                                if (prePair.first == 1) {
                                    calculateApi.getResult(prePair.second, action.two)
                                } else {
                                    calculateApi.getOne(action.two, prePair.second)
                                }
                            }
                        }
                        1 -> {
                            queueAction.add(Pair(2, action.two))
                            calculateApi.getResult(pair.second, action.two)
                        }
                        else -> {
                            queueAction.add(Pair(2, action.two))
                            calculateApi.getOne(action.two, pair.second)
                        }
                    }
                }
            }
            is WroteResult -> {
                if (queueAction.isEmpty()) {
                    queueAction.add(Pair(3, action.result))
                    calculateApi.getOneAndTwo(action.result)
                } else {
                    val pair = queueAction.last()
                    when (pair.first) {
                        3 -> {
                            queueAction.removeAt(queueAction.lastIndex)
                            queueAction.add(pair.copy(second = action.result))
                            if (queueAction.size == 1) {
                                calculateApi.getOneAndTwo(action.result)
                            } else {
                                val prePair = queueAction[queueAction.lastIndex - 1]
                                if (prePair.first == 1) {
                                    calculateApi.getTwo(prePair.second, action.result)
                                } else {
                                    calculateApi.getOne(prePair.second, action.result)
                                }
                            }
                        }
                        2 -> {
                            queueAction.add(Pair(3, action.result))
                            calculateApi.getOne(pair.second, action.result)
                        }
                        else -> {
                            queueAction.add(Pair(3, action.result))
                            calculateApi.getTwo(pair.second, action.result)
                        }
                    }
                }
            }
            else -> throw IllegalStateException()
        }
    }
}