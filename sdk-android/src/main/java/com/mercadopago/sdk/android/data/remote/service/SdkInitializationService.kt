package com.mercadopago.sdk.android.data.remote.service

import com.mercadopago.sdk.android.data.remote.response.SiteIdResponse
import retrofit2.http.GET

private const val BRICKS_API = "bricks_api"

internal interface SdkInitializationService {

    @GET("$BRICKS_API/site_id")
    suspend fun fetchSiteId(): SiteIdResponse
}
