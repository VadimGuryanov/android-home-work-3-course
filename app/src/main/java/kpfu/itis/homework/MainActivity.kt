package kpfu.itis.homework

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.dynamicanimation.animation.SpringForce.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var tvAnim1: AnimatorSet
    private lateinit var tvAnim2: AnimatorSet
    private lateinit var btnAnim1: ValueAnimator
    private lateinit var btnAnim2: ValueAnimator
    private var tvCurrantValue = 0f
    private var btnCurrantValue = 0f
    private var statusAnim = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initAnimations()
        initListener()
    }

    private fun initListener() {
        btn_show_or_hide.setOnClickListener {
            if (statusAnim) {
                animateImage2()
            } else {
                animateImage1()
            }
            when {
                tvAnim1.isRunning -> runAnimation2()
                tvAnim2.isRunning -> runAnimation1()
                else -> {
                    btnCurrantValue = 0f
                    tvCurrantValue = 0f
                    initAnimations()
                    if (tv_hw.visibility == View.VISIBLE) {
                        tvAnim1.start()
                        btnAnim1.start()
                    } else {
                        tvAnim2.start()
                        btnAnim2.start()
                    }
                }
            }
        }
        tv_hw.setOnClickListener {
            getTextViewSpringAnimation(it,  100f).start()
            getTextViewSpringAnimation(it, -100f).start()
        }
    }

    private fun runAnimation2() {
        tvCurrantValue = (tvAnim1.childAnimations.last() as ValueAnimator).let {
            it.animatedValue as Float
        }
        btnCurrantValue = btnAnim1.animatedValue as Float
        tvAnim1.removeAllListeners()
        tvAnim1.end()
        btnAnim1.end()
        tvAnim2.play(getTextViewRotation(tvCurrantValue, 0f, true))
        tvAnim2.start()
        btnAnim2 = getButtonAnimation(btnCurrantValue, -100f)
        btnAnim2.start()
    }

    private fun runAnimation1() {
        tvCurrantValue = (tvAnim2.childAnimations.last() as ValueAnimator).let {
            it.animatedValue as Float
        }
        btnCurrantValue = btnAnim2.animatedValue as Float
        tvAnim2.removeAllListeners()
        tvAnim2.end()
        btnAnim2.end()
        tvAnim1.play(getTextViewRotation(tvCurrantValue, 360f, false))
        tvAnim1.start()
        btnAnim1 = getButtonAnimation(btnCurrantValue, 100f)
        btnAnim1.start()
    }

    private fun initAnimations() {
        tvAnim1 = AnimatorSet().apply {
            play(getTextViewRotation(0f, 360f, false))
            duration = 1000
            interpolator = DecelerateInterpolator()
        }
        tvAnim2 = AnimatorSet().apply {
            play(getTextViewRotation(360f, 0f, true))
            duration = 1000
            interpolator = DecelerateInterpolator()
        }
        btnAnim1 = getButtonAnimation(end = 100f)
        btnAnim2 = getButtonAnimation(end = -100f)
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

    private fun getButtonAnimation(start: Float = -1f, end: Float): ValueAnimator {
        return ValueAnimator.ofFloat(
            if (start == -1f) btn_show_or_hide.translationY else start,
            end
        ).apply {
            duration = 1000
            addUpdateListener {
                btn_show_or_hide.translationY = it.animatedValue as Float
            }
        }
    }

    private fun getTextViewSpringAnimation(view: View, position: Float): SpringAnimation {
        val springAnim = SpringAnimation(view, SpringAnimation.TRANSLATION_X)
        val springForce = SpringForce().apply {
            finalPosition = position
            stiffness = STIFFNESS_LOW
            dampingRatio = DAMPING_RATIO_MEDIUM_BOUNCY
        }
        springAnim.spring = springForce
        return springAnim
    }

    private fun animateImage1() {
        iv_1.animate().setDuration(1000).alpha(0f)
        iv_2.animate().setDuration(1000).rotation(360f)
        iv_3.animate().setDuration(1000).alpha(0f)
        iv_4.animate().setDuration(1000).rotation(360f)
        statusAnim = true
    }

    private fun animateImage2() {
        iv_1.animate().setDuration(1000).alpha(1f)
        iv_2.animate().setDuration(1000).rotation(0f)
        iv_3.animate().setDuration(1000).alpha(1f)
        iv_4.animate().setDuration(1000).rotation(0f)
        statusAnim = false
    }
}