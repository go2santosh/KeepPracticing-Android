package com.go2santosh.keeppracticing.userinterfaces

import android.app.Activity
import android.os.Bundle
import com.go2santosh.keeppracticing.R
import com.go2santosh.keeppracticing.models.QuizProvider
import kotlinx.android.synthetic.main.activity_quiz.*
import android.content.DialogInterface
import android.app.AlertDialog
import android.animation.ObjectAnimator

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
        buttonSubmit.apply { setOnClickListener { submitAnswer() } }
    }

    private fun timeout(message: String) {
        submitAnswer()
    }

    private fun showResult(notAttempted: Int, correct: Int, incorrect: Int) {
        val message = """
            Correct $correct
            Incorrect $incorrect
            Not Attempted $notAttempted
        """.trimIndent()
        AlertDialog.Builder(this)
            .setTitle("Quiz Completed!")
            .setMessage(message)
            .setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener { dialog, which ->
                finish()
            })
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }

    private fun getAnswers(): List<String> {
        return listOf(editTextAnswer.text.toString())
    }

    private fun finishQuiz() {
        quizProvider.finishQuiz()
    }

    private fun submitAnswer() {
        quizProvider.submit(getAnswers())
        editTextAnswer.text.clear()
        flipQuestionView()
    }

    private fun flipQuestionView() {
        val animation = ObjectAnimator.ofFloat(layoutQuestion, "rotationY", 0.0f, 360f)
        animation.duration = 500
        animation.start()
    }
}
