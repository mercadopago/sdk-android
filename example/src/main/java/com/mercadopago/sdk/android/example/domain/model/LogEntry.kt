package com.mercadopago.sdk.android.example.domain.model

data class LogEntry(
    val type: LogType,
    val message: String,
    val response: String,
    val timestamp: String,
)
