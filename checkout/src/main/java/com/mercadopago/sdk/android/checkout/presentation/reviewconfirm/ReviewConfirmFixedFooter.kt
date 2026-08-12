package com.mercadopago.sdk.android.checkout.presentation.reviewconfirm

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmFooter
import com.mercadopago.sdk.android.checkout.presentation.extensions.toAmountParts
import com.mercadopago.sdk.android.checkout.presentation.extensions.toMPAmountData
import com.mercadopago.sdk.android.components.MPFixedFooter
import com.mercadopago.sdk.android.components.MPFixedFooterButtonData

@Composable
internal fun ReviewConfirmFixedFooter(
    footer: ReviewConfirmFooter,
    isLoading: Boolean,
    onConfirmClick: () -> Unit,
    onFooterPositioned: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    MPFixedFooter(
        title = footer.description.orEmpty(),
        modifier = modifier
            .onGloballyPositioned { layoutCoordinates ->
                onFooterPositioned(layoutCoordinates.size.height)
            },
        amount = footer.totalAmount.toAmountParts().toMPAmountData(),
        subtitle = footer.interestLabel,
        button = MPFixedFooterButtonData(
            text = footer.buttonLabel,
            isLoading = isLoading,
            onClick = onConfirmClick,
        ),
    )
}
