package com.mercadopago.sdk.android.coremethods.domain.interactor

import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.coremethods.analytics.provideMetricInstallmentFetch
import com.mercadopago.sdk.android.coremethods.di.CoreMethodsModulesProvider
import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.model.Installment
import com.mercadopago.sdk.android.coremethods.domain.model.ProcessingMode
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.coremethods.exceptions.InitializationException
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState

class CoreMethods internal constructor(
    private val coreMethodsProvider: CoreMethodsModulesProvider,
    private val analytics: MPAnalytics,
) {
    companion object {
        @Volatile
        private var instance: CoreMethods? = null

        fun getInstance(): CoreMethods {
            return instance ?: throw InitializationException()
        }

        fun initialize(publicKey: String) {
            MPAnalytics.initialize("", "", "")
            instance = CoreMethods(
                coreMethodsProvider = CoreMethodsModulesProvider(publicKey),
                analytics = MPAnalytics.getInstance(),
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
            securityCode = securityCodeState.input,
        )
    }

    /**
     * Get installment list call.
     * This return a [Result.Success] of [Installment] data model or a [Result.Error] of [ResultError]
     *
     * This is a suspend function and should be called only from a coroutine or another suspend function
     * @param bin the credit card bin
     * @param amount order item amount
     * @param processingMode the processing mode ([ProcessingMode.Aggregator] or [ProcessingMode.Gateway])
     * @see ProcessingMode
     * @see Result
     * @see ResultError
     */
    suspend fun getInstallments(
        bin: String,
        amount: Long,
        processingMode: ProcessingMode = ProcessingMode.Aggregator,
    ): Result<Installment, ResultError> {
        val result = coreMethodsProvider.provideGetInstallmentUseCase().invoke(
            bin = bin,
            amount = amount,
            processingMode = processingMode.mode,
        )

        when (result) {
            is Result.Error -> {
                analytics.trackMetric(provideMetricInstallmentFetch(isDeveloping = false))
            }

            is Result.Success -> {
                analytics.trackMetric(provideMetricInstallmentFetch(isDeveloping = false))
            }
        }
        return result
    }
}
