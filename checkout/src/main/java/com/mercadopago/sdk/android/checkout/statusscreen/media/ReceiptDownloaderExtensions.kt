package com.mercadopago.sdk.android.checkout.statusscreen.media

import okhttp3.Response
import java.io.InputStream
import java.io.OutputStream

private const val PDF_MIME_TYPE = "application/pdf"
private const val STREAM_BUFFER_SIZE = 8 * 1024

internal fun Response.isValidPdf(
    maxBytes: Long,
): Boolean {
    val contentLength = header("Content-Length")?.toLongOrNull()
    return isSuccessful &&
        header("Content-Type").orEmpty().startsWith(PDF_MIME_TYPE) &&
        (contentLength == null || contentLength <= maxBytes)
}

internal fun InputStream.copyBounded(
    output: OutputStream,
    maxBytes: Long,
): Boolean {
    var bytesWritten = 0L
    val buffer = ByteArray(STREAM_BUFFER_SIZE)
    while (true) {
        val read = read(buffer)
        if (read == -1) break
        bytesWritten += read
        if (bytesWritten > maxBytes) return false
        output.write(buffer, 0, read)
    }
    return bytesWritten > 0
}
