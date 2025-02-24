package com.mercadopago.sdk.android.initializer.coroutines

import kotlin.test.assertNotNull
import org.junit.Test

internal class SdkCoroutineProviderTest {

    @Test
    fun `when provideSDKCoroutineScope is called Then return a CoroutineScope`() {
        // When
        val sdkCoroutineProvider = SdkCoroutineProvider.provideSDKCoroutineScope()

        // Then
        assertNotNull(sdkCoroutineProvider)
    }
}
