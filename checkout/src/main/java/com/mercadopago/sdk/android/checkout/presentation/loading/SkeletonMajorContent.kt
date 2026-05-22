package com.mercadopago.sdk.android.checkout.presentation.loading

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.components.MPSkeleton
import com.mercadopago.sdk.android.components.MPSkeletonThumbnail
import com.mercadopago.sdk.android.components.model.MPSkeletonThumbnailType
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme

private val HeaderSellerRowHeight = 44.dp
private val SellerLineHeight = 28.dp
private val TextBarHeight = 14.dp
private val TitleGap = 10.dp

@Composable
internal fun SkeletonMajorContent(
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        HeaderBlock()
        SellerBlock()
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) {
            ListRowWithBadge()
            ListRowWithBadge()
        }
        SummaryBlock()
    }
}

@Composable
private fun HeaderBlock() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = MercadoPagoTheme.spacing.paddings.xsmall),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = MercadoPagoTheme.spacing.paddings.xtiny,
                    vertical = MercadoPagoTheme.spacing.paddings.micro,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MPSkeletonThumbnail(type = MPSkeletonThumbnailType.Squared)
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(HeaderSellerRowHeight)
                .padding(horizontal = MercadoPagoTheme.spacing.paddings.xtiny),
            verticalArrangement = Arrangement.Center,
        ) {
            MPSkeleton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(SellerLineHeight),
            )
        }
    }
}

@Composable
private fun SellerBlock() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MercadoPagoTheme.spacing.paddings.xtiny)
            .padding(bottom = MercadoPagoTheme.spacing.paddings.xsmall),
        verticalArrangement = Arrangement.spacedBy(MercadoPagoTheme.spacing.gap.xmicro),
    ) {
        MPSkeletonThumbnail(type = MPSkeletonThumbnailType.LargeRounded)
        MPSkeleton(
            modifier = Modifier
                .fillMaxWidth()
                .height(MercadoPagoTheme.spacing.paddings.tiny),
        )
    }
}

@Composable
private fun ListRowWithBadge() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = MercadoPagoTheme.spacing.paddings.xtiny,
                vertical = MercadoPagoTheme.spacing.paddings.micro,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(TitleGap),
        ) {
            MPSkeleton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = MercadoPagoTheme.spacing.paddings.xmega)
                    .height(TextBarHeight),
            )
            MPSkeleton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = MercadoPagoTheme.spacing.paddings.huge)
                    .height(TextBarHeight),
            )
        }
        MPSkeleton(
            modifier = Modifier
                .padding(start = MercadoPagoTheme.spacing.gap.micro)
                .width(MercadoPagoTheme.spacing.paddings.huge)
                .height(MercadoPagoTheme.spacing.paddings.small),
            shape = MercadoPagoTheme.shape.medium,
        )
    }
}

@Composable
private fun SummaryBlock() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(SellerLineHeight)
                .padding(
                    horizontal = MercadoPagoTheme.spacing.paddings.xtiny,
                    vertical = MercadoPagoTheme.spacing.paddings.xnano,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                MPSkeleton(
                    modifier = Modifier
                        .width(MercadoPagoTheme.spacing.paddings.mega)
                        .height(TextBarHeight),
                )
            }
            MPSkeleton(
                modifier = Modifier
                    .padding(start = MercadoPagoTheme.spacing.gap.micro)
                    .width(MercadoPagoTheme.spacing.paddings.huge)
                    .fillMaxHeight(),
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(MercadoPagoTheme.spacing.paddings.mega)
                .padding(
                    horizontal = MercadoPagoTheme.spacing.paddings.xtiny,
                    vertical = MercadoPagoTheme.spacing.paddings.xnano,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MPSkeleton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MercadoPagoTheme.spacing.paddings.large),
            )
        }
    }
}
