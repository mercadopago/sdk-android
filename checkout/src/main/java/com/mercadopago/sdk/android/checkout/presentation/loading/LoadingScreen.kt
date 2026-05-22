package com.mercadopago.sdk.android.checkout.presentation.loading

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mercadopago.sdk.android.components.MPProgressIndicator
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemes

/**
 * Tela de loading do checkout — renderiza um de três modos conforme [type]:
 * - [LoadingType.Spinner]: indicador circular centralizado
 * - [LoadingType.SkeletonMajor]: skeleton de tela cheia (header, seller, list rows, summary)
 * - [LoadingType.SkeletonMinor]: skeleton enxuto (header, área única, summary)
 */
@Composable
internal fun LoadingScreen(
    type: LoadingType,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MercadoPagoTheme.color.background.primary),
        contentAlignment = Alignment.Center,
    ) {
        when (type) {
            LoadingType.Spinner -> MPProgressIndicator()
            LoadingType.SkeletonMajor -> SkeletonMajorContent(modifier = Modifier.fillMaxSize())
            LoadingType.SkeletonMinor -> SkeletonMinorContent(modifier = Modifier.fillMaxSize())
        }
    }
}

@Preview(name = "Loading - Spinner")
@Composable
private fun LoadingScreenSpinnerPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Default) {
        LoadingScreen(type = LoadingType.Spinner)
    }
}

@Preview(name = "Loading - Skeleton Major")
@Composable
private fun LoadingScreenMajorPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Default) {
        LoadingScreen(type = LoadingType.SkeletonMajor)
    }
}

@Preview(name = "Loading - Skeleton Minor")
@Composable
private fun LoadingScreenMinorPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Default) {
        LoadingScreen(type = LoadingType.SkeletonMinor)
    }
}
