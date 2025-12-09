package com.example.baseproject.activities

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class CircularProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private var progress = 0f
    private var maxProgress = 100f

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        color = Color.parseColor("#AFFF3F")
        strokeWidth = 45f
    }

    private val rectF = RectF()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val padding = paint.strokeWidth / 2
        rectF.set(padding, padding, width - padding, height - padding)

        val sweepAngle = -360 * (progress / maxProgress)
        canvas.drawArc(rectF, -90f, sweepAngle, false, paint)
    }

    fun setProgress(current: Float) {
        this.progress = current
        invalidate()
    }

    fun setStrokeWidth(width: Float) {
        paint.strokeWidth = width
        invalidate()
    }

    fun setColor(colorHex: String) {
        paint.color = Color.parseColor(colorHex)
        invalidate()
    }
}