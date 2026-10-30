package com.mercadopago.sdk.android.checkout.core.model.internal

import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType
import com.mercadopago.sdk.android.checkout.core.model.MPOrder
import com.mercadopago.sdk.android.checkout.domain.model.MethodSelectionOption
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class ProcessOrderParamsExtensionsTest {
    private val order = MPOrder(
        orderId = "ORD_TEST",
        clientToken = "token-test",
    )

    private val config = CheckoutConfiguration(
        checkoutType = MPCheckoutType.Payment(order = order),
        paymentMethodConfigs = emptyList(),
    )

    private val option = MethodSelectionOption(
        id = "rapipago",
        name = "Rapipago",
    )

    @Test
    fun `given a payment type id when building params for method selection then it is forwarded as is`() {
        val params = config.buildProcessOrderParamsForMethodSelection(option = option, paymentTypeId = "ticket")

        assertEquals("ticket", params?.paymentMethodType)
        assertEquals("rapipago", params?.paymentMethodId)
    }

    @Test
    fun `given method selection option when building params then amount is not populated from any UI total`() {
        val params = config.buildProcessOrderParamsForMethodSelection(option = option, paymentTypeId = "ticket")

        assertEquals("", params?.amount)
    }

    @Test
    fun `given method selection option then orderId and clientToken come from the order`() {
        val params = config.buildProcessOrderParamsForMethodSelection(option = option, paymentTypeId = "ticket")

        assertEquals("ORD_TEST", params?.orderId)
        assertEquals("token-test", params?.clientToken)
    }

    @Test
    fun `given checkout type is not Payment when building params for method selection then returns null`() {
        val nonPaymentConfig = CheckoutConfiguration(
            checkoutType = MPCheckoutType.CardSave,
            paymentMethodConfigs = emptyList(),
        )

        val params = nonPaymentConfig.buildProcessOrderParamsForMethodSelection(
            option = option,
            paymentTypeId = "ticket",
        )

        assertNull(params)
    }
}
