package kpfu.itis.homework.store.side_effects

import com.freeletics.rxredux.SideEffect
import kpfu.itis.homework.store.CalculateAction
import kpfu.itis.homework.store.CalculateState

typealias CalculateSideEffects = SideEffect<CalculateState, CalculateAction>