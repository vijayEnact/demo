package com.enact.app.model

data class CommonObject<T>(
    val `data`: T,
    val message: String,
    val success: Boolean,
    val token:String?="",
    val status: Int?=0,
)