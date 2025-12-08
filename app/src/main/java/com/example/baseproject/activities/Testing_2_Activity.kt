package com.example.baseproject.activities

import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.baseproject.R
import com.example.baseproject.bases.BaseActivity
import com.example.baseproject.databinding.ActivityTesting2Binding

class Testing_2_Activity : BaseActivity<ActivityTesting2Binding>(ActivityTesting2Binding::inflate) {
    override fun initData() {
    }

    override fun initView() {
        setupCircularAlphabet()
        val progressBar = binding.progressCircular
        var currentProgress = 0f

        val animator = ValueAnimator.ofFloat(0f, 100f).apply {
            duration = 5000
            addUpdateListener { animation ->
                val value = animation.animatedValue as Float
                progressBar.setProgress(value)
            }
        }
        animator.start()
    }

    override fun initActionView() {
    }

    private fun setupCircularAlphabet() {
        val rootLayout = binding.rootContainer
        val centerId = R.id.centerAnchor
        val radius = dpToPx(154)
        val alphabet = listOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "L", "M", "N", "O", "P", "R", "S", "T", "U", "V")
        val angleStep = 360f / alphabet.size

        val itemSize = dpToPx(50)

        val defaultColor = ContextCompat.getColor(this@Testing_2_Activity, R.color.mainTextColor)

        val dimColor = androidx.core.graphics.ColorUtils.setAlphaComponent(defaultColor, 153)
        val customTypeface = ResourcesCompat.getFont(this, R.font.grold_rounded_slim)

        val textViews = ArrayList<TextView>(emptyList())
        alphabet.forEach { char ->
            val tv = TextView(this).apply {
                id = View.generateViewId()
                text = char
                textSize = dpToPx(8).toFloat()
                setTextColor(defaultColor)
                typeface = customTypeface
                gravity = Gravity.CENTER
                setBackgroundResource(R.drawable.bg_white_rounded)
                layoutParams = ConstraintLayout.LayoutParams(itemSize, itemSize)
            }
            rootLayout.addView(tv, 0)
            textViews.add(tv)
        }

        val set = ConstraintSet()
        set.clone(rootLayout)

        textViews.forEachIndexed { index, view ->
            val angle = (index * angleStep).toFloat()
            set.constrainCircle(view.id, centerId, radius, angle )

            set.setRotation(view.id, angle + 180f)


            view.setOnClickListener {
                it.isSelected = !it.isSelected
                val backgroundResources = if (it.isSelected) R.drawable.blur_bg_selected else R.drawable.bg_white_rounded
                it.setBackgroundResource(backgroundResources)
                (it as TextView).setTextColor(if (it.isSelected) dimColor else defaultColor)
            }
        }

        set.applyTo(rootLayout)
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }

}