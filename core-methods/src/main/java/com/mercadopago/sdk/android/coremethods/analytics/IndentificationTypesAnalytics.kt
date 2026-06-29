package com.mercadopago.sdk.android.coremethods.analytics

import com.google.gson.annotations.SerializedName
import com.mercadopago.sdk.android.analytics.domain.constants.AnalyticsConstants.ERROR_PATH
import com.mercadopago.sdk.android.analytics.domain.models.EventData
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import com.mercadopago.sdk.android.core.utils.KoverIgnore
import com.mercadopago.sdk.android.coremethods.analytics.CoreMethodsAnalyticsConstants.CORE_METHODS_PATH
import com.mercadopago.sdk.android.initializer.analytics.SDK_NATIVE_PATH

private const val IDENTIFICATION_TYPES_PATH = "/identification_types"

@KoverIgnore("in development")
internal fun metricIdentificationCallSuccess(
    identificationTypes: List<String>,
) = Metric(
    path = "$SDK_NATIVE_PATH$CORE_METHODS_PATH$IDENTIFICATION_TYPES_PATH",
    type = TrackType.EVENT,
    data = IdentificationTypesEventData(identificationTypes),
)

@KoverIgnore("in development")
internal fun metricIdentificationCallError(
    error: String,
    documentTypes: List<String> = emptyList(),
) = Metric(
    path = "$SDK_NATIVE_PATH$CORE_METHODS_PATH$IDENTIFICATION_TYPES_PATH$ERROR_PATH",
    type = TrackType.EVENT,
    data = IdentificationTypesErrorData(
        errorType = error,
        documentTypes = documentTypes,
    ),
)

internal data class IdentificationTypesEventData(
    @SerializedName("document_types")
    val documentTypes: List<String>,
) : EventData

internal data class IdentificationTypesErrorData(
    @SerializedName("error_type")
    val errorType: String,
    @SerializedName("document_types")
    val documentTypes: List<String>,
) : EventData
