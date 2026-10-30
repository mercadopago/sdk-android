package com.mercadopago.sdk.android.checkout.presentation.reviewconfirm

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.checkout.presentation.extensions.getTotal
import com.mercadopago.sdk.android.checkout.presentation.extensions.getTotalDecimalPart
import com.mercadopago.sdk.android.checkout.presentation.model.ReviewConfirmFooterUiModel
import com.mercadopago.sdk.android.components.MPAmountData
import com.mercadopago.sdk.android.components.MPFixedFooter
import com.mercadopago.sdk.android.components.MPFixedFooterButtonData
import com.mercadopago.sdk.android.components.MPText
import com.mercadopago.sdk.android.components.model.MPSubtitle
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme

@Composable
internal fun ReviewConfirmFixedFooter(
    footer: ReviewConfirmFooterUiModel,
    isLoading: Boolean,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val subtitle = footer.installments?.let { installments ->
        MPSubtitle(
            content = {
                Row {
                    MPText(
                        text = installments.label,
                        style = MercadoPagoTheme.typography.body.default.medium,
                        color = MercadoPagoTheme.color.text.secondary,
                    )
                    installments.secondaryLabel?.let { secondary ->
                        Spacer(modifier = Modifier.width(4.dp))
                        MPText(
                            text = secondary,
                            style = MercadoPagoTheme.typography.body.default.medium,
                            color = MercadoPagoTheme.color.feedback.positive.fillLoud,
                        )
                    }
                }
            },
        )
    } ?: footer.interestLabel?.let { MPSubtitle(text = it) }

    MPFixedFooter(
        title = footer.totalLabel ?: "Total",
        modifier = modifier,
        amount = MPAmountData(
            currencySymbol = footer.currencySymbol.orEmpty(),
            integerPart = footer.totalAmount.getTotal(),
            decimalPart = footer.totalAmount.getTotalDecimalPart(),
        ),
        subtitle = subtitle,
        showDivider = true,
        button = MPFixedFooterButtonData(
            text = footer.buttonLabel,
            icon = Icons.Filled.Lock,
            isLoading = isLoading,
            onClick = onConfirmClick,
        ),
    )
}
