package com.go2santosh.keeppracticing.models

import java.util.*

class QuizProvider(
    val progress: (String) -> Unit,
    val question: (String) -> Unit,
    val quizTimer: (Int) -> Unit,
    val timeout: (message: String) -> Unit,
    val result: (notAttempted: Int, correct: Int, incorrect: Int) -> Unit) {

    val TIMER_DELAY = 0L
    val TIMER_PERIOD = 1000L
    val TIMER_TIMEOUT = 5

    private var timer = Timer()
    private var timerTimeout = TIMER_TIMEOUT
    private var questions: List<QuestionEntity> = QuizDataProvider.questions
    private var currentQuestionIndex: Int = -1
    private var notAttempted = 0
    private var correct = 0
    private var incorrect = 0
    private var isCompleted = false

    init {
        startQuiz()
    }

    protected fun finalize() {
        stopTimer()
    }

    private fun startQuiz() {
        resumeQuiz()
    }

    private fun getProgress(): String {
        return "Question ${currentQuestionIndex + 1} | Correct $correct | Incorrect $incorrect | Not Attempted $notAttempted"
    }

    private fun getQuestion(): String {
        return questions[currentQuestionIndex].question!!
    }

    private fun startTimer() {
        timerTimeout = TIMER_TIMEOUT
        timer = Timer("alertTimer", true)
        timer.scheduleAtFixedRate(
            object : TimerTask() {
                override fun run() {
                    quizTimer(if (timerTimeout > 0) timerTimeout-- else {stopTimer(); timeout("Timed out!"); 0})
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
        currentQuestionIndex++
        if (currentQuestionIndex in questions.indices) {
            progress(getProgress())
            question(getQuestion())
            startTimer()
        } else {
            finishQuiz()
        }
    }

    fun submit(answers: List<String>) {
        checkAnswers(answers)
        if (!isCompleted) resumeQuiz()
    }

    fun checkAnswers(answers: List<String>) {
        if (currentQuestionIndex in questions.indices) {
            val validAnswers = answers.filter { answer -> !answer.isNullOrBlank() }
            if (validAnswers.isEmpty()) {
                notAttempted++
            }
            val question = questions[currentQuestionIndex].question!!
        }
    }

    fun finishQuiz() {
        stopTimer()
        if (!isCompleted) {
            isCompleted = true
            result(notAttempted, correct,incorrect)
        }
    }
}