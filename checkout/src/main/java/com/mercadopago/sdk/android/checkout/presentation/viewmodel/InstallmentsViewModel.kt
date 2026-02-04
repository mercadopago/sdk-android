package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentsScreenState
import com.mercadopago.sdk.android.coremethods.domain.interactor.CoreMethods
import com.mercadopago.sdk.android.coremethods.domain.interactor.coreMethods
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.initializer.MercadoPagoSDK
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal

internal class InstallmentsViewModel(
    private val coreMethods: CoreMethods = MercadoPagoSDK.getInstance().coreMethods,
) : ViewModel() {

    private val _viewState = MutableStateFlow(InstallmentsScreenState())
    val viewState: StateFlow<InstallmentsScreenState> = _viewState.asStateFlow()

    fun getInstallments(
        bin: String,
        amount: BigDecimal,
    ) {
        viewModelScope.launch {
            val result = coreMethods.getInstallments(bin = bin, amount = amount)
            when (result) {
                is Result.Success -> Unit
                is Result.Error -> Unit
            }
        }
    }
}
