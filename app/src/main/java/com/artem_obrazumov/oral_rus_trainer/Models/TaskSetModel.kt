package com.artem_obrazumov.oral_rus_trainer.Models

data class TaskSetModel(
    val Questions1: String = "",
    val Questions2: String = "",
    val Questions3: String = "",
    val id: String = "",
    val setName: String = "",
    val task2: String = "",
    val task3Image: String = "",
    val task3ImageText: String = "",
    val task3SpeakingText: String = "",
    val task3StoryText: String = "",
    val text1: String = "",
    val text1Image: String = "",
    val text1Task: String = ""
)