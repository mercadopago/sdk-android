package com.mercadopago.sdk.android.checkout.data.remote.response

internal data class OrderProcessResponse(
    val id: String?,
    val productId: String?,
    val processingMode: String?,
    val externalReference: String?,
    val description: String?,
    val totalAmount: String?,
    val totalPaidAmount: String?,
    val expirationTime: String?,
    val checkoutAvailableAt: String?,
    val siteId: String?,
    val userId: String?,
    val createdDate: String?,
    val lastUpdatedDate: String?,
    val type: String?,
    val status: String?,
    val statusDetail: String?,
    val captureMode: String?,
    val currency: String?,
    val config: OrderConfigResponse?,
    val integrationData: OrderIntegrationDataResponse?,
    val payer: OrderPayerResponse?,
    val shipment: OrderShipmentResponse?,
    val transactions: OrderTransactionsResponse?,
    val items: List<OrderItemResponse>?,
)

internal data class OrderConfigResponse(
    val online: OrderOnlineConfigResponse?,
)

internal data class OrderOnlineConfigResponse(
    val transactionSecurity: OrderTransactionSecurityConfigResponse?,
)

internal data class OrderTransactionSecurityConfigResponse(
    val validation: String?,
    val liabilityShift: String?,
)

internal data class OrderIntegrationDataResponse(
    val applicationId: String?,
    val product: String?,
    val integratorId: String?,
    val platformId: String?,
    val sponsor: OrderSponsorResponse?,
)

internal data class OrderSponsorResponse(
    val id: String?,
)

internal data class OrderPayerResponse(
    val entityType: String?,
    val email: String?,
    val firstName: String?,
    val lastName: String?,
    val identification: OrderIdentificationResponse?,
    val phone: OrderPhoneResponse?,
)

internal data class OrderIdentificationResponse(
    val type: String?,
    val number: String?,
)

internal data class OrderPhoneResponse(
    val areaCode: String?,
    val number: String?,
)

internal data class OrderShipmentResponse(
    val address: OrderAddressResponse?,
)

internal data class OrderAddressResponse(
    val streetName: String?,
    val streetNumber: String?,
    val neighborhood: String?,
    val city: String?,
    val state: String?,
    val zipCode: String?,
)

internal data class OrderTransactionsResponse(
    val payments: List<OrderPaymentResponse>?,
    val refunds: List<OrderRefundResponse>?,
)

internal data class OrderPaymentResponse(
    val id: String?,
    val referenceId: String?,
    val amount: String?,
    val status: String?,
    val statusDetail: String?,
    val paidAmount: String?,
    val tipAmount: String?,
    val confirmedAmount: String?,
    val installmentAmount: String?,
    val source: String?,
    val provider: String?,
    val expirationTime: String?,
    val dateOfExpiration: String?,
    val paymentMethod: OrderPaymentMethodResponse?,
    val card: OrderCardResponse?,
    val reference: OrderReferenceResponse?,
)

internal data class OrderPaymentMethodResponse(
    val id: String?,
    val type: String?,
    val token: String?,
    val statementDescriptor: String?,
    val installments: Int?,
    val e2eId: String?,
    val redirectUrl: String?,
    val barcodeContent: String?,
    val ticketUrl: String?,
    val transactionSecurity: OrderPaymentTransactionSecurityResponse?,
)

internal data class OrderPaymentTransactionSecurityResponse(
    val id: String?,
    val url: String?,
    val type: String?,
    val status: String?,
    val validation: String?,
    val liabilityShift: String?,
)

internal data class OrderCardResponse(
    val firstDigits: String?,
    val lastDigits: String?,
)

internal data class OrderReferenceResponse(
    val id: String?,
    val source: String?,
    val metadata: OrderReferenceMetadataResponse?,
)

internal data class OrderReferenceMetadataResponse(
    val fromId: String?,
    val to: List<OrderReferenceTargetResponse>?,
)

internal data class OrderReferenceTargetResponse(
    val id: String?,
    val userId: String?,
)

internal data class OrderRefundResponse(
    val id: String?,
)

internal data class OrderItemResponse(
    val title: String?,
    val description: String?,
    val unitPrice: String?,
    val externalCode: String?,
    val categoryId: String?,
    val type: String?,
    val pictureUrl: String?,
    val quantity: Int?,
    val warranty: Boolean?,
    val eventDate: String?,
    val unitMeasure: String?,
    val externalPosition: String?,
)
