package com.mercadopago.sdk.android.components.model

import androidx.compose.ui.text.AnnotatedString

/**
 * List Item content information
 * @param title List Item title
 * @param header List Item header
 * @param description List Item description
 * @param titleStyle List Item title emphasis; [MPListItemTitleStyle.Bold] by default
 * @param annotatedDescription styled description; when set it is shown instead of [description]
 */
data class MPListItemContentInfo(
    val title: String? = null,
    val header: String? = null,
    val description: String? = null,
    val titleStyle: MPListItemTitleStyle = MPListItemTitleStyle.Bold,
    val annotatedDescription: AnnotatedString? = null,
)

/**
 * List Item title emphasis.
 */
enum class MPListItemTitleStyle {
    /** Heading typography (default). */
    Bold,

    /** Body typography, without bold emphasis. */
    Regular,
}
