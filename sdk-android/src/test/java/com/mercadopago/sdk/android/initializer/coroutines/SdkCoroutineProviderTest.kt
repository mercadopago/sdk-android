package com.mercadopago.sdk.android.initializer.coroutines

import org.junit.Test
import kotlin.test.assertNotNull

internal class SdkCoroutineProviderTest {

    @Test
    fun `when provideSDKCoroutineScope is called Then return a CoroutineScope`() {
        // When
        val sdkCoroutineProvider = SdkCoroutineProvider.provideSDKCoroutineScope()

        // Then
        assertNotNull(sdkCoroutineProvider)
    }
}
