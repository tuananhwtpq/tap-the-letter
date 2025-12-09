package com.example.baseproject.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import com.example.baseproject.R

class StrokedTextView : AppCompatTextView {

    //region Constructors

    constructor(ctx: Context) : super(ctx, null) {
        init()
    }

    constructor(ctx: Context, attr: AttributeSet?) : super(ctx, attr, 0) {
        getStyledAttributes(attr)
        init()
    }

    constructor(ctx: Context, attr: AttributeSet?, defStyleAttr: Int) : super(
        ctx,
        attr,
        defStyleAttr
    ) {
        getStyledAttributes(attr)
        init()
    }
    //endregion

    //region Members

    private var calcWidth = 0
    private var calcHeight = 0

    var strokeWidth = 4f
    private var strokeColor = Color.RED

    private lateinit var staticLayout: StaticLayout

    private val staticLayoutPaint by lazy {
        TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = this@StrokedTextView.textSize
            typeface = this@StrokedTextView.typeface
        }
    }
    //endregion

    //region Methods

    private fun init() {
        // Set default gravity to center
        gravity = Gravity.CENTER
    }

    private fun getStyledAttributes(attr: AttributeSet?) {
        context.obtainStyledAttributes(attr, R.styleable.StrokedTextView).apply {
            strokeWidth =
                getDimensionPixelSize(R.styleable.StrokedTextView_strokeThickness, 4).toFloat()
            strokeColor = getColor(R.styleable.StrokedTextView_strokeColor, Color.RED)
            recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Measure the width and height without adjusting padding
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        calcWidth = widthSize - paddingStart - paddingEnd + strokeWidth.toInt() * 2
        calcHeight = heightSize - paddingTop - paddingBottom + strokeWidth.toInt() * 2
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas) {
        reinitializeStaticLayout()
        with(canvas) {
            save()
            translate(paddingStart.toFloat() + strokeWidth, paddingTop.toFloat())

            // Draw stroke first
            staticLayoutPaint.configureForStroke()
            staticLayout.draw(this)

            // Draw text
            staticLayoutPaint.configureForFill()
            staticLayout.draw(this)

            restore()
        }
    }

    private fun reinitializeStaticLayout() {
        Log.e("TAG", "reinitializeStaticLayout: ${layoutDirection == LAYOUT_DIRECTION_RTL}")
        var textAlign = Layout.Alignment.ALIGN_CENTER
        if (Common.getSelectedLanguage().key == "ar") {
            textAlign = Layout.Alignment.ALIGN_CENTER
        }
        staticLayout =
            StaticLayout.Builder
                .obtain(text, 0, text.length, staticLayoutPaint, calcWidth)
                .setLineSpacing(lineSpacingExtra, lineSpacingMultiplier)
                .setAlignment(textAlign)
                .build()
    }

    private fun Paint.configureForFill() {
        style = Paint.Style.FILL
        color = textColors.defaultColor
    }

    private fun Paint.configureForStroke() {
        style = Paint.Style.STROKE
        color = strokeColor
        strokeWidth = this@StrokedTextView.strokeWidth
    }

    fun setStrokeColor(color: Int) {
        strokeColor = color
        invalidate() // Request to redraw the view
    }

    //endregion
}

