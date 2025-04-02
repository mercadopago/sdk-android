package com.mercadopago.sdk.android.coremethods.analytics

import com.google.gson.annotations.SerializedName
import com.mercadopago.sdk.android.analytics.domain.models.EventData
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import com.mercadopago.sdk.android.core.utils.KoverIgnore

internal const val PCI_FIELD = "PCI_FIELD"
internal const val FRAMEWORK = "Compose"

@KoverIgnore("in development")
internal fun metricPCIFieldInitialization(field: String, frameworkUI: String = FRAMEWORK) = Metric(
    path = "/sdk-native/core-methods/pci_field",
    type = TrackType.VIEW,
    data = MetricPCIFieldData(field, frameworkUI)
)

@KoverIgnore("in development")
internal fun metricPCIFieldFocus(field: String, frameworkUI: String = FRAMEWORK) = Metric(
    path = "/sdk-native/core-methods/pci_field/focus",
    type = TrackType.EVENT,
    data = MetricPCIFieldData(field, frameworkUI)
)

@KoverIgnore("in development")
internal data class MetricPCIFieldData(
    @SerializedName("field")
    val field: String = PCI_FIELD,
    @SerializedName("frameworkUI")
    val frameworkUI: String = FRAMEWORK
) : EventData
