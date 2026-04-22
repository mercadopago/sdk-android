package com.mercadopago.sdk.android.checkout.domain.usecase

import com.mercadopago.sdk.android.checkout.data.remote.datasource.CardFormRemoteDataSource
import com.mercadopago.sdk.android.checkout.data.remote.response.CardFormInitResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.CardNumberConfig
import com.mercadopago.sdk.android.checkout.data.remote.response.CardNumberTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.DocumentTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.ExpirationDateConfig
import com.mercadopago.sdk.android.checkout.data.remote.response.ExpirationDateTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.HolderNameConfig
import com.mercadopago.sdk.android.checkout.data.remote.response.HolderNameTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.IdentificationType
import com.mercadopago.sdk.android.checkout.data.remote.response.InstallmentsHeaderTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.InstallmentsTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.LengthConfig
import com.mercadopago.sdk.android.checkout.data.remote.response.SecurityCodeConfig
import com.mercadopago.sdk.android.checkout.data.remote.response.SecurityCodeTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.Translations
import com.mercadopago.sdk.android.checkout.domain.exception.ErrorCode
import com.mercadopago.sdk.android.checkout.domain.exception.ErrorLocalized
import com.mercadopago.sdk.android.checkout.domain.model.CardFormInitializationOutput
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

internal class InitializeCardFormUseCaseTest {
    private val dataSource = mockk<CardFormRemoteDataSource>()
    private val useCase = InitializeCardFormUseCase(dataSource)

    private val amount = "100.00"
    private val checkoutType = "card_form"

    private val translations = Translations(
        cardFormTitle = "Dados do cartão",
        cardFormFooterButtonLabel = "Continuar",
        cardNumber = CardNumberTranslations(
            label = "Número do cartão",
            placeholder = "0000 0000 0000 0000",
            errorEmptyField = "Campo obrigatório",
            errorIncompleteField = "Número incompleto",
            errorInvalidField = "Número inválido",
        ),
        holderName = HolderNameTranslations(
            label = "Nome no cartão",
            placeholder = "NOME",
            errorEmptyField = "Campo obrigatório",
            errorIncompleteField = "Nome incompleto",
            errorInvalidField = "Nome inválido",
        ),
        expirationDate = ExpirationDateTranslations(
            label = "Validade",
            placeholder = "MM/AA",
            errorEmptyField = "Campo obrigatório",
            errorIncompleteField = "Data incompleta",
            errorInvalidField = "Data inválida",
        ),
        securityCode = SecurityCodeTranslations(
            label = "CVV",
            placeholder = "123",
            tooltip = "3 dígitos no verso",
            errorEmptyField = "Campo obrigatório",
            errorIncompleteField = "CVV incompleto",
        ),
        document = DocumentTranslations(
            label = "CPF",
            errorEmptyField = "Campo obrigatório",
            errorIncompleteField = "CPF incompleto",
            errorInvalidField = "CPF inválido",
        ),
        installments = InstallmentsTranslations(
            header = InstallmentsHeaderTranslations(chevron = ">", radio = "o", title = "Parcelas"),
            interestFreeLabel = "sem juros",
            totalLabel = "total",
        ),
    )

    private val identificationTypes = listOf(
        IdentificationType(
            id = "CPF",
            name = "CPF",
            minLength = 11,
            maxLength = 11,
            placeholder = "999.999.999-99",
            mask = "###.###.###-##",
            type = "number",
            sequence = "1",
        ),
    )

    private fun buildResponse() = CardFormInitResponse(
        identificationTypes = identificationTypes,
        cardNumber = CardNumberConfig(
            type = "number",
            length = LengthConfig(min = 16, max = 19),
            mask = "#### #### #### ####",
        ),
        securityCode = SecurityCodeConfig(length = 3, type = "number"),
        holderName = HolderNameConfig(
            type = "text",
            length = LengthConfig(min = 3, max = 50),
        ),
        expirationDate = ExpirationDateConfig(
            type = "number",
            mask = "##/##",
            length = LengthConfig(min = 4, max = 4),
        ),
        translations = translations,
    )

    @Test
    fun `given dataSource returns success then returns CardFormInitializationOutput`() = runTest {
        coEvery { dataSource.fetchInitialization(amount, checkoutType) } returns Result.Success(buildResponse())

        val result = useCase(amount, checkoutType)

        assertIs<Result.Success<CardFormInitializationOutput>>(result)
    }

