package com.mercadopago.sdk.android.checkout.presentation.usecase

import com.mercadopago.sdk.android.checkout.domain.exception.ErrorLocalized
import com.mercadopago.sdk.android.checkout.domain.exception.ExceptionFactory.mapToCheckoutError
import com.mercadopago.sdk.android.checkout.domain.extensions.withResultErrorHandling
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.coremethods.domain.interactor.CoreMethods
import com.mercadopago.sdk.android.coremethods.domain.interactor.coreMethods
import com.mercadopago.sdk.android.coremethods.domain.model.BuyerIdentification
import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import com.mercadopago.sdk.android.initializer.MercadoPagoSDK

/**
 * Generates a card token for a saved card within the PaymentBrick flow.
 *
 * Wraps [CoreMethods.generateCardToken] using the saved card ID and the CVV
 * captured via [PCIFieldState] — the CVV value is never exposed beyond this call.
 * The resulting [CardToken.token] is sent to the BFF via `POST /process` and
 * never returned to the seller.
 */
internal class GenerateCardTokenForPaymentBrickUseCase(
    private val coreMethods: CoreMethods = MercadoPagoSDK.getInstance().coreMethods,
) {
    suspend operator fun invoke(
        cardId: String,
        securityCodeState: PCIFieldState,
        buyerIdentification: BuyerIdentification = BuyerIdentification(name = "", number = "", type = null),
    ): Result<CardToken, MercadoPagoCheckoutError> =
        withResultErrorHandling {
            coreMethods.generateCardToken(
                cardId = cardId,
                securityCodeState = securityCodeState,
                buyerIdentification = buyerIdentification,
            )
        }.mapToCheckoutError(ErrorLocalized.TOKENIZATION)
}
