package com.mercadopago.sdk.android.coremethods.analytics

import com.google.gson.annotations.SerializedName
import com.mercadopago.sdk.android.analytics.domain.constants.AnalyticsConstants
import com.mercadopago.sdk.android.analytics.domain.constants.AnalyticsConstants.ERROR_PATH
import com.mercadopago.sdk.android.analytics.domain.models.EventData
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import com.mercadopago.sdk.android.core.utils.KoverIgnore
import com.mercadopago.sdk.android.coremethods.analytics.CoreMethodsAnalyticsConstants.CORE_METHODS_PATH
import com.mercadopago.sdk.android.initializer.analytics.SDK_NATIVE_PATH

private const val GENERATE_CARD_TOKEN_PATH = "/tokenization"

@KoverIgnore("in development")
internal fun metricGenerateCardTokenCallSuccess(
    identityType: String? = null,
    isSavedCard: Boolean = false,
    typeWallet: String = "coremethods"
) = Metric(
    path = "$SDK_NATIVE_PATH$CORE_METHODS_PATH$GENERATE_CARD_TOKEN_PATH",
    type = TrackType.EVENT,
    data = GenerateCardAnalyticsData(
        identityType,
        isSavedCard,
        typeWallet
    ),
)

@KoverIgnore("in development")
internal fun metricGenerateCardTokenCallError(
    identityType: String? = null,
    isSavedCard: Boolean = false,
    typeWallet: String = "coremethods",
    error: String
) =
    Metric(
        path = "$SDK_NATIVE_PATH$CORE_METHODS_PATH$GENERATE_CARD_TOKEN_PATH$ERROR_PATH",
        type = TrackType.EVENT,
        data = GenerateCardAnalyticsData(
            identityType,
            isSavedCard,
            typeWallet,
            error
        )
    )

@KoverIgnore("in development")
internal data class GenerateCardAnalyticsData(
    @SerializedName("identity_document_type")
    val identityType: String?,
    @SerializedName("is_saved_card")
    val isSavedCard: Boolean,
    @SerializedName("type_wallet")
    val typeWallet: String?,

    @SerializedName("error_type")
    val errorType: String? = null,
) : EventData
