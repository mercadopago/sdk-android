package com.mercadopago.sdk.android.coremethods.domain.model

/**
 * IdentificationType contains details related to the identification type.
 * @param id: The ID of the identification type.
 * @param name: The name of the identification type.
 * @param type: The type of the identification type.
 * @param minLength: The minimum length of the identification type.
 * @param maxLength: The maximum length of the identification type.
 */
data class IdentificationType(
    val id: String? = null,
    val name: String? = null,
    val type: String? = null,
    val minLength: Int? = null,
    val maxLength: Int? = null,
)
