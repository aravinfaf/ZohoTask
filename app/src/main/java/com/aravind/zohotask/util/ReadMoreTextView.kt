package com.aravind.zohotask.util

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import androidx.core.view.doOnLayout
import androidx.core.view.isInvisible
import com.aravind.zohotask.R

class ReadMoreTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var readMoreMaxLine = DEFAULT_MAX_LINE
    private var readMoreText = context.getString(R.string.read_more)
    private var readMoreColor = ContextCompat.getColor(context, R.color.red)

    private var originalText: CharSequence = ""
    private var collapseText: CharSequence = ""

    init {
        setupAttributes(context, attrs, defStyleAttr)
        setupListener()
    }

    private fun setupAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.ReadMoreTextView, defStyleAttr, 0)

        readMoreMaxLine =
            typedArray.getInt(R.styleable.ReadMoreTextView_readMoreMaxLine, readMoreMaxLine)
        readMoreText =
            typedArray.getString(R.styleable.ReadMoreTextView_readMoreText) ?: readMoreText
        readMoreColor =
            typedArray.getColor(R.styleable.ReadMoreTextView_readMoreColor, readMoreColor)
        typedArray.recycle()
    }

    private fun setupListener() {
        super.setOnClickListener {
           // toggle()
        }
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(text, type)
        doOnLayout {
            post { setupReadMore() }
        }
    }

    private fun setupReadMore() {
        if (needSkipSetupReadMore()) {
            return
        }
        originalText = text

        val adjustCutCount = getAdjustCutCount(readMoreMaxLine, readMoreText)
        val maxTextIndex = layout.getLineVisibleEnd(readMoreMaxLine - 1)
        val originalSubText = originalText.substring(0, maxTextIndex - 1 - adjustCutCount)

        collapseText = buildSpannedString {
            append(originalSubText)
            color(readMoreColor) { append(readMoreText) }
        }

        text = collapseText

    }

    private fun needSkipSetupReadMore(): Boolean =
        isInvisible || lineCount <= readMoreMaxLine  || text == null || text == collapseText

    private fun getAdjustCutCount(maxLine: Int, readMoreText: String): Int {

        val lastLineStartIndex = layout.getLineVisibleEnd(maxLine - 2) + 1
        val lastLineEndIndex = layout.getLineVisibleEnd(maxLine - 1)
        val lastLineText = text.substring(lastLineStartIndex, lastLineEndIndex)

        val bounds = Rect()
        paint.getTextBounds(lastLineText, 0, lastLineText.length, bounds)

        var adjustCutCount = -1
        do {
            adjustCutCount++
            val subText = lastLineText.substring(0, lastLineText.length - adjustCutCount)
            val replacedText = subText + readMoreText
            paint.getTextBounds(replacedText, 0, replacedText.length, bounds)
            val replacedTextWidth = bounds.width()
        } while (replacedTextWidth > width)

        return adjustCutCount
    }

    companion object {
        private const val DEFAULT_MAX_LINE = 4
    }

}