package com.mercadopago.sdk.android.checkout.presentation.navigation.offlinemethodselector

import androidx.compose.runtime.Composable
import com.mercadopago.sdk.android.checkout.core.model.internal.buildProcessOrderParamsForMethodSelection
import com.mercadopago.sdk.android.checkout.domain.model.SelectionDisplayType
import com.mercadopago.sdk.android.checkout.presentation.extensions.toPlainAmountString
import com.mercadopago.sdk.android.checkout.presentation.methodselection.MethodSelectionScreenDestination

@Composable
internal fun CheckoutOfflineMethodSelectorDestination(
    param: CheckoutOfflineMethodSelectorParam,
    actions: CheckoutOfflineMethodSelectorActions,
) {
    MethodSelectionScreenDestination(
        screenData = param.screenData,
        onOptionSelected = { option ->
            val totalAmount = param.screenData.footer?.totalAmount.orEmpty().toPlainAmountString()
            param.checkoutConfiguration.buildProcessOrderParamsForMethodSelection(
                option = option,
                amount = totalAmount,
            )?.let { params ->
                if (param.screenData.selectionType == SelectionDisplayType.Chevron) {
                    actions.onOpenReview(params)
                } else {
                    actions.onProcessOrder(params)
                }
            }
        },
        onBackClick = actions.onBackClick,
    )
}
