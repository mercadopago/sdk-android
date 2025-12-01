package com.mercadopago.sdk.android.coremethods.domain.interactor

import android.app.Activity
import androidx.annotation.RestrictTo
import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.coremethods.analytics.metricCardIssuersCallError
import com.mercadopago.sdk.android.coremethods.analytics.metricCardIssuersCallSuccess
import com.mercadopago.sdk.android.coremethods.analytics.metricGenerateCardTokenCallError
import com.mercadopago.sdk.android.coremethods.analytics.metricGenerateCardTokenCallSuccess
import com.mercadopago.sdk.android.coremethods.analytics.metricIdentificationCallError
import com.mercadopago.sdk.android.coremethods.analytics.metricIdentificationCallSuccess
import com.mercadopago.sdk.android.coremethods.analytics.metricInstallmentsCallError
import com.mercadopago.sdk.android.coremethods.analytics.metricInstallmentsCallSuccess
import com.mercadopago.sdk.android.coremethods.analytics.metricPaymentMethodCallError
import com.mercadopago.sdk.android.coremethods.analytics.metricPaymentMethodCallSuccess
import com.mercadopago.sdk.android.coremethods.di.CoreMethodsModulesProvider
import com.mercadopago.sdk.android.coremethods.domain.model.BuyerIdentification
import com.mercadopago.sdk.android.coremethods.domain.model.CardIssuer
import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType
import com.mercadopago.sdk.android.coremethods.domain.model.Installment
import com.mercadopago.sdk.android.coremethods.domain.model.PaymentMethod
import com.mercadopago.sdk.android.coremethods.domain.model.ProcessingMode
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.provider.ThreeDSProvider
import com.mercadopago.sdk.android.coremethods.domain.provider.models.ThreeDSAuthenticationModel
import com.mercadopago.sdk.android.coremethods.domain.provider.models.ThreeDSChallengeResult
import com.mercadopago.sdk.android.coremethods.domain.provider.models.ThreeDSRequestParams
import com.mercadopago.sdk.android.coremethods.domain.provider.models.ThreeDSWarning
import com.mercadopago.sdk.android.coremethods.domain.usecase.GenerateCardIdTokenUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.GenerateCardTokenUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.GetCardIssuersUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.GetIdentificationTypesUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.GetInstallmentsUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.GetPaymentMethodsUseCase
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import com.mercadopago.sdk.android.initializer.MercadoPagoSDK
import org.koin.core.Koin
import java.math.BigDecimal

/**
 * CoreMethods is the main class responsible for handling all core payment-related operations in the MercadoPago SDK.
 * It provides methods for card tokenization, installment calculations, and payment method management.
 * This class ensures secure handling of sensitive card data through PCI-compliant field states.
 *
 * Example:
 * ```kotlin
 * // Get CoreMethods instance
 * val coreMethods = MercadoPagoSDK.getInstance().coreMethods
 * ```
 *
 * @see MercadoPagoSDK
 * @see MercadoPagoSDK.coreMethods
 *
 */
