package com.mercadopago.sdk.android.checkout.statusscreen.share

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

private const val PDF_MIME_TYPE = "application/pdf"

internal class ReceiptSharer(
    private val context: Context,
    private val fileProviderAuthority: String = "${context.packageName}.mercadopago.checkout.fileprovider",
    private val resolveUri: (
        File,
    ) -> Uri = { file -> FileProvider.getUriForFile(context, fileProviderAuthority, file) },
) {
    fun share(
        file: File,
    ): Boolean =
        runCatching {
            val uri = resolveUri(file)
            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                type = PDF_MIME_TYPE
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(sendIntent, null))
            true
        }.getOrElse {
            file.delete()
            false
        }
}
