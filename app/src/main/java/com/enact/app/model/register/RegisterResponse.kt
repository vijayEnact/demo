package com.enact.app.model.register

data class RegisterResponse(
    val account_type: String,
    val email: String,
    val email_verified: String,
    val expires_in: Int,
    val name: String,
    val phone: String,
    val phone_verified: String,
    val token: String,
    val token_type: String,
    val type: String,
    val user_id: Int
)