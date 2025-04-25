package com.mercadopago.sdk.android.initializer.analytics

import android.content.Context
import android.os.Build
import com.google.gson.annotations.SerializedName
import com.mercadopago.sdk.android.analytics.domain.models.EventData
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import com.mercadopago.sdk.android.core.BuildConfig
import com.mercadopago.sdk.android.core.utils.isDebugApp
import java.util.Locale

private const val MIN_SDK_23 = "23"
private const val MAVEN = "MAVEN"
private const val INITIALIZE_PATH = "/initialize"

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
        path = "$SDK_NATIVE_PATH$INITIALIZE_PATH",
        data = SdkInitializerEventData(
            minVersion = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                context.applicationInfo.minSdkVersion.toString()
            } else {
                MIN_SDK_23
            },
            distribution = MAVEN,
            errorType = errorType,
            publicKey = publicKey,
            sdkVersion = BuildConfig.SdkVersion,
            developerMode = isDebugApp(context),
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
    @SerializedName("developer_mode")
    val developerMode: Boolean,
) : EventData
