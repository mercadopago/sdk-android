package com.mercadopago.sdk.android.checkout.presentation.reviewconfirm

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.checkout.presentation.model.ReviewConfirmFooterSummaryUiModel
import com.mercadopago.sdk.android.components.MPText
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme

@Composable
internal fun ReviewConfirmFooterSummarySection(
    summary: ReviewConfirmFooterSummaryUiModel,
) {
    summary.products?.forEach { product ->
        FooterSummaryRow(
            label = product.label,
            amount = product.amount,
        )
    }

    summary.coupon?.let { coupon ->
        FooterSummaryRow(
            label = coupon.label,
            amount = coupon.amount,
        )
    }

    summary.interest?.let { interest ->
        FooterSummaryRow(
            label = interest.title,
            amount = interest.amount,
        )
    }
}

@Composable
private fun FooterSummaryRow(
    label: String,
    amount: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        MPText(
            text = label,
            style = MercadoPagoTheme.typography.body.default.medium,
            color = MercadoPagoTheme.color.text.primary,
        )
        MPText(
            text = amount,
            style = MercadoPagoTheme.typography.body.default.medium,
            color = MercadoPagoTheme.color.text.primary,
        )
    }
}
