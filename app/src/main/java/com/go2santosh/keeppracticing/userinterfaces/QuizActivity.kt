package com.go2santosh.keeppracticing.userinterfaces

import android.app.Activity
import android.os.Bundle
import com.go2santosh.keeppracticing.R
import com.go2santosh.keeppracticing.models.quizmodel.QuizProvider
import kotlinx.android.synthetic.main.activity_quiz.*
import android.animation.ObjectAnimator
import android.view.View

class QuizActivity : Activity() {

    private val quizProvider = QuizProvider(
        progressHandler = { runOnUiThread { textViewProgress.text = it } },
        questionHandler = { question, keyboard ->
            runOnUiThread {
                textViewQuestion.text = question
                simpleKeyboard.setKeyboard(keyboard)
            }
        },
        quizTimerHandler = { seconds ->
            runOnUiThread {
                textViewTimer.text = getString(R.string.timer_with_1_replacable).replace("$0", seconds.toString())
            }
        },
        timeoutHandler = { runOnUiThread { timeout() } },
        resultHandler = { notAttempted, correct, incorrect ->
            runOnUiThread { showResult(notAttempted, correct, incorrect) }
        })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        buttonFinish.apply { setOnClickListener { finishQuiz() } }
        simpleKeyboard.setListeners({ textViewAnswer.text = it }, { submitAnswer() })
    }

    private fun timeout() {
        submitAnswer()
    }

    private fun showResult(notAttempted: Int, correct: Int, incorrect: Int) {
        layoutQuestion.visibility = View.GONE
        textViewResult.text = getString(R.string.quiz_completed_with_3_replacables)
            .replace("$0", correct.toString())
            .replace("$1", incorrect.toString())
            .replace("$2", notAttempted.toString())
            .trimIndent()
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
