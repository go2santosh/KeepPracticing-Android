package com.go2santosh.keeppracticing.userinterfaces

import android.app.Activity
import android.os.Bundle
import com.go2santosh.keeppracticing.R
import com.go2santosh.keeppracticing.models.QuizProvider
import kotlinx.android.synthetic.main.activity_quiz.*
import android.animation.ObjectAnimator
import android.view.View

class QuizActivity : Activity() {

    private val quizProvider = QuizProvider(
        progress = { runOnUiThread { textViewProgress.text = it } },
        question = { runOnUiThread { textViewQuestion.text = it } },
        quizTimer = { runOnUiThread { textViewTimer.text = "Timer: $it" } },
        timeout = { runOnUiThread { timeout(it) }},
        result = { notAttempted, correct, incorrect -> runOnUiThread { showResult(notAttempted, correct, incorrect) }})

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        buttonFinish.apply { setOnClickListener { finishQuiz() } }
        simpleKeyboard.setListeners({ textViewAnswer.text = it }, { submitAnswer() })
    }

    private fun timeout(message: String) {
        submitAnswer()
    }

    private fun showResult(notAttempted: Int, correct: Int, incorrect: Int) {
        layoutQuestion.visibility = View.GONE
        textViewResult.text = """
            Quiz Completed!

            Correct $correct
            Incorrect $incorrect
            Not Attempted $notAttempted
        """.trimIndent()
    }

    private fun getAnswers(): List<String> {
        return listOf(textViewAnswer.text.toString())
    }

    private fun finishQuiz() {
        if (!quizProvider.isCompleted) {
            quizProvider.finishQuiz()
        } else {
            finish()
        }
    }

    private fun submitAnswer() {
        quizProvider.submit(getAnswers())
        simpleKeyboard.clear()
        flipQuestionView()
    }

    private fun flipQuestionView() {
        val animation = ObjectAnimator.ofFloat(layoutQuestion, "rotationY", 0.0f, 360f)
        animation.duration = 500
        animation.start()
    }
}
