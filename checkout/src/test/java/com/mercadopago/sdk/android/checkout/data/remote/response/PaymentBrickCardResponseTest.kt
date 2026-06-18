package com.mercadopago.sdk.android.checkout.data.remote.response

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class PaymentBrickCardResponseTest {
    private val gson = GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create()

    private fun parse(
        json: String,
    ) = gson.fromJson(json, PaymentBrickCardResponse::class.java)

    @Test
    fun `given full card response then top-level fields are mapped`() {
        val response = parse(FULL_RESPONSE_JSON)

        assertNotNull(response.translations)
        assertNotNull(response.installment)
        assertEquals(1, response.paymentMethods.size)
    }

    @Test
    fun `given full card response then translations are mapped`() {
        val t = parse(FULL_RESPONSE_JSON).translations

        assertEquals("Ingresá tu tarjeta", t.cardFormTitle)
        assertEquals("Pagar", t.cardFormFooterButtonLabel)
        assertEquals("Número de tarjeta", t.cardNumber.label)
        assertEquals("Código de seguridad", t.securityCode.label)
        assertEquals("Fecha de vencimiento", t.expirationDate.label)
        assertEquals("Titular de tarjeta", t.holderName.label)
        assertEquals("Como aparece en la tarjeta", t.holderName.helper)
    }

    @Test
    fun `given full card response then installments translations are mapped`() {
        val inst = parse(FULL_RESPONSE_JSON).translations.installments

        assertEquals("Elegí las cuotas", inst.header.title)
        assertEquals("Sin interés", inst.interestFreeLabel)
        assertEquals("Total", inst.totalLabel)
    }

    @Test
    fun `given full card response then installment quotas are mapped`() {
        val config = assertNotNull(parse(FULL_RESPONSE_JSON).installment)

        assertEquals("radio_button", config.selectionType)
        assertEquals(2, config.quotas.size)

        val firstQuota = config.quotas[0]
        assertEquals(1, firstQuota.installments)
        assertEquals(BigDecimal("500.00"), firstQuota.installmentAmount)
        assertEquals(BigDecimal("500.00"), firstQuota.totalAmount)
        assertEquals("1x $ 500,00", firstQuota.primaryLabel)
        assertEquals("", firstQuota.secondaryLabel)
        assertEquals("none", firstQuota.state)
        assertNotNull(firstQuota.accessibilityLabel)

        val secondQuota = config.quotas[1]
        assertEquals(3, secondQuota.installments)
        assertEquals("success", secondQuota.state)
    }

    @Test
    fun `given full card response then payment method config is mapped`() {
        val pm = parse(FULL_RESPONSE_JSON).paymentMethods[0]

        assertEquals("visa", pm.id)
        assertEquals("credit_card", pm.paymentTypeId)

        val cardNumber = assertNotNull(pm.cardNumber)
        assertEquals("Number", cardNumber.type)
        assertEquals(16, cardNumber.length.min)
        assertEquals(16, cardNumber.length.max)
        assertEquals("#### #### #### ####", cardNumber.mask)

        val secCode = assertNotNull(pm.securityCode)
        assertEquals("mandatory", secCode.mode)
        assertEquals(3, secCode.length)

        val issuers = assertNotNull(pm.issuers)
        assertEquals(1, issuers.size)
        assertEquals("1", issuers[0].id)
        assertEquals("Visa", issuers[0].name)
    }

    @Test
    fun `given response without installment then installment is null`() {
        val json = FULL_RESPONSE_JSON.replace(
            """"installment": {""",
            """"installment_absent": {""",
        )
        val response = parse(json)

        assertNull(response.installment)
    }

    @Test
    fun `given data classes then equality and copy work as value types`() {
        val length = CardFieldLength(min = 16, max = 16)
        assertEquals(length, length.copy())

        val issuer = CardIssuerConfig(id = "1", name = "Visa")
        assertEquals(issuer, issuer.copy())
        assertTrue(issuer.toString().contains("CardIssuerConfig"))
    }

    private companion object {
        val FULL_RESPONSE_JSON = """
            {
              "translations": {
                "card_form_title": "Ingresá tu tarjeta",
                "card_form_footer_button_label": "Pagar",
                "card_number": {
                  "label": "Número de tarjeta",
                  "placeholder": "1234 1234 1234 1234",
                  "error_empty_field": "Completá este campo.",
                  "error_incomplete_field": "Ingresá el número completo.",
                  "error_invalid_field": "Ingresalo como figura en la tarjeta."
                },
                "security_code": {
                  "label": "Código de seguridad",
                  "placeholder": "Ej.: ***",
                  "tooltip": "Es un número de 3 dígitos que está en el reverso de tu tarjeta.",
                  "error_empty_field": "Completá este campo.",
                  "error_incomplete_field": "Ingresá el código completo."
                },
                "expiration_date": {
                  "label": "Fecha de vencimiento",
                  "placeholder": "MM/AA",
                  "error_empty_field": "Completá este campo.",
                  "error_incomplete_field": "Ingresá la fecha completa.",
                  "error_invalid_field": "Ingresá una fecha válida."
                },
                "holder_name": {
                  "label": "Titular de tarjeta",
                  "placeholder": "Ej.: Maria Lopez",
                  "helper": "Como aparece en la tarjeta"
                },
                "installments": {
                  "header": {
                    "chevron": "Elegí las cuotas",
                    "radio": "Elegí las cuotas",
                    "title": "Elegí las cuotas"
                  },
                  "interest_free_label": "Sin interés",
                  "total_label": "Total"
                }
              },
              "installment": {
                "selection_type": "radio_button",
                "quotas": [
                  {
                    "installments": 1,
                    "installment_amount": 500.00,
                    "total_amount": 500.00,
                    "primary_label": "1x $ 500,00",
                    "secondary_label": "",
                    "state": "none",
                    "accessibility_label": "1 cuota de $ 500,00. Total $ 500,00."
                  },
                  {
                    "installments": 3,
                    "installment_amount": 170.00,
                    "total_amount": 510.00,
                    "primary_label": "3x $ 170,00",
                    "secondary_label": "Sin interés",
                    "state": "success",
                    "accessibility_label": "3 cuotas de $ 170,00. Total $ 510,00."
                  }
                ]
              },
              "payment_methods": [
                {
                  "id": "visa",
                  "payment_type_id": "credit_card",
                  "card_number": {
                    "type": "Number",
                    "length": { "min": 16, "max": 16 },
                    "mask": "#### #### #### ####"
                  },
                  "security_code": {
                    "mode": "mandatory",
                    "length": 3,
                    "type": "Number",
                    "tooltip": "Es un número de 3 dígitos que está en el reverso de tu tarjeta.",
                    "placeholder": "Ej.: ***"
                  },
                  "issuers": [
                    { "id": "1", "name": "Visa" }
                  ]
                }
              ]
            }
        """.trimIndent()
    }
}
