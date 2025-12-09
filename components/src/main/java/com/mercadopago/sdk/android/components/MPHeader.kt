package com.mercadopago.sdk.android.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme

private const val EXPANDED_HEIGHT_DP = 120
private const val COLLAPSED_HEIGHT_DP = 56
private const val BACK_BUTTON_SIZE_DP = 40
private const val BACK_BUTTON_ID = "backButton"
private const val TITLE_ID = "title"

enum class MPHeaderHierarchy {
    Loud,
    Quiet,
    Mute,
}

private fun createMotionScene(): String = """
    {
        ConstraintSets: {
            start: {
                $BACK_BUTTON_ID: {
                    top: ['parent', 'top', 16],
                    start: ['parent', 'start', 16],
                    width: $BACK_BUTTON_SIZE_DP,
                    height: $BACK_BUTTON_SIZE_DP
                },
                $TITLE_ID: {
                    top: ['$BACK_BUTTON_ID', 'bottom', 16],
                    start: ['parent', 'start', 16]
                }
            },
            end: {
                $BACK_BUTTON_ID: {
                    top: ['parent', 'top', 8],
                    bottom: ['parent', 'bottom', 8],
                    start: ['parent', 'start', 8],
                    width: $BACK_BUTTON_SIZE_DP,
                    height: $BACK_BUTTON_SIZE_DP
                },
                $TITLE_ID: {
                    top: ['parent', 'top', 0],
                    bottom: ['parent', 'bottom', 0],
                    start: ['parent', 'start', 0],
                    end: ['parent', 'end', 0]
                }
            }
        },
        Transitions: {
            default: {
                from: 'start',
                to: 'end'
            }
        }
    }
""".trimIndent()

@Composable
private fun HeaderBackButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(BACK_BUTTON_SIZE_DP.dp)
            .clip(RoundedCornerShape(MercadoPagoTheme.spacing.xs))
            .background(MercadoPagoTheme.color.secondary)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Back",
            tint = MercadoPagoTheme.color.text.accent,
            modifier = Modifier.size(24.dp),
        )
    }
}

@Composable
private fun rememberNestedScrollConnection(
    maxOffsetPx: Float,
    onProgressChanged: (Float) -> Unit,
): NestedScrollConnection {
    var currentOffset by rememberSaveable { mutableFloatStateOf(0f) }
    return remember(maxOffsetPx) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val newOffset = (currentOffset + available.y).coerceIn(-maxOffsetPx, 0f)
                val consumed = newOffset - currentOffset
                currentOffset = newOffset
                onProgressChanged((-currentOffset / maxOffsetPx).coerceIn(0f, 1f))
                return Offset(0f, consumed)
            }
        }
    }
}

@OptIn(ExperimentalMotionApi::class)
@Composable
fun MPHeader(
    title: String,
    modifier: Modifier = Modifier,
    hierarchy: MPHeaderHierarchy = MPHeaderHierarchy.Loud,
    showBackButton: Boolean = true,
    backIcon: ImageVector = Icons.AutoMirrored.Filled.ArrowBack,
    expandedHeight: Dp = EXPANDED_HEIGHT_DP.dp,
    collapsedHeight: Dp = COLLAPSED_HEIGHT_DP.dp,
    backgroundColor: Color? = null,
    onBackClick: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    val density = LocalDensity.current
    val maxOffsetPx = with(density) { (expandedHeight - collapsedHeight).toPx() }
    var progress by rememberSaveable { mutableFloatStateOf(0f) }
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 300),
        label = "headerProgress",
    )
    val nestedScrollConnection = rememberNestedScrollConnection(maxOffsetPx) { progress = it }
    val bgColor = backgroundColor ?: MercadoPagoTheme.color.background.primary
    val motionScene = remember { createMotionScene() }
    val currentProgress = if (hierarchy == MPHeaderHierarchy.Mute) 1f else animatedProgress
    val currentHeight = when (hierarchy) {
        MPHeaderHierarchy.Mute -> collapsedHeight
        else -> expandedHeight - (expandedHeight - collapsedHeight) * animatedProgress
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(bgColor)
            .nestedScroll(nestedScrollConnection),
    ) {
        MotionLayout(
            motionScene = MotionScene(content = motionScene),
            progress = currentProgress,
            modifier = Modifier
                .fillMaxWidth()
                .height(currentHeight)
                .statusBarsPadding()
                .background(bgColor),
        ) {
            if (showBackButton && hierarchy != MPHeaderHierarchy.Quiet) {
                HeaderBackButton(
                    icon = backIcon,
                    onClick = onBackClick,
                    modifier = Modifier.layoutId(BACK_BUTTON_ID),
                )
            } else {
                Spacer(modifier = Modifier.layoutId(BACK_BUTTON_ID).size(0.dp))
            }
            MPText(
                text = title,
                textStyle = if (currentProgress < 0.5f) MPTextStyle.Title else MPTextStyle.BodyMediumSemiBold,
                colorType = MPTextColorType.Primary,
                modifier = Modifier.layoutId(TITLE_ID),
            )
        }
        Box(modifier = Modifier.fillMaxSize()) {
            content()
        }
    }
}

@Preview(name = "MPHeader Preview", group = "HEADER")
@Composable
private fun MPHeaderPreview() {
    MercadoPagoTheme {
        MPHeader(
            title = "Page Title",
            hierarchy = MPHeaderHierarchy.Loud,
            onBackClick = {},
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(50) { index ->
                    MPText(
                        text = "Item $index",
                        textStyle = MPTextStyle.BodyMediumRegular,
                        colorType = MPTextColorType.Primary,
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                    )
                }
            }
        }
    }
}
