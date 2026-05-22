package com.mercadopago.sdk.android.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemes

private const val SKELETON_GROUP = "Skeleton"
private const val SHIMMER_DURATION_MS = 1500
private const val SHIMMER_TRAVEL_FACTOR = 2f
private const val SHIMMER_ALPHA_EDGE = 0f
private const val SHIMMER_ALPHA_EARLY = 0.2f
private const val SHIMMER_ALPHA_MIDDLE = 0.8f

/**
 * Skeleton base shape — átomo de loading com efeito shimmer.
 *
 * Usado como building block para placeholders enquanto o conteúdo real
 * está sendo carregado. Pinta um fundo cinza neutro com gradiente
 * branco animado deslizando horizontalmente.
 *
 * Width/height são controlados pelo chamador via [modifier]
 * (ex.: `Modifier.fillMaxWidth().height(16.dp)` ou `Modifier.size(48.dp)`).
 *
 * @param modifier component modifier — define largura, altura e demais ajustes externos
 * @param shape forma do skeleton; default é [MercadoPagoTheme.shape.small] (8dp)
 */
@Composable
fun MPSkeleton(
    modifier: Modifier = Modifier,
    shape: Shape = MercadoPagoTheme.shape.small,
) {
    val baseColor = MercadoPagoTheme.color.background.secondary
    val transition = rememberInfiniteTransition(label = "MPSkeleton-shimmer")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = SHIMMER_DURATION_MS, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "MPSkeleton-shimmer-progress",
    )

    Spacer(
        modifier = modifier
            .clip(shape)
            .background(baseColor)
            .drawWithCache {
                val width = size.width
                val translateX = progress * SHIMMER_TRAVEL_FACTOR * width - width
                val brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = SHIMMER_ALPHA_EDGE),
                        Color.White.copy(alpha = SHIMMER_ALPHA_EARLY),
                        Color.White.copy(alpha = SHIMMER_ALPHA_MIDDLE),
                        Color.White.copy(alpha = SHIMMER_ALPHA_EARLY),
                        Color.White.copy(alpha = SHIMMER_ALPHA_EDGE),
                    ),
                    start = Offset(translateX, 0f),
                    end = Offset(translateX + width, 0f),
                )
                onDrawBehind { drawRect(brush = brush) }
            },
    )
}

@Preview(name = "Skeleton - Line", group = SKELETON_GROUP)
@Composable
internal fun MPSkeletonLinePreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Default) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
        ) {
            MPSkeleton(
                modifier = Modifier
                    .width(168.dp)
                    .height(16.dp),
            )
            Spacer(Modifier.size(16.dp))
            MPSkeleton(
                modifier = Modifier
                    .width(120.dp)
                    .height(14.dp),
            )
        }
    }
}

@Preview(name = "Skeleton - Block", group = SKELETON_GROUP)
@Composable
internal fun MPSkeletonBlockPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Default) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
        ) {
            MPSkeleton(
                modifier = Modifier
                    .width(320.dp)
                    .height(48.dp),
                shape = MercadoPagoTheme.shape.medium,
            )
        }
    }
}
