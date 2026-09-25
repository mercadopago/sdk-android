package com.mercadopago.sdk.android.checkout.presentation

import com.mercadopago.sdk.android.checkout.domain.callback.CheckoutCallbackHolder
import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.checkout.domain.exception.ErrorCode
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.MPUserCancelledContext
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.presentation.state.CardPaymentViewEvent
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.CardPaymentViewModel
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertSame

internal class CheckoutControllerTest {
    @After
    fun tearDown() {
        CheckoutCallbackHolder.setCallback<MPPaymentData.CardSave, MPUserCancelledContext.CardSave>(null)
    }

    @Test
    fun `given CardForm failure then callback receives the same public error`() {
        val expectedError = MercadoPagoCheckoutError.NetworkError(
            code = ErrorCode.NETWORK_CONNECTION_FAILED,
            messageError = "Connection failed",
            localized = "checkout",
            throwable = null,
        )
        val viewModel = mockk<CardPaymentViewModel>(relaxed = true)
        var callbackResult: MercadoPagoCheckoutResult<MPPaymentData.CardSave, MPUserCancelledContext.CardSave>? = null
        CheckoutCallbackHolder.setCallback<MPPaymentData.CardSave, MPUserCancelledContext.CardSave> {
            callbackResult = it
        }

        handleCardFormFailure(CardPaymentViewEvent.OnFailure(expectedError), viewModel)

        val errorResult = assertIs<MercadoPagoCheckoutResult.Error>(callbackResult)
        assertSame(expectedError, errorResult.error)
        verify(exactly = 1) { viewModel.onViewEventConsumed() }
    }
}
