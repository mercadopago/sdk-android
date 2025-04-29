package com.mercadopago.sdk.android.coremethods.ui.components.textfield.securityfield

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.securitycode.SecurityCodeTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.securitycode.SecurityCodeTextFieldXML
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.Test

@RunWith(RobolectricTestRunner::class)
class SecurityCodeTextFieldXMLTest {

    private fun createTextField(
        readOnly: Boolean = false,
        onEvent: (SecurityCodeTextFieldEvent) -> Unit = {},
        securityCodeSize: Int = 3,
    ): SecurityCodeTextFieldXML {
        val context: Context = ApplicationProvider.getApplicationContext()
        return SecurityCodeTextFieldXML(context = context).apply {
            this.readOnly = readOnly
            this.onEvent = onEvent
            this.securityCodeSize = securityCodeSize
        }
    }

    @Test
    fun `when create field then set default values`() {
        val field = createTextField()
        assertEquals(3, field.securityCodeSize)
        assertFalse(field.readOnly)
    }

    @Test
    fun `when create field with params then set params values`() {
        val field = createTextField(readOnly = true, securityCodeSize = 4)
        assertTrue(field.readOnly)
        assertEquals(4, field.securityCodeSize)
    }
}
