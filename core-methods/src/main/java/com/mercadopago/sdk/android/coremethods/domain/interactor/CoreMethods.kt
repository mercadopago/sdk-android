package com.mercadopago.sdk.android.coremethods.domain.interactor

import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.coremethods.analytics.metricCardIssuersCallError
import com.mercadopago.sdk.android.coremethods.analytics.metricCardIssuersCallSuccess
import com.mercadopago.sdk.android.coremethods.analytics.metricGenerateCardTokenCallError
import com.mercadopago.sdk.android.coremethods.analytics.metricGenerateCardTokenCallSuccess
import com.mercadopago.sdk.android.coremethods.analytics.metricIdentificationCallError
import com.mercadopago.sdk.android.coremethods.analytics.metricIdentificationCallSuccess
import com.mercadopago.sdk.android.coremethods.analytics.metricInstallmentsCallError
import com.mercadopago.sdk.android.coremethods.analytics.metricInstallmentsCallSuccess
import com.mercadopago.sdk.android.coremethods.di.CoreMethodsModulesProvider
import com.mercadopago.sdk.android.coremethods.domain.model.CardIssuer
import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType
import com.mercadopago.sdk.android.coremethods.domain.model.Installment
import com.mercadopago.sdk.android.coremethods.domain.model.ProcessingMode
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.usecase.GenerateCardTokenUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.GetCardIssuersUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.GetIdentificationTypesUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.GetInstallmentsUseCase
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import com.mercadopago.sdk.android.initializer.MercadoPagoSDK
import org.koin.core.Koin

/**
 * CoreMethods class
 *
 * This class handlers the core-methods calls.
 * Use the [MercadoPagoSDK] extension method: [MercadoPagoSDK.coreMethods] to get this class instance.
 *
 * Example:
 * ```
 * MercadoPagoSDK.getInstance().coreMethods
 * ```
 * @see MercadoPagoSDK
 * @see MercadoPagoSDK.coreMethods
 */
