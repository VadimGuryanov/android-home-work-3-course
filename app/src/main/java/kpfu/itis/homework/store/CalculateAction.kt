package kpfu.itis.homework.store

sealed class CalculateAction {

    class WroteOne(val one: Int): CalculateAction()

    class WroteTwo(val two: Int): CalculateAction()

    class WroteResult(val result: Int): CalculateAction()

    object CalculateError: CalculateAction()

    class CalculateSuccess(val values: Triple<Int,Int,Int>): CalculateAction()

    object CalculateStarted: CalculateAction()
}