package com.mercadopago.sdk.android.threeds.di.datasource

import com.mercadopago.sdk.android.threeds.data.datasource.remote.ThreeDSRemoteDataSource
import com.mercadopago.sdk.android.threeds.data.datasource.remote.ThreeDSRemoteDataSourceImpl
import com.mercadopago.sdk.android.threeds.data.remote.service.ThreeDSService
import io.mockk.mockk
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

internal class DataSourceModuleTest : KoinTest {

    @Test
    fun `when datasource module is loaded Then ThreeDSRemoteDataSource should be provided`() {
        // Given
        val mockService = mockk<ThreeDSService>(relaxed = true)

        startKoin {
            modules(
                module {
                    single { mockService }
                },
                provideDataSourceModule()
            )
        }

        // When
        val dataSource: ThreeDSRemoteDataSource by inject()

        // Then
        assertNotNull(dataSource)
        assertTrue(dataSource is ThreeDSRemoteDataSourceImpl)

        // Clean up
        stopKoin()
    }

    @Test
    fun `when datasource module is loaded multiple times Then should provide different instances`() {
        // Given
        val mockService = mockk<ThreeDSService>(relaxed = true)

        startKoin {
            modules(
                module {
                    single { mockService }
                },
                provideDataSourceModule()
            )
        }

        // When
        val dataSource1: ThreeDSRemoteDataSource by inject()
        val dataSource2: ThreeDSRemoteDataSource by inject()

        // Then
        assertNotNull(dataSource1)
        assertNotNull(dataSource2)
        // Factory scope should provide different instances
        assertTrue(dataSource1 !== dataSource2)

        // Clean up
        stopKoin()
    }

    @Test
    fun `when provideDataSourceModule is called Then should return valid module`() {
        // When
        val module = provideDataSourceModule()

        // Then
        assertNotNull(module)
    }
}
