package com.mercadopago.sdk.android.checkout.presentation.email

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.mercadopago.sdk.android.checkout.domain.model.EmailInitializationOutput
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.EmailViewModel
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemes
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.Test
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
internal class EmailScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private fun setScreen(
        prefilledEmail: String? = null,
        onBackClick: () -> Unit = {},
        onClick: () -> Unit = {},
    ) {
        val viewModel = EmailViewModel().apply {
            initialize(
                EmailInitializationOutput(
                    title = "Complete your email",
                    buttonLabel = "Continue",
                    fieldLabel = "Email",
                    fieldPlaceholder = "maria.sosa@gmail.com",
                    errorFieldEmpty = "Enter an email",
                    errorEmailInvalid = "Invalid email",
                    prefilledEmail = prefilledEmail,
                ),
            )
        }
        composeTestRule.setContent {
            MercadoPagoTheme(theme = MercadoPagoThemes.Default) {
                EmailScreen(
                    viewModel = viewModel,
                    onBackClick = onBackClick,
                    onClick = onClick,
                )
            }
        }
    }

    @Test
    fun `when screen is rendered then title and continue button are displayed`() {
        setScreen()

        composeTestRule.onAllNodesWithText("Complete your email").onFirst().assertIsDisplayed()
        composeTestRule.onNodeWithText("Continue").assertIsDisplayed()
    }

    @Test
    fun `when no email is typed then continue button is disabled`() {
        setScreen()

        composeTestRule.onNodeWithText("Continue").assertIsNotEnabled()
    }

    @Test
    fun `when prefilled with valid email then continue button is enabled`() {
        setScreen(prefilledEmail = "user@example.com")

        composeTestRule.onNodeWithText("Continue").assertIsEnabled()
    }

    @Test
    fun `when a valid email is typed then continue button becomes enabled`() {
        setScreen()

        composeTestRule.onNode(hasSetTextAction()).performTextInput("user@example.com")

        composeTestRule.onNodeWithText("Continue").assertIsEnabled()
    }

    @Test
    fun `when continue is clicked then callback is invoked`() {
        var continueClicked = false
        setScreen(onClick = { continueClicked = true })

        composeTestRule.onNode(hasSetTextAction()).performTextInput("user@example.com")
        composeTestRule.onNodeWithText("Continue").performClick()

        assertTrue(continueClicked)
    }

    @Test
    fun `when back is clicked then back callback is invoked`() {
        var backClicked = false
        setScreen(onBackClick = { backClicked = true })

        composeTestRule.onNodeWithContentDescription("Back").performClick()

        assertTrue(backClicked)
    }
}
