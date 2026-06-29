package com.mercadopago.sdk.android.example.utils

import android.util.Log
import com.mercadopago.sdk.android.example.domain.model.LogEntry
import com.mercadopago.sdk.android.example.domain.model.LogType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.Locale

object LogHelper {

    private val _logs = MutableStateFlow<List<LogEntry>>(emptyList())
    val logs: StateFlow<List<LogEntry>> = _logs

    fun log(
        message: String,
        type: LogType,
        response: String,
    ) {
        val newLog = LogEntry(
            message = message,
            type = type,
            response = response,
            timestamp = SimpleDateFormat("h:mm:ss a", Locale.getDefault()).format(System.currentTimeMillis()),
        )
        Log.e(message, response)
        _logs.value = _logs.value + newLog
    }

    fun clearLogs() {
        _logs.value = emptyList()
    }
}