class CoreMethods internal constructor(
    internal val koin: Koin,
) {
    /**
     * Private property to hold the 3DS provider instance.
     * This is set by the client application through setThreeDSProvider method.
     */
    private var threeDSProvider: ThreeDSProvider? = null

    /**
     * Sets the 3DS provider implementation for this CoreMethods instance.
     * This method should be called once during application initialization to enable 3DS functionality.
     *
     * @param provider The ThreeDSProvider implementation to use for 3DS operations
     *
     * Example:
     * ```kotlin
     * val coreMethods = MercadoPagoSDK.getInstance().coreMethods
     * val threeDS = MPThreeDS.getInstance(context)
     * coreMethods.setThreeDSProvider(MPThreeDSProviderAdapter(threeDS))
     * ```
     */
    fun setThreeDSProvider(provider: ThreeDSProvider) {
        threeDSProvider = provider
    }

    /**
     * Private method to check if a 3DS provider is available.
     *
     * @return true if a provider is set, false otherwise
     */
    private fun hasThreeDSProvider(): Boolean = threeDSProvider != null

    /**
     * Creates a 3DS transaction for the specified payment method.
     * This method requires a 3DS provider to be set via setThreeDSProvider.
     *
     * @param paymentMethodId The payment method ID to create the transaction for (e.g., "visa", "mastercard")
     * @return [Result.Success] with Unit if the transaction was created successfully,
     *         [Result.Error] with ResultError if the provider is not available or an error occurred
     *
     * Example:
     * ```kotlin
     * val result = coreMethods.createThreeDSTransaction("visa")
     * when (result) {
     *     is Result.Success -> {
     *         // Transaction created, proceed with authentication
     *         val params = coreMethods.getThreeDSAuthenticationParameters()
     *     }
     *     is Result.Error -> {
     *         // Handle error - provider not available or transaction creation failed
     *     }
     * }
     * ```
     */
    fun createThreeDSTransaction(paymentMethodId: String): Result<Unit, ResultError> {
        return if (hasThreeDSProvider()) {
            try {
                threeDSProvider?.createTransaction(paymentMethodId)
                Result.Success(Unit)
            } catch (e: Exception) {
                Result.Error(
                    ResultError.Request(
                        code = "",
                        message = "Error creating 3DS transaction: ${e.message}",
                    ),
                )
            }
        } else {
            Result.Error(
                ResultError.Validation(
                    message = "3DS provider not available. Please set the provider using setThreeDSProvider() method.",
                ),
            )
        }
    }

    /**
     * Retrieves the authentication request parameters for the current 3DS transaction.
     * These parameters should be sent to your backend for authentication.
     * This method requires a 3DS provider to be set via setThreeDSProvider.
     *
     * @return [Result.Success] with ThreeDSRequestParams if parameters are available,
     *         [Result.Error] with ResultError if the provider is not available or no transaction exists
     *
     * Example:
     * ```kotlin
     * val result = coreMethods.getThreeDSAuthenticationParameters()
     * when (result) {
     *     is Result.Success -> {
     *         val params = result.data
     *         // Send params to your backend
     *         backendService.authenticate3DS(params)
     *     }
     *     is Result.Error -> {
     *         // Handle error
     *     }
     * }
     * ```
     */
    fun getThreeDSAuthenticationParameters(): Result<ThreeDSRequestParams, ResultError> {
        return if (hasThreeDSProvider()) {
            val params = threeDSProvider?.getAuthenticationRequestParameters()
            if (params != null) {
                Result.Success(params)
            } else {
                Result.Error(
                    ResultError.Validation(
                        message = "No active 3DS transaction. Please call createThreeDSTransaction first.",
                    ),
                )
            }
        } else {
            Result.Error(
                ResultError.Validation(
                    message = "3DS provider not available. Please set the provider using setThreeDSProvider() method.",
                ),
            )
        }
    }

    /**
     * Starts the 3DS challenge flow with the provided authentication response.
     * This method requires a 3DS provider to be set via setThreeDSProvider.
     *
     * @param activity The activity context for displaying the challenge UI
     * @param authentication The authentication response received from your backend
     * @param timeout The challenge timeout in minutes (default: 10)
     * @return [Result.Success] with ThreeDSChallengeResult if the challenge completed,
     *         [Result.Error] with ResultError if the provider is not available or an error occurred
     *
     * Example:
     * ```kotlin
     * val result = coreMethods.startThreeDSChallenge(
     *     activity = this,
     *     authentication = authenticationModel,
     *     timeout = 10
     * )
     * when (result) {
     *     is Result.Success -> {
     *         when (val challengeResult = result.data) {
     *             is ThreeDSChallengeResult.OnSuccess -> {
     *                 // Challenge completed successfully
     *                 val authenticated = challengeResult.result
     *             }
     *             is ThreeDSChallengeResult.OnError -> {
     *                 // Challenge failed
     *             }
     *             is ThreeDSChallengeResult.OnCancel -> {
     *                 // User cancelled
     *             }
     *             is ThreeDSChallengeResult.OnTimedOut -> {
     *                 // Challenge timed out
     *             }
     *         }
     *     }
     *     is Result.Error -> {
     *         // Provider not available
     *     }
     * }
     * ```
     */
    suspend fun startThreeDSChallenge(
        activity: Activity,
        authentication: ThreeDSAuthenticationModel,
        timeout: Int = 10,
    ): Result<ThreeDSChallengeResult, ResultError> {
        return if (hasThreeDSProvider()) {
            try {
                val challengeResult = threeDSProvider?.doChallenge(
                    activity = activity,
                    authentication = authentication,
                    timeout = timeout,
                )
                if (challengeResult != null) {
                    Result.Success(challengeResult)
                } else {
                    Result.Error(
                        ResultError.Request(
                            code = "",
                            message = "Failed to execute 3DS challenge.",
                        ),
                    )
                }
            } catch (e: Exception) {
                Result.Error(
                    ResultError.Request(
                        code = "",
                        message = "Error during 3DS challenge: ${e.message}",
                    ),
                )
            }
        } else {
            Result.Error(
                ResultError.Validation(
                    message = "3DS provider not available. Please set the provider using setThreeDSProvider() method.",
                ),
            )
        }
    }

    /**
     * Closes the current 3DS transaction and releases associated resources.
     * This method requires a 3DS provider to be set via setThreeDSProvider.
     *
     * @return [Result.Success] with Unit if the transaction was closed successfully,
     *         [Result.Error] with ResultError if the provider is not available
     *
     * Example:
     * ```kotlin
     * val result = coreMethods.closeThreeDSTransaction()
     * when (result) {
     *     is Result.Success -> {
     *         // Transaction closed successfully
     *     }
     *     is Result.Error -> {
     *         // Handle error
     *     }
     * }
     * ```
     */
    fun closeThreeDSTransaction(): Result<Unit, ResultError> {
        return if (hasThreeDSProvider()) {
            try {
                threeDSProvider?.close()
                Result.Success(Unit)
            } catch (e: Exception) {
                Result.Error(
                    ResultError.Request(
                        code = "",
                        message = "Error closing 3DS transaction: ${e.message}",
                    ),
                )
            }
        } else {
            Result.Error(
                ResultError.Validation(
                    message = "3DS provider not available. Please set the provider using setThreeDSProvider() method.",
                ),
            )
        }
    }

    /**
     * Retrieves warnings generated by the 3DS SDK during initialization or operation.
     * This method requires a 3DS provider to be set via setThreeDSProvider.
     *
     * @return [Result.Success] with a list of ThreeDSWarning,
     *         [Result.Error] with ResultError if the provider is not available
     *
     * Example:
     * ```kotlin
     * val result = coreMethods.getThreeDSWarnings()
     * when (result) {
     *     is Result.Success -> {
     *         result.data.forEach { warning ->
     *             println("Warning: ${warning.message}")
     *         }
     *     }
     *     is Result.Error -> {
     *         // Handle error
     *     }
     * }
     * ```
     */
    fun getThreeDSWarnings(): Result<List<ThreeDSWarning>, ResultError> {
        return if (hasThreeDSProvider()) {
            try {
                val warnings = threeDSProvider?.getWarnings() ?: emptyList()
                Result.Success(warnings)
            } catch (e: Exception) {
                Result.Error(
                    ResultError.Request(
                        code = "",
                        message = "Error getting 3DS warnings: ${e.message}",
                    ),
                )
            }
        } else {
            Result.Error(
                ResultError.Validation(
                    message = "3DS provider not available. Please set the provider using setThreeDSProvider() method.",
                ),
            )
        }
    }

    /**
     * Generates a secure card token from the provided card details.
     * This method handles the tokenization of card information in a PCI-compliant manner,
     * ensuring sensitive data is never exposed in the application.
     *
     * @param cardNumberState [PCIFieldState] containing the card number
     * @param expirationDateState [PCIFieldState] containing the card expiration date
     * @param securityCodeState [PCIFieldState]containing the card security code
     * @param buyerIdentification [BuyerIdentification] information including name and document
     * @return [Result]: On Success [CardToken], On Error[ResultError]
     *
     * Example:
     * ```kotlin
     * val result = coreMethods.generateCardToken(
     *     cardNumberState = cardNumberPCIState,
     *     expirationDateState = expirationDatePCIState,
     *     securityCodeState = securityCodePCIState,
     *     buyerIdentification = BuyerIdentification(
     *         name = "APRO",
     *         number = "012345678909",
     *         type = "CPF"
     *     )
     * )
     *
     * when (result) {
     *     is Result.Success -> {
     *         val token = result.data.token
     *         // Use token for payment processing
     *     }
     *     is Result.Error -> {
     *         // Handle error
     *     }
     * }
     * ```
     *
     * @see PCIFieldState
     * @see CardToken
     * @see Result
     * @see ResultError
     *
     */
    suspend fun generateCardToken(
        cardNumberState: PCIFieldState,
        expirationDateState: PCIFieldState,
        securityCodeState: PCIFieldState,
        buyerIdentification: BuyerIdentification,
    ): Result<CardToken, ResultError> {
        val result = koin.get<GenerateCardTokenUseCase>().invoke(
            cardNumber = cardNumberState.input,
            expirationDate = expirationDateState.input,
            securityCode = securityCodeState.input,
            buyerIdentification = buyerIdentification,
        )

        when (result) {
            is Result.Error -> {
                when (result.error) {
                    is ResultError.Request -> {
                        MPAnalytics.getInstance().trackMetric(
                            metricGenerateCardTokenCallError(
                                identityType = buyerIdentification.type,
                                error = result.error.message,
                            ),
                        )
                    }

                    is ResultError.Validation -> {
                        MPAnalytics.getInstance().trackMetric(
                            metricGenerateCardTokenCallError(
                                identityType = buyerIdentification.type,
                                error = result.error.message,
                            ),
                        )
                    }
                }
            }

            is Result.Success -> {
                MPAnalytics.getInstance().trackMetric(
                    metricGenerateCardTokenCallSuccess(
                        identityType = buyerIdentification.type,
                    ),
                )
            }
        }
        return result
    }

    /**
     * Generate Card Token with a cardId call.
     * This uses the [PCIFieldState] for pass the values of the card in a secure way
     *
     * @param cardId [String] The card id of a saved card
     * @param securityCodeState [PCIFieldState]containing the card security code
     * @param expirationDateState [PCIFieldState] containing the card expiration date
     * @param buyerIdentification [BuyerIdentification] information including name and document
     * @return [Result]: On Success  [CardToken], On Error [ResultError]
     * name, number and type
     *
     * Example:
     * ```kotlin
     * val result = coreMethods.generateCardToken(
     *     cardId = "<cardID>",
     *     expirationDateState = expirationDatePCIState,
     *     securityCodeState = securityCodePCIState,
     *     buyerIdentification = BuyerIdentification(
     *         name = "APRO",
     *         number = "012345678909",
     *         type = "CPF"
     *     )
     * )
     *
     * when (result) {
     *     is Result.Success -> {
     *         val token = result.data.token
     *         // Use token for payment processing
     *     }
     *     is Result.Error -> {
     *         // Handle error
     *     }
     * }
     * ```
     *
     * @see PCIFieldState
     * @see CardToken
     * @see Result
     * @see ResultError
     */
    suspend fun generateCardToken(
        cardId: String,
        securityCodeState: PCIFieldState,
        expirationDateState: PCIFieldState? = null,
        buyerIdentification: BuyerIdentification,
    ): Result<CardToken, ResultError> {
        val result = koin.get<GenerateCardIdTokenUseCase>().invoke(
            cardId = cardId,
            expirationDate = expirationDateState?.input,
            securityCode = securityCodeState.input,
            buyerIdentification = buyerIdentification,
        )
        when (result) {
            is Result.Error -> {
                when (result.error) {
                    is ResultError.Request -> {
                        MPAnalytics.getInstance().trackMetric(
                            metricGenerateCardTokenCallError(
                                isSavedCard = true,
                                identityType = buyerIdentification.type,
                                error = result.error.message,
                            ),
                        )
                    }

                    is ResultError.Validation -> {
                        MPAnalytics.getInstance().trackMetric(
                            metricGenerateCardTokenCallError(
                                isSavedCard = true,
                                identityType = buyerIdentification.type,
                                error = result.error.message,
                            ),
                        )
                    }
                }
            }

            is Result.Success -> {
                MPAnalytics.getInstance().trackMetric(
                    metricGenerateCardTokenCallSuccess(isSavedCard = true, identityType = buyerIdentification.type),
                )
            }
        }
        return result
    }

    /**
     * Retrieves available installment options for a given card and amount.
     * This method calculates all possible installment plans based on the card's BIN,
     * transaction amount, and processing mode.
     *
     * @param bin The first 8 digits of the card number from [CardNumberTextFieldEvent].OnBinChanged value
     * @param amount [BigDecimal] The total transaction amount
     * @param processingMode [ProcessingMode] of the payment (Aggregator or Gateway)
     * @return [Result]: On Success a list of  [Installment], On error [ResultError]
     *
     * Example:
     * ```kotlin
     * val result = coreMethods.getInstallments(
     *     bin = bin, // CardNumberTextFieldEvent.OnBinChanged value
     *     amount = BigDecimal("100.00"),
     *     processingMode = ProcessingMode.Aggregator
     * )
     *
     * when (result) {
     *     is Result.Success -> {
     *         val installments = result.data
     *         // Display installment options to user
     *     }
     *     is Result.Error -> {
     *         // Handle error
     *     }
     * }
     * ```
     *
     * @see Installment
     * @see ProcessingMode
     * @see Result
     * @see ResultError
     */
    suspend fun getInstallments(
        bin: String,
        amount: BigDecimal,
        processingMode: ProcessingMode = ProcessingMode.Aggregator,
    ): Result<List<Installment>, ResultError> {
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
                        paymentType = result.data.getOrNull(0)?.paymentTypeId.orEmpty(),
                        transactionAmount = amount,
                    ),
                )
            }
        }
        return result
    }

    /**
     * Retrieves the list of available identification types for the current country.
     * This method helps identify which types of identification documents (CPF, CNPJ, DNI, etc.)
     * are accepted for payment processing in the current market.
     *
     * @return [Result]: On Success a list of [IdentificationType], On Error [ResultError]
     *
     * Example:
     * ```
     * val result = coreMethods.getIdentificationTypes()
     *
     * when (result) {
     *     is Result.Success -> {
     *         val identificationTypes = result.data
     *         // Display available identification types to user
     *     }
     *     is Result.Error -> {
     *         // Handle error
     *     }
     * }
     * ```
     *
     * @see IdentificationType
     * @see Result
     * @see ResultError
     *
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
                    metricIdentificationCallSuccess(
                        result.data.mapNotNull { identificationType -> identificationType.name },
                    ),
                )
            }
        }
        return result
    }

    /**
     * Retrieves the list of available card issuers for a given card BIN and payment method.
     * This method helps identify which banks or financial institutions can process the card
     * based on its first digits and the selected payment method.
     *
     * @param bin The first 8 digits of the card number from [CardNumberTextFieldEvent].OnBinChanged value
     * @param paymentMethodId The ID of the payment method to check issuers for
     * @return [Result]: On Success a list of [CardIssuer], On Error [ResultError]
     *
     * Example:
     * ```kotlin
     * val result = coreMethods.getCardIssuers(
     *     bin = bin, // CardNumberTextFieldEvent.OnBinChanged value
     *     paymentMethodId = "paymentMethodId" // Get in PaymentMethods response call
     * )
     *
     * when (result) {
     *     is Result.Success -> {
     *         val issuers = result.data
     *         // Display available issuers to user
     *     }
     *     is Result.Error -> {
     *         // Handle error
     *     }
     * }
     * ```
     *
     * @see CardIssuer
     * @see Result
     * @see ResultError
     *
     */
    suspend fun getCardIssuers(
        bin: String,
        paymentMethodId: String,
    ): Result<List<CardIssuer>, ResultError> {
        val result = koin.get<GetCardIssuersUseCase>().invoke(
            bin = bin,
            paymentMethodId = paymentMethodId,
        )

        when (result) {
            is Result.Error -> {
                when (result.error) {
                    is ResultError.Request -> {
                        MPAnalytics.getInstance().trackMetric(
                            metricCardIssuersCallError(
                                error = result.error.message,
                            ),
                        )
                    }

                    is ResultError.Validation -> {
                        MPAnalytics.getInstance().trackMetric(
                            metricCardIssuersCallError(
                                error = result.error.message,
                            ),
                        )
                    }
                }
            }

            is Result.Success -> {
                MPAnalytics.getInstance().trackMetric(
                    metricCardIssuersCallSuccess(
                        issuers = result.data.mapNotNull { it.id },
                    ),
                )
            }
        }

        return result
    }

    /**
     * Retrieves the list of available payment methods for a given card BIN.
     * This method helps identify which payment methods (credit card, debit card, etc.)
     * can be used based on the card's first digits.
     *
     * @param bin The first 8 digits of the card number from [CardNumberTextFieldEvent].OnBinChanged value
     * @return [Result] On Success a list of [PaymentMethod], On Error [ResultError]
     *
     * Example:
     * ```kotlin
     * val result = coreMethods.getPaymentMethods(
     *     bin = bin, // CardNumberTextFieldEvent.OnBinChanged value
     * )
     *
     * when (result) {
     *     is Result.Success -> {
     *         val paymentMethods = result.data
     *         // Display available payment methods to user
     *     }
     *     is Result.Error -> {
     *         // Handle error
     *     }
     * }
     * ```
     *
     * @see PaymentMethod
     * @see Result
     * @see ResultError
     *
     */
    suspend fun getPaymentMethods(bin: String): Result<List<PaymentMethod>, ResultError> {
        val result = koin.get<GetPaymentMethodsUseCase>().invoke(
            bin = bin,
        )

        when (result) {
            is Result.Error -> {
                when (result.error) {
                    is ResultError.Request -> {
                        MPAnalytics.getInstance().trackMetric(
                            metricPaymentMethodCallError(
                                error = result.error.message,
                            ),
                        )
                    }

                    is ResultError.Validation -> {
                        MPAnalytics.getInstance().trackMetric(
                            metricPaymentMethodCallError(
                                error = result.error.message,
                            ),
                        )
                    }
                }
            }

            is Result.Success -> {
                MPAnalytics.getInstance().trackMetric(
                    metricPaymentMethodCallSuccess(
                        issuer = result.data.firstOrNull()?.issuer?.id.orEmpty(),
                        cardBrand = result.data.firstOrNull()?.id.orEmpty(),
                    ),
                )
            }
        }

        return result
    }

    /**
     * @suppress
     * Generate Card Token call.
     * This call don't use the [PCIFieldState] for handles card information in a PCI-compliant manner,
     * It is recommended to be used only by sellers who already have a PCI seal.
     * This should only be used in cases where PCI is not required.
     *
     * @param cardNumber [String] of the card number text field
     * @param expirationDate [String] of the expiration date text field
     * @param securityCode [String]  of the security code text field
     * @param buyerIdentification [BuyerIdentification] data class that`s handle the buyer identification
     * @return [Result]: On Success [CardToken], On Error [ResultError]
     */
    suspend fun generateCardToken(
        cardNumber: String,
        expirationDate: String,
        securityCode: String?,
        buyerIdentification: BuyerIdentification,
    ): Result<CardToken, ResultError> {
        val result = koin.get<GenerateCardTokenUseCase>().invoke(
            cardNumber = cardNumber,
            expirationDate = expirationDate,
            securityCode = securityCode,
            buyerIdentification = buyerIdentification,
        )

        when (result) {
            is Result.Error -> {
                when (result.error) {
                    is ResultError.Request -> {
                        MPAnalytics.getInstance().trackMetric(
                            metricGenerateCardTokenCallError(
                                error = result.error.message,
                            ),
                        )
                    }

                    is ResultError.Validation -> {
                        MPAnalytics.getInstance().trackMetric(
                            metricGenerateCardTokenCallError(
                                error = result.error.message,
                            ),
                        )
                    }
                }
            }

            is Result.Success -> {
                MPAnalytics.getInstance().trackMetric(
                    metricGenerateCardTokenCallSuccess(),
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
            return instance ?: synchronized(this) {
                instance ?: CoreMethods(
                    koin = CoreMethodsModulesProvider().koinApp,
                ).also {
                    instance = it
                }
            }
        }

        /**
         * @suppress
         * Only for internal usage. DO NOT USE IN PRODUCTION.
         * Clear the current instance of the CoreMethods for testing purposes.
         */
        @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
        fun clearInstance() {
            instance = null
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
