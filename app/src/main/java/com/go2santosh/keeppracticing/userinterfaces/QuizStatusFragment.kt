package com.go2santosh.keeppracticing.userinterfaces

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.go2santosh.keeppracticing.R
import kotlinx.android.synthetic.main.fragment_quiz_status.*

class QuizStatusFragment: Fragment() {

    companion object {
        fun newInstance(): QuizStatusFragment {
            return QuizStatusFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_quiz_status, container, false)
        return view
    }

    fun setTimer(seconds: Int) {
        if (textViewQuizStatusTimer != null) {
            textViewQuizStatusTimer.text = getString(R.string.timer_with_1_replacable).replace("$0", seconds.toString())
        }
    }

    fun setProgress(currentQuestionNumber: Int, totalQuestions: Int, totalCorrectAnswers: Int, totalIncorrectAnswers: Int, totalNotAttempted: Int) {
        textViewStatus.text = getString(R.string.quiz_status)
            .replace("$0", currentQuestionNumber.toString())
            .replace("$1", totalQuestions.toString())
            .replace("$2", totalCorrectAnswers.toString())
            .replace("$3", totalIncorrectAnswers.toString())
            .replace("$4", totalNotAttempted.toString())
    }
}