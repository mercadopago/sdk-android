package com.mercadopago.sdk.android.checkout.core.model.internal

import android.os.Parcel
import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType
import com.mercadopago.sdk.android.checkout.core.model.MPOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@RunWith(RobolectricTestRunner::class)
internal class CheckoutConfigurationParcelTest {
    @Test
    fun `configuration with email callback survives parcel round trip without serializing callback`() {
        val configuration = CheckoutConfiguration(
            checkoutType = MPCheckoutType.Payment(
                order = MPOrder(orderId = "order-id", clientToken = "client-token"),
            ),
            paymentMethodConfigs = emptyList(),
            screenConfigs = listOf(ScreenConfig.ReviewAndConfirm(onEmailChangeRequested = {})),
        )
        val parcel = Parcel.obtain()

        try {
            parcel.writeParcelable(configuration, 0)
            parcel.setDataPosition(0)
            @Suppress("DEPRECATION")
            val restored = assertNotNull(
                parcel.readParcelable<CheckoutConfiguration>(CheckoutConfiguration::class.java.classLoader),
            )

            assertNull(restored.getOnEmailChangeRequested())
        } finally {
            parcel.recycle()
        }
    }
}
