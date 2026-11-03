package com.mercadopago.sdk.android.example.presentation.checkout

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun CheckoutFlowScreen(
    type: String,
    modifier: Modifier = Modifier,
) {
    CheckoutExampleScreen(flowType = type, modifier = modifier)
}
