package com.mercadopago.sdk.android.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.components.extensions.SkeletonListRowDescriptionEndSpace
import com.mercadopago.sdk.android.components.extensions.SkeletonListRowDescriptionHeight
import com.mercadopago.sdk.android.components.extensions.SkeletonListRowTitleEndSpace
import com.mercadopago.sdk.android.components.extensions.SkeletonListRowTitleHeight
import com.mercadopago.sdk.android.components.model.MPSkeletonThumbnailType
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemes

private const val SKELETON_LIST_ROW_GROUP = "Skeleton"

/**
 * Skeleton list row — placeholder de linha com thumbnail + título + descrição.
 *
 * Composição padrão: um [MPSkeletonThumbnail] à esquerda seguido por uma coluna
 * com dois [MPSkeleton] em forma de barra (título e descrição).
 *
 * O componente ocupa toda a largura disponível; controle via [modifier] caso
 * precise restringir.
 *
 * @param modifier component modifier
 * @param thumbnailType tipo do thumbnail; default circular 48dp
 */
@Composable
fun MPSkeletonListRow(
    modifier: Modifier = Modifier,
    thumbnailType: MPSkeletonThumbnailType = MPSkeletonThumbnailType.LargeRounded,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = MercadoPagoTheme.spacing.paddings.xtiny),
        horizontalArrangement = Arrangement.spacedBy(MercadoPagoTheme.spacing.gap.micro),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        MPSkeletonThumbnail(type = thumbnailType)
        Column(
            verticalArrangement = Arrangement.spacedBy(MercadoPagoTheme.spacing.gap.nano),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = SkeletonListRowTitleEndSpace),
            ) {
                MPSkeleton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(SkeletonListRowTitleHeight),
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = SkeletonListRowDescriptionEndSpace),
            ) {
                MPSkeleton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(SkeletonListRowDescriptionHeight),
                )
            }
        }
    }
}

@Preview(name = "Skeleton List Row", group = SKELETON_LIST_ROW_GROUP)
@Composable
internal fun MPSkeletonListRowPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Default) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
        ) {
            MPSkeletonListRow()
            MPSkeletonListRow(thumbnailType = MPSkeletonThumbnailType.Squared)
        }
    }
}
