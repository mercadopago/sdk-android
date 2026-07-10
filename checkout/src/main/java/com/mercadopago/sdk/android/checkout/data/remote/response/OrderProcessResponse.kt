package com.mercadopago.sdk.android.checkout.data.remote.response

import com.google.gson.annotations.SerializedName

internal data class OrderProcessResponse(
    @SerializedName("id")
    val id: String?,
    @SerializedName("product_id")
    val productId: String?,
    @SerializedName("processing_mode")
    val processingMode: String?,
    @SerializedName("external_reference")
    val externalReference: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("total_amount")
    val totalAmount: String?,
    @SerializedName("total_paid_amount")
    val totalPaidAmount: String?,
    @SerializedName("expiration_time")
    val expirationTime: String?,
    @SerializedName("checkout_available_at")
    val checkoutAvailableAt: String?,
    @SerializedName("site_id")
    val siteId: String?,
    @SerializedName("user_id")
    val userId: String?,
    @SerializedName("created_date")
    val createdDate: String?,
    @SerializedName("last_updated_date")
    val lastUpdatedDate: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("status_detail")
    val statusDetail: String?,
    @SerializedName("capture_mode")
    val captureMode: String?,
    @SerializedName("currency")
    val currency: String?,
    @SerializedName("config")
    val config: OrderConfigResponse?,
    @SerializedName("integration_data")
    val integrationData: OrderIntegrationDataResponse?,
    @SerializedName("payer")
    val payer: OrderPayerResponse?,
    @SerializedName("shipment")
    val shipment: OrderShipmentResponse?,
    @SerializedName("transactions")
    val transactions: OrderTransactionsResponse?,
    @SerializedName("items")
    val items: List<OrderItemResponse>?,
)

internal data class OrderConfigResponse(
    @SerializedName("online")
    val online: OrderOnlineConfigResponse?,
)

internal data class OrderOnlineConfigResponse(
    @SerializedName("transaction_security")
    val transactionSecurity: OrderTransactionSecurityConfigResponse?,
)

internal data class OrderTransactionSecurityConfigResponse(
    @SerializedName("validation")
    val validation: String?,
    @SerializedName("liability_shift")
    val liabilityShift: String?,
)

internal data class OrderIntegrationDataResponse(
    @SerializedName("application_id")
    val applicationId: String?,
    @SerializedName("product")
    val product: String?,
    @SerializedName("integrator_id")
    val integratorId: String?,
    @SerializedName("platform_id")
    val platformId: String?,
    @SerializedName("sponsor")
    val sponsor: OrderSponsorResponse?,
)

internal data class OrderSponsorResponse(
    @SerializedName("id")
    val id: String?,
)

internal data class OrderPayerResponse(
    @SerializedName("entity_type")
    val entityType: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("first_name")
    val firstName: String?,
    @SerializedName("last_name")
    val lastName: String?,
    @SerializedName("identification")
    val identification: OrderIdentificationResponse?,
    @SerializedName("phone")
    val phone: OrderPhoneResponse?,
)

internal data class OrderIdentificationResponse(
    @SerializedName("type")
    val type: String?,
    @SerializedName("number")
    val number: String?,
)

internal data class OrderPhoneResponse(
    @SerializedName("area_code")
    val areaCode: String?,
    @SerializedName("number")
    val number: String?,
)

internal data class OrderShipmentResponse(
    @SerializedName("address")
    val address: OrderAddressResponse?,
)

internal data class OrderAddressResponse(
    @SerializedName("street_name")
    val streetName: String?,
    @SerializedName("street_number")
    val streetNumber: String?,
    @SerializedName("neighborhood")
    val neighborhood: String?,
    @SerializedName("city")
    val city: String?,
    @SerializedName("state")
    val state: String?,
    @SerializedName("zip_code")
    val zipCode: String?,
)

internal data class OrderTransactionsResponse(
    @SerializedName("payments")
    val payments: List<OrderPaymentResponse>?,
    @SerializedName("refunds")
    val refunds: List<OrderRefundResponse>?,
)

internal data class OrderPaymentResponse(
    @SerializedName("id")
    val id: String?,
    @SerializedName("reference_id")
    val referenceId: String?,
    @SerializedName("amount")
    val amount: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("status_detail")
    val statusDetail: String?,
    @SerializedName("paid_amount")
    val paidAmount: String?,
    @SerializedName("tip_amount")
    val tipAmount: String?,
    @SerializedName("confirmed_amount")
    val confirmedAmount: String?,
    @SerializedName("installment_amount")
    val installmentAmount: String?,
    @SerializedName("source")
    val source: String?,
    @SerializedName("provider")
    val provider: String?,
    @SerializedName("expiration_time")
    val expirationTime: String?,
    @SerializedName("date_of_expiration")
    val dateOfExpiration: String?,
    @SerializedName("payment_method")
    val paymentMethod: OrderPaymentMethodResponse?,
    @SerializedName("card")
    val card: OrderCardResponse?,
    @SerializedName("reference")
    val reference: OrderReferenceResponse?,
)

internal data class OrderPaymentMethodResponse(
    @SerializedName("id")
    val id: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("token")
    val token: String?,
    @SerializedName("statement_descriptor")
    val statementDescriptor: String?,
    @SerializedName("installments")
    val installments: Int?,
    @SerializedName("e2e_id")
    val e2eId: String?,
    @SerializedName("redirect_url")
    val redirectUrl: String?,
    @SerializedName("barcode_content")
    val barcodeContent: String?,
    @SerializedName("ticket_url")
    val ticketUrl: String?,
    @SerializedName("transaction_security")
    val transactionSecurity: OrderPaymentTransactionSecurityResponse?,
)

internal data class OrderPaymentTransactionSecurityResponse(
    @SerializedName("id")
    val id: String?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("validation")
    val validation: String?,
    @SerializedName("liability_shift")
    val liabilityShift: String?,
)

internal data class OrderCardResponse(
    @SerializedName("first_digits")
    val firstDigits: String?,
    @SerializedName("last_digits")
    val lastDigits: String?,
)

internal data class OrderReferenceResponse(
    @SerializedName("id")
    val id: String?,
    @SerializedName("source")
    val source: String?,
    @SerializedName("metadata")
    val metadata: OrderReferenceMetadataResponse?,
)

internal data class OrderReferenceMetadataResponse(
    @SerializedName("from_id")
    val fromId: String?,
    @SerializedName("to")
    val to: List<OrderReferenceTargetResponse>?,
)

internal data class OrderReferenceTargetResponse(
    @SerializedName("id")
    val id: String?,
    @SerializedName("user_id")
    val userId: String?,
)

internal data class OrderRefundResponse(
    @SerializedName("id")
    val id: String?,
)

internal data class OrderItemResponse(
    @SerializedName("title")
    val title: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("unit_price")
    val unitPrice: String?,
    @SerializedName("external_code")
    val externalCode: String?,
    @SerializedName("category_id")
    val categoryId: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("picture_url")
    val pictureUrl: String?,
    @SerializedName("quantity")
    val quantity: Int?,
    @SerializedName("warranty")
    val warranty: Boolean?,
    @SerializedName("event_date")
    val eventDate: String?,
    @SerializedName("unit_measure")
    val unitMeasure: String?,
    @SerializedName("external_position")
    val externalPosition: String?,
)