class CoreMethods internal constructor(
    private val koin: Koin,
) {

    /**
     * Generate Card Token call.
     *
     * This return a [Result.Success] of [CardToken] data model or a [Result.Error] of [ResultError]
     * This uses the [PCIFieldState] for pass the values of the card in a secure way
     * This is a suspend function and should be called only from a coroutine or another suspend functionC
     * @param cardNumberState [PCIFieldState] of the card number text field
     * @param expirationDateState [PCIFieldState] of the expiration date text field
     * @param securityCodeState [PCIFieldState]  of the security code text field
     * @see PCIFieldState
     * @see CardToken
     * @see Result
     * @see ResultError
     */
    suspend fun generateCardToken(
        cardNumberState: PCIFieldState,
        expirationDateState: PCIFieldState,
        securityCodeState: PCIFieldState,
    ): Result<CardToken, ResultError> {
        val result = koin.get<GenerateCardTokenUseCase>().invoke(
            cardNumber = cardNumberState.input,
            expirationDate = expirationDateState.input,
            securityCode = securityCodeState.input,
        )

        when (result) {
            is Result.Error -> {
                when (result.error) {
                    is ResultError.Request -> {
                        MPAnalytics.getInstance().trackMetric(
                            metricGenerateCardTokenCallError(
                                error = result.error.message
                            )
                        )
                    }

                    is ResultError.Validation -> {
                        MPAnalytics.getInstance().trackMetric(
                            metricGenerateCardTokenCallError(
                                error = result.error.message
                            )
                        )
                    }
                }
            }

            is Result.Success -> {
                MPAnalytics.getInstance().trackMetric(
                    metricGenerateCardTokenCallSuccess()
                )
            }
        }
        return result
    }

    /**
     * Generate Card Token with a cardId call.
     *
     * This return a [Result.Success] of [CardToken] data model or a [Result.Error] of [ResultError]
     * This uses the [PCIFieldState] for pass the values of the card in a secure way
     * This is a suspend function and should be called only from a coroutine or another suspend functionC
     * @param cardId [String] The card id of a saved card
     * @param securityCodeState [PCIFieldState]  of the security code text field
     * @param expirationDateState [PCIFieldState] of the expiration date text field.
     * This should only be provided if required.
     * @see PCIFieldState
     * @see CardToken
     * @see Result
     * @see ResultError
     */
    suspend fun generateCardToken(
        cardId: String,
        securityCodeState: PCIFieldState,
        expirationDateState: PCIFieldState? = null,
    ): Result<CardToken, ResultError> {
        val result = koin.get<GenerateCardTokenUseCase>().invoke(
            cardNumber = cardId,
            expirationDate = expirationDateState?.input,
            securityCode = securityCodeState.input,
        )

        when (result) {
            is Result.Error -> {
                when (result.error) {
                    is ResultError.Request -> {
                        MPAnalytics.getInstance().trackMetric(
                            metricGenerateCardTokenCallError(
                                error = result.error.message,
                            )
                        )
                    }

                    is ResultError.Validation -> {
                        MPAnalytics.getInstance().trackMetric(
                            metricGenerateCardTokenCallError(
                                error = result.error.message,
                            )
                        )
                    }
                }
            }

            is Result.Success -> {
                MPAnalytics.getInstance().trackMetric(
                    metricGenerateCardTokenCallSuccess()
                )
            }
        }
        return result
    }

    /**
     * Get installment list call.
     *
     * This return a [Result.Success] of [Installment] data model or a [Result.Error] of [ResultError]
     * This is a suspend function and should be called only from a coroutine or another suspend functionC
     * @param bin the credit card bin
     * @param amount order item amount
     * @param processingMode the processing mode ([ProcessingMode.Aggregator] or [ProcessingMode.Gateway])
     * @see Installment
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
                when (result.error) {
                    is ResultError.Request -> {
                        MPAnalytics.getInstance().trackMetric(
                            metricInstallmentsCallError(
                                error = result.error.message,
                            ),
                        )
                    }

                    is ResultError.Validation -> {
                        MPAnalytics.getInstance().trackMetric(
                            metricInstallmentsCallError(
                                error = result.error.message,
                            ),
                        )
                    }
                }
            }

            is Result.Success -> {
                MPAnalytics.getInstance().trackMetric(
                    metricInstallmentsCallSuccess(
                        paymentType = result.data.paymentTypeId.orEmpty(),
                        merchantAccountId = result.data.merchantAccountId.orEmpty(),
                    ),
                )
            }
        }
        return result
    }

    /**
     * Get identification types call.
     *
     * This return a [Result.Success] of [IdentificationType] data model or a [Result.Error] of [ResultError]
     * This is a suspend function and should be called only from a coroutine or another suspend functionC
     * @see IdentificationType
     * @see Result
     * @see ResultError
     */
    suspend fun getIdentificationTypes(): Result<List<IdentificationType>, ResultError> {
        val result = koin.get<GetIdentificationTypesUseCase>().invoke()

        when (result) {
            is Result.Error -> {
                when (result.error) {
                    is ResultError.Request -> {
                        MPAnalytics.getInstance().trackMetric(
                            metricIdentificationCallError(
                                error = result.error.message,
                            ),
                        )
                    }

                    is ResultError.Validation -> {
                        MPAnalytics.getInstance().trackMetric(
                            metricIdentificationCallError(
                                error = result.error.message,
                            ),
                        )
                    }
                }
            }

            is Result.Success -> {
                MPAnalytics.getInstance().trackMetric(
                    metricIdentificationCallSuccess(),
                )
            }
        }
        return result
    }

    /**
     * Get card issuer list
     *
     * This return a [Result.Success] of [CardIssuer] data model or a [Result.Error] of [ResultError]
     * This is a suspend function and should be called only from a coroutine or another suspend function
     *
     * @param bin: the credit card bin
     * @param paymentMethodId: payment method id
     */
    suspend fun getCardIssuers(
        bin: Int,
        paymentMethodId: String
    ): Result<List<CardIssuer>, ResultError> {
        val result = koin.get<GetCardIssuersUseCase>().invoke(
            bin = bin,
            paymentMethodId = paymentMethodId
        )

        when (result) {
            is Result.Error -> {
                MPAnalytics.getInstance().trackMetric(
                    metricCardIssuersCallError(
                        error = result.error.message,
                    ),
                )
            }

            is Result.Success -> {
                MPAnalytics.getInstance().trackMetric(
                    metricCardIssuersCallSuccess(),
                )
            }
        }

        return result
    }

    /**
     * Companion object for the [CoreMethods] class.
     */
    companion object {
        @Volatile
        private var instance: CoreMethods? = null

        /**
         * Get the instance of the [CoreMethods] class to call its methods.
         */
        fun getInstance(): CoreMethods {
            return instance ?: CoreMethods(
                koin = CoreMethodsModulesProvider().koinApp,
            )
        }
    }
}

/**
 * Mercado Pago SDK - CoreMethods
 *
 * Use this to get the instance of CoreMethods and it's methods
 *
 * Example:
 * ```
 *  val coreMethods: CoreMethods = MercadoPagoSDK.getInstance().coreMethods
 * ```
 */
val MercadoPagoSDK.coreMethods: CoreMethods
    get() = CoreMethods.getInstance()
