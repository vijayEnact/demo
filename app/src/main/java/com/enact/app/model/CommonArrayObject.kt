package com.app.demo.model

data class CommonArrayObject<T>(
    val `data`: List<T>,
    val message: String,
    val success: Boolean,
    val status: Int?=0,
    val token:String?=""
)