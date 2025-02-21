package com.mercadopago.sdk.android.coremethods.domain.interactor

import com.mercadopago.sdk.android.coremethods.di.CoreMethodsModulesProvider
import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.usecase.GenerateCardTokenUseCase
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import com.mercadopago.sdk.android.initializer.MercadoPagoSDK
import org.koin.core.Koin

class CoreMethods internal constructor(
    private val koin: Koin,
) {

    suspend fun generateCardToken(
        cardNumberState: PCIFieldState,
        expirationDateState: PCIFieldState,
        securityCodeState: PCIFieldState,
    ): Result<CardToken, ResultError> {
        return koin.get<GenerateCardTokenUseCase>().invoke(
            cardNumber = cardNumberState.input,
            expirationDate = expirationDateState.input,
            securityCode = securityCodeState.input,
        )
    }

    companion object {
        @Volatile
        private var instance: CoreMethods? = null

        fun getInstance(): CoreMethods {
            return instance ?: CoreMethods(
                koin = CoreMethodsModulesProvider().koinApp,
            )
        }
    }
}

val MercadoPagoSDK.coreMethods: CoreMethods
    get() = CoreMethods.getInstance()
