package com.go2santosh.keeppracticing.models.quizmodel

import java.util.*

class QuizProvider(
    val progressHandler: (message: String) -> Unit,
    val questionHandler: (question: String, keyboard: String) -> Unit,
    val quizTimerHandler: (remainingSeconds: Int) -> Unit,
    val timeoutHandler: () -> Unit,
    val resultHandler: (notAttempted: Int, correct: Int, incorrect: Int) -> Unit
) {

    private val timerDelay = 0L
    private val timerPeriod = 1000L
    private val defaultTimerTimeout = 60

    private var timer = Timer()
    private var timerTimeoutCountdown = defaultTimerTimeout
    private var currentQuestionIndex: Int = -1
    private var notAttempted = 0
    private var correct = 0
    private var incorrect = 0

    internal var isCompleted = false

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

    private fun startTimer() {
        timerTimeoutCountdown = defaultTimerTimeout
        timer = Timer("alertTimer", true)
        timer.scheduleAtFixedRate(
            object : TimerTask() {
                override fun run() {
                    quizTimerHandler(
                        if (timerTimeoutCountdown > 0) {
                            timerTimeoutCountdown--
                        } else {
                            stopTimer()
                            timeoutHandler()
                            0
                        }
                    )
                }
            },
            timerDelay,
            timerPeriod
        )
    }

    private fun stopTimer() {
        timer.cancel()
    }

    private fun resumeQuiz() {
        currentQuestionIndex++
        if (currentQuestionIndex in QuizDataProvider.questions.indices) {
            progressHandler(getProgress())
            questionHandler(QuizDataProvider.questions[currentQuestionIndex].question!!, QuizDataProvider.questions[currentQuestionIndex].keyboard!!)
            startTimer()
        } else {
            finishQuiz()
        }
    }

    fun submit(answers: List<String>) {
        stopTimer()
        checkAnswers(answers)
        if (!isCompleted) resumeQuiz()
    }

    private fun checkAnswers(answers: List<String>) {
        if (currentQuestionIndex in QuizDataProvider.questions.indices) {
            val validAnswers = answers.filter { answer -> !answer.isBlank() }
            if (validAnswers.isEmpty()) {
                notAttempted++
            } else {
                val question = QuizDataProvider.questions[currentQuestionIndex]
                var allMatched = true
                for (i in 0..question.answers.lastIndex) {
                    allMatched = question.answers[i] == answers[i]
                    if (!allMatched) break
                }
                if (allMatched) correct++ else incorrect++
            }
        }
    }

    fun finishQuiz() {
        stopTimer()
        if (!isCompleted) {
            isCompleted = true
            resultHandler(notAttempted, correct, incorrect)
        }
    }
}