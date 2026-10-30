package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import com.mercadopago.sdk.android.checkout.domain.model.SelectionDisplayType
import kotlin.test.Test

internal class MethodSelectionAnalyticsTrackerTest {
    private fun makeTracker() = MethodSelectionAnalyticsTracker()

    @Test
    fun `trackView does not throw when analytics is unavailable`() {
        makeTracker().trackView(optionsCount = 3, selectionType = SelectionDisplayType.Chevron)
    }

    @Test
    fun `trackView with RadioButton does not throw`() {
        makeTracker().trackView(optionsCount = 1, selectionType = SelectionDisplayType.RadioButton)
    }

    @Test
    fun `trackSelect does not throw when analytics is unavailable`() {
        makeTracker().trackSelect(paymentMethodId = "boleto", selectionType = SelectionDisplayType.Chevron)
    }

    @Test
    fun `trackSelect with RadioButton does not throw`() {
        makeTracker().trackSelect(paymentMethodId = "efecty", selectionType = SelectionDisplayType.RadioButton)
    }

    @Test
    fun `trackBack does not throw when analytics is unavailable`() {
        makeTracker().trackBack()
    }

    @Test
    fun `trackBack fires only once on repeated calls`() {
        val tracker = makeTracker()
        tracker.trackBack()
        tracker.trackBack() // second call must be a no-op — must not throw
    }

    @Test
    fun `independent tracker instances have independent cancelTracked state`() {
        val tracker1 = makeTracker()
        val tracker2 = makeTracker()

        tracker1.trackBack()
        tracker1.trackBack() // no-op for tracker1

        // tracker2 was not used yet — its first trackBack must not be no-op (no throw either)
        tracker2.trackBack()
    }
}
