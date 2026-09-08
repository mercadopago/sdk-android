package com.mercadopago.sdk.android.checkout.analytics

import com.mercadopago.sdk.android.analytics.domain.classifier.NativeErrorClassifier
import com.mercadopago.sdk.android.analytics.domain.classifier.NativeErrorInput
import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.analytics.domain.models.NativeErrorCode
import com.mercadopago.sdk.android.analytics.domain.models.NativeErrorOperation
import com.mercadopago.sdk.android.checkout.domain.exception.ErrorLocalized
import com.mercadopago.sdk.android.checkout.domain.exception.ObservedCheckoutErrorFactory
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.checkout.presentation.model.CancelReason
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.CardFormAnalyticsTracker
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.InstallmentsAnalyticsTracker
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals

internal class CheckoutClassifierParityTest {
    private val classifier = NativeErrorClassifier()

    @Test
    fun `all checkout seams use neutral evidence and canonical classifier output`() {
        val operations = mutableListOf<NativeErrorOperation>()
        val inputs = mutableListOf<NativeErrorInput>()
        val analytics = mockk<MPAnalytics>()
        every { analytics.trackError(capture(operations), capture(inputs), any()) } returns Unit
        val observability = CheckoutErrorObservability { analytics }
        val cardFormTracker = CardFormAnalyticsTracker(
            isLoading = { false },
            errorObservability = observability,
        )
        val installmentsTracker = InstallmentsAnalyticsTracker(
            checkoutType = "card_form",
            paymentData = mockk(),
            installmentData = mockk(),
            orderId = "private",
            errorObservability = observability,
        )

        cardFormTracker.trackInitializeError(
            ObservedCheckoutErrorFactory.from(
                ResultError.Validation("private"),
                ErrorLocalized.CARD_FORM_INITIALIZATION,
            ),
        )
        cardFormTracker.trackSubmitError(observed("EMPTY_BODY"))
        cardFormTracker.trackOrderError(observed("SERVICE_ERROR", 503), orderId = "private")
        cardFormTracker.trackUserCanceled(CancelReason.SystemBack)
        installmentsTracker.trackUserCanceled(InstallmentsCancelReason.BackPressed)

        assertEquals(
            listOf(
                NativeErrorOperation.CARD_FORM_INITIALIZATION to NativeErrorCode.INPUT_VALIDATION_FAILED,
                NativeErrorOperation.CARD_FORM_SUBMISSION to NativeErrorCode.RESPONSE_CONTRACT_INVALID,
                NativeErrorOperation.ORDER_SUBMISSION to NativeErrorCode.UPSTREAM_REJECTED,
                NativeErrorOperation.CARD_FORM_CANCELLATION to NativeErrorCode.USER_CANCELLED,
                NativeErrorOperation.INSTALLMENTS_CANCELLATION to NativeErrorCode.USER_CANCELLED,
            ),
            operations.zip(inputs).map { (operation, input) ->
                operation to classifier.classify(operation, input).code
            },
        )
    }

    private fun observed(
        code: String,
        status: Int? = null,
    ) = ObservedCheckoutErrorFactory.from(
        ResponseError(code = code, message = "private", httpStatus = status),
        ErrorLocalized.CARD_FORM_INITIALIZATION,
    )
}
