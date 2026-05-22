package com.mercadopago.sdk.android.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.components.extensions.shape
import com.mercadopago.sdk.android.components.extensions.size
import com.mercadopago.sdk.android.components.model.MPSkeletonThumbnailType
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemes

private const val SKELETON_THUMBNAIL_GROUP = "Skeleton"

/**
 * Skeleton thumbnail — placeholder shimmer para ícones e avatares.
 *
 * Compõe o [MPSkeleton] base aplicando tamanho e forma definidos por [type].
 *
 * @param modifier component modifier
 * @param type tipo (tamanho + forma) — ver [MPSkeletonThumbnailType]
 */
@Composable
fun MPSkeletonThumbnail(
    modifier: Modifier = Modifier,
    type: MPSkeletonThumbnailType = MPSkeletonThumbnailType.LargeRounded,
) {
    MPSkeleton(
        modifier = modifier.size(type.size),
        shape = type.shape(),
    )
}

@Preview(name = "Skeleton Thumbnail - All Types", group = SKELETON_THUMBNAIL_GROUP)
@Composable
internal fun MPSkeletonThumbnailPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Default) {
        Row(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            MPSkeletonThumbnail(type = MPSkeletonThumbnailType.Squared)
            MPSkeletonThumbnail(type = MPSkeletonThumbnailType.LargeRounded)
            MPSkeletonThumbnail(type = MPSkeletonThumbnailType.XLargeRounded)
            MPSkeletonThumbnail(type = MPSkeletonThumbnailType.HugeRounded)
        }
    }
}
