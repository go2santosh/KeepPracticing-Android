package com.go2santosh.keeppracticing.userinterfaces

import android.app.Activity
import android.os.Bundle
import com.go2santosh.keeppracticing.R
import com.go2santosh.keeppracticing.models.QuizProvider
import kotlinx.android.synthetic.main.activity_quiz.*
import android.content.DialogInterface
import android.app.AlertDialog

class QuizActivity : Activity() {

    val quizProvider = QuizProvider(
        progress = { runOnUiThread { textViewProgress.text = it } },
        question = { runOnUiThread { textViewQuestion.text = it } },
        quizTimer = { runOnUiThread { textViewTimer.text = "Timer: $it" } },
        timeout = { runOnUiThread { showAlert(it) }},
        result = { notAttempted, correct, incorrect -> runOnUiThread { showResult(notAttempted, correct, incorrect) }})

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_quiz)
    }

    private fun showAlert(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Quiz Alert")
            .setMessage(message)
            .setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener { dialog, which ->
                val answers = ArrayList<String>()
                val answer = editTextAnswer.text.toString()
                if (!answer.isNullOrBlank()) {
                    answers.add(editTextAnswer.text.toString())
                }
                quizProvider.submit(answers)
            })
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }

    private fun showResult(notAttempted: Int, correct: Int, incorrect: Int) {
        showAlert("Quiz Completed! Correct=$correct, Incorrect=$incorrect, Not Attempted=$notAttempted")
    }
}
