package com.mercadopago.sdk.android.checkout.domain.usecase

import com.mercadopago.sdk.android.checkout.domain.model.SecurityCodeOutput
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCodeScreenOutput
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCodeState

/**
 * Decides whether the CVV screen must be shown for the selected saved card.
 *
 * The presence of [SecurityCodeOutput.screen] is the single source of truth: when the BFF sends it
 * the screen is displayed, otherwise the flow skips straight to Revisa e Confirma. The use case
 * never reads business rules such as `has_preapproval_scope` — that decision belongs to the BFF.
 */
internal class GetSecurityCodeScreenUseCase {
    operator fun invoke(
        securityCode: SecurityCodeOutput,
    ): Pair<String, SecurityCodeState>? {
        val screen = securityCode.screen ?: return null
        return screen.headerTitle to securityCode.toSecurityCodeState(screen)
    }

    private fun SecurityCodeOutput.toSecurityCodeState(
        screen: SecurityCodeScreenOutput,
    ): SecurityCodeState =
        SecurityCodeState(
            label = screen.field.label,
            placeholder = screen.field.placeholder,
            helper = screen.field.helper,
            error = screen.field.error.orEmpty(),
            length = length,
            maxLength = length,
        )
}
