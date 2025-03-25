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
    val id: String,
    val name: String,
    val type: String,
    val minLength: Int,
    val maxLength: Int,
)
