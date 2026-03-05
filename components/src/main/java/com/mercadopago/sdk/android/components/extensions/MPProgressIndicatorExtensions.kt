package com.mercadopago.sdk.android.components.extensions

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.components.model.MPProgressIndicatorSize

internal val MPProgressIndicatorSize.strokeWidth: Dp
    get() = when (this) {
        MPProgressIndicatorSize.XSmall -> 1.5.dp
        MPProgressIndicatorSize.Small -> 2.dp
        MPProgressIndicatorSize.Medium -> 4.dp
        MPProgressIndicatorSize.Large -> 6.dp
        MPProgressIndicatorSize.XLarge -> 8.dp
    }

internal val MPProgressIndicatorSize.circularSize: Dp
    get() = when (this) {
        MPProgressIndicatorSize.XSmall -> 16.dp
        MPProgressIndicatorSize.Small -> 24.dp
        MPProgressIndicatorSize.Medium -> 40.dp
        MPProgressIndicatorSize.Large -> 56.dp
        MPProgressIndicatorSize.XLarge -> 72.dp
    }
