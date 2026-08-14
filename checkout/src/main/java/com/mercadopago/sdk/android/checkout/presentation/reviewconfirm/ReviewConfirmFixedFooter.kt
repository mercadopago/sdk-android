package com.mercadopago.sdk.android.checkout.presentation.reviewconfirm

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import com.mercadopago.sdk.android.checkout.presentation.extensions.toAmountParts
import com.mercadopago.sdk.android.checkout.presentation.extensions.toMPAmountData
import com.mercadopago.sdk.android.checkout.presentation.model.ReviewConfirmFooterUiModel
import com.mercadopago.sdk.android.components.MPFixedFooter
import com.mercadopago.sdk.android.components.MPFixedFooterButtonData

@Composable
internal fun ReviewConfirmFixedFooter(
    footer: ReviewConfirmFooterUiModel,
    isLoading: Boolean,
    onConfirmClick: () -> Unit,
    onFooterPositioned: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val subtitleText = footer.installments?.let { installments ->
        buildString {
            append(installments.label)
            installments.secondaryLabel?.let { secondary ->
                append(" $secondary")
            }
        }
    } ?: footer.interestLabel

    MPFixedFooter(
        title = footer.totalLabel ?: "Total",
        modifier = modifier
            .onGloballyPositioned { layoutCoordinates ->
                onFooterPositioned(layoutCoordinates.size.height)
            },
        amount = footer.totalAmount.toAmountParts().toMPAmountData(),
        subtitle = subtitleText,
        button = MPFixedFooterButtonData(
            text = footer.buttonLabel,
            isLoading = isLoading,
            onClick = onConfirmClick,
        ),
    )
}
