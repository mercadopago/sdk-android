package com.mercadopago.sdk.android.example.presentation.logs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadopago.sdk.android.example.domain.model.LogType
import com.mercadopago.sdk.android.example.utils.LogHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class LogsViewModel : ViewModel() {

    private val _viewState = MutableStateFlow<LogsViewState>(LogsViewState())
    val viewState: StateFlow<LogsViewState> = _viewState

    init {
        viewModelScope.launch {
            LogHelper.logs.collect {
                val filter = _viewState.value.currentFilter
                _viewState.value = _viewState.value.copy(logsList = it.filter { filter == LogType.All || it.type == filter })
            }
        }
    }

    fun onClearLogs() {
        LogHelper.clearLogs()
    }

    fun onFilterChanged(filter: LogType) {
        _viewState.value = _viewState.value.copy(
            currentFilter = filter,
            logsList =  LogHelper.logs.value.filter { filter == LogType.All || it.type == filter },
        )
    }
}
