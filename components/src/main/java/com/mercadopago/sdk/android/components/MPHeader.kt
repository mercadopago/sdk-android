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

private const val HEADER_GROUP = "HEADER"
private const val ANIMATION_DURATION_MS = 300
private const val EXPANDED_HEIGHT_DP = 120
private const val COLLAPSED_HEIGHT_DP = 56
private const val BACK_BUTTON_SIZE_DP = 40
private const val TITLE_START_EXPANDED_DP = 16
private const val TITLE_START_COLLAPSED_DP = 56
private const val BACK_BUTTON_ID = "backButton"
private const val TITLE_ID = "title"

/**
 * Header hierarchy enum class, used to determine the visual appearance of the header
 * This is used to control the header's collapse state and navigation visibility
 */
enum class MPHeaderHierarchy {
    /**
     * Loud: Expanded header with back button and large title below it
     */
    Loud,

    /**
     * Quiet: Header with only the title, no back button visible
     */
    Quiet,

    /**
     * Mute: Collapsed header with back button and title inline
     */
    Mute,
}

/**
 * Data class representing the state of the collapsible header
 *
 * @property progress The current progress of the collapse animation (0f = expanded, 1f = collapsed)
 * @property isCollapsed Whether the header is currently in collapsed state
 */
data class MPHeaderState(
    val progress: Float = 0f,
    val isCollapsed: Boolean = false,
)

/**
 * Creates a nested scroll connection for the collapsible header behavior
 *
 * @param expandedHeightPx The expanded height in pixels
 * @param collapsedHeightPx The collapsed height in pixels
 * @param onProgressChanged Callback when the collapse progress changes
 * @return NestedScrollConnection that handles the scroll behavior
 */
@Composable
private fun rememberHeaderNestedScrollConnection(
    expandedHeightPx: Float,
    collapsedHeightPx: Float,
    onProgressChanged: (Float) -> Unit,
): NestedScrollConnection {
    var currentOffset by rememberSaveable { mutableFloatStateOf(0f) }
    val maxOffset = expandedHeightPx - collapsedHeightPx
    return remember(expandedHeightPx, collapsedHeightPx) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = (currentOffset + delta).coerceIn(-maxOffset, 0f)
                val consumed = newOffset - currentOffset
                currentOffset = newOffset
                val progress = (-currentOffset / maxOffset).coerceIn(0f, 1f)
                onProgressChanged(progress)
                return Offset(0f, consumed)
            }
        }
    }
}

/**
 * Creates the MotionScene JSON for the collapsible header animation
 *
 * @param expandedHeightDp The expanded height in dp
 * @param collapsedHeightDp The collapsed height in dp
 * @param backButtonSizeDp The back button size in dp
 * @param titleStartExpandedDp The title start position when expanded
 * @param titleStartCollapsedDp The title start position when collapsed
 * @return MotionScene JSON string
 */