    @Test
    fun `given dataSource returns success then title is mapped from translations`() = runTest {
        coEvery { dataSource.fetchInitialization(amount, checkoutType) } returns Result.Success(buildResponse())

        val result = useCase(amount, checkoutType)

        assertIs<Result.Success<CardFormInitializationOutput>>(result)
        assertEquals("Dados do cartão", result.data.title)
    }

    @Test
    fun `given dataSource returns success then button is mapped from translations`() = runTest {
        coEvery { dataSource.fetchInitialization(amount, checkoutType) } returns Result.Success(buildResponse())

        val result = useCase(amount, checkoutType)

        assertIs<Result.Success<CardFormInitializationOutput>>(result)
        assertEquals("Continuar", result.data.button)
    }

    @Test
    fun `given dataSource returns success then identificationTypes are mapped correctly`() = runTest {
        coEvery { dataSource.fetchInitialization(amount, checkoutType) } returns Result.Success(buildResponse())

        val result = useCase(amount, checkoutType)

        assertIs<Result.Success<CardFormInitializationOutput>>(result)
        val idTypes = result.data.identificationTypes
        assertEquals(1, idTypes.size)
        assertEquals("CPF", idTypes.first().id)
        assertEquals(11, idTypes.first().minLength)
        assertEquals(11, idTypes.first().maxLength)
    }

    @Test
    fun `given dataSource returns success then passes correct amount and checkoutType`() = runTest {
        coEvery { dataSource.fetchInitialization(amount, checkoutType) } returns Result.Success(buildResponse())

        useCase(amount, checkoutType)

        coVerify(exactly = 1) { dataSource.fetchInitialization(amount, checkoutType) }
    }

    @Test
    fun `given dataSource returns network error then returns NetworkError`() = runTest {
        val error = ResultError.Request(message = "Connection failed", code = "NETWORK_ERROR")
        coEvery { dataSource.fetchInitialization(amount, checkoutType) } returns Result.Error(error)

        val result = useCase(amount, checkoutType)

        assertIs<Result.Error<MercadoPagoCheckoutError>>(result)
        assertIs<MercadoPagoCheckoutError.NetworkError>(result.error)
        assertEquals(ErrorCode.NETWORK_CONNECTION_FAILED, result.error.errorCode)
        assertEquals(ErrorLocalized.CARD_FORM_INITIALIZATION.name, result.error.errorLocalized)
    }

    @Test
    fun `given dataSource returns timeout error then returns NetworkError with timeout code`() = runTest {
        val error = ResultError.Request(message = "Timeout", code = "TIMEOUT_ERROR")
        coEvery { dataSource.fetchInitialization(amount, checkoutType) } returns Result.Error(error)

        val result = useCase(amount, checkoutType)

        assertIs<Result.Error<MercadoPagoCheckoutError>>(result)
        assertIs<MercadoPagoCheckoutError.NetworkError>(result.error)
        assertEquals(ErrorCode.NETWORK_TIMEOUT, result.error.errorCode)
        assertEquals(ErrorLocalized.CARD_FORM_INITIALIZATION.name, result.error.errorLocalized)
    }

    @Test
    fun `given dataSource returns generic request error then returns ServiceError`() = runTest {
        val error = ResultError.Request(message = "Server error", code = "SERVER_ERROR")
        coEvery { dataSource.fetchInitialization(amount, checkoutType) } returns Result.Error(error)

        val result = useCase(amount, checkoutType)

        assertIs<Result.Error<MercadoPagoCheckoutError>>(result)
        assertIs<MercadoPagoCheckoutError.ServiceError>(result.error)
        assertEquals(ErrorCode.SERVICE_ERROR, result.error.errorCode)
        assertEquals(ErrorLocalized.CARD_FORM_INITIALIZATION.name, result.error.errorLocalized)
    }

    @Test
    fun `given dataSource returns validation error then returns ServiceError`() = runTest {
        val error = ResultError.Validation(message = "Invalid params")
        coEvery { dataSource.fetchInitialization(amount, checkoutType) } returns Result.Error(error)

        val result = useCase(amount, checkoutType)

        assertIs<Result.Error<MercadoPagoCheckoutError>>(result)
        assertIs<MercadoPagoCheckoutError.ServiceError>(result.error)
        assertEquals(ErrorCode.SERVICE_ERROR, result.error.errorCode)
        assertEquals(ErrorLocalized.CARD_FORM_INITIALIZATION.name, result.error.errorLocalized)
    }
}
