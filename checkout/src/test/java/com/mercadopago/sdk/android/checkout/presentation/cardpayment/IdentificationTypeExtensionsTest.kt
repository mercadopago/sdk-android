package com.mercadopago.sdk.android.checkout.presentation.cardpayment

import com.mercadopago.sdk.android.components.model.MPBottomSheetListItem
import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class IdentificationTypeExtensionsTest {
    @Test
    fun `given IdentificationType with name then toBottomSheetListItem uses name as label`() {
        val type = IdentificationType(name = "CPF")

        val item = type.toBottomSheetListItem()

        assertEquals("CPF", item.label)
    }

    @Test
    fun `given IdentificationType with null name then toBottomSheetListItem uses empty label`() {
        val type = IdentificationType(name = null)

        val item = type.toBottomSheetListItem()

        assertEquals("", item.label)
    }

    @Test
    fun `given list of IdentificationType then toBottomSheetItems maps each to item`() {
        val types = listOf(
            IdentificationType(name = "CPF"),
            IdentificationType(name = "CNPJ"),
        )

        val items = types.toBottomSheetItems()

        assertEquals(2, items.size)
        assertEquals("CPF", items[0].label)
        assertEquals("CNPJ", items[1].label)
    }

    @Test
    fun `given matching item then findByBottomSheetItem returns the IdentificationType`() {
        val cpf = IdentificationType(name = "CPF")
        val cnpj = IdentificationType(name = "CNPJ")
        val types = listOf(cpf, cnpj)

        val found = types.findByBottomSheetItem(MPBottomSheetListItem(label = "CNPJ"))

        assertEquals(cnpj, found)
    }

    @Test
    fun `given non-matching item then findByBottomSheetItem returns null`() {
        val types = listOf(IdentificationType(name = "CPF"))

        val found = types.findByBottomSheetItem(MPBottomSheetListItem(label = "DNI"))

        assertNull(found)
    }
}
