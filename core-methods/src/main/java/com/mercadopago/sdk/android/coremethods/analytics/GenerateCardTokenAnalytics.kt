package com.mercadopago.sdk.android.coremethods.analytics

import com.google.gson.annotations.SerializedName
import com.mercadopago.sdk.android.analytics.domain.constants.AnalyticsConstants.ERROR_PATH
import com.mercadopago.sdk.android.analytics.domain.models.EventData
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import com.mercadopago.sdk.android.core.utils.KoverIgnore
import com.mercadopago.sdk.android.coremethods.analytics.CoreMethodsAnalyticsConstants.CORE_METHODS_PATH
import com.mercadopago.sdk.android.initializer.analytics.SDK_NATIVE_PATH

private const val GENERATE_CARD_TOKEN_PATH = "/tokenization"
private const val TYPE_WALLET_CORE_METHODS = "coremethods"

@KoverIgnore("in development")
internal fun metricGenerateCardTokenCallSuccess(
    identityType: String? = null,
    isSavedCard: Boolean = false,
    typeWallet: String = TYPE_WALLET_CORE_METHODS,
) = Metric(
    path = "$SDK_NATIVE_PATH$CORE_METHODS_PATH$GENERATE_CARD_TOKEN_PATH",
    type = TrackType.EVENT,
    data = GenerateCardAnalyticsData(
        identityType = identityType,
        isSavedCard = isSavedCard,
        typeWallet = typeWallet,
    ),
)

@KoverIgnore("in development")
internal fun metricGenerateCardTokenCallError(
    error: String,
    observabilityEventId: String,
    identityType: String? = null,
    typeWallet: String = TYPE_WALLET_CORE_METHODS,
) = Metric(
    path = "$SDK_NATIVE_PATH$CORE_METHODS_PATH$GENERATE_CARD_TOKEN_PATH$ERROR_PATH",
    type = TrackType.EVENT,
    data = GenerateCardTokenErrorData(
        errorType = error,
        observabilityEventId = observabilityEventId,
        identityType = identityType,
        typeWallet = typeWallet,
    ),
)

@KoverIgnore("in development")
internal data class GenerateCardAnalyticsData(
    @SerializedName("identity_document_type")
    val identityType: String?,
    @SerializedName("is_saved_card")
    val isSavedCard: Boolean,
    @SerializedName("type_wallet")
    val typeWallet: String,
) : EventData

@KoverIgnore("in development")
internal data class GenerateCardTokenErrorData(
    @SerializedName("error_type")
    val errorType: String,
    @SerializedName("observability_event_id")
    val observabilityEventId: String,
    @SerializedName("identity_document_type")
    val identityType: String?,
    @SerializedName("type_wallet")
    val typeWallet: String,
) : EventData
