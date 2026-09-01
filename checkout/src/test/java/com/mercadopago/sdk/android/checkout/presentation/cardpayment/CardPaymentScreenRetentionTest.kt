package com.mercadopago.sdk.android.checkout.presentation.cardpayment

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import com.mercadopago.android.sdk.checkout.R
import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.checkout.presentation.navigation.form.CheckoutFormActions
import com.mercadopago.sdk.android.checkout.presentation.navigation.form.CheckoutFormDestination
import com.mercadopago.sdk.android.checkout.presentation.navigation.form.CheckoutFormParam
import com.mercadopago.sdk.android.checkout.presentation.state.CardHolderState
import com.mercadopago.sdk.android.checkout.presentation.state.CardNumberState
import com.mercadopago.sdk.android.checkout.presentation.state.CardPaymentScreenState
import com.mercadopago.sdk.android.checkout.presentation.state.CardPaymentViewEvent
import com.mercadopago.sdk.android.checkout.presentation.state.IdentificationTypeState
import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeState
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.CardPaymentViewModel
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import kotlin.test.Test

@RunWith(RobolectricTestRunner::class)
internal class CardPaymentScreenRetentionTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        mockkObject(MPAnalytics.Companion)
        val analytics = mockk<MPAnalytics>(relaxed = true)
        every { MPAnalytics.getInstance() } returns analytics
        every { MPAnalytics.tryGetInstance() } returns analytics
    }

    @After
    fun tearDown() {
        unmockkObject(MPAnalytics.Companion)
    }

    @Test
    fun `when card form returns to composition then card number remains filled`() {
        val viewModel = cardPaymentViewModel()
        val showScreen = mutableStateOf(true)

        composeTestRule.setContent {
            MercadoPagoTheme {
                if (showScreen.value) {
                    CardPaymentScreen(viewModel)
                }
            }
        }
        composeTestRule.onNodeWithTag(CARD_NUMBER_TEST_TAG).performTextInput(CARD_NUMBER)
        composeTestRule.onNodeWithTag(CARD_NUMBER_TEST_TAG).assert(hasText(MASKED_CARD_NUMBER))

        composeTestRule.runOnIdle { showScreen.value = false }
        composeTestRule.waitForIdle()
        composeTestRule.runOnIdle { showScreen.value = true }

        composeTestRule.onNodeWithTag(CARD_NUMBER_TEST_TAG)
            .assert(hasText(MASKED_CARD_NUMBER))
    }

    @Test
    fun `when review confirm error is received then generic snackbar is displayed`() {
        val viewModel = cardPaymentViewModel()
        val viewEvent = MutableStateFlow<CardPaymentViewEvent?>(
            CardPaymentViewEvent.OnReviewConfirmError,
        )
        every { viewModel.viewEvent } returns viewEvent
        every { viewModel.onViewEventConsumed() } answers { viewEvent.value = null }
        val genericErrorMessage = RuntimeEnvironment.getApplication()
            .getString(R.string.card_form_generic_error)

        composeTestRule.setContent {
            MercadoPagoTheme {
                CheckoutFormDestination(
                    param = CheckoutFormParam(viewModel),
                    actions = checkoutFormActions(),
                )
            }
        }

        composeTestRule.onNodeWithText(genericErrorMessage).assertIsDisplayed()
    }

    private fun cardPaymentViewModel(): CardPaymentViewModel {
        val viewModel = mockk<CardPaymentViewModel>(relaxed = true)
        every { viewModel.viewState } returns MutableStateFlow(
            CardPaymentScreenState(
                cardNumberState = CardNumberState(
                    label = "Card number",
                    maxLength = CARD_NUMBER.length,
                    mask = CARD_NUMBER_MASK,
                ),
                cardHolderState = CardHolderState(show = false),
                identificationTypeState = IdentificationTypeState(show = false),
                secureCodeState = SecurityCodeState(optional = true),
            ),
        )
        every { viewModel.cardNumberPCIState } returns PCIFieldState.create()
        every { viewModel.expirationDatePCIState } returns PCIFieldState.create()
        every { viewModel.securityCodePCIState } returns PCIFieldState.create()
        every { viewModel.cardHolderPCIState } returns PCIFieldState.create()
        every { viewModel.identificationPCIState } returns PCIFieldState.create()
        return viewModel
    }

    private fun checkoutFormActions() = CheckoutFormActions(
        onOpenInstallments = { _, _ -> },
        onInstallmentConfirmed = {},
        onOpenReview = {},
        onFinishCheckout = {},
        onInvalidInstallmentData = {},
        onMarkScreenPresented = {},
    )

    private companion object {
        const val CARD_NUMBER_TEST_TAG = "pci_card_number_field"
        const val CARD_NUMBER = "1234123412341234"
        const val CARD_NUMBER_MASK = "#### #### #### ####"
        const val MASKED_CARD_NUMBER = "1234 1234 1234 1234"
    }
}
