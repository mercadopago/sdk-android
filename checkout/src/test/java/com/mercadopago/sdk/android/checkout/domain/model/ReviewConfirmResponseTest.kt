package com.mercadopago.sdk.android.checkout.domain.model

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals

internal class ReviewConfirmResponseTest {
    private val gson = GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create()

    @Test
    fun `given numeric total amount then maps footer amount as BigDecimal`() {
        val response = gson.fromJson(RESPONSE_JSON, ReviewConfirmResponse::class.java)

        assertEquals(BigDecimal("15"), response.footer.totalAmount)
        assertEquals("$", response.footer.currencySymbol)
    }

    private companion object {
        val RESPONSE_JSON =
            """
            {
              "header": {
                "title": "Revisá los datos antes de pagar",
                "seller_icon_url": "https://example.com/seller.png"
              },
              "items": [
                {
                  "type": "payment_method",
                  "label": "Medio de pago",
                  "value": "Visa •••• 1234",
                  "button": { "label": "Modificar" }
                }
              ],
              "footer_summary": {
                "products": [
                  {
                    "label": "Producto sin nombre",
                    "amount": "$ 15"
                  }
                ]
              },
              "footer": {
                "button": { "label": "Pagar" },
                "total_label": "Total",
                "currency_symbol": "$",
                "total_amount": 15,
                "installments": {
                  "label": "1x $ 15",
                  "secondary_label": "Sin interés",
                  "state": "success"
                }
              }
            }
            """.trimIndent()
    }
}
