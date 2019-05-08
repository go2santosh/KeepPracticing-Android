package com.go2santosh.keeppracticing.models.quizmodel

import java.util.*

class QuizProvider(
    val progressHandler: (String) -> Unit,
    val questionHandler: (String) -> Unit,
    val quizTimerHandler: (Int) -> Unit,
    val timeoutHandler: () -> Unit,
    val resultHandler: (notAttempted: Int, correct: Int, incorrect: Int) -> Unit
) {

    private val timerDelay = 0L
    private val timerPeriod = 1000L
    private val defaultTimerTimeout = 60

    private var timer = Timer()
    private var timerTimeoutCountdown = defaultTimerTimeout
    private var questions: List<QuestionEntity> =
        QuizDataProvider.questions
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

    private fun getQuestion(): String {
        return questions[currentQuestionIndex].question!!
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
        if (currentQuestionIndex in questions.indices) {
            progressHandler(getProgress())
            questionHandler(getQuestion())
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
        if (currentQuestionIndex in questions.indices) {
            val validAnswers = answers.filter { answer -> !answer.isBlank() }
            if (validAnswers.isEmpty()) {
                notAttempted++
            } else {
                val question = questions[currentQuestionIndex]
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