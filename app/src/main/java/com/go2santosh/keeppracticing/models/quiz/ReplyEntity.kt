package com.go2santosh.keeppracticing.models.quiz

data class ReplyEntity (
    val question: String,
    val answers: ArrayList<String>,
    val reply: List<String>
)