package com.example.mobile_cue_things.objects

data class TableColumn<T>(
    val title: String,
    val valueProvider: (T) -> String
)
