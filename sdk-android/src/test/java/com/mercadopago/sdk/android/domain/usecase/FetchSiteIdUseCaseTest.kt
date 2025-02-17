package com.mercadopago.sdk.android.domain.usecase

import app.cash.turbine.test
import com.mercadopago.sdk.android.domain.model.SiteId
import com.mercadopago.sdk.android.domain.repository.SdkInitializationRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

internal class FetchSiteIdUseCaseTest {

    private val sdkInitializationRepository = mockk<SdkInitializationRepository>()
    private val useCase = FetchSiteIdUseCase(sdkInitializationRepository)

    @Test
    fun `when invoke is called with success Then emit siteId`() = runTest {
        // Given
        val siteId = SiteId("123")
        val publicKey = "public_key"
        every { sdkInitializationRepository.fetchSiteId(publicKey) } returns flowOf(siteId)

        // When
        val result = useCase(publicKey)

        // Then
        result.test {
            assertEquals(siteId, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `when invoke is called with error Then emit error`() = runTest {
        // Given
        val exception = Exception()
        val publicKey = "public_key"
        every {
            sdkInitializationRepository.fetchSiteId(publicKey)
        } returns flow { throw exception }

        // When
        val result = useCase(publicKey)

        // Then
        result.test {
            assertEquals(exception, awaitError())
        }
    }
}
