package com.mercadopago.sdk.android.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
private const val SCROLL_THRESHOLD = 0.5f
private const val PREVIEW_ITEM_COUNT = 50

/**
 * Defines the visual hierarchy level for [MPHeader].
 *
 * The hierarchy determines how prominent the header appears in the UI.
 */
enum class MPHeaderHierarchy {
    /** Maximum prominence with full visual treatment. */
    Loud,

    /** Reduced prominence without back button. */
    Quiet,

    /** Minimal prominence, collapsed state only. */
    Mute,
}

private val MOTION_SCENE = """
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

/**
 * A collapsible header component with motion animation support.
 *
 * @param title The title text displayed in the header.
 * @param modifier Modifier to be applied to the header container.
 * @param hierarchy The visual hierarchy level of the header.
 * @param showBackButton Whether to display the back navigation button.
 * @param backIcon The icon to use for the back button.
 * @param expandedHeight The height of the header when fully expanded.
 * @param collapsedHeight The height of the header when collapsed.
 * @param backgroundColor Optional background color override.
 * @param onBackClick Callback invoked when the back button is clicked.
 * @param content The content to display below the header.
 */
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
    content: @Composable (PaddingValues) -> Unit,
) {
    val density = LocalDensity.current
    val maxOffsetPx = with(density) { (expandedHeight - collapsedHeight).toPx() }
    var progress by rememberSaveable { mutableFloatStateOf(0f) }
    val nestedScrollConnection = rememberNestedScrollConnection(maxOffsetPx) { progress = it }
    val bgColor = backgroundColor ?: MercadoPagoTheme.color.background.primary
    val springSpec = spring<Float>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMediumLow,
    )
    val targetProgress = if (hierarchy == MPHeaderHierarchy.Mute) 1f else progress
    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = springSpec,
        label = "headerProgress",
    )
    val targetHeight = computeTargetHeight(hierarchy, collapsedHeight, expandedHeight, progress)
    val dpSpringSpec = spring<Dp>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMediumLow,
    )
    val animatedHeight by animateDpAsState(
        targetValue = targetHeight,
        animationSpec = dpSpringSpec,
        label = "headerHeight",
    )
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bgColor)
            .nestedScroll(nestedScrollConnection),
    ) {
        content(PaddingValues(top = expandedHeight))
        ContentFadeOverlay(
            backgroundColor = bgColor,
            headerHeight = animatedHeight,
        )
        MPHeaderMotionLayout(
            params = MPHeaderMotionLayoutParams(
                title = title,
                hierarchy = hierarchy,
                showBackButton = showBackButton,
                backIcon = backIcon,
                animatedProgress = animatedProgress,
                animatedHeight = animatedHeight,
                onBackClick = onBackClick,
            ),
        )
    }
}

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
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource,
            ): Offset {
                val delta = available.y
                return if (delta < 0) {
                    val newOffset = (currentOffset + delta).coerceIn(-maxOffsetPx, 0f)
                    val consumed = newOffset - currentOffset
                    currentOffset = newOffset
                    onProgressChanged((-currentOffset / maxOffsetPx).coerceIn(0f, 1f))
                    Offset(0f, consumed)
                } else {
                    Offset.Zero
                }
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource,
            ): Offset {
                return if (available.y > SCROLL_THRESHOLD) {
                    val newOffset = (currentOffset + available.y).coerceIn(-maxOffsetPx, 0f)
                    val consumedY = newOffset - currentOffset
                    currentOffset = newOffset
                    onProgressChanged((-currentOffset / maxOffsetPx).coerceIn(0f, 1f))
                    Offset(0f, consumedY)
                } else {
                    Offset.Zero
                }
            }
        }
    }
}

@Composable
private fun ContentFadeOverlay(
    backgroundColor: Color,
    headerHeight: Dp,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(headerHeight + 24.dp)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        backgroundColor,
                        backgroundColor.copy(alpha = 0.95f),
                        backgroundColor.copy(alpha = 0.8f),
                        backgroundColor.copy(alpha = 0.4f),
                        Color.Transparent,
                    ),
                ),
            ),
    )
}

@Composable
private fun computeTargetHeight(
    hierarchy: MPHeaderHierarchy,
    collapsedHeight: Dp,
    expandedHeight: Dp,
    progress: Float,
): Dp =
    when (hierarchy) {
        MPHeaderHierarchy.Mute -> collapsedHeight
        else -> expandedHeight - (expandedHeight - collapsedHeight) * progress
    }

private data class MPHeaderMotionLayoutParams(
    val title: String,
    val hierarchy: MPHeaderHierarchy,
    val showBackButton: Boolean,
    val backIcon: ImageVector,
    val animatedProgress: Float,
    val animatedHeight: Dp,
    val onBackClick: () -> Unit,
)

@OptIn(ExperimentalMotionApi::class)
@Composable
private fun MPHeaderMotionLayout(params: MPHeaderMotionLayoutParams) {
    MotionLayout(
        motionScene = MotionScene(content = MOTION_SCENE),
        progress = params.animatedProgress,
        modifier = Modifier
            .fillMaxWidth()
            .height(params.animatedHeight)
            .statusBarsPadding(),
    ) {
        if (params.showBackButton && params.hierarchy != MPHeaderHierarchy.Quiet) {
            HeaderBackButton(
                icon = params.backIcon,
                onClick = params.onBackClick,
                modifier = Modifier.layoutId(BACK_BUTTON_ID),
            )
        } else {
            Spacer(modifier = Modifier.layoutId(BACK_BUTTON_ID).size(0.dp))
        }
        val textStyle = if (params.animatedProgress < SCROLL_THRESHOLD) {
            MPTextStyle.Title
        } else {
            MPTextStyle.BodyMediumSemiBold
        }
        MPText(
            text = params.title,
            textStyle = textStyle,
            colorType = MPTextColorType.Primary,
            modifier = Modifier.layoutId(TITLE_ID),
        )
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
        ) { contentPadding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = contentPadding,
            ) {
                items(PREVIEW_ITEM_COUNT) { index ->
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
