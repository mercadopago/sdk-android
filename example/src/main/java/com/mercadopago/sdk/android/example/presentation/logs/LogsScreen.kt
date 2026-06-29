package com.mercadopago.sdk.android.example.presentation.logs

import androidx.compose.foundation.background
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import com.mercadopago.sdk.android.example.domain.model.LogEntry
import com.mercadopago.sdk.android.example.domain.model.LogType

@Composable
internal fun DebugLogsScreen(
    modifier: Modifier = Modifier,
    viewModel: LogsViewModel = viewModel(),
) {
    val viewState by viewModel.viewState.collectAsState()

    DebugLogsScreen(
        viewState = viewState,
        onFilterChanged = viewModel::onFilterChanged,
        onClearLogsClick = viewModel::onClearLogs,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DebugLogsScreen(
    viewState: LogsViewState,
    onFilterChanged: (LogType) -> Unit,
    onClearLogsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            Text(
                text = "Debug Logs",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(vertical = 16.dp)
                    .fillMaxWidth(),
            )
        },
        bottomBar = {
            TextButton(
                onClick = onClearLogsClick,
                enabled = viewState.logsList.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Text("Clear Logs")
            }
        },
        modifier = modifier,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {

            TabRow(selectedTabIndex = LogType.entries.indexOf(viewState.currentFilter)) {
                LogType.entries.forEach { type ->
                    Tab(
                        selected = viewState.currentFilter == type,
                        onClick = { onFilterChanged(type) },
                        text = { Text(type.name) }
                    )
                }
            }

            LazyColumn {
                items(viewState.logsList) { entry ->
                    LogEntryRow(entry)
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
private fun LogEntryRow(entry: LogEntry) {
    var expanded by remember { mutableStateOf(false) }
    val clipboardManager = LocalClipboardManager.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                expanded = !expanded
            }
            .padding(12.dp)
    ) {
        Row {
            Text(
                text = entry.type.name,
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = entry.timestamp,
                style = MaterialTheme.typography.bodySmall,
            )
        }
        Text(
            text = entry.message,
            style = MaterialTheme.typography.bodyMedium,
        )

        if (expanded) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = entry.response,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable {
                        clipboardManager.setText(AnnotatedString(entry.response))
                    },
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PreviewDebugLogsApp() {
    DebugLogsScreen(
        viewState = LogsViewState(),
        onFilterChanged = { },
        onClearLogsClick = { },
    )
}
