package com.mercadopago.sdk.android.components.model

/**
 * List Item content information
 * @param title List Item title
 * @param header List Item header
 * @param description List Item description
 */
data class MPListItemContentInfo(
    val title: String? = null,
    val header: String? = null,
    val description: String? = null,
)
