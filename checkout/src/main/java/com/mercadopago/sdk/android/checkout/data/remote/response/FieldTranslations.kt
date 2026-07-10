package com.mercadopago.sdk.android.checkout.data.remote.response

import com.google.gson.annotations.SerializedName

internal data class FieldTranslations(
    @SerializedName("label")
    val label: String,
    @SerializedName("placeholder")
    val placeholder: String,
    @SerializedName("helper")
    val helper: String? = null,
    @SerializedName("tooltip")
    val tooltip: String? = null,
    @SerializedName("error_empty_field")
    val errorEmptyField: String,
    @SerializedName("error_incomplete_field")
    val errorIncompleteField: String,
    @SerializedName("error_invalid_field")
    val errorInvalidField: String,
)
