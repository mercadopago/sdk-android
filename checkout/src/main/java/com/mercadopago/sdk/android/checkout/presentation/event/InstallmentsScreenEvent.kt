package com.mercadopago.sdk.android.checkout.presentation.event

internal sealed class InstallmentsScreenEvent {

    data class OnInstallmentsSelected(val installment: Int) : InstallmentsScreenEvent()
    object Idle : InstallmentsScreenEvent()
}
