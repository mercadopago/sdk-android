package com.mercadopago.sdk.android.initializer.analytics

import android.content.Context
import android.os.Build
import com.google.gson.annotations.SerializedName
import com.mercadopago.sdk.android.analytics.domain.models.EventData
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import com.mercadopago.sdk.android.core.BuildConfig
import java.util.Locale

private const val UNKNOWN = "UNKNOWN"
private const val MAVEN = "MAVEN"

/**
 * Path for the native SDK analytics route
 */
const val SDK_NATIVE_PATH = "/checkout_api_native"

internal object SdkInitializerAnalytics {

    internal fun buildSdkInitializerEvent(
        context: Context,
        publicKey: String,
        errorType: String? = null,
    ) = Metric(
        type = TrackType.EVENT,
        path = "$SDK_NATIVE_PATH/initialize",
        data = SdkInitializerEventData(
            minVersion = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                context.applicationInfo.minSdkVersion.toString()
            } else {
                UNKNOWN
            },
            distribution = MAVEN,
            errorType = errorType,
            publicKey = publicKey,
            sdkVersion = BuildConfig.SdkVersion,
        ),
    )
}

internal class SdkInitializerEventData(
    @SerializedName("distribution")
    val distribution: String,
    @SerializedName("locale")
    val locale: String = Locale.getDefault().toString().replace("_", "-"),
    @SerializedName("error_type")
    val errorType: String?,
    @SerializedName("public_key")
    val publicKey: String,
    @SerializedName("min_version")
    val minVersion: String,
    @SerializedName("sdk_version")
    val sdkVersion: String,
) : EventData()
