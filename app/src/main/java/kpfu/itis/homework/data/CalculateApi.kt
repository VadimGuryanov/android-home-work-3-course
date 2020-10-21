package kpfu.itis.homework.data

import io.reactivex.Single
import java.util.concurrent.TimeUnit

class CalculateApi {

    companion object {
        private const val TIME = 5L
    }

    fun getOne(two: Int, result: Int): Single<Triple<Int, Int, Int>> {
        return Single.just(Triple((result - two), two, result))
            .delay(TIME, TimeUnit.SECONDS)
    }

    fun getTwo(one: Int, result: Int): Single<Triple<Int, Int, Int>> {
        return Single.just(Triple(one, (result - one), result))
            .delay(TIME, TimeUnit.SECONDS)
    }

    fun getResult(one: Int, two: Int): Single<Triple<Int, Int, Int>> {
        return Single.just(Triple(one, two, one + two))
            .delay(TIME, TimeUnit.SECONDS)
    }

    fun getOneAndTwo(result: Int): Single<Triple<Int, Int, Int>> {
        return if (result % 2 == 0) {
            Single.just(Triple(result / 2, result / 2, result))
                .delay(TIME, TimeUnit.SECONDS)
        } else {
            Single.just(Triple((result / 2) + 1, result / 2, result))
                .delay(TIME, TimeUnit.SECONDS)
        }
    }
}