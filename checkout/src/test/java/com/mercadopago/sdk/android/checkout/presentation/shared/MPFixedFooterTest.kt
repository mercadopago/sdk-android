package com.mercadopago.sdk.android.checkout.presentation.shared

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import com.mercadopago.sdk.android.components.MPAmountData
import com.mercadopago.sdk.android.components.MPFixedFooter
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.Test
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
internal class MPFixedFooterTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `given zero decimal part then displays both decimal digits`() {
        setFooter(decimalPart = "00")

        composeTestRule.onNodeWithText("00").assertIsDisplayed()
    }

    @Test
    fun `given empty decimal part then does not display decimal text`() {
        setFooter(decimalPart = "")

        assertTrue(composeTestRule.onAllNodesWithText("00").fetchSemanticsNodes().isEmpty())
    }

    private fun setFooter(
        decimalPart: String,
    ) {
        composeTestRule.setContent {
            MercadoPagoTheme {
                MPFixedFooter(
                    title = "Total",
                    amount = MPAmountData(
                        currencySymbol = "$",
                        integerPart = "15",
                        decimalPart = decimalPart,
                    ),
                )
            }
        }
    }
}
