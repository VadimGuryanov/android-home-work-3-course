package kpfu.itis.homework

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var set1: AnimatorSet
    private lateinit var set2: AnimatorSet
    private var animatedValue = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initAnimations()
        initListener()
    }

    private fun initListener() {
        btn_show_or_hide.setOnClickListener {
            when {
                set1.isRunning -> runAnimation1()
                set2.isRunning -> runAnimation2()
                else -> if (tv_hw.visibility == View.VISIBLE) set1.start() else set2.start()
            }
        }
    }

    private fun runAnimation1() {
        animatedValue = (set1.childAnimations.last() as ValueAnimator).let {
            it.animatedValue as Float
        }
        set1.removeAllListeners()
        set1.end()
        set2.play(getTextViewRotation(animatedValue, 0f, true))
        set2.start()
    }

    private fun runAnimation2() {
        animatedValue = (set2.childAnimations.last() as ValueAnimator).let {
            it.animatedValue as Float
        }
        set2.removeAllListeners()
        set2.end()
        set1.play(getTextViewRotation(animatedValue, 360f, false))
        set1.start()
    }

    private fun initAnimations() {
        set1 = AnimatorSet().apply {
            play(getTextViewRotation(0f, 360f, false))
            duration = 1000
            interpolator = DecelerateInterpolator()
        }
        set2 = AnimatorSet().apply {
            play(getTextViewRotation(360f, 0f, true))
            duration = 1000
            interpolator = DecelerateInterpolator()
        }
    }

    private fun getTextViewRotation(start: Float, end: Float, isVisible: Boolean): ObjectAnimator {
        return ObjectAnimator.ofFloat(tv_hw, View.ROTATION, start, end).apply {
            if (isVisible) {
                doOnStart { tv_hw.visibility = View.VISIBLE }
            } else {
                doOnEnd { tv_hw.visibility = View.GONE }
            }
        }
    }
}