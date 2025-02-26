package com.mercadopago.sdk.android.domain.usecase

import app.cash.turbine.test
import com.mercadopago.sdk.android.domain.model.CountryCode
import com.mercadopago.sdk.android.domain.repository.SdkInitializationRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

internal class SetSiteIdUseCaseTest {

    private val sdkInitializationRepository = mockk<SdkInitializationRepository>()
    private val useCase = SetSiteIdUseCase(sdkInitializationRepository)

    @Test
    fun `when invoke is called with success Then emit unit`() = runTest {
        // Given
        val countryCode = CountryCode.ARG
        val publicKey = "public_key"
        every {
            sdkInitializationRepository.setSiteId(publicKey, countryCode)
        } returns flowOf(Unit)

        // When
        val result = useCase(publicKey, countryCode)

        // Then
        result.test {
            assertEquals(Unit, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `when invoke is called with error Then emit error`() = runTest {
        // Given
        val exception = Exception()
        val countryCode = CountryCode.ARG
        val publicKey = "public_key"
        every {
            sdkInitializationRepository.setSiteId(publicKey, countryCode)
        } returns flow { throw exception }

        // When
        val result = useCase(publicKey, countryCode)

        // Then
        result.test {
            assertEquals(exception, awaitError())
        }
    }
}
