package com.mercadopago.sdk.android.coremethods.di

import junit.framework.TestCase.assertNotNull
import org.koin.core.context.GlobalContext.stopKoin
import kotlin.test.Test

internal class CoreMethodsModulesProviderTest {
    @Test
    fun `test CoreMethodsModulesProvider provides GenerateCardTokenUseCase`() {
        val publicKey = "test_public_key"
        val provider = CoreMethodsModulesProvider(publicKey)
        val useCase = provider.provideGenerateCardTokenUseCase()

        assertNotNull(useCase)
        stopKoin()
    }
}
