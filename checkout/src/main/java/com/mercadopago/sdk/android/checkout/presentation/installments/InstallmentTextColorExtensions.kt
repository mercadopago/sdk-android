package com.mercadopago.sdk.android.checkout.presentation.installments

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentTextColor
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme

@Composable
internal fun InstallmentTextColor?.toColor(): Color? =
    when (this) {
        InstallmentTextColor.Positive -> MercadoPagoTheme.color.feedback.positive.textLoud
        null -> null
    }
