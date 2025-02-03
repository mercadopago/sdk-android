package com.mercadopago.sdk.android.coremethods.domain.interactor

import com.mercadopago.sdk.android.coremethods.di.CoreMethodsModulesProvider
import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.coremethods.exceptions.InitializationException
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState

class CoreMethods internal constructor(
    private val coreMethodsProvider: CoreMethodsModulesProvider
) {

    companion object {
        @Volatile
        private var instance: CoreMethods? = null

        fun getInstance(): CoreMethods {
            return instance ?: throw InitializationException()
        }

        fun initialize(
            publicKey: String,
        ) {
            instance = CoreMethods(
                coreMethodsProvider = CoreMethodsModulesProvider(publicKey)
            )
        }
    }

    suspend fun generateCardToken(
        cardNumberState: PCIFieldState,
        expirationDateState: PCIFieldState,
        securityCodeState: PCIFieldState,
    ): Result<CardToken, ResultError> {
        return coreMethodsProvider.provideGenerateCardTokenUseCase().invoke(
            cardNumber = cardNumberState.input,
            expirationDate = expirationDateState.input,
            securityCode = securityCodeState.input
        )
    }
}
