package com.mercadopago.sdk.android.checkout.analytics

import com.google.gson.annotations.SerializedName
import com.mercadopago.sdk.android.analytics.domain.models.EventData
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import com.mercadopago.sdk.android.checkout.analytics.CheckoutAnalyticsConstants.CHECKOUT_CARD_FORM_PATH
import com.mercadopago.sdk.android.core.utils.KoverIgnore
import com.mercadopago.sdk.android.initializer.analytics.SDK_NATIVE_PATH

private const val INPUT_VALIDATION_PATH = "/input_validation"
private const val DROPDOWN_SELECTION_PATH = "/dropdown_selection"

@KoverIgnore("in development")
internal fun metricCardFormInputValidation(
    field: String,
    isInputValid: Boolean,
) = Metric(
    path = "$SDK_NATIVE_PATH$CHECKOUT_CARD_FORM_PATH$INPUT_VALIDATION_PATH",
    type = TrackType.EVENT,
    data = CardFormInputValidationEventData(field = field, isInputValid = isInputValid),
)

@KoverIgnore("in development")
internal fun metricCardFormDropdownSelection(
    dropdownSelectionType: String,
) = Metric(
    path = "$SDK_NATIVE_PATH$CHECKOUT_CARD_FORM_PATH$DROPDOWN_SELECTION_PATH",
    type = TrackType.EVENT,
    data = CardFormDropdownSelectionEventData(dropdownSelectionType = dropdownSelectionType),
)

@KoverIgnore("in development")
internal data class CardFormInputValidationEventData(
    @SerializedName("field")
    val field: String,
    @SerializedName("is_input_valid")
    val isInputValid: Boolean,
) : EventData

@KoverIgnore("in development")
internal data class CardFormDropdownSelectionEventData(
    @SerializedName("dropdown_selection_type")
    val dropdownSelectionType: String,
) : EventData
