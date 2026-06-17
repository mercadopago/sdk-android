package com.mercadopago.sdk.android.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.components.extensions.shape
import com.mercadopago.sdk.android.components.extensions.size
import com.mercadopago.sdk.android.components.model.MPSkeletonThumbnailType
import com.mercadopago.sdk.android.components.model.MPSkeletonType
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemes

private const val SKELETON_GROUP = "Skeleton"
private const val SHIMMER_DURATION_MS = 1500
private const val SHIMMER_TRAVEL_FACTOR = 2f
private const val SHIMMER_ALPHA_EDGE = 0f
private const val SHIMMER_ALPHA_EARLY = 0.2f
private const val SHIMMER_ALPHA_MIDDLE = 0.8f
private const val SHIMMER_STOP_EDGE_START = 0f
private const val SHIMMER_STOP_EARLY = 0.239f
private const val SHIMMER_STOP_MIDDLE = 0.499f
private const val SHIMMER_STOP_LATE = 0.739f
private const val SHIMMER_STOP_EDGE_END = 0.999f
private val SHIMMER_BLUR_RADIUS = 17.dp

/**
 * Skeleton base shape — átomo de loading com efeito shimmer.
 *
 * Usado como building block para placeholders enquanto o conteúdo real
 * está sendo carregado. Pinta um fundo cinza neutro ([MercadoPagoTheme.color.background.secondary])
 * com um gradiente de destaque ([MercadoPagoTheme.color.background.primary]) animado e
 * desfocado deslizando horizontalmente.
 *
 * Width/height são controlados pelo chamador via [modifier]
 * (ex.: `Modifier.fillMaxWidth().height(16.dp)` ou `Modifier.size(48.dp)`).
 *
 * Width/height são controlados pelo chamador via [modifier], exceto para
 * [MPSkeletonType.Thumbnail], que aplica um tamanho pré-definido próprio.
 *
 * @param modifier component modifier — define largura, altura e demais ajustes externos
 * @param type tipo do skeleton; define a forma (shape). Default é [MPSkeletonType.Row].
 *   Para uma forma fora dos tipos semânticos, use [MPSkeletonType.Custom];
 *   para avatares/ícones de tamanho fixo, use [MPSkeletonType.Thumbnail].
 */
@Composable
fun MPSkeleton(
    modifier: Modifier = Modifier,
    type: MPSkeletonType = MPSkeletonType.Row,
) {
    val shape = type.shape()
    val baseColor = MercadoPagoTheme.color.background.secondary
    val highlightColor = MercadoPagoTheme.color.background.primary
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

    val sizedModifier = if (type is MPSkeletonType.Thumbnail) {
        modifier.size(type.type.size)
    } else {
        modifier
    }

    Box(
        modifier = sizedModifier
            .clip(shape)
            .background(baseColor),
    ) {
        Spacer(
            modifier = Modifier
                .matchParentSize()
                .blur(SHIMMER_BLUR_RADIUS)
                .drawWithCache {
                    val width = size.width
                    val translateX = progress * SHIMMER_TRAVEL_FACTOR * width - width
                    val brush = Brush.linearGradient(
                        colorStops = arrayOf(
                            SHIMMER_STOP_EDGE_START to highlightColor.copy(alpha = SHIMMER_ALPHA_EDGE),
                            SHIMMER_STOP_EARLY to highlightColor.copy(alpha = SHIMMER_ALPHA_EARLY),
                            SHIMMER_STOP_MIDDLE to highlightColor.copy(alpha = SHIMMER_ALPHA_MIDDLE),
                            SHIMMER_STOP_LATE to highlightColor.copy(alpha = SHIMMER_ALPHA_EARLY),
                            SHIMMER_STOP_EDGE_END to highlightColor.copy(alpha = SHIMMER_ALPHA_EDGE),
                        ),
                        start = Offset(translateX, 0f),
                        end = Offset(translateX + width, 0f),
                    )
                    onDrawBehind { drawRect(brush = brush) }
                },
        )
    }
}

@Preview(name = "Skeleton - Line (atom)", group = SKELETON_GROUP)
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

