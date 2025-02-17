package com.mercadopago.sdk.android.initializer.analytics

import android.content.Context
import android.os.Build
import com.google.gson.annotations.SerializedName
import com.mercadopago.sdk.android.analytics.domain.models.EventData
import java.util.Locale

private const val UNKNOWN = "UNKNOWN"
private const val MAVEN = "MAVEN"
internal const val SDK_NATIVE_PATH = "/sdk-native"

internal fun buildSdkInitializerEventData(context: Context) = SdkInitializerEventData(
    minimumAppVersion = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        context.applicationInfo.minSdkVersion.toString()
    } else {
        UNKNOWN
    },
    distribution = MAVEN,
)

internal class SdkInitializerEventData(
    @SerializedName("minimum_version_app")
    val minimumAppVersion: String,
    @SerializedName("distribution")
    val distribution: String,
    @SerializedName("locale")
    val locale: String = Locale.getDefault().toString().replace("_", "-"),
) : EventData
