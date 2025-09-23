package com.mercadopago.sdk.android.data.remote.service

import com.mercadopago.sdk.android.data.remote.response.SiteIdResponse
import retrofit2.http.GET

private const val BRICKS_API = "cho-off"
private const val VERSION = "v1"

internal interface SdkInitializationService {

    @GET("$BRICKS_API/$VERSION/site_id")
    suspend fun fetchSiteId(): SiteIdResponse
}
