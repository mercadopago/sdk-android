package com.mercadopago.sdk.android.checkout.presentation.reviewconfirm

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mercadopago.sdk.android.checkout.domain.model.FooterSummaryInterest
import com.mercadopago.sdk.android.checkout.domain.model.FooterSummaryRow
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmFooterSummary
import com.mercadopago.sdk.android.components.MPText
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme

@Composable
internal fun ReviewConfirmSummarySection(
    summary: ReviewConfirmFooterSummary,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = MercadoPagoTheme.spacing.paddings.xtiny,
                vertical = MercadoPagoTheme.spacing.paddings.xnano,
            ),
        verticalArrangement = Arrangement.spacedBy(MercadoPagoTheme.spacing.paddings.xnano),
    ) {
        summary.products?.forEach { row -> ReviewConfirmSummaryRow(row = row) }
        summary.coupon?.let { coupon -> ReviewConfirmSummaryRow(row = coupon) }
        summary.interest?.let { interest -> ReviewConfirmSummaryInterestRow(interest = interest) }
    }
}

@Composable
private fun ReviewConfirmSummaryRow(
    row: FooterSummaryRow,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        MPText(
            text = row.label,
            style = MercadoPagoTheme.typography.body.default.medium,
            color = MercadoPagoTheme.color.text.secondary,
        )
        MPText(
            text = row.amount,
            style = MercadoPagoTheme.typography.body.default.medium,
            color = MercadoPagoTheme.color.text.secondary,
        )
    }
}

@Composable
private fun ReviewConfirmSummaryInterestRow(
    interest: FooterSummaryInterest,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        MPText(
            text = interest.title,
            style = MercadoPagoTheme.typography.body.default.medium,
            color = MercadoPagoTheme.color.text.secondary,
        )
        MPText(
            text = interest.amount,
            style = MercadoPagoTheme.typography.body.default.medium,
            color = MercadoPagoTheme.color.text.secondary,
        )
    }
}
