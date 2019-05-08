package com.go2santosh.keeppracticing.views

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import com.go2santosh.keeppracticing.R
import kotlinx.android.synthetic.main.view_keyboard_simple.view.*

class SimpleKeyboardView(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private var output: String = ""
    private var _textUpdateListener: ((String) -> Unit)? = null
    private var _submitListener: (() -> Unit)? = null
    private var textSize = resources.getDimensionPixelSize(R.dimen.text_size_medium)
    private var submitText = resources.getString(R.string.submit)
    private var deleteText = resources.getString(R.string.delete)
    private var clearText = resources.getString(R.string.clear)

    init {
        LayoutInflater.from(context).inflate(R.layout.view_keyboard_simple, this, true)
        attrs?.let {
            val styledAttributes = context.obtainStyledAttributes(it, R.styleable.SimpleKeyboardView, 0, 0)
            textSize = styledAttributes.getDimensionPixelSize(
                R.styleable.SimpleKeyboardView_textSize,
                resources.getDimensionPixelSize(R.dimen.text_size_medium)
            )
            val submitTextValue = styledAttributes.getString(R.styleable.SimpleKeyboardView_submitText)
            if (submitTextValue != null) {
                submitText = submitTextValue
            }
            val deleteTextValue = styledAttributes.getString(R.styleable.SimpleKeyboardView_deleteText)
            if (deleteTextValue != null) {
                deleteText = deleteTextValue
            }
            val clearTextValue = styledAttributes.getString(R.styleable.SimpleKeyboardView_clearText)
            if (clearTextValue != null) {
                clearText = clearTextValue
            }
            val firstLineKeyValues = styledAttributes.getString(R.styleable.SimpleKeyboardView_firstLineKeyValues)
            if (firstLineKeyValues != null) {
                addKeysToViewGroup(
                    firstLineKeyValues,
                    viewGroupFirstLineKeys as LinearLayout
                )
            }
            val secondLineKeyValues = styledAttributes.getString(R.styleable.SimpleKeyboardView_secondLineKeyValues)
            if (secondLineKeyValues != null) {
                addKeysToViewGroup(
                    secondLineKeyValues,
                    viewGroupSecondLineKeys as LinearLayout
                )
            }
            styledAttributes.recycle()
        }
    }

    fun setListeners(
        textUpdateListener: (String) -> Unit,
        submitListener: () -> Unit
    ) {
        _textUpdateListener = textUpdateListener
        _submitListener = submitListener
    }

    fun clear() {
        output = ""
        _textUpdateListener?.invoke(output)
    }

    private fun addKeysToViewGroup(values: String, viewGroup: ViewGroup) {
        val keyValues = values.split("|")
        keyValues.forEach { keyValue ->
            val button = when (keyValue.trim()) {
                "Submit" ->
                    createButton(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.MATCH_PARENT,
                        2F,
                        keyValue,
                        textSize.toFloat()
                    ).apply {
                        text = submitText
                        setOnClickListener { _submitListener?.invoke() }
                    }
                "Delete" ->
                    createButton(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.MATCH_PARENT,
                        2F,
                        keyValue,
                        textSize.toFloat()
                    ).apply {
                        text = deleteText
                        setOnClickListener {
                            output = output.dropLast(1)
                            _textUpdateListener?.invoke(output)
                        }
                    }
                "Clear" ->
                    createButton(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.MATCH_PARENT,
                        2F,
                        keyValue,
                        textSize.toFloat()
                    ).apply {
                        text = clearText
                        setOnClickListener {
                            output = ""
                            _textUpdateListener?.invoke(output)
                        }
                    }
                else ->
                    createButton(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.MATCH_PARENT,
                        1F,
                        keyValue,
                        textSize.toFloat()
                    ).apply {
                        setOnClickListener {
                            output = "$output${(it as Button).text}"
                            _textUpdateListener?.invoke(output)
                        }
                    }
            }
            viewGroup.addView(button)
        }
    }

    private fun createButton(
        width: Int, height: Int, weight: Float,
        text: String,
        textSize: Float,
        isAllCaps: Boolean = false
    ): Button {
        val button = Button(context)
        button.layoutParams = LayoutParams(width, height, weight)
        button.text = text.trim()
        button.isAllCaps = isAllCaps
        button.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
        return button
    }
}