package com.mercadopago.sdk.android.googlepay.data.remote.service

import com.mercadopago.sdk.android.googlepay.data.remote.response.GooglePayWalletResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

internal interface GooglePayWalletsService {
    @GET("bricks/wallets")
    suspend fun getWallets(
        @Query("public_key") publicKey: String,
        @Query("product_id") productId: String,
    ): Response<List<GooglePayWalletResponse>>
}
