package com.mercadopago.sdk.android.coremethods.domain.interactor

import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.coremethods.analytics.provideMetricInstallmentFetch
import com.mercadopago.sdk.android.coremethods.di.CoreMethodsModulesProvider
import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationTypes
import com.mercadopago.sdk.android.coremethods.domain.model.Installment
import com.mercadopago.sdk.android.coremethods.domain.model.ProcessingMode
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.usecase.GenerateCardTokenUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.GetIdentificationTypesUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.GetInstallmentsUseCase
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
        val result = koin.get<GetInstallmentsUseCase>().invoke(
            bin = bin,
            amount = amount,
            processingMode = processingMode.mode,
        )

        when (result) {
            is Result.Error -> {
                MPAnalytics.getInstance().trackMetric(
                    provideMetricInstallmentFetch(
                        error = result.error.code
                    )
                )
            }

            is Result.Success -> {
                MPAnalytics.getInstance().trackMetric(
                    provideMetricInstallmentFetch(
                        paymentType = result.data.paymentTypeId.orEmpty(),
                        merchantAccountId = result.data.merchantAccountId.orEmpty(),
                    )
                )
            }
        }
        return result
    }

    /**
     *
     */
    suspend fun getIdentificationTypes(): Result<List<IdentificationTypes>, ResultError> {
        val result = koin.get<GetIdentificationTypesUseCase>().invoke()

        when (result) {
            is Result.Error -> {
            }

            is Result.Success -> {
            }
        }
        return result
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