private fun createMotionScene(
    expandedHeightDp: Int,
    collapsedHeightDp: Int,
    backButtonSizeDp: Int,
    titleStartExpandedDp: Int,
    titleStartCollapsedDp: Int,
): String {
    return """
        {
            ConstraintSets: {
                start: {
                    $BACK_BUTTON_ID: {
                        top: ['parent', 'top', 16],
                        start: ['parent', 'start', 16],
                        width: $backButtonSizeDp,
                        height: $backButtonSizeDp,
                        alpha: 1.0
                    },
                    $TITLE_ID: {
                        top: ['$BACK_BUTTON_ID', 'bottom', 16],
                        start: ['parent', 'start', $titleStartExpandedDp],
                        alpha: 1.0,
                        custom: {
                            textSize: 24
                        }
                    }
                },
                end: {
                    $BACK_BUTTON_ID: {
                        top: ['parent', 'top', 8],
                        start: ['parent', 'start', 8],
                        width: $backButtonSizeDp,
                        height: $backButtonSizeDp,
                        alpha: 1.0
                    },
                    $TITLE_ID: {
                        top: ['parent', 'top', 16],
                        start: ['$BACK_BUTTON_ID', 'end', 8],
                        centerVertically: 'parent',
                        alpha: 1.0,
                        custom: {
                            textSize: 18
                        }
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
}

/**
 * Back button composable for the header
 *
 * @param icon The icon to display
 * @param onClick Callback when the button is clicked
 * @param modifier Modifier for the button
 */
@Composable
private fun HeaderBackButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = modifier
            .size(BACK_BUTTON_SIZE_DP.dp)
            .clip(RoundedCornerShape(MercadoPagoTheme.spacing.xs))
            .background(MercadoPagoTheme.color.secondary)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            ),
        contentAlignment = androidx.compose.ui.Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Back",
            tint = MercadoPagoTheme.color.text.accent,
            modifier = Modifier.size(24.dp),
        )
    }
}

/**
 * Collapsible header component with MotionLayout animation and nested scroll support
 *
 * This component provides a header that collapses when scrolling down and expands when at the top.
 * It supports three hierarchy states:
 * - Loud: Expanded with back button and large title
 * - Quiet: Only title visible (no back button)
 * - Mute: Collapsed with back button and title inline
 *
 * @param title The title text to display in the header
 * @param modifier Modifier for the header
 * @param hierarchy The initial hierarchy state of the header
 * @param showBackButton Whether to show the back button
 * @param backIcon The icon for the back button
 * @param expandedHeight The height of the header when expanded
 * @param collapsedHeight The height of the header when collapsed
 * @param backgroundColor The background color of the header
 * @param onBackClick Callback when the back button is clicked
 * @param content The scrollable content below the header
 */
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
    val expandedHeightPx = with(density) { expandedHeight.toPx() }
    val collapsedHeightPx = with(density) { collapsedHeight.toPx() }
    var progress by rememberSaveable { mutableFloatStateOf(0f) }
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = ANIMATION_DURATION_MS),
        label = "headerProgress",
    )
    val nestedScrollConnection = rememberHeaderNestedScrollConnection(
        expandedHeightPx = expandedHeightPx,
        collapsedHeightPx = collapsedHeightPx,
        onProgressChanged = { progress = it },
    )
    val effectiveBackgroundColor = backgroundColor ?: MercadoPagoTheme.color.background.primary
    val shouldShowBackButton = showBackButton && hierarchy != MPHeaderHierarchy.Quiet
    val currentHeight = calculateCurrentHeight(
        hierarchy = hierarchy,
        expandedHeight = expandedHeight,
        collapsedHeight = collapsedHeight,
        animatedProgress = animatedProgress,
    )
    val motionSceneContent = remember(expandedHeight, collapsedHeight) {
        createMotionScene(
            expandedHeightDp = expandedHeight.value.toInt(),
            collapsedHeightDp = collapsedHeight.value.toInt(),
            backButtonSizeDp = BACK_BUTTON_SIZE_DP,
            titleStartExpandedDp = TITLE_START_EXPANDED_DP,
            titleStartCollapsedDp = TITLE_START_COLLAPSED_DP,
        )
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(effectiveBackgroundColor)
            .nestedScroll(nestedScrollConnection),
    ) {
        MotionLayout(
            motionScene = MotionScene(content = motionSceneContent),
            progress = if (hierarchy == MPHeaderHierarchy.Mute) 1f else animatedProgress,
            modifier = Modifier
                .fillMaxWidth()
                .height(currentHeight)
                .statusBarsPadding()
                .background(effectiveBackgroundColor),
        ) {
            if (shouldShowBackButton) {
                HeaderBackButton(
                    icon = backIcon,
                    onClick = onBackClick,
                    modifier = Modifier
                        .layoutId(BACK_BUTTON_ID)
                        .graphicsLayer {
                            alpha = if (hierarchy == MPHeaderHierarchy.Quiet) 0f else 1f
                        },
                )
            } else {
                Spacer(
                    modifier = Modifier
                        .layoutId(BACK_BUTTON_ID)
                        .size(0.dp),
                )
            }
            Box(
                modifier = Modifier
                    .layoutId(TITLE_ID)
                    .fillMaxWidth(),
                contentAlignment = if (animatedProgress < 0.5f && hierarchy != MPHeaderHierarchy.Mute) {
                    Alignment.CenterStart
                } else {
                    Alignment.Center
                }
            ) {
                MPText(
                    text = title,
                    textStyle = if (animatedProgress < 0.5f && hierarchy != MPHeaderHierarchy.Mute) {
                        MPTextStyle.Title
                    } else {
                        MPTextStyle.BodyMediumSemiBold
                    },
                    colorType = MPTextColorType.Primary,
                    modifier = Modifier.layoutId(TITLE_ID),
                )
            }
        }
        Box(modifier = Modifier.fillMaxSize()) {
            content()
        }
    }
}

/**
 * Calculates the current height of the header based on hierarchy and animation progress
 */
@Composable
private fun calculateCurrentHeight(
    hierarchy: MPHeaderHierarchy,
    expandedHeight: Dp,
    collapsedHeight: Dp,
    animatedProgress: Float,
): Dp {
    return when (hierarchy) {
        MPHeaderHierarchy.Loud -> {
            expandedHeight - (expandedHeight - collapsedHeight) * animatedProgress
        }

        MPHeaderHierarchy.Quiet -> {
            expandedHeight - (expandedHeight - collapsedHeight) * animatedProgress
        }

        MPHeaderHierarchy.Mute -> collapsedHeight
    }
}

@Preview(name = "Header with Scrollable Content", group = HEADER_GROUP)
@Composable
private fun MPHeaderWithContentPreview() {
    MercadoPagoTheme {
        MPHeader(
            title = "Page Title",
            hierarchy = MPHeaderHierarchy.Loud,
            onBackClick = {},
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
            ) {
                items(50) { index ->
                    MPText(
                        text = "Item $index",
                        textStyle = MPTextStyle.BodyMediumRegular,
                        colorType = MPTextColorType.Primary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    )
                }
            }
        }
    }
}
