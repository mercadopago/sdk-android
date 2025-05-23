package com.mercadopago.sdk.android.example.presentation.sdkinitializer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.mercadopago.sdk.android.domain.model.CountryCode
import com.mercadopago.sdk.android.example.BuildConfig
import com.mercadopago.sdk.android.example.domain.model.PublicKey
import com.mercadopago.sdk.android.initializer.MercadoPagoSDK
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.json.Json

internal class SdkInitializerViewModel(
    application: Application,
) : AndroidViewModel(application) {

    private val _viewState = MutableStateFlow<SdkInitializerViewState>(SdkInitializerViewState())
    val viewState: StateFlow<SdkInitializerViewState> = _viewState

    init {
        _viewState.value = _viewState.value.copy(
            sdkState = SdkState(
                isInitialized = MercadoPagoSDK.isInitialized,
                publicKey = MercadoPagoSDK.publicKey.orEmpty(),
                countryCode = MercadoPagoSDK.countryCode ?: CountryCode.ARG,
            ),
            publicKeyList = getDefaultPublicKeyList(),
        )
    }

    fun onPublicKeyChange(publicKey: String) {
        _viewState.value = _viewState.value.copy(publicKeyInput = publicKey)
    }

    fun onCountryCodeSelected(countryCode: CountryCode) {
        _viewState.value = _viewState.value.copy(selectedCountryCode = countryCode)
    }

    fun onInitializeSdkClick() {
        val publicKey = _viewState.value.publicKeyInput
        if (publicKey.isEmpty()) {
            _viewState.value = _viewState.value.copy(
                dialogState = SdkInitializerDialogState.EmptyPublicKey,
            )
            return
        }
        MercadoPagoSDK.initialize(
            context = getApplication(),
            publicKey = publicKey,
            countryCode = _viewState.value.selectedCountryCode,
        )
        _viewState.value = _viewState.value.copy(
            sdkState = SdkState(
                isInitialized = true,
                publicKey = publicKey,
                countryCode = _viewState.value.selectedCountryCode,
            )
        )
    }

    fun onDestroySdkInstanceClick() {
        MercadoPagoSDK.clearInstance()
        _viewState.value = _viewState.value.copy(
            sdkState = SdkState(
                isInitialized = false,
                publicKey = "",
                countryCode = _viewState.value.selectedCountryCode,
            )
        )
    }

    fun onDialogStateChanged(dialogState: SdkInitializerDialogState) {
        _viewState.value = _viewState.value.copy(dialogState = dialogState)
    }

    private fun getDefaultPublicKeyList(): List<PublicKey> {
        return try {
            Json.decodeFromString<List<PublicKey>>(BuildConfig.DEFAULT_PUBLIC_KEY_LIST.trim('"').replace("\\\"", "\""))
        } catch (_: Exception) {
            emptyList()
        }
    }
}
