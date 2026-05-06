package com.mercadopago.sdk.android.checkout.presentation.event

internal sealed class InstallmentsScreenEvent {
    data class OnInstallmentsSelected(val installment: Int) : InstallmentsScreenEvent()

    data class ConfirmPayment(val installment: Int) : InstallmentsScreenEvent()

    data object Idle : InstallmentsScreenEvent()
}
