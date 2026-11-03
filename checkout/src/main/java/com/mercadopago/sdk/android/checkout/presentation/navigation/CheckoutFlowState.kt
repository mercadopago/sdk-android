package com.mercadopago.sdk.android.checkout.presentation.navigation

import com.mercadopago.sdk.android.checkout.domain.model.MethodSelectionScreenData
import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeScreenConfig

internal class CheckoutFlowState(
    val installmentContext: InstallmentContext? = null,
    val securityCodeContext: SecurityCodeScreenConfig? = null,
    val reviewContext: ReviewContext? = null,
    val offlineMethodSelectorContext: MethodSelectionScreenData? = null,
    val statusScreenContext: StatusScreenContext? = null,
) {
    internal fun copy(
        installmentContext: InstallmentContext? = this.installmentContext,
        securityCodeContext: SecurityCodeScreenConfig? = this.securityCodeContext,
        reviewContext: ReviewContext? = this.reviewContext,
        offlineMethodSelectorContext: MethodSelectionScreenData? = this.offlineMethodSelectorContext,
        statusScreenContext: StatusScreenContext? = this.statusScreenContext,
    ): CheckoutFlowState =
        CheckoutFlowState(
            installmentContext = installmentContext,
            securityCodeContext = securityCodeContext,
            reviewContext = reviewContext,
            offlineMethodSelectorContext = offlineMethodSelectorContext,
            statusScreenContext = statusScreenContext,
        )

    override fun toString(): String =
        "CheckoutFlowState(" +
            "hasInstallmentContext=${installmentContext != null}, " +
            "hasSecurityCodeContext=${securityCodeContext != null}, " +
            "hasReviewContext=${reviewContext != null}, " +
            "reviewOrigin=${reviewContext?.origin}, " +
            "hasOfflineMethodSelectorContext=${offlineMethodSelectorContext != null}, " +
            "hasStatusScreenContext=${statusScreenContext != null}" +
            ")"
}
