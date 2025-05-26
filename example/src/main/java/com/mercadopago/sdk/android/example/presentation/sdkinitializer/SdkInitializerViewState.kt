package com.mercadopago.sdk.android.example.presentation.sdkinitializer

import com.mercadopago.sdk.android.domain.model.CountryCode
import com.mercadopago.sdk.android.example.domain.model.PublicKey

internal data class SdkInitializerViewState(
    val sdkState: SdkState = SdkState(),
    var publicKeyInput: String = "",
    var selectedCountryCode: CountryCode = CountryCode.ARG,
    val countryCodeOptions: List<CountryCode> = CountryCode.entries,
    val publicKeyList: List<PublicKey> = emptyList(),
    val dialogState: SdkInitializerDialogState = SdkInitializerDialogState.Hidden,
)

internal data class SdkState(
    val isInitialized: Boolean = false,
    val publicKey: String = "",
    val countryCode: CountryCode = CountryCode.ARG,
)

internal sealed interface SdkInitializerDialogState {
    object Hidden : SdkInitializerDialogState
    object EmptyPublicKey : SdkInitializerDialogState
    object Autofill : SdkInitializerDialogState
}
