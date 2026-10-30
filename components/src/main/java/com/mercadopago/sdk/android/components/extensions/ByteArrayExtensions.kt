package com.mercadopago.sdk.android.components.extensions

import android.graphics.BitmapFactory

private const val MAX_IMAGE_DIMENSION_PX = 2000
private const val PNG_MIME_TYPE = "image/png"

internal fun ByteArray.hasValidPngBounds(): Boolean {
    val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    BitmapFactory.decodeByteArray(this, 0, size, bounds)
    return bounds.outMimeType == PNG_MIME_TYPE &&
        bounds.outWidth in 1..MAX_IMAGE_DIMENSION_PX &&
        bounds.outHeight in 1..MAX_IMAGE_DIMENSION_PX
}
