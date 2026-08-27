package com.mercadopago.sdk.android.checkout.presentation.navigation

import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType
import com.mercadopago.sdk.android.checkout.core.model.MPOrder
import com.mercadopago.sdk.android.checkout.domain.exception.ErrorCode
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.OrderProcessOutput
import com.mercadopago.sdk.android.checkout.presentation.navigation.reviewconfirm.ReviewConfirmNavigationAction
import com.mercadopago.sdk.android.checkout.presentation.navigation.reviewconfirm.ReviewConfirmNavigationPolicy
import com.mercadopago.sdk.android.checkout.presentation.state.ReviewConfirmViewEvent
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertSame

internal class ReviewConfirmNavigationPolicyTest {
    private val order = MPOrder(orderId = "", clientToken = "")
    private val payment = MPCheckoutType.Payment(order)
    private val cardTransaction = MPCheckoutType.CardTransaction(order)
    private val origins = ReviewOrigin.entries
    private val checkoutTypes = listOf<MPCheckoutType<*, *>?>(
        payment,
        cardTransaction,
        MPCheckoutType.CardSave,
        null,
    )

    @Test
    fun `back navigates up preserving every origin`() {
        origins.forEach { origin ->
            assertEquals(
                ReviewConfirmNavigationAction.NavigateUp(origin),
                ReviewConfirmNavigationPolicy.resolveBack(origin),
            )
        }
    }

    @Test
    fun `payment success finishes preserving output for every origin and checkout type`() {
        val output = OrderProcessOutput(id = "order-id", status = "approved")
        val event = ReviewConfirmViewEvent.OnPaymentSuccess(output)

        origins.forEach { origin ->
            checkoutTypes.forEach { checkoutType ->
                val action = assertIs<ReviewConfirmNavigationAction.FinishWithSuccess>(
                    resolve(event, checkoutType, origin),
                )

                assertSame(output, action.output)
            }
        }
    }

    @Test
    fun `payment error finishes preserving error for every origin and checkout type`() {
        val error = checkoutError()
        val event = ReviewConfirmViewEvent.OnPaymentError(error)

        origins.forEach { origin ->
            checkoutTypes.forEach { checkoutType ->
                val action = assertIs<ReviewConfirmNavigationAction.FinishWithError>(
                    resolve(event, checkoutType, origin),
                )

                assertSame(error, action.error)
            }
        }
    }

    @Test
    fun `load failure returns to payment selector for every origin in payment checkout`() {
        val event = ReviewConfirmViewEvent.OnLoadFailure(checkoutError())

        origins.forEach { origin ->
            assertSame(
                ReviewConfirmNavigationAction.ReturnToPaymentSelectorWithGenericError,
                resolve(event, payment, origin),
            )
        }
    }

    @Test
    fun `load failure navigates up preserving origin when payment selector is unavailable`() {
        val event = ReviewConfirmViewEvent.OnLoadFailure(checkoutError())
        val checkoutTypesWithoutPaymentSelector = listOf<MPCheckoutType<*, *>?>(
            cardTransaction,
            MPCheckoutType.CardSave,
            null,
        )

        origins.forEach { origin ->
            checkoutTypesWithoutPaymentSelector.forEach { checkoutType ->
                assertEquals(
                    ReviewConfirmNavigationAction.NavigateUp(origin),
                    resolve(event, checkoutType, origin),
                )
            }
        }
    }

    @Test
    fun `modify payment returns to payment selector for every origin in payment checkout`() {
        origins.forEach { origin ->
            assertSame(
                ReviewConfirmNavigationAction.ReturnToPaymentSelector,
                resolve(modifyPaymentEvent(), payment, origin),
            )
        }
    }

    @Test
    fun `modify payment finishes with cancellation for every origin in card transaction checkout`() {
        origins.forEach { origin ->
            assertSame(
                ReviewConfirmNavigationAction.FinishWithCardTransactionCancellation,
                resolve(modifyPaymentEvent(), cardTransaction, origin),
            )
        }
    }

    @Test
    fun `modify payment navigates up preserving every origin for fallback checkout types`() {
        val fallbackCheckoutTypes = listOf<MPCheckoutType<*, *>?>(MPCheckoutType.CardSave, null)

        origins.forEach { origin ->
            fallbackCheckoutTypes.forEach { checkoutType ->
                assertEquals(
                    ReviewConfirmNavigationAction.NavigateUp(origin),
                    resolve(modifyPaymentEvent(), checkoutType, origin),
                )
            }
        }
    }

    @Test
    fun `modify email finishes email change for every origin in payment checkout`() {
        origins.forEach { origin ->
            assertSame(
                ReviewConfirmNavigationAction.FinishForEmailChange,
                resolve(modifyEmailEvent(), payment, origin),
            )
        }
    }

    @Test
    fun `modify email navigates up preserving every origin for fallback checkout types`() {
        val fallbackCheckoutTypes = listOf<MPCheckoutType<*, *>?>(
            cardTransaction,
            MPCheckoutType.CardSave,
            null,
        )

        origins.forEach { origin ->
            fallbackCheckoutTypes.forEach { checkoutType ->
                assertEquals(
                    ReviewConfirmNavigationAction.NavigateUp(origin),
                    resolve(modifyEmailEvent(), checkoutType, origin),
                )
            }
        }
    }

    private fun resolve(
        event: ReviewConfirmViewEvent,
        checkoutType: MPCheckoutType<*, *>?,
        origin: ReviewOrigin,
    ): ReviewConfirmNavigationAction = ReviewConfirmNavigationPolicy.resolve(
        event = event,
        checkoutType = checkoutType,
        origin = origin,
    )

    private fun modifyPaymentEvent() =
        ReviewConfirmViewEvent.OnModifyPaymentMethod(itemType = "payment_method")

    private fun modifyEmailEvent() = ReviewConfirmViewEvent.OnModifyEmail

    private fun checkoutError() = MercadoPagoCheckoutError.ServiceError(
        code = ErrorCode.SERVICE_ERROR,
        messageError = "",
        localized = "review_confirm",
    )
}
