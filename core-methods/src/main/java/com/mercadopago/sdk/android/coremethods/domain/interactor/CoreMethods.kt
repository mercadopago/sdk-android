package com.mercadopago.sdk.android.coremethods.domain.interactor

import com.mercadopago.sdk.android.coremethods.di.CoreMethodsModulesProvider
import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.model.CardTokenFields
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.coremethods.exceptions.InitializationException
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import kotlinx.coroutines.flow.Flow

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

    fun generateCardToken(
        cardNumberState: PCIFieldState,
        expirationDateState: PCIFieldState,
        securityCodeState: PCIFieldState,
    ): Flow<Result<CardToken, ResultError>> {
        val expirationMonth = expirationDateState.input.take(2)
        val expirationYear = expirationDateState.input.takeLast(4)
        return coreMethodsProvider.provideCoreMethodsRepository().generateCardToken(
            CardTokenFields(
                cardNumber = cardNumberState.input,
                expirationMonth = expirationMonth.toInt(),
                expirationYear = expirationYear.toInt(),
                securityCode = securityCodeState.input
            )
        )
    }
}
