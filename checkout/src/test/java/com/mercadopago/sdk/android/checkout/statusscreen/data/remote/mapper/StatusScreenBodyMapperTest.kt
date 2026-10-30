package com.mercadopago.sdk.android.checkout.statusscreen.data.remote.mapper

import com.mercadopago.sdk.android.checkout.statusscreen.data.remote.response.StatusScreenBodyNodeResponse
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenBodyItem
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenIcon
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenListItemStyle
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull

internal class StatusScreenBodyMapperTest {
    @Test
    fun `given approved response then body preserves seller then payment order`() {
        // Given
        val response = approvedResponse()

        // When
        val body = assertNotNull(response.toViewDataOrNull()).body

        // Then
        assertEquals(2, body.size)
        val seller = assertIs<StatusScreenBodyItem.ListItem>(body[0])
        assertEquals("Seller name", seller.title)
        val payment = assertIs<StatusScreenBodyItem.ListItem>(body[1])
        assertEquals("Visa Crédito •••• 1234 · 3x $ 300", payment.title)
        assertEquals("$ 900 sin interés", payment.subtitle)
        assertEquals("icon", payment.leadingType)
        assertEquals("card", payment.leadingValue)
    }

    @Test
    fun `given approved response with barcode then it is appended after the two mandatory items`() {
        // Given
        val response = approvedResponse(body = listOf(sellerListItem(), paymentListItem(), barcodeNode()))

        // When
        val body = assertNotNull(response.toViewDataOrNull()).body

        // Then
        assertEquals(3, body.size)
        val barcode = assertIs<StatusScreenBodyItem.Barcode>(body[2])
        assertEquals("Copiar código", barcode.copyLabel)
        assertEquals("Código copiado", barcode.copyFeedback)
    }

    @Test
    fun `given barcode with valid base64 png data uri then imageDataUri is preserved`() {
        // Given
        val dataUri = "data:image/png;base64,iVBORw0KGgo="
        val response = approvedResponse(body = listOf(sellerListItem(), paymentListItem(), barcodeNode(dataUri)))

        // When
        val barcode = assertIs<StatusScreenBodyItem.Barcode>(assertNotNull(response.toViewDataOrNull()).body[2])

        // Then
        assertEquals(dataUri, barcode.imageDataUri)
    }

    @Test
    fun `given barcode with label and a COPY icon then both are mapped`() {
        // Given
        val nodeWithLabel = barcodeNode().let {
            it.copy(data = it.data!!.copy(label = "Código de pago", icon = "COPY"))
        }
        val response = approvedResponse(body = listOf(sellerListItem(), paymentListItem(), nodeWithLabel))

        // When
        val barcode = assertIs<StatusScreenBodyItem.Barcode>(assertNotNull(response.toViewDataOrNull()).body[2])

        // Then
        assertEquals("Código de pago", barcode.label)
        assertEquals(StatusScreenIcon.Copy, barcode.icon)
    }

    @Test
    fun `given barcode with an unknown or missing icon then icon is null without dropping the barcode`() {
        // Given
        val unknownIconNode = barcodeNode().let { it.copy(data = it.data!!.copy(icon = "UNKNOWN")) }
        val missingIconNode = barcodeNode()
        val response = approvedResponse(body = listOf(sellerListItem(), paymentListItem(), unknownIconNode))
        val responseWithoutIcon = approvedResponse(body = listOf(sellerListItem(), paymentListItem(), missingIconNode))

        // When
        val barcode = assertIs<StatusScreenBodyItem.Barcode>(assertNotNull(response.toViewDataOrNull()).body[2])
        val barcodeWithoutIcon =
            assertIs<StatusScreenBodyItem.Barcode>(assertNotNull(responseWithoutIcon.toViewDataOrNull()).body[2])

        // Then
        assertNull(barcode.icon)
        assertNull(barcodeWithoutIcon.icon)
    }

    @Test
    fun `given barcode with invalid image data then imageDataUri becomes null without dropping the line`() {
        // Given
        val response = approvedResponse(
            body = listOf(sellerListItem(), paymentListItem(), barcodeNode("not-a-data-uri")),
        )

        // When
        val barcode = assertIs<StatusScreenBodyItem.Barcode>(assertNotNull(response.toViewDataOrNull()).body[2])

        // Then
        assertNull(barcode.imageDataUri)
        assertEquals("Copiar código", barcode.copyLabel)
    }

    @Test
    fun `given a body with a single item then it is still mapped`() {
        // Given
        val response = approvedResponse(body = listOf(sellerListItem()))

        // When
        val body = assertNotNull(response.toViewDataOrNull()).body

        // Then
        assertEquals(1, body.size)
        assertIs<StatusScreenBodyItem.ListItem>(body[0])
    }

    @Test
    fun `given a body with more than the usual two ListItems then all of them are mapped`() {
        // Given
        val response = approvedResponse(body = listOf(sellerListItem(), paymentListItem(), sellerListItem()))

        // When
        val body = assertNotNull(response.toViewDataOrNull()).body

        // Then
        assertEquals(3, body.size)
    }

    @Test
    fun `given body contains an unknown component then it is dropped without invalidating the rest`() {
        // Given
        val unknown = StatusScreenBodyNodeResponse(component = "MPUnknown", data = null)
        val response = approvedResponse(body = listOf(sellerListItem(), paymentListItem(), unknown))

        // When
        val body = assertNotNull(response.toViewDataOrNull()).body

        // Then
        assertEquals(2, body.size)
    }

    @Test
    fun `given an offline method body with only a seller ListItem and a Barcode then it is still mapped`() {
        // Given
        val response = approvedResponse(body = listOf(sellerListItem(), barcodeNode()))

        // When
        val body = assertNotNull(response.toViewDataOrNull()).body

        // Then
        assertEquals(2, body.size)
        assertIs<StatusScreenBodyItem.ListItem>(body[0])
        assertIs<StatusScreenBodyItem.Barcode>(body[1])
    }

    @Test
    fun `given list item optional image url is not https then imageUrl becomes null without dropping the item`() {
        // Given
        val invalidSeller = sellerListItem().copy(
            data = sellerListItem().data!!.copy(imageUrl = "http://insecure.com/x.png"),
        )
        val response = approvedResponse(body = listOf(invalidSeller, paymentListItem()))

        // When
        val seller = assertIs<StatusScreenBodyItem.ListItem>(assertNotNull(response.toViewDataOrNull()).body[0])

        // Then
        assertNull(seller.imageUrl)
        assertEquals("Seller name", seller.title)
    }

    @Test
    fun `given list item title is blank then title becomes null without dropping the item`() {
        // Given
        val blankTitleItem = sellerListItem().copy(data = sellerListItem().data!!.copy(title = ""))
        val response = approvedResponse(body = listOf(blankTitleItem, paymentListItem()))

        // When
        val seller = assertIs<StatusScreenBodyItem.ListItem>(assertNotNull(response.toViewDataOrNull()).body[0])

        // Then
        assertNull(seller.title)
    }

    @Test
    fun `given barcode missing required content then it is dropped without invalidating the rest`() {
        // Given
        val incompleteBarcode = barcodeNode().copy(data = barcodeNode().data!!.copy(content = null))
        val response = approvedResponse(body = listOf(sellerListItem(), paymentListItem(), incompleteBarcode))

        // When
        val body = assertNotNull(response.toViewDataOrNull()).body

        // Then
        assertEquals(2, body.size)
    }

    @Test
    fun `given list item with simple style then style is mapped to Simple`() {
        // Given
        val simpleItem = paymentListItem().let { it.copy(data = it.data!!.copy(style = "simple")) }
        val response = approvedResponse(body = listOf(simpleItem))

        // When
        val item = assertIs<StatusScreenBodyItem.ListItem>(assertNotNull(response.toViewDataOrNull()).body[0])

        // Then
        assertEquals(StatusScreenListItemStyle.Simple, item.style)
    }

    @Test
    fun `given list item with an unknown or missing style then style is null without dropping the item`() {
        // Given
        val unknownStyleItem = paymentListItem().let { it.copy(data = it.data!!.copy(style = "UNKNOWN")) }
        val response = approvedResponse(body = listOf(unknownStyleItem, paymentListItem()))

        // When
        val body = assertNotNull(response.toViewDataOrNull()).body

        // Then
        assertNull(assertIs<StatusScreenBodyItem.ListItem>(body[0]).style)
        assertNull(assertIs<StatusScreenBodyItem.ListItem>(body[1]).style)
    }

    @Test
    fun `given list item with icon leading and bill value then leadingIcon is Bill`() {
        // Given
        val billItem = paymentListItem().let { it.copy(data = it.data!!.copy(leadingValue = "bill")) }
        val response = approvedResponse(body = listOf(billItem))

        // When
        val item = assertIs<StatusScreenBodyItem.ListItem>(assertNotNull(response.toViewDataOrNull()).body[0])

        // Then
        assertEquals(StatusScreenIcon.Bill, item.leadingIcon)
    }

    @Test
    fun `given list item with an unknown leading icon value then leadingIcon is null without dropping the item`() {
        // Given
        val response = approvedResponse(body = listOf(paymentListItem()))

        // When
        val item = assertIs<StatusScreenBodyItem.ListItem>(assertNotNull(response.toViewDataOrNull()).body[0])

        // Then
        assertNull(item.leadingIcon)
        assertEquals("Visa Crédito •••• 1234 · 3x $ 300", item.title)
    }

    @Test
    fun `given bill value without icon leading type then leadingIcon is null`() {
        // Given
        val notIconItem = paymentListItem().let {
            it.copy(data = it.data!!.copy(leadingType = "image", leadingValue = "bill"))
        }
        val response = approvedResponse(body = listOf(notIconItem))

        // When
        val item = assertIs<StatusScreenBodyItem.ListItem>(assertNotNull(response.toViewDataOrNull()).body[0])

        // Then
        assertNull(item.leadingIcon)
    }
}
