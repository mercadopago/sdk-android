package com.mercadopago.sdk.android.checkout.presentation.loading

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mercadopago.sdk.android.components.MPProgressIndicator
import com.mercadopago.sdk.android.components.model.MPProgressIndicatorSize
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemes

@Composable
internal fun LoadingScreen(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MercadoPagoTheme.color.background.primary),
        contentAlignment = Alignment.Center,
    ) {
        MPProgressIndicator(size = MPProgressIndicatorSize.Large)
    }
}

@Preview
@Composable
private fun LoadingScreenPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Default) {
        LoadingScreen()
    }
}
