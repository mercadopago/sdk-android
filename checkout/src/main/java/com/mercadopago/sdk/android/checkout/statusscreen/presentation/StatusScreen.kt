package com.mercadopago.sdk.android.checkout.statusscreen.presentation

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.WindowManager
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.core.os.ConfigurationCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.mercadopago.android.sdk.checkout.R
import com.mercadopago.sdk.android.checkout.analytics.StatusScreenCloseSource
import com.mercadopago.sdk.android.checkout.domain.model.OrderProcessOutput
import com.mercadopago.sdk.android.checkout.statusscreen.data.remote.mapper.toStatusScreenViewData
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenFallbackCopy
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenHeader
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenViewData
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.fallbackInstallments
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.methodDisplayName
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.paidAmount
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.ticketAmount
import com.mercadopago.sdk.android.components.MPProgressIndicator
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale
import com.mercadopago.sdk.android.components.R as ComponentsR

@Composable
internal fun StatusScreen(
    viewModel: StatusScreenViewModel,
    actions: StatusScreenActions,
    isOpeningReceipt: Boolean = false,
) {
    val state by viewModel.state.collectAsState()

    BackHandler(onBack = actions.onBack)

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MercadoPagoTheme.color.background.primary),
    ) {
        when (val currentState = state) {
            StatusScreenState.Idle, StatusScreenState.Loading -> MPProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
            )

            is StatusScreenState.Ready -> {
                SecureWhileVisible()
                LaunchedEffect(Unit) {
                    viewModel.trackRender()
                }
                TrackCloseOnBackStackExit(viewModel)
                StatusScreenContent(
                    data = currentState.data,
                    onBack = {
                        viewModel.trackClose(StatusScreenCloseSource.Back)
                        actions.onBack()
                    },
                    onOpenPdf = actions.onOpenPdf,
                    isOpeningReceipt = isOpeningReceipt,
                    onChangePaymentMethod = {
                        viewModel.trackRetry()
                        actions.onChangePaymentMethod()
                    },
                    onCopy = actions.onCopy,
                    onOpenUrl = actions.onOpenUrl,
                )
            }

            is StatusScreenState.Unavailable -> {
                TrackCloseOnBackStackExit(viewModel)
                StatusScreenContent(
                    data = rememberFallbackData(
                        payment = currentState.paymentProcessed,
                        lastFourDigits = currentState.lastFourDigits,
                        orderStatus = currentState.orderStatus,
                    ),
                    onBack = {
                        viewModel.trackClose(StatusScreenCloseSource.Back)
                        actions.onBack()
                    },
                    onOpenPdf = actions.onOpenPdf,
                    isOpeningReceipt = isOpeningReceipt,
                    onChangePaymentMethod = actions.onChangePaymentMethod,
                    onCopy = actions.onCopy,
                    onOpenUrl = actions.onOpenUrl,
                )
            }
        }
    }
}

@Composable
private fun rememberFallbackData(
    payment: OrderProcessOutput.PaymentProcessed?,
    lastFourDigits: String?,
    orderStatus: String?,
): StatusScreenViewData {
    val locale = ConfigurationCompat.getLocales(LocalConfiguration.current)[0] ?: Locale.getDefault()
    val texts = rememberFallbackAmountTexts(payment, locale)
    val copy = StatusScreenFallbackCopy(
        title = texts.approvedTitle,
        ticketTitle = texts.ticketTitle,
        paymentSubtitle = texts.paymentSubtitle,
        backLabel = stringResource(R.string.status_screen_fallback_back),
        openUrlLabel = stringResource(R.string.status_screen_fallback_open_url),
        barcodeLabel = stringResource(R.string.status_screen_fallback_barcode_label),
        barcodeCopyLabel = stringResource(R.string.status_screen_fallback_barcode_copy),
        barcodeCopyFeedback = stringResource(R.string.status_screen_fallback_barcode_copied),
        iconRes = ComponentsR.drawable.ic_feedback_success,
        rejectedHeader = StatusScreenHeader(
            title = stringResource(R.string.status_screen_fallback_rejected_title),
            iconRes = ComponentsR.drawable.ic_feedback_error,
        ),
        errorHeader = StatusScreenHeader(
            title = stringResource(R.string.status_screen_fallback_error_title),
            iconRes = R.drawable.cho_off_feedback_error,
        ),
        pendingHeader = StatusScreenHeader(
            title = stringResource(R.string.status_screen_fallback_pending_title),
            iconRes = R.drawable.cho_off_feedback_pending,
        ),
    )
    return remember(copy, payment, lastFourDigits, orderStatus) {
        copy.toStatusScreenViewData(
            payment = payment,
            lastFourDigits = lastFourDigits,
            orderStatus = orderStatus,
        )
    }
}

private data class FallbackAmountTexts(
    val approvedTitle: String,
    val ticketTitle: String,
    val paymentSubtitle: String?,
)

@Composable
private fun rememberFallbackAmountTexts(
    payment: OrderProcessOutput.PaymentProcessed?,
    locale: Locale,
): FallbackAmountTexts {
    val paidLabel = payment?.paidAmount()?.let {
        stringResource(R.string.status_screen_fallback_amount, it.formatForFallback(locale))
    }
    val ticketTitle = payment?.let { ticket ->
        val amount = ticket.ticketAmount()
        val method = ticket.methodDisplayName()
        if (amount != null && method != null) {
            stringResource(
                R.string.status_screen_fallback_ticket_title,
                stringResource(R.string.status_screen_fallback_amount, amount.formatForFallback(locale)),
                method,
            )
        } else {
            null
        }
    }.orEmpty()
    val installments = payment?.fallbackInstallments()
    val paymentSubtitle = if (installments != null && paidLabel != null) {
        stringResource(
            if (installments.interestFree) {
                R.string.status_screen_fallback_installments_no_interest
            } else {
                R.string.status_screen_fallback_installments_with_interest
            },
            paidLabel,
            installments.count,
            stringResource(
                R.string.status_screen_fallback_amount,
                installments.perInstallment.formatForFallback(locale),
            ),
        )
    } else {
        paidLabel
    }
    return FallbackAmountTexts(
        approvedTitle = paidLabel?.let { stringResource(R.string.status_screen_fallback_approved_title, it) }.orEmpty(),
        ticketTitle = ticketTitle,
        paymentSubtitle = paymentSubtitle,
    )
}

private const val FALLBACK_FRACTION_DIGITS = 2

private fun BigDecimal.formatForFallback(
    locale: Locale,
): String =
    NumberFormat.getNumberInstance(locale)
        .apply {
            minimumFractionDigits = FALLBACK_FRACTION_DIGITS
            maximumFractionDigits = FALLBACK_FRACTION_DIGITS
        }
        .format(this)

@Composable
private fun TrackCloseOnBackStackExit(
    viewModel: StatusScreenViewModel,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                viewModel.trackClose(StatusScreenCloseSource.Dismiss)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
}

@Composable
private fun SecureWhileVisible() {
    val activity = LocalContext.current.findActivity() ?: return
    DisposableEffect(activity) {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        onDispose {
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
    }
}

private tailrec fun Context.findActivity(): Activity? =
    when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }
