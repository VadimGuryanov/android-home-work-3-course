package kpfu.itis.homework

import android.content.Context
import java.lang.StringBuilder

object Calculator {

    private val numbers: MutableList<String> = mutableListOf("0")
    private val operations: MutableList<String> = mutableListOf()
    private var isApplyNumerical = true

    fun applyCommand(command: String, context: Context): Pair<String, Number> {
        return with(context) {
            when (command) {
                getString(R.string.value_equals) -> applyEquals()
                getString(R.string.value_plus) -> applyOperations("+")
                getString(R.string.value_minus) -> applyOperations("-")
                getString(R.string.value_clear) -> applyClear()
                getString(R.string.value_clear_one) -> applyOneSymbleClear()
                else -> applyNumeral(command.toInt())
            }
        }
    }

    private fun applyNumeral(numeral: Int): Pair<String, Number> {
        with(numbers) {
            if (isApplyNumerical) {
                if (get(lastIndex) == "0") {
                    removeAt(lastIndex)
                    add(numeral.toString())
                } else {
                    val last = get(lastIndex)
                    removeAt(lastIndex)
                    add(last + numeral.toString())
                }
            } else {
                add(numeral.toString())
            }
        }
        isApplyNumerical = true
        return Pair(getExpression(), getResult())
    }

    private fun applyOperations(operation: String): Pair<String, Number> {
        with(operations) {
            if (isApplyNumerical) {
                add(operation)
            } else {
                removeAt(size - 1)
                add(operation)
            }
        }
        isApplyNumerical = false
        return Pair(getExpression(), getResult())
    }

    private fun getExpression(): String {
        val stringBuffer = StringBuilder()
        stringBuffer.append(numbers[0])
        if (operations.isNotEmpty()) {
            for (i in 1 until numbers.size) {
                stringBuffer.append(operations[i - 1])
                stringBuffer.append(numbers[i])
            }
            if (!isApplyNumerical) {
                stringBuffer.append(operations[operations.lastIndex])
            }
        }
        if (stringBuffer.toString() == "0") return ""
        return stringBuffer.toString()
    }

    private fun applyClear(): Pair<String, Number> {
        numbers.clear()
        numbers.add("0")
        operations.clear()
        isApplyNumerical = true
        return Pair(getExpression(), getResult())
    }

    private fun applyOneSymbleClear(): Pair<String, Number> {
        if (isApplyNumerical) {
            val value = numbers[numbers.lastIndex]
            numbers[numbers.lastIndex] = value.substring(0, value.length - 1)
            if (numbers[numbers.lastIndex].isEmpty()) {
                numbers.removeAt(numbers.lastIndex)
                isApplyNumerical = false
            }
            if (numbers.isEmpty()) numbers.add("0")
        } else {
            operations.removeAt(operations.lastIndex)
            isApplyNumerical = true
        }
        return Pair(getExpression(), getResult())
    }

    private fun applyEquals(): Pair<String, Number> {
        return Pair(getExpression(), getResult())
    }

    private fun getResult(): Number {
        var result: Number = 0
        if (operations.isNotEmpty()) {
            if (numbers.size == 1) return numbers[0].toLong()
            result = if (isApplyNumerical) {
                calculate(operations.size)
            } else {
                calculate(operations.size - 1)
            }
        } else {
            return numbers[0].toLong()
        }
        return result
    }

    private fun calculate(size: Int): Number {
        var result: Long = numbers[0].toLong()
        for (i in 0 until size) {
            result += when(operations[i]) {
                "+" -> numbers[i+1].toLong()
                else -> -numbers[i+1].toLong()
            }
        }
        return result
    }

//    fun applayPoint(): Pair<String, Number> {
//        if (isApplyNumerical) {
//                val last = numbers[numbers.lastIndex]
//                numbers[numbers.lastIndex] = "$last."
//        } else {
//            operations.add("0.")
//        }
//        isApplyNumerical = false
//        return Pair(getExpression(), 0)
//    }
//
//    fun applayPercent(): Pair<String, Number> {
//        if (isApplyNumerical) {
//            numbers[numbers.lastIndex] = numbers[numbers.lastIndex]
//        }
//        isApplyNumerical = false
//        return Pair(getExpression(), 0)
//    }

}