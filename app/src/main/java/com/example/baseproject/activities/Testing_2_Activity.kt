package com.example.baseproject.activities

import android.animation.ValueAnimator
import android.view.Gravity
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.baseproject.R
import com.example.baseproject.bases.BaseActivity
import com.example.baseproject.databinding.ActivityTesting2Binding
import com.example.baseproject.fragments.ResultFragment
import com.example.baseproject.utils.SharedPrefManager

class Testing_2_Activity : BaseActivity<ActivityTesting2Binding>(ActivityTesting2Binding::inflate) {

    private var timeSelected: Int = 0
    val textViews = ArrayList<TextView>(emptyList())

    private var currentAnimator: ValueAnimator? = null
    private var isGameFinished: Boolean = false

    private var isWin: Boolean = false

    val alphabet = listOf(
        "A",
        "B",
        "C",
        "D",
        "E",
        "F",
        "G",
        "H",
        "I",
        "J",
        "L",
        "M",
        "N",
        "O",
        "P",
        "R",
        "S",
        "T",
        "U",
        "V"
    )

    private val onBackPressedCallBack = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }

    override fun initData() {
        timeSelected = SharedPrefManager.getInt("currentTime", 0)
    }

    override fun initView() {
        setupCircularAlphabet()
        onBackPressedDispatcher.addCallback(onBackPressedCallBack)

    }

    override fun initActionView() {

        binding.btnReset.setOnClickListener {
            resetGame()
        }

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.icStar.setOnClickListener {
            if (it.isSelected) return@setOnClickListener

            it.isSelected = true
            val duration = getDurationInMillis()
            loadProgress(duration, binding.progressCircular)
        }

        binding.tvTap.setOnClickListener {
            if (it.isSelected) return@setOnClickListener

            it.isSelected = true
            val duration = getDurationInMillis()
            loadProgress(duration, binding.progressCircular)
        }
    }

    private fun setupCircularAlphabet() {
        val rootLayout = binding.rootContainer
        val centerId = R.id.centerAnchor
        val radius = dpToPx(154)
        val angleStep = 360f / alphabet.size

        val itemSize = dpToPx(50)

        val defaultColor = ContextCompat.getColor(this@Testing_2_Activity, R.color.mainTextColor)

        val dimColor = androidx.core.graphics.ColorUtils.setAlphaComponent(defaultColor, 153)
        val customTypeface = ResourcesCompat.getFont(this, R.font.grold_rounded_slim)

        alphabet.forEach { char ->
            val tv = TextView(this).apply {
                id = View.generateViewId()
                text = char
                textSize = dpToPx(8).toFloat()
                setTextColor(defaultColor)
                typeface = customTypeface
                gravity = Gravity.CENTER
                setBackgroundResource(R.drawable.bg_soft_shadow_letter)
                //setPadding(0, 0, 0, dpToPx(4))
                layoutParams = ConstraintLayout.LayoutParams(itemSize, itemSize)
            }
            rootLayout.addView(tv, 0)
            textViews.add(tv)
        }

        val set = ConstraintSet()
        set.clone(rootLayout)

        textViews.forEachIndexed { index, view ->
            val angle = (index * angleStep).toFloat()
            set.constrainCircle(view.id, centerId, radius, angle)

            set.setRotation(view.id, angle + 180f)


            view.setOnClickListener {

                if (isGameFinished || it.isSelected) return@setOnClickListener
                it.isSelected = true
//                val backgroundResources =
//                    if (stateSelected) R.drawable.blur_bg_selected else R.drawable.bg_soft_shadow_letter
                it.setBackgroundResource(R.drawable.blur_bg_selected)
                (it as TextView).setTextColor(dimColor)

                //it.setPadding(0, 0, 0, dpToPx(4))
                val duration = getDurationInMillis()
                loadProgress(duration, binding.progressCircular)

                checkResult()
            }
        }

        set.applyTo(rootLayout)
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }

    private fun loadProgress(duration: Long, progressBar: CircularProgressView) {
        currentAnimator?.removeAllListeners()
        currentAnimator?.cancel()

        if (isGameFinished) return

        currentAnimator = ValueAnimator.ofFloat(0f, 100f).apply {
            this.duration = duration

            interpolator = LinearInterpolator()

            addUpdateListener { animation ->
                val value = animation.animatedValue as Float
                progressBar.setProgress(value)
            }

            addListener(object : android.animation.AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: android.animation.Animator) {
                    super.onAnimationEnd(animation)
                    if (!isGameFinished) {
                        showGameResult(false)
                    }
                }
            })
        }
        currentAnimator?.start()
    }

    private fun checkResult() {
        val allSelected = textViews.all { it.isSelected }

        if (allSelected) {
            showGameResult(true)
        }
    }

    private fun showGameResult(isSuccess: Boolean) {
        if (isGameFinished) return
        isGameFinished = true

        currentAnimator?.cancel()

        val dialog = ResultFragment.newInstance(isSuccess)
        dialog.show(supportFragmentManager, ResultFragment.TAG)
    }

    private fun getDurationInMillis(): Long {

        return when (timeSelected) {
            0 -> 10000L
            1 -> 20000L
            2 -> 30000L
            else -> 10000L
        }

    }

    fun resetGame() {
        isGameFinished = false
        currentAnimator?.removeAllListeners()
        currentAnimator?.cancel()

        val defaultColor = ContextCompat.getColor(this, R.color.mainTextColor)

        textViews.forEach { view ->
            view.isSelected = false
            view.setBackgroundResource(R.drawable.bg_soft_shadow_letter)
            view.setTextColor(defaultColor)
        }

        val duration = getDurationInMillis()
        loadProgress(duration, binding.progressCircular)
    }

    override fun onDestroy() {
        super.onDestroy()
        currentAnimator?.removeAllListeners()
        currentAnimator?.cancel()
        currentAnimator = null
    }

}