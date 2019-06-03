package com.go2santosh.keeppracticing.models.quiz

import java.util.*

class QuizProvider(
    val quizFileName: String,
    val progressHandler: (currentQuestionNumber: Int, totalQuestions: Int, totalCorrectAnswers: Int, totalIncorrectAnswers: Int, totalNotAttempted: Int) -> Unit,
    val questionHandler: (question: String, keyboard: String) -> Unit,
    val quizTimerHandler: (remainingSeconds: Int) -> Unit,
    val timeoutHandler: () -> Unit,
    val resultHandler: (notAttempted: Int, correct: Int, incorrect: Int) -> Unit
) {

    private val timerDelay = 0L
    private val timerPeriod = 1000L
    private val defaultTimerTimeout = 90

    private var timer = Timer()
    private var timerTimeoutCountdown = defaultTimerTimeout
    private var currentQuestionIndex: Int = -1
    private var notAttempted = 0
    private var correct = 0
    private var incorrect = 0
    private val quizQuestions: ArrayList<QuestionEntity> = QuizDataProvider(dataFileName = "data/$quizFileName").questions

    internal var isCompleted = false

    init {
        quizQuestions.shuffle()
        startQuiz()
    }

    protected fun finalize() {
        stopTimer()
    }

    private fun startQuiz() {
        resumeQuiz()
    }

    private fun startTimer() {
        val questionTimeout = quizQuestions[currentQuestionIndex].timeout ?: defaultTimerTimeout
        timerTimeoutCountdown = if (questionTimeout > 0) questionTimeout else defaultTimerTimeout
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
        if (currentQuestionIndex in quizQuestions.indices) {
            progressHandler(
                currentQuestionIndex + 1,
                quizQuestions.size,
                correct,
                incorrect,
                notAttempted
            )
            questionHandler(
                quizQuestions[currentQuestionIndex].question!!,
                quizQuestions[currentQuestionIndex].keyboard!!
            )
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
        if (currentQuestionIndex in quizQuestions.indices) {
            val validAnswers = answers.filter { answer -> !answer.isBlank() }
            if (validAnswers.isEmpty()) {
                notAttempted++
            } else {
                val question = quizQuestions[currentQuestionIndex]
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