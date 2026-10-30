package com.mercadopago.sdk.android.checkout.statusscreen.media

import com.mercadopago.sdk.android.checkout.statusscreen.data.remote.mapper.asHttpsUrlOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import java.io.File

private const val MAX_RECEIPT_BYTES = 5L * 1024 * 1024
private const val RECEIPT_TTL_MILLIS = 24 * 60 * 60 * 1000L

internal class ReceiptDownloader(
    private val cacheDir: File,
    private val client: OkHttpClient = OkHttpClient.Builder()
        .followRedirects(false)
        .followSslRedirects(false)
        .build(),
    private val validateUrl: (String) -> String? = String::asHttpsUrlOrNull,
) {
    suspend fun download(
        receiptUrl: String,
    ): File? =
        withContext(Dispatchers.IO) {
            sweepStale()
            validateUrl(receiptUrl)?.let { url -> runCatching { fetch(url) }.getOrNull() }
        }

    fun sweepStale() {
        val cutoff = System.currentTimeMillis() - RECEIPT_TTL_MILLIS
        receiptDir().listFiles()?.forEach { file ->
            if (file.lastModified() < cutoff) file.delete()
        }
    }

    private fun fetch(
        url: String,
    ): File? {
        val request = Request.Builder().url(url).get().build()
        return client.newCall(request).execute().use { response ->
            response.body.takeIf { response.isValidPdf(MAX_RECEIPT_BYTES) }?.let(::writeToFile)
        }
    }

    private fun writeToFile(
        body: ResponseBody,
    ): File? {
        val file = File.createTempFile("receipt_", ".pdf", receiptDir())
        val written = runCatching {
            body.byteStream().use { source ->
                file.outputStream().use { output -> source.copyBounded(output, MAX_RECEIPT_BYTES) }
            }
        }.getOrDefault(false)
        if (!written) file.delete()
        return file.takeIf { written }
    }

    private fun receiptDir(): File = File(cacheDir, RECEIPT_CACHE_DIR_NAME).apply { mkdirs() }

    internal companion object {
        internal const val RECEIPT_CACHE_DIR_NAME = "status_screen_receipts"
    }
}
