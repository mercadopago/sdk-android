package com.mercadopago.sdk.android.checkout.domain.usecase

import com.mercadopago.sdk.android.checkout.domain.exception.ErrorLocalized
import com.mercadopago.sdk.android.checkout.domain.exception.ExceptionFactory.mapToCheckoutError
import com.mercadopago.sdk.android.checkout.domain.extensions.map
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.coremethods.domain.interactor.CoreMethods
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState

/**
 * Tokenizes a saved card using [CoreMethods.generateCardTokenWithSecurityCode].
 * Called by [com.mercadopago.sdk.android.checkout.presentation.viewmodel.SecurityCodeViewModel]
 * when the user taps "Continuar" with a valid CVV.
 *
 * @return [Result.Success] with the token string, or [Result.Error] with [MercadoPagoCheckoutError].
 */
internal class GenerateTokenWithCardIdUseCase(
    private val coreMethods: CoreMethods = CoreMethods.getInstance(),
) {
    suspend operator fun invoke(
        cardId: String,
        securityCodeState: PCIFieldState,
    ): Result<String, MercadoPagoCheckoutError> =
        coreMethods
            .generateCardTokenWithSecurityCode(cardId, securityCodeState)
            .mapToCheckoutError(ErrorLocalized.TOKENIZATION)
            .map { it.token }
}
