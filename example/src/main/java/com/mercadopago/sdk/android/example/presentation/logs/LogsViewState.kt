package com.mercadopago.sdk.android.example.presentation.logs

import com.mercadopago.sdk.android.example.domain.model.LogEntry
import com.mercadopago.sdk.android.example.domain.model.LogType

internal data class LogsViewState(
    val logsList: List<LogEntry> = emptyList(),
    val currentFilter: LogType = LogType.All,
)
