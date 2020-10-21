package kpfu.itis.homework.store

import com.freeletics.rxredux.reduxStore
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kpfu.itis.homework.store.side_effects.CalculateSideEffects

class CalculateStore (
    sideEffects: List<CalculateSideEffects>
) {

    val actionRelay = PublishSubject.create<CalculateAction>()

    val state: Observable<CalculateState> = actionRelay.reduxStore(
        CalculateState(),
        sideEffects,
        CalculateReducer()::invoke
    )
        .distinctUntilChanged()
}
