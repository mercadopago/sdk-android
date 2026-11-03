package com.mercadopago.sdk.android.checkout.statusscreen.share

import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
internal class ReceiptSharerTest {
    private val context: ComponentActivity = Robolectric.buildActivity(ComponentActivity::class.java).create().get()

    @Test
    fun `given a resolvable uri when shared then a chooser intent is started with the pdf`() {
        // Given
        val file = File.createTempFile("receipt_", ".pdf", context.cacheDir).apply { writeText("%PDF-1.4") }
        val uri = Uri.parse("content://com.mercadopago.checkout.fileprovider/status_screen_receipts/${file.name}")
        val sharer = ReceiptSharer(context = context, resolveUri = { uri })

        // When
        val result = sharer.share(file)

        // Then
        assertTrue(result)
        val started = Shadows.shadowOf(context).nextStartedActivity
        assertEquals(Intent.ACTION_CHOOSER, started.action)
        val sendIntent = started.getParcelableExtra<Intent>(Intent.EXTRA_INTENT)
        assertEquals(Intent.ACTION_SEND, sendIntent?.action)
        assertEquals("application/pdf", sendIntent?.type)
        assertEquals(uri, sendIntent?.getParcelableExtra(Intent.EXTRA_STREAM))
        assertEquals(
            Intent.FLAG_GRANT_READ_URI_PERMISSION,
            sendIntent?.flags?.and(Intent.FLAG_GRANT_READ_URI_PERMISSION),
        )
    }

    @Test
    fun `given uri resolution fails when shared then it returns false and deletes the file`() {
        // Given
        val file = File.createTempFile("receipt_", ".pdf", context.cacheDir).apply { writeText("%PDF-1.4") }
        val sharer = ReceiptSharer(
            context = context,
            resolveUri = { throw IllegalArgumentException("not under a configured root") },
        )

        // When
        val result = sharer.share(file)

        // Then
        assertFalse(result)
        assertFalse(file.exists())
    }
}
