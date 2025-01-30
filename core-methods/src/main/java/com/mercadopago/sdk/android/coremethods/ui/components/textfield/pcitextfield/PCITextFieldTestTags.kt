package com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield

internal enum class PCITextFieldTestTags(
    val tag: String,
) {
    Field("pci_text_field"),
    SecurityCode("pci_security_code_field"),
    ExpirationDate("pci_expiration_date_field"),
    CardNumber("pci_card_number_field"),
}
