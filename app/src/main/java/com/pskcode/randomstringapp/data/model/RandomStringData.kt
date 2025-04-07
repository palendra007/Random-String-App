package com.pskcode.randomstringapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RandomStringData(
    val value: String,
    val length: Int,
    val created: String
)