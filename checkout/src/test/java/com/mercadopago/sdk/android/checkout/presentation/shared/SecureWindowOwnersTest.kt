package com.mercadopago.sdk.android.checkout.presentation.shared

import android.view.WindowManager
import androidx.activity.ComponentActivity
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
internal class SecureWindowOwnersTest {
    private val window = Robolectric.buildActivity(ComponentActivity::class.java).create().get().window

    private fun isSecure() = window.attributes.flags and WindowManager.LayoutParams.FLAG_SECURE != 0

    @Test
    fun `flag stays while another secure screen still holds the window`() {
        SecureWindowOwners.acquire(window)
        SecureWindowOwners.acquire(window)

        SecureWindowOwners.release(window)

        assertTrue(isSecure())
    }

    @Test
    fun `flag is cleared when the last secure screen releases the window`() {
        SecureWindowOwners.acquire(window)
        SecureWindowOwners.acquire(window)
        SecureWindowOwners.release(window)

        SecureWindowOwners.release(window)

        assertFalse(isSecure())
    }

    @Test
    fun `flag set by the host is kept after the secure screen releases`() {
        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        SecureWindowOwners.acquire(window)

        SecureWindowOwners.release(window)

        assertTrue(isSecure())
    }
}
