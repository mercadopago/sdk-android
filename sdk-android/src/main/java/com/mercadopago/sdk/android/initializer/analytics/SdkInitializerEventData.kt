package com.mercadopago.sdk.android.initializer.analytics

import android.content.Context
import android.os.Build
import com.google.gson.annotations.SerializedName
import com.mercadopago.sdk.android.analytics.domain.models.EventData
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import java.util.Locale

private const val UNKNOWN = "UNKNOWN"
private const val MAVEN = "MAVEN"
internal const val SDK_NATIVE_PATH = "/sdk-native"

internal object SdkInitializerAnalytics {

    internal fun buildSdkInitializerEvent(
        context: Context,
        isError: Boolean = false,
    ) = Metric(
        type = TrackType.EVENT,
        path = SDK_NATIVE_PATH,
        data = SdkInitializerEventData(
            minimumAppVersion = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                context.applicationInfo.minSdkVersion.toString()
            } else {
                UNKNOWN
            },
            distribution = MAVEN,
            isError = isError,
        ),
    )
}

internal class SdkInitializerEventData(
    @SerializedName("minimum_version_app")
    val minimumAppVersion: String,
    @SerializedName("distribution")
    val distribution: String,
    @SerializedName("locale")
    val locale: String = Locale.getDefault().toString().replace("_", "-"),
    @SerializedName("isError")
    val isError: Boolean = false,
) : EventData
