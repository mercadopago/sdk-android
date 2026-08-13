package com.mercadopago.sdk.android.checkout.presentation.methodselection

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mercadopago.sdk.android.checkout.domain.model.MethodSelectionOption
import com.mercadopago.sdk.android.checkout.presentation.state.MethodSelectionScreenState
import com.mercadopago.sdk.android.components.MPHeader

@Composable
internal fun MethodSelectionScreen(
    screenState: MethodSelectionScreenState,
    onOptionTap: (MethodSelectionOption) -> Unit,
    onCtaTap: () -> Unit,
    onBack: () -> Unit,
) {
    BackHandler(onBack = onBack)
    MPHeader(
        title = screenState.headerTitle,
        onBackClick = onBack,
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(screenState.options) { option ->
                    MethodSelectionOptionRow(
                        option = option,
                        isArrowLayout = screenState.isArrowLayout,
                        isSelected = option.id == screenState.selectedOptionId,
                        onTap = { tappedOption -> onOptionTap(tappedOption) },
                    )
                }
            }
            Text(text = screenState.footerState.title)
            screenState.footerState.subtitle?.let { Text(text = it) }
            screenState.footerState.buttonLabel?.let { label ->
                Button(
                    onClick = onCtaTap,
                    enabled = screenState.footerState.buttonState?.enabled ?: false,
                ) {
                    Text(label)
                }
            }
        }
    }
}
