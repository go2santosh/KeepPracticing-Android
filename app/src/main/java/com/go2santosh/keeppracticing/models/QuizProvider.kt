package com.go2santosh.keeppracticing.models

import java.util.*

class QuizProvider(
    val progress: (String) -> Unit,
    val question: (String) -> Unit,
    val quizTimer: (Int) -> Unit,
    val alert: (String) -> Unit) {

    val TIMER_DELAY = 0L
    val TIMER_PERIOD = 1000L
    val TIMER_TIMEOUT = 15
    private var timer = Timer()
    private var timerTimeout = TIMER_TIMEOUT

    init {
        startQuiz()
    }

    protected fun finalize() {
        stopTimer()
    }

    private fun startQuiz() {
        progress(getProgress())
        question(getQuestion())
        startTimer()
    }

    private fun getProgress(): String {
        return "Question 1"
    }

    private fun getQuestion(): String {
        return "What is the sum of 276 + 25?"
    }

    private fun startTimer() {
        timerTimeout = TIMER_TIMEOUT
        timer = Timer("alertTimer", true)
        timer.scheduleAtFixedRate(
            object : TimerTask() {
                override fun run() {
                    quizTimer(if (timerTimeout > 0) timerTimeout-- else {stopTimer(); alert("Timed out!"); 0})
                }
            },
            TIMER_DELAY,
            TIMER_PERIOD
        )
    }

    private fun stopTimer() {
        timer.cancel()
    }

    fun resumeQuiz() {
        startQuiz()
    }
}