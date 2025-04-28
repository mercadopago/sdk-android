package com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdatefield

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate.ExpirationDateFormat
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate.ExpirationDateTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate.ExpirationDateTextFieldXML
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.Test

@RunWith(RobolectricTestRunner::class)
internal class ExpirationDateTextFieldXMLTest {

    private fun createTextField(
        readOnly: Boolean = false,
        onEvent: (ExpirationDateTextFieldEvent) -> Unit = {},
        dateFormat: ExpirationDateFormat = ExpirationDateFormat.ShortFormat,
    ): ExpirationDateTextFieldXML {
        val context: Context = ApplicationProvider.getApplicationContext()
        return ExpirationDateTextFieldXML(context = context).apply {
            this.readOnly = readOnly
            this.onEvent = onEvent
            this.dateFormat = dateFormat
        }
    }

    @Test
    fun `when create field then set default values`() {
        val field = createTextField()
        assertEquals(ExpirationDateFormat.ShortFormat, field.dateFormat)
        assertFalse(field.readOnly)
    }

    @Test
    fun `when create field with params then set params values`() {
        val field = createTextField(readOnly = true, dateFormat = ExpirationDateFormat.LongFormat)
        assertTrue(field.readOnly)
        assertEquals(ExpirationDateFormat.LongFormat, field.dateFormat)
    }
}
