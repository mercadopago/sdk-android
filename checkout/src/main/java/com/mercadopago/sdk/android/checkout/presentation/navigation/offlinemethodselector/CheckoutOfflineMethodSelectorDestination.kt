package com.mercadopago.sdk.android.checkout.presentation.navigation.offlinemethodselector

import androidx.compose.runtime.Composable
import com.mercadopago.sdk.android.checkout.core.model.internal.buildProcessOrderParamsForMethodSelection
import com.mercadopago.sdk.android.checkout.core.model.internal.hasReviewAndConfirm
import com.mercadopago.sdk.android.checkout.presentation.methodselection.MethodSelectionScreenDestination

@Composable
internal fun CheckoutOfflineMethodSelectorDestination(
    param: CheckoutOfflineMethodSelectorParam,
    actions: CheckoutOfflineMethodSelectorActions,
) {
    MethodSelectionScreenDestination(
        screenData = param.screenData,
        onOptionSelected = { option ->
            param.checkoutConfiguration.buildProcessOrderParamsForMethodSelection(
                option = option,
                paymentTypeId = param.screenData.paymentTypeId,
            )?.let { params ->
                if (param.checkoutConfiguration.hasReviewAndConfirm()) {
                    actions.onOpenReview(params)
                } else {
                    actions.onProcessOrder(params)
                }
            }
        },
        onBackClick = actions.onBackClick,
    )
}
