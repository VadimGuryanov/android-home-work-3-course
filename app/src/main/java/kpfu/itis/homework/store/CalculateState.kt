package kpfu.itis.homework.store

data class CalculateState(
    val isCalculate: Boolean = false,
    val result: Int = 0,
    val number1: Int = 0,
    val number2: Int = 0,
    val queueAction: MutableList<Pair<Int,Int>> = mutableListOf()
)