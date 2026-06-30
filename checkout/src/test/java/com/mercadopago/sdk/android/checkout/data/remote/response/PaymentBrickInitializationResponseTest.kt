package com.mercadopago.sdk.android.checkout.data.remote.response

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class PaymentBrickInitializationResponseTest {
    // Same Gson configuration as the checkout Retrofit stack (RetrofitFactory).
    private val gson = GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create()

    private fun parse(
        json: String,
    ) =
        gson.fromJson(json, PaymentBrickInitializationResponse::class.java)

    @Test
    fun `given full initialization payload then maps every field of the tree`() {
        val response = parse(FULL_RESPONSE_JSON)

        assertEquals("Elegí cómo pagar", response.headerTitle)
        assertEquals("Total", response.footer.totalLabel)
        assertEquals("$ 188.000", response.footer.totalAmount)
        assertEquals(1, response.sections.size)

        val section = response.sections.first()
        assertEquals("Otros medios de pago", section.title)
        assertEquals(4, section.methods.size)
    }

    @Test
    fun `given saved card with cvv screen and installments then maps card data`() {
        val method = parse(FULL_RESPONSE_JSON).sections.first().methods[0]

        assertEquals("saved_card", method.type)
        assertEquals("Visa **** 1234", method.title)
        assertEquals("Visa · Crédito", method.subtitle)
        assertNull(method.options)

        val card = assertNotNull(method.cardData)
        assertEquals("123456", card.id)
        assertEquals("503143", card.bin)
        assertEquals("1234", card.lastFourDigits)
        assertEquals("visa", card.paymentMethodId)
        assertEquals("credit_card", card.paymentTypeId)
        assertEquals(1, card.issuerId)
        assertEquals(3, card.securityCode.length)

        val screen = assertNotNull(card.securityCode.screen)
        assertEquals("Ingresá el código de seguridad", screen.headerTitle)
        assertEquals("Continuar", screen.buttonLabel)
        assertEquals("Código de seguridad", screen.field.label)
        assertEquals("Ej.: 123", screen.field.placeholder)
        assertEquals("Está en el reverso de tu tarjeta.", screen.field.helper)

        val installments = assertNotNull(card.installments)
        assertEquals("Elegí las cuotas", installments.header.title)
        assertEquals("Total", installments.totalLabel)
        assertEquals("Pagar", installments.payButtonLabel)
        assertEquals("radio_button", installments.selectionType)
        assertEquals(2, installments.quotas.size)

        val quota = installments.quotas[1]
        assertEquals(3, quota.installments)
        assertEquals(BigDecimal("170.00"), quota.installmentAmount)
        assertEquals(BigDecimal("510.00"), quota.totalAmount)
        assertEquals("3x $ 170,00", quota.primaryLabel)
        assertEquals("$ 510,00", quota.secondaryLabel)
        assertEquals("interest_free", quota.state)
    }

    @Test
    fun `given saved card without cvv screen and without installments then optionals are null`() {
        val card = assertNotNull(parse(FULL_RESPONSE_JSON).sections.first().methods[1].cardData)

        assertEquals(3, card.securityCode.length)
        assertNull(card.securityCode.screen)
        assertNull(card.installments)
    }

    @Test
    fun `given ticket method then maps offline options and has no card data`() {
        val method = parse(FULL_RESPONSE_JSON).sections.first().methods[2]

        assertEquals("ticket", method.type)
        assertEquals("Efectivo", method.title)
        assertNull(method.cardData)

        val options = assertNotNull(method.options)
        assertEquals(2, options.size)
        assertEquals("pagofacil", options[0].id)
        assertEquals("Pago Fácil", options[0].name)
        assertEquals("https://.../pagofacil.png", options[0].iconUrl)
        assertEquals("rapipago", options[1].id)
    }

    @Test
    fun `given new card method then carries neither card data nor options`() {
        val method = parse(FULL_RESPONSE_JSON).sections.first().methods[3]

        assertEquals("new_card", method.type)
        assertEquals("Nueva tarjeta", method.title)
        assertEquals("Crédito y débito", method.subtitle)
        assertNull(method.cardData)
        assertNull(method.options)
    }

    @Test
    fun `given data classes then equality and copy behave as value types`() {
        val footer = PaymentBrickFooter(totalLabel = "Total", totalAmount = "$ 10")

        assertEquals(footer, footer.copy())
        assertEquals(footer.hashCode(), footer.copy().hashCode())
        assertTrue(footer.toString().contains("PaymentBrickFooter"))

        val option = TicketOption(id = "pagofacil", name = "Pago Fácil", iconUrl = "url")
        assertEquals(option, option.copy())
    }

    private companion object {
        val FULL_RESPONSE_JSON = """
            {
              "header_title": "Elegí cómo pagar",
              "sections": [
                {
                  "title": "Otros medios de pago",
                  "methods": [
                    {
                      "type": "saved_card",
                      "title": "Visa **** 1234",
                      "subtitle": "Visa · Crédito",
                      "icon_url": "https://.../visa.png",
                      "card_data": {
                        "id": "123456",
                        "bin": "503143",
                        "last_four_digits": "1234",
                        "payment_method_id": "visa",
                        "payment_type_id": "credit_card",
                        "issuer_id": 1,
                        "security_code": {
                          "length": 3,
                          "screen": {
                            "header_title": "Ingresá el código de seguridad",
                            "field": {
                              "label": "Código de seguridad",
                              "placeholder": "Ej.: 123",
                              "helper": "Está en el reverso de tu tarjeta."
                            },
                            "continue_button_label": "Continuar"
                          }
                        },
                        "installments": {
                          "header": { "title": "Elegí las cuotas" },
                          "total_label": "Total",
                          "pay_button_label": "Pagar",
                          "selection_type": "radio_button",
                          "quotas": [
                            {
                              "installments": 1,
                              "installment_amount": 500.00,
                              "total_amount": 500.00,
                              "primary_label": "1x $ 500,00",
                              "secondary_label": "",
                              "state": "none"
                            },
                            {
                              "installments": 3,
                              "installment_amount": 170.00,
                              "total_amount": 510.00,
                              "primary_label": "3x $ 170,00",
                              "secondary_label": "$ 510,00",
                              "state": "interest_free"
                            }
                          ]
                        }
                      }
                    },
                    {
                      "type": "saved_card",
                      "title": "Master **** 5678",
                      "subtitle": "Mastercard · Débito",
                      "icon_url": "https://.../master.png",
                      "card_data": {
                        "id": "789012",
                        "bin": "516105",
                        "last_four_digits": "5678",
                        "payment_method_id": "master",
                        "payment_type_id": "debit_card",
                        "issuer_id": 2,
                        "security_code": { "length": 3 }
                      }
                    },
                    {
                      "type": "ticket",
                      "title": "Efectivo",
                      "subtitle": "Pago Fácil y Rapipago",
                      "options": [
                        { "id": "pagofacil", "name": "Pago Fácil", "icon_url": "https://.../pagofacil.png" },
                        { "id": "rapipago", "name": "Rapipago", "icon_url": "https://.../rapipago.png" }
                      ]
                    },
                    {
                      "type": "new_card",
                      "title": "Nueva tarjeta",
                      "subtitle": "Crédito y débito",
                      "icon_url": "https://.../new_card.png"
                    }
                  ]
                }
              ],
              "footer": {
                "total_label": "Total",
                "total_amount": "$ 188.000"
              }
            }
        """.trimIndent()
    }
}
