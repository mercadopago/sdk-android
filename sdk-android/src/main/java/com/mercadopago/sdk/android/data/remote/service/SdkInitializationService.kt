package com.mercadopago.sdk.android.data.remote.service

import com.mercadopago.sdk.android.BuildConfig
import com.mercadopago.sdk.android.data.remote.response.SiteIdResponse
import retrofit2.http.GET
import retrofit2.http.Query

private const val BRICKS_API = "cho-off"
private const val VERSION = "v1"
internal const val PRODUCT_ID: String = BuildConfig.CORE_METHODS_PRODUCT_ID

internal interface SdkInitializationService {

    @GET("$BRICKS_API/$VERSION/site_id")
    suspend fun fetchSiteId(
        @Query("product_id") productId: String? = PRODUCT_ID,
    ): SiteIdResponse
}
