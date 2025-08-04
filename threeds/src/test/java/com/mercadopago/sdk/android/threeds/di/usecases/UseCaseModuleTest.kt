package com.mercadopago.sdk.android.threeds.di.usecases

import com.mercadopago.sdk.android.threeds.domain.adapter.ThreeDSSDKAdapter
import com.mercadopago.sdk.android.threeds.domain.repository.ThreeDSRepository
import com.mercadopago.sdk.android.threeds.domain.usecase.AuthenticateUseCase
import com.mercadopago.sdk.android.threeds.domain.usecase.RequestChallengeUseCase
import io.mockk.mockk
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.assertNotNull

internal class UseCaseModuleTest : KoinTest {

    @Test
    fun `when usecase module is loaded Then AuthenticateUseCase should be provided`() {
        // Given
        val mockRepository = mockk<ThreeDSRepository>(relaxed = true)
        val mockAdapter = mockk<ThreeDSSDKAdapter>(relaxed = true)

        startKoin {
            modules(
                module {
                    single { mockRepository }
                    single { mockAdapter }
                },
                provideUseCaseModule()
            )
        }

        // When
        val authenticateUseCase: AuthenticateUseCase by inject()

        // Then
        assertNotNull(authenticateUseCase)

        // Clean up
        stopKoin()
    }

    @Test
    fun `when usecase module is loaded Then RequestChallengeUseCase should be provided`() {
        // Given
        val mockRepository = mockk<ThreeDSRepository>(relaxed = true)
        val mockAdapter = mockk<ThreeDSSDKAdapter>(relaxed = true)

        startKoin {
            modules(
                module {
                    single { mockRepository }
                    single { mockAdapter }
                },
                provideUseCaseModule()
            )
        }

        // When
        val requestChallengeUseCase: RequestChallengeUseCase by inject()

        // Then
        assertNotNull(requestChallengeUseCase)

        // Clean up
        stopKoin()
    }

    @Test
    fun `when usecase module is loaded multiple times Then should provide different instances`() {
        // Given
        val mockRepository = mockk<ThreeDSRepository>(relaxed = true)
        val mockAdapter = mockk<ThreeDSSDKAdapter>(relaxed = true)

        startKoin {
            modules(
                module {
                    single { mockRepository }
                    single { mockAdapter }
                },
                provideUseCaseModule()
            )
        }

        // When
        val authenticateUseCase1: AuthenticateUseCase by inject()
        val authenticateUseCase2: AuthenticateUseCase by inject()
        val requestChallengeUseCase1: RequestChallengeUseCase by inject()
        val requestChallengeUseCase2: RequestChallengeUseCase by inject()

        // Then
        assertNotNull(authenticateUseCase1)
        assertNotNull(authenticateUseCase2)
        assertNotNull(requestChallengeUseCase1)
        assertNotNull(requestChallengeUseCase2)

        // Factory scope should provide different instances
        assert(authenticateUseCase1 !== authenticateUseCase2)
        assert(requestChallengeUseCase1 !== requestChallengeUseCase2)

        // Clean up
        stopKoin()
    }

    @Test
    fun `when provideUseCaseModule is called Then should return valid module`() {
        // When
        val module = provideUseCaseModule()

        // Then
        assertNotNull(module)
    }
}
