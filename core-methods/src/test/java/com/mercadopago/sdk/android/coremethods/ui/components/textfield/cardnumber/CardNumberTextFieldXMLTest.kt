package com.mercadopago.sdk.android.coremethods.ui.components.textfield.cardnumber

import android.content.Context
import androidx.compose.ui.text.input.VisualTransformation
import androidx.test.core.app.ApplicationProvider
import com.mercadopago.sdk.android.coremethods.ui.utils.MaskVisualTransformationDefaults
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.Test

@RunWith(RobolectricTestRunner::class)
internal class CardNumberTextFieldXMLTest {

    private fun createTextField(
        visualTransformation: VisualTransformation = MaskVisualTransformationDefaults.CardNumber,
        readOnly: Boolean = false,
        onEvent: (CardNumberTextFieldEvent) -> Unit = {},
        maxLength: Int = DEFAULT_CARD_NUMBER_MAX_LENGTH,
    ): CardNumberTextFieldXML {
        val context: Context = ApplicationProvider.getApplicationContext()
        return CardNumberTextFieldXML(context = context).apply {
            this.readOnly = readOnly
            this.onEvent = onEvent
            this.visualTransformation = visualTransformation
            this.maxLength = maxLength
        }
    }

    @Test
    fun `when create field then set default values`() {
        val field = createTextField()
        assertEquals(DEFAULT_CARD_NUMBER_MAX_LENGTH, field.maxLength)
        assertFalse(field.readOnly)
    }

    @Test
    fun `when create field with params then set params values`() {
        val field = createTextField(readOnly = true, maxLength = 16)
        assertTrue(field.readOnly)
        assertEquals(16, field.maxLength)
    }
}
