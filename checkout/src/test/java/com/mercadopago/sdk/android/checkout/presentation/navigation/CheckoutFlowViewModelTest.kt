package com.mercadopago.sdk.android.checkout.presentation.navigation

import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.MethodSelectionScreenData
import com.mercadopago.sdk.android.checkout.domain.model.SelectionDisplayType
import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams
import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeScreenConfig
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertSame

internal class CheckoutFlowViewModelTest {
    private val viewModel = CheckoutFlowViewModel()

    @Test
    fun `when installment context is set and cleared then its values are updated`() {
        val installmentData = mockk<MPInstallmentData>()
        val paymentData = mockk<MPPaymentData>()

        viewModel.setInstallmentContext(installmentData, paymentData)

        assertSame(installmentData, viewModel.state.value.installmentContext?.installmentData)
        assertSame(paymentData, viewModel.state.value.installmentContext?.paymentData)

        viewModel.clearInstallmentContext()

        assertNull(viewModel.state.value.installmentContext)
    }

    @Test
    fun `when security code context is set and cleared then its value is updated`() {
        val config = mockk<SecurityCodeScreenConfig>()

        viewModel.setSecurityCodeContext(config)

        assertSame(config, viewModel.state.value.securityCodeContext)

        viewModel.clearSecurityCodeContext()

        assertNull(viewModel.state.value.securityCodeContext)
    }

    @Test
    fun `when review context is set then params and every origin are preserved until cleared`() {
        val params = createProcessOrderParams()

        ReviewOrigin.values().forEach { origin ->
            viewModel.setReviewContext(params, origin)

            assertSame(params, viewModel.state.value.reviewContext?.params)
            assertSame(origin, viewModel.state.value.reviewContext?.origin)
        }

        viewModel.clearReviewContext()

        assertNull(viewModel.state.value.reviewContext)
    }

    @Test
    fun `when offline method selector context is set and cleared then its value is updated`() {
        val data = mockk<MethodSelectionScreenData>()

        viewModel.setOfflineMethodSelectorContext(data)

        assertSame(data, viewModel.state.value.offlineMethodSelectorContext)

        viewModel.clearOfflineMethodSelectorContext()

        assertNull(viewModel.state.value.offlineMethodSelectorContext)
    }

    @Test
    fun `when payment feedback is shown and consumed then it is emitted only until consumed`() {
        viewModel.paymentFeedback.show(PaymentFeedback.GenericError)
        val event = requireNotNull(viewModel.paymentFeedback.event.value)

        assertEquals(PaymentFeedback.GenericError, event.feedback)

        viewModel.paymentFeedback.consume(event)

        assertNull(viewModel.paymentFeedback.event.value)
    }

    @Test
    fun `when stale payment feedback is consumed then newer feedback is preserved`() {
        viewModel.paymentFeedback.show(PaymentFeedback.GenericError)
        val staleEvent = requireNotNull(viewModel.paymentFeedback.event.value)
        viewModel.paymentFeedback.show(PaymentFeedback.GenericError)
        val currentEvent = requireNotNull(viewModel.paymentFeedback.event.value)

        viewModel.paymentFeedback.consume(staleEvent)

        assertSame(currentEvent, viewModel.paymentFeedback.event.value)
    }

    @Test
    fun `when clearAll is called then every transient context is removed`() {
        viewModel.setInstallmentContext(
            installmentData = mockk(),
            paymentData = mockk(),
        )
        viewModel.setSecurityCodeContext(mockk())
        viewModel.setReviewContext(createProcessOrderParams(), ReviewOrigin.Form)
        viewModel.setOfflineMethodSelectorContext(mockk())
        viewModel.paymentFeedback.show(PaymentFeedback.GenericError)

        viewModel.clearAll()

        assertNull(viewModel.state.value.installmentContext)
        assertNull(viewModel.state.value.securityCodeContext)
        assertNull(viewModel.state.value.reviewContext)
        assertNull(viewModel.state.value.offlineMethodSelectorContext)
        assertNull(viewModel.paymentFeedback.event.value)
    }

    @Test
    fun `when review context is converted to string then sensitive params are not exposed`() {
        val context = ReviewContext(
            params = createProcessOrderParams(),
            origin = ReviewOrigin.Payment,
        )

        val result = context.toString()

        assertEquals("ReviewContext(origin=Payment)", result)
        assertFalse(result.contains(CLIENT_TOKEN))
        assertFalse(result.contains(PAYMENT_TOKEN))
        assertFalse(result.contains(ORDER_ID))
        assertFalse(result.contains(PAYMENT_METHOD_ID))
    }

    @Test
    fun `when flow state is converted to string then it contains only flags and origin`() {
        val state = CheckoutFlowState(
            installmentContext = InstallmentContext(
                installmentData = MPInstallmentData(),
                paymentData = MPPaymentData.CardSave(
                    token = PAYMENT_DATA_TOKEN,
                    paymentMethodId = PAYMENT_METHOD_ID,
                    paymentTypeId = PAYMENT_TYPE_ID,
                    payer = null,
                    issuerId = ISSUER_ID,
                ),
            ),
            securityCodeContext = mockk(),
            reviewContext = ReviewContext(
                params = createProcessOrderParams(),
                origin = ReviewOrigin.OfflineMethodSelector,
            ),
            offlineMethodSelectorContext = MethodSelectionScreenData(
                headerTitle = METHOD_SELECTION_TITLE,
                selectionType = SelectionDisplayType.RadioButton,
                options = emptyList(),
            ),
        )

        val result = state.toString()

        assertEquals(
            "CheckoutFlowState(" +
                "hasInstallmentContext=true, " +
                "hasSecurityCodeContext=true, " +
                "hasReviewContext=true, " +
                "reviewOrigin=OfflineMethodSelector, " +
                "hasOfflineMethodSelectorContext=true" +
                ")",
            result,
        )
        assertFalse(result.contains(CLIENT_TOKEN))
        assertFalse(result.contains(PAYMENT_TOKEN))
        assertFalse(result.contains(PAYMENT_DATA_TOKEN))
        assertFalse(result.contains(PAYMENT_METHOD_ID))
        assertFalse(result.contains(PAYMENT_TYPE_ID))
        assertFalse(result.contains(ISSUER_ID))
        assertFalse(result.contains(METHOD_SELECTION_TITLE))
    }

    private fun createProcessOrderParams() = ProcessOrderParams(
        orderId = ORDER_ID,
        clientToken = CLIENT_TOKEN,
        paymentMethodId = PAYMENT_METHOD_ID,
        paymentMethodType = PAYMENT_TYPE_ID,
        token = PAYMENT_TOKEN,
        installments = 3,
        amount = "123.45",
    )

    private companion object {
        const val ORDER_ID = "sensitive-order-id"
        const val CLIENT_TOKEN = "sensitive-client-token"
        const val PAYMENT_TOKEN = "sensitive-payment-token"
        const val PAYMENT_DATA_TOKEN = "sensitive-payment-data-token"
        const val PAYMENT_METHOD_ID = "sensitive-payment-method-id"
        const val PAYMENT_TYPE_ID = "sensitive-payment-type-id"
        const val ISSUER_ID = "sensitive-issuer-id"
        const val METHOD_SELECTION_TITLE = "sensitive-method-selection-title"
    }
}
