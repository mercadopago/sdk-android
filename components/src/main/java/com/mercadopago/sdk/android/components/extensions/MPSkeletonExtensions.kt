package com.mercadopago.sdk.android.components.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.components.model.MPSkeletonThumbnailType
import com.mercadopago.sdk.android.components.model.MPSkeletonType
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme

internal val MPSkeletonThumbnailType.size: Dp
    get() = when (this) {
        MPSkeletonThumbnailType.Squared -> 40.dp
        MPSkeletonThumbnailType.LargeRounded -> 48.dp
        MPSkeletonThumbnailType.XLargeRounded -> 56.dp
        MPSkeletonThumbnailType.HugeRounded -> 64.dp
    }

@Composable
internal fun MPSkeletonThumbnailType.shape(): Shape =
    when (this) {
        MPSkeletonThumbnailType.Squared -> MercadoPagoTheme.shape.medium
        MPSkeletonThumbnailType.LargeRounded,
        MPSkeletonThumbnailType.XLargeRounded,
        MPSkeletonThumbnailType.HugeRounded,
        -> MercadoPagoTheme.shape.full
    }

@Composable
internal fun MPSkeletonType.shape(): Shape =
    when (this) {
        MPSkeletonType.Row -> MercadoPagoTheme.shape.small
        MPSkeletonType.Squared -> MercadoPagoTheme.shape.medium
        MPSkeletonType.Rounded -> MercadoPagoTheme.shape.full
        is MPSkeletonType.Custom -> shape
        is MPSkeletonType.Thumbnail -> type.shape()
    }
