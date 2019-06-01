package com.go2santosh.keeppracticing.userinterfaces

import android.os.Bundle
import com.go2santosh.keeppracticing.R
import com.go2santosh.keeppracticing.models.quiz.QuizProvider
import kotlinx.android.synthetic.main.activity_quiz.*
import android.animation.ObjectAnimator
import android.support.v7.app.AppCompatActivity
import android.view.View

class QuizActivity : AppCompatActivity() {

    private lateinit var quizProvider: QuizProvider
    private val quizStatusFragment: QuizStatusFragment = QuizStatusFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        if (savedInstanceState == null) {
            addQuizStatusFragment()
        }

        buttonFinish.apply { setOnClickListener { finishQuiz() } }
        simpleKeyboard.setListeners({ textViewAnswer.text = it }, { submitAnswer() })
    }

    override fun onStart() {
        super.onStart()

        textViewTopic.text = intent.getExtras().getString("quizTopic")
        quizProvider = QuizProvider(
            quizFileName = intent.getExtras().getString("quizFileName"),
            progressHandler = { currentQuestionNumber: Int, totalQuestions: Int, totalCorrectAnswers: Int, totalIncorrectAnswers: Int, totalNotAttempted: Int ->
                runOnUiThread { quizStatusFragment.setProgress(currentQuestionNumber, totalQuestions, totalCorrectAnswers, totalIncorrectAnswers, totalNotAttempted) }
            },
            questionHandler = { question, keyboard ->
                runOnUiThread {
                    textViewQuestion.text = question
                    simpleKeyboard.setKeyboard(keyboard)
                }
            },
            quizTimerHandler = { seconds ->
                runOnUiThread {
                    quizStatusFragment.setTimer(seconds)
                }
            },
            timeoutHandler = { runOnUiThread { timeout() } },
            resultHandler = { notAttempted, correct, incorrect ->
                runOnUiThread { showResult(notAttempted, correct, incorrect) }
            }
        )
    }

    private fun timeout() {
        submitAnswer()
    }

    private fun showResult(notAttempted: Int, correct: Int, incorrect: Int) {
        layoutQuestion.visibility = View.GONE
        removeQuizStatusFragment()
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

    private fun addQuizStatusFragment() {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.layoutQuizStatus, quizStatusFragment, "quizStatus")
            .commit()
    }

    private fun removeQuizStatusFragment() {
        supportFragmentManager
            .beginTransaction()
            .remove(quizStatusFragment)
            .commit()
    }
}
