package com.mercadopago.sdk.android.components.model

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Enum representing the available icon sizes.
 * Each size corresponds to a specific dimension in dp.
 *
 * @property size The size dimension in dp
 */
enum class MPIconSize(val size: Dp) {
    /** Extra small icon size (16dp). */
    XSmall(16.dp),

    /** Small icon size (20dp). */
    Small(20.dp),

    /** Medium icon size (24dp). */
    Medium(24.dp),

    /** Large icon size (32dp). */
    Large(32.dp),

    /** Extra large icon size (40dp). */
    XLarge(40.dp),

    /** Extra extra large icon size (48dp). */
    XXLarge(48.dp),
}
