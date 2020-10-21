package kpfu.itis.homework.store

import com.freeletics.rxredux.Reducer
import kpfu.itis.homework.store.CalculateAction.WroteOne
import kpfu.itis.homework.store.CalculateAction.WroteTwo
import kpfu.itis.homework.store.CalculateAction.WroteResult
import kpfu.itis.homework.store.CalculateAction.CalculateError
import kpfu.itis.homework.store.CalculateAction.CalculateStarted
import kpfu.itis.homework.store.CalculateAction.CalculateSuccess

class CalculateReducer: Reducer<CalculateState, CalculateAction> {

    override fun invoke(state: CalculateState, action: CalculateAction): CalculateState {
        return when (action) {
            CalculateError -> state.copy(isCalculate = false)
            CalculateStarted -> state.copy(isCalculate = true)
            is CalculateSuccess -> state.copy(
                isCalculate = false,
                number1 = action.values.first,
                number2 = action.values.second,
                result = action.values.third
            )
            is WroteOne -> state.copy(number1 = action.one)
            is WroteTwo -> state.copy(number2 = action.two)
            is WroteResult -> state.copy(result = action.result)
        }
    }
}