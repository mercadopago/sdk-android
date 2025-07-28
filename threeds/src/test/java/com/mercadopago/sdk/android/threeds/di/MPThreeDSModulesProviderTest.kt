package com.mercadopago.sdk.android.threeds.di

import android.content.Context
import com.mercadopago.sdk.android.domain.model.CountryCode
import com.mercadopago.sdk.android.initializer.MercadoPagoSDK
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import kotlin.test.assertTrue

internal class MPThreeDSModulesProviderTest {

    private val context = mockk<Context>(relaxed = true)

    @Before
    fun start(){
        MercadoPagoSDK.initialize(context, "public_key", CountryCode.BRA)
    }

    @Test
    fun `when MPThreeDSModulesProvider is created Then should provide all required modules`() {
        // Given
        val context = mockk<Context>(relaxed = true)

        // When & Then
        val modulesProvider = MPThreeDSModulesProvider(context)
        val modules = modulesProvider.provideModules()

        // Verify modules were created
        assertTrue(modules.isNotEmpty(), "Should provide at least one module")
        assertTrue(
            modules.size == 5,
            "Should provide exactly 5 modules (network, datasource, repository, usecase, adapters)"
        )
    }
}
