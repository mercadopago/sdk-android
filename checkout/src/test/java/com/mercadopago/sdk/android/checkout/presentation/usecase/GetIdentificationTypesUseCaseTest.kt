package com.mercadopago.sdk.android.checkout.presentation.usecase

import com.mercadopago.sdk.android.checkout.domain.exception.ErrorCode
import com.mercadopago.sdk.android.checkout.domain.exception.ErrorLocalized
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.coremethods.domain.interactor.CoreMethods
import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.domain.model.CountryCode
import com.mercadopago.sdk.android.initializer.MercadoPagoSDK
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

internal class GetIdentificationTypesUseCaseTest {
    private val coreMethods = mockk<CoreMethods>()
    private val useCase = GetIdentificationTypesUseCase(coreMethods)

    @Before
    fun setUp() {
        mockkObject(MercadoPagoSDK.Companion)
    }

    @After
    fun tearDown() {
        unmockkObject(MercadoPagoSDK.Companion)
    }

    @Test
    fun `given country code is MEX then returns empty list without calling coreMethods`() = runTest {
        every { MercadoPagoSDK.countryCode } returns CountryCode.MEX

        val result = useCase()

        assertIs<Result.Success<List<IdentificationType>>>(result)
        assertEquals(emptyList(), result.data)
        coVerify(exactly = 0) { coreMethods.getIdentificationTypes() }
    }

    @Test
    fun `given country code is not MEX then calls coreMethods and returns identification types`() = runTest {
        val identificationTypes = listOf(mockk<IdentificationType>(), mockk<IdentificationType>())
        every { MercadoPagoSDK.countryCode } returns CountryCode.BRA
        coEvery { coreMethods.getIdentificationTypes() } returns Result.Success(identificationTypes)

        val result = useCase()

        assertIs<Result.Success<List<IdentificationType>>>(result)
        assertEquals(identificationTypes, result.data)
    }

    @Test
    fun `given country code is null then calls coreMethods`() = runTest {
        val identificationTypes = listOf(mockk<IdentificationType>())
        every { MercadoPagoSDK.countryCode } returns null
        coEvery { coreMethods.getIdentificationTypes() } returns Result.Success(identificationTypes)

        val result = useCase()

        assertIs<Result.Success<List<IdentificationType>>>(result)
        assertEquals(identificationTypes, result.data)
    }

    @Test
    fun `given country code is not MEX and coreMethods returns network error then returns NetworkError`() = runTest {
        val requestError = ResultError.Request(message = "Connection failed", code = "NETWORK_ERROR")
        every { MercadoPagoSDK.countryCode } returns CountryCode.ARG
        coEvery { coreMethods.getIdentificationTypes() } returns Result.Error(requestError)

        val result = useCase()

        assertIs<Result.Error<MercadoPagoCheckoutError>>(result)
        assertIs<MercadoPagoCheckoutError.NetworkError>(result.error)
        assertEquals(ErrorCode.NETWORK_CONNECTION_FAILED, result.error.errorCode)
        assertEquals(ErrorLocalized.IDENTIFICATION.name, result.error.errorLocalized)
    }

    @Test
    fun `given country code is not MEX and coreMethods returns validation error then returns ServiceError`() = runTest {
        val validationError = ResultError.Validation(message = "Validation failed")
        every { MercadoPagoSDK.countryCode } returns CountryCode.COL
        coEvery { coreMethods.getIdentificationTypes() } returns Result.Error(validationError)

        val result = useCase()

        assertIs<Result.Error<MercadoPagoCheckoutError>>(result)
        assertIs<MercadoPagoCheckoutError.ServiceError>(result.error)
        assertEquals(ErrorCode.SERVICE_ERROR, result.error.errorCode)
        assertEquals(ErrorLocalized.IDENTIFICATION.name, result.error.errorLocalized)
    }
}
