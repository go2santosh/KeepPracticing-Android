package com.go2santosh.keeppracticing.views

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import com.go2santosh.keeppracticing.R
import kotlinx.android.synthetic.main.view_keyboard_simple.view.*
import kotlinx.android.synthetic.main.view_keyboard_simple.view.buttonSubmit

class SimpleKeyboardView(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private val inputButtons: List<Button> by lazy {
        listOf(
            button1,
            button2,
            button3,
            button4,
            button5,
            button6,
            button7,
            button8,
            button9,
            button0,
            buttonDot,
            buttonComma
        )
    }

    private var output: String = ""
    private var _textUpdateListener: ((String) -> Unit)? = null
    private var _submitListener: (() -> Unit)? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_keyboard_simple, this, true)
        attrs?.let {
            val styledAttributes = context.obtainStyledAttributes(it, R.styleable.SimpleKeyboardView, 0, 0)
            val textSize = styledAttributes.getDimensionPixelSize(R.styleable.SimpleKeyboardView_textSize, 24)
            (inputButtons + buttonDelete + buttonSubmit).forEach { button ->
                button.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
            }
            buttonSubmit.text = styledAttributes.getString(R.styleable.SimpleKeyboardView_submitText)
            buttonDelete.text = styledAttributes.getString(R.styleable.SimpleKeyboardView_deleteText)
            styledAttributes.recycle()
        }
    }

    fun setListeners(
        textUpdateListener: (String) -> Unit,
        submitListener: () -> Unit
    ) {

        _textUpdateListener = textUpdateListener
        _submitListener = submitListener

        inputButtons.forEach { button ->
            button.apply {
                setOnClickListener {
                    output = "$output${(it as Button).text}"
                    _textUpdateListener?.invoke(output)
                }
            }
        }

        buttonDelete.apply {
            setOnClickListener {
                output = output.dropLast(1)
                _textUpdateListener?.invoke(output)
            }
        }

        buttonSubmit.apply { setOnClickListener { _submitListener?.invoke() } }
    }

    fun clear() {
        output = ""
        _textUpdateListener?.invoke(output)
    }
}