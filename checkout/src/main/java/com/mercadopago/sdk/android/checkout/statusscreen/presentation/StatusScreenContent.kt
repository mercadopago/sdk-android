package com.mercadopago.sdk.android.checkout.statusscreen.presentation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenButtonStyle
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenFooterButton
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenIcon
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenListItemStyle
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenSegmentState
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenSubtitleSegment
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenViewData
import com.mercadopago.sdk.android.components.MPBarcode
import com.mercadopago.sdk.android.components.MPButton
import com.mercadopago.sdk.android.components.MPButtonStyle
import com.mercadopago.sdk.android.components.MPFeedback
import com.mercadopago.sdk.android.components.MPListItem
import com.mercadopago.sdk.android.components.MPText
import com.mercadopago.sdk.android.components.R
import com.mercadopago.sdk.android.components.model.MPListItemContentInfo
import com.mercadopago.sdk.android.components.model.MPListItemLeading
import com.mercadopago.sdk.android.components.model.MPListItemTitleStyle
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenBodyItem as BodyItem
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenHeader as StatusScreenHeaderModel

@Composable
internal fun StatusScreenContent(
    data: StatusScreenViewData,
    onBack: () -> Unit,
    onOpenPdf: (String) -> Unit,
    onChangePaymentMethod: () -> Unit,
    onCopy: () -> Unit,
    modifier: Modifier = Modifier,
    onOpenUrl: (String) -> Unit = {},
    isOpeningReceipt: Boolean = false,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = MercadoPagoTheme.spacing.paddings.xtiny,
                    vertical = MercadoPagoTheme.spacing.paddings.small,
                ),
            verticalArrangement = Arrangement.spacedBy(MercadoPagoTheme.spacing.gap.xsmall),
        ) {
            StatusScreenHeader(header = data.header)
            data.body.forEach { item -> StatusScreenBodyItem(item = item, onCopy = onCopy) }
        }
        StatusScreenFooter(
            buttons = data.footer.buttons,
            onBack = onBack,
            onOpenPdf = onOpenPdf,
            onChangePaymentMethod = onChangePaymentMethod,
            onOpenUrl = onOpenUrl,
            isOpeningReceipt = isOpeningReceipt,
        )
    }
}

@Composable
private fun StatusScreenHeader(
    header: StatusScreenHeaderModel,
) {
    val iconRes = header.iconRes
    val iconUrl = header.iconUrl
    when {
        iconRes != null -> MPFeedback(title = header.title, iconRes = iconRes, subtitle = header.subtitle)
        iconUrl != null -> MPFeedback(title = header.title, iconUrl = iconUrl, subtitle = header.subtitle)
    }
}

@Composable
private fun StatusScreenBodyItem(
    item: BodyItem,
    onCopy: () -> Unit,
) {
    when (item) {
        is BodyItem.ListItem -> MPListItem(
            modifier = Modifier.offset(x = -MercadoPagoTheme.spacing.paddings.xmicro),
            contentInfo = item.toContentInfo(),
            leftImage = item.toLeading(),
        )

        is BodyItem.Message -> MPText(
            text = item.text,
            style = MercadoPagoTheme.typography.body.default.medium,
            color = MercadoPagoTheme.color.text.primary,
        )

        is BodyItem.Barcode -> MPBarcode(
            content = item.content,
            codeFormatted = item.codeFormatted,
            copyLabel = item.copyLabel,
            copyFeedback = item.copyFeedback,
            imageDataUri = item.imageDataUri,
            label = item.label,
            icon = item.icon?.toDrawableRes(),
            onCopy = onCopy,
        )
    }
}

// A simple row without title shows its text as the title in the primary color, as iOS does
@Composable
private fun BodyItem.ListItem.toContentInfo(): MPListItemContentInfo =
    if (title == null && style == StatusScreenListItemStyle.Simple && subtitle != null) {
        MPListItemContentInfo(
            title = subtitle,
            titleStyle = MPListItemTitleStyle.Regular,
        )
    } else {
        MPListItemContentInfo(
            title = title,
            description = subtitle,
            titleStyle = style.toTitleStyle(),
            annotatedDescription = subtitleSegments.toAnnotatedSubtitle(),
        )
    }

@Composable
private fun BodyItem.ListItem.toLeading(): MPListItemLeading? =
    when {
        imageUrl != null -> MPListItemLeading.ThumbnailConstrained(imageUrl)
        leadingIcon != null -> MPListItemLeading.Icon(
            icon = ImageVector.vectorResource(leadingIcon.toDrawableRes()),
            size = MercadoPagoTheme.spacing.paddings.tiny,
        )
        else -> null
    }

@Composable
private fun List<StatusScreenSubtitleSegment>.toAnnotatedSubtitle(): AnnotatedString? {
    val successColor = MercadoPagoTheme.color.feedback.positive.textLoud
    return remember(this, successColor) {
        takeIf { it.isNotEmpty() }?.toAnnotatedString { state ->
            when (state) {
                StatusScreenSegmentState.Success -> successColor
            }
        }
    }
}

private fun StatusScreenListItemStyle?.toTitleStyle(): MPListItemTitleStyle =
    when (this) {
        StatusScreenListItemStyle.Simple -> MPListItemTitleStyle.Regular
        null -> MPListItemTitleStyle.Bold
    }

@DrawableRes
private fun StatusScreenIcon.toDrawableRes(): Int =
    when (this) {
        StatusScreenIcon.Copy -> R.drawable.ic_copy
        StatusScreenIcon.Bill -> R.drawable.ic_bill
    }

@Composable
@Suppress("LongParameterList")
private fun StatusScreenFooter(
    buttons: List<StatusScreenFooterButton>,
    onBack: () -> Unit,
    onOpenPdf: (String) -> Unit,
    onChangePaymentMethod: () -> Unit,
    onOpenUrl: (String) -> Unit,
    isOpeningReceipt: Boolean,
) {
    if (buttons.isEmpty()) return
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = MercadoPagoTheme.spacing.paddings.xtiny,
                vertical = MercadoPagoTheme.spacing.paddings.xtiny,
            ),
        verticalArrangement = Arrangement.spacedBy(MercadoPagoTheme.spacing.gap.xmicro),
    ) {
        buttons.forEach { button ->
            MPButton(
                text = button.label,
                modifier = Modifier.fillMaxWidth(),
                style = button.toStyle(),
                isLoading = isOpeningReceipt && button is StatusScreenFooterButton.OpenPdf,
                onClick = {
                    when (button) {
                        is StatusScreenFooterButton.Back -> onBack()
                        is StatusScreenFooterButton.OpenPdf -> onOpenPdf(button.receiptUrl)
                        is StatusScreenFooterButton.ChangePaymentMethod -> onChangePaymentMethod()
                        is StatusScreenFooterButton.OpenUrl -> onOpenUrl(button.url)
                    }
                },
            )
        }
    }
}

private fun StatusScreenFooterButton.toStyle(): MPButtonStyle =
    when (style) {
        StatusScreenButtonStyle.Loud -> MPButtonStyle.Loud
        StatusScreenButtonStyle.Quiet -> MPButtonStyle.Quiet
        StatusScreenButtonStyle.Transparent -> MPButtonStyle.Transparent
        null -> when (this) {
            is StatusScreenFooterButton.Back -> MPButtonStyle.Quiet
            is StatusScreenFooterButton.OpenPdf,
            is StatusScreenFooterButton.OpenUrl,
            is StatusScreenFooterButton.ChangePaymentMethod,
            -> MPButtonStyle.Loud
        }
    }
