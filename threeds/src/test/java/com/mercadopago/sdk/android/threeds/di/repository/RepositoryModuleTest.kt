package com.mercadopago.sdk.android.threeds.di.repository

import com.mercadopago.sdk.android.threeds.data.datasource.remote.ThreeDSRemoteDataSource
import com.mercadopago.sdk.android.threeds.data.repository.ThreeDSRepositoryImpl
import com.mercadopago.sdk.android.threeds.domain.repository.ThreeDSRepository
import io.mockk.mockk
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

internal class RepositoryModuleTest : KoinTest {

    @Test
    fun `when repository module is loaded Then ThreeDSRepository should be provided`() {
        // Given
        val mockDataSource = mockk<ThreeDSRemoteDataSource>(relaxed = true)

        startKoin {
            modules(
                module {
                    single { mockDataSource }
                },
                provideRepositoryModule()
            )
        }

        // When
        val repository: ThreeDSRepository by inject()

        // Then
        assertNotNull(repository)
        assertTrue(repository is ThreeDSRepositoryImpl)

        // Clean up
        stopKoin()
    }

    @Test
    fun `when repository module is loaded multiple times Then should provide different instances`() {
        // Given
        val mockDataSource = mockk<ThreeDSRemoteDataSource>(relaxed = true)

        startKoin {
            modules(
                module {
                    single { mockDataSource }
                },
                provideRepositoryModule()
            )
        }

        // When
        val repository1: ThreeDSRepository by inject()
        val repository2: ThreeDSRepository by inject()

        // Then
        assertNotNull(repository1)
        assertNotNull(repository2)
        // Factory scope should provide different instances
        assertTrue(repository1 !== repository2)

        // Clean up
        stopKoin()
    }

    @Test
    fun `when provideRepositoryModule is called Then should return valid module`() {
        // When
        val module = provideRepositoryModule()

        // Then
        assertNotNull(module)
    }
}
