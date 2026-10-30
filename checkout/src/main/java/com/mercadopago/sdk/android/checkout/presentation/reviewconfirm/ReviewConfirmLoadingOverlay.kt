package com.mercadopago.sdk.android.checkout.presentation.reviewconfirm

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mercadopago.sdk.android.components.MPProgressIndicator

@Composable
internal fun ReviewConfirmLoadingOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center,
    ) {
        MPProgressIndicator()
    }
}