@Preview(name = "Skeleton - Types (atom)", group = SKELETON_GROUP)
@Composable
internal fun MPSkeletonTypesPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Default) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Row — linha de texto (shape.small)
            MPSkeleton(
                modifier = Modifier.width(320.dp).height(16.dp),
                type = MPSkeletonType.Row,
            )
            // Squared — retângulo arredondado (shape.medium)
            MPSkeleton(
                modifier = Modifier.width(320.dp).height(48.dp),
                type = MPSkeletonType.Squared,
            )
            // Rounded — circular/pílula (shape.full)
            MPSkeleton(
                modifier = Modifier.size(48.dp),
                type = MPSkeletonType.Rounded,
            )
            // Custom — shape específico fornecido pelo chamador
            MPSkeleton(
                modifier = Modifier.width(320.dp).height(48.dp),
                type = MPSkeletonType.Custom(MercadoPagoTheme.shape.large),
            )
        }
    }
}

/**
 * Uso: placeholder de parágrafo de texto — várias linhas de larguras diferentes,
 * a última mais curta, simulando um bloco de texto carregando.
 */
@Preview(name = "Skeleton use - Text paragraph", group = SKELETON_GROUP)
@Composable
internal fun MPSkeletonTextParagraphPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Default) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
                .width(320.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            MPSkeleton(modifier = Modifier.fillMaxWidth().height(14.dp))
            MPSkeleton(modifier = Modifier.fillMaxWidth().height(14.dp))
            MPSkeleton(modifier = Modifier.fillMaxWidth().height(14.dp))
            MPSkeleton(modifier = Modifier.width(180.dp).height(14.dp))
        }
    }
}

@Preview(name = "Skeleton - Thumbnails (atom)", group = SKELETON_GROUP)
@Composable
internal fun MPSkeletonThumbnailsPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Default) {
        Row(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MPSkeleton(type = MPSkeletonType.Thumbnail(MPSkeletonThumbnailType.Squared))
            MPSkeleton(type = MPSkeletonType.Thumbnail(MPSkeletonThumbnailType.LargeRounded))
            MPSkeleton(type = MPSkeletonType.Thumbnail(MPSkeletonThumbnailType.XLargeRounded))
            MPSkeleton(type = MPSkeletonType.Thumbnail(MPSkeletonThumbnailType.HugeRounded))
        }
    }
}

/**
 * Uso: header de perfil — avatar ([MPSkeletonType.Thumbnail]) + nome e subtítulo
 * ([MPSkeletonType.Row]).
 */
@Preview(name = "Skeleton use - Profile header", group = SKELETON_GROUP)
@Composable
internal fun MPSkeletonProfileHeaderPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Default) {
        Row(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
                .width(320.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MPSkeleton(type = MPSkeletonType.Thumbnail(MPSkeletonThumbnailType.HugeRounded))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                MPSkeleton(modifier = Modifier.width(160.dp).height(16.dp))
                MPSkeleton(modifier = Modifier.width(96.dp).height(12.dp))
            }
        }
    }
}

/**
 * Uso: card de conteúdo — imagem/banner em bloco, título, descrição em duas
 * linhas e um botão, mostrando o skeleton em tipos variados.
 */
@Preview(name = "Skeleton use - Content card", group = SKELETON_GROUP)
@Composable
internal fun MPSkeletonContentCardPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Default) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
                .width(280.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            MPSkeleton(
                modifier = Modifier.fillMaxWidth().height(140.dp),
                type = MPSkeletonType.Squared,
            )
            MPSkeleton(modifier = Modifier.width(200.dp).height(18.dp))
            MPSkeleton(modifier = Modifier.fillMaxWidth().height(12.dp))
            MPSkeleton(modifier = Modifier.width(160.dp).height(12.dp))
            MPSkeleton(
                modifier = Modifier.fillMaxWidth().height(44.dp),
                type = MPSkeletonType.Squared,
            )
        }
    }
}

/**
 * Uso: campos de formulário — label curta seguida de um input em bloco,
 * repetidos, mais um botão de envio.
 */
@Preview(name = "Skeleton use - Form fields", group = SKELETON_GROUP)
@Composable
internal fun MPSkeletonFormFieldsPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Default) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
                .width(320.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            repeat(2) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    MPSkeleton(modifier = Modifier.width(96.dp).height(12.dp))
                    MPSkeleton(
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        type = MPSkeletonType.Squared,
                    )
                }
            }
            MPSkeleton(
                modifier = Modifier.fillMaxWidth().height(48.dp),
                type = MPSkeletonType.Rounded,
            )
        }
    }
}
