package com.mercadopago.sdk.android.analytics.data.remote.mapper

import android.content.Context
import android.os.Build
import com.mercadopago.sdk.android.analytics.data.remote.models.request.AnalyticsRequest
import com.mercadopago.sdk.android.analytics.data.remote.models.request.ApplicationRequest
import com.mercadopago.sdk.android.analytics.data.remote.models.request.DeviceRequest
import com.mercadopago.sdk.android.analytics.data.remote.models.request.TrackRequest
import com.mercadopago.sdk.android.analytics.data.remote.models.request.UserRequest
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import com.mercadopago.sdk.android.core.BuildConfig
import com.mercadopago.sdk.android.core.utils.checkNetworkType
import java.util.Calendar
import java.util.UUID

private const val VIEW = "view"
private const val EVENT = "event"
private const val BUSINESS = "mercadopago"
private const val PLATFORM = "/mobile/android"

internal fun Metric.toRequest(
    context: Context,
    siteId: String?,
    sessionId: String,
    uid: String,
) = AnalyticsRequest(
    tracks = listOf(
        TrackRequest(
            path = path,
            type = when (type) {
                TrackType.VIEW -> VIEW
                TrackType.EVENT -> EVENT
            },
            id = UUID.randomUUID().toString(),
            userTime = Calendar.getInstance().timeInMillis.toString(),
            eventData = data,
            user = UserRequest(
                uid = uid,
                sessionId = sessionId,
            ),
            application = ApplicationRequest(
                business = BUSINESS,
                siteId = siteId,
                version = BuildConfig.SdkVersion,
                appName = context.applicationInfo?.packageName.orEmpty(),
            ),
            device = DeviceRequest(
                platform = PLATFORM,
                connectivityType = checkNetworkType(context).text,
                osVersion = Build.VERSION.SDK_INT.toString(),
            )
        )
    )
)
