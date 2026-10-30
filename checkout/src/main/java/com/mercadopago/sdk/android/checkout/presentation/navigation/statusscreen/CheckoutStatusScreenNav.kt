package com.mercadopago.sdk.android.checkout.presentation.navigation.statusscreen

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.mercadopago.sdk.android.checkout.analytics.StatusScreenReceiptOutcome
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.domain.callback.CheckoutCallbackHolder
import com.mercadopago.sdk.android.checkout.presentation.navigation.CheckoutFlowViewModel
import com.mercadopago.sdk.android.checkout.presentation.navigation.CheckoutLoadDestination
import com.mercadopago.sdk.android.checkout.presentation.navigation.checkoutGraphEntry
import com.mercadopago.sdk.android.checkout.presentation.navigation.rememberCheckoutNavigator
import com.mercadopago.sdk.android.checkout.presentation.state.CheckoutDestination
import com.mercadopago.sdk.android.checkout.statusscreen.media.ReceiptDownloader
import com.mercadopago.sdk.android.checkout.statusscreen.presentation.StatusScreen
import com.mercadopago.sdk.android.checkout.statusscreen.presentation.StatusScreenActions
import com.mercadopago.sdk.android.checkout.statusscreen.presentation.StatusScreenViewModel
import com.mercadopago.sdk.android.checkout.statusscreen.share.ReceiptSharer
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

internal fun NavGraphBuilder.statusScreenDestination(
    navController: NavHostController,
    checkoutConfiguration: CheckoutConfiguration?,
    startDestination: CheckoutDestination,
) {
    composable<CheckoutDestination.StatusScreen> { backStackEntry ->
        val graphEntry = checkoutGraphEntry(navController, backStackEntry)
        val flowViewModel: CheckoutFlowViewModel = koinViewModel(viewModelStoreOwner = graphEntry)
        val navigator = rememberCheckoutNavigator(navController, flowViewModel, checkoutConfiguration)
        val statusScreenContext = remember(backStackEntry) { flowViewModel.state.value.statusScreenContext }
        if (statusScreenContext == null) {
            CheckoutLoadDestination(navigator, startDestination)
            return@composable
        }

        val viewModel: StatusScreenViewModel = koinViewModel {
            parametersOf(statusScreenContext)
        }

        val onExit = CheckoutCallbackHolder.statusScreenExitCallbackOrNull()
        val context = LocalContext.current
        val uriHandler = LocalUriHandler.current
        val coroutineScope = rememberCoroutineScope()
        val receiptDownloader = remember { ReceiptDownloader(cacheDir = context.cacheDir) }
        val receiptSharer = remember { ReceiptSharer(context = context) }

        var isOpeningReceipt by remember { mutableStateOf(false) }

        LaunchedEffect(receiptDownloader) {
            receiptDownloader.sweepStale()
        }

        StatusScreen(
            viewModel = viewModel,
            isOpeningReceipt = isOpeningReceipt,
            actions = StatusScreenActions(
                onBack = { navigator.closeStatusScreen(onExit) },
                onOpenPdf = { receiptUrl ->
                    if (!isOpeningReceipt) {
                        isOpeningReceipt = true
                        viewModel.trackReceipt(StatusScreenReceiptOutcome.Requested)
                        coroutineScope.launch {
                            try {
                                openReceipt(
                                    receiptUrl = receiptUrl,
                                    receiptDownloader = receiptDownloader,
                                    receiptSharer = receiptSharer,
                                    openUri = uriHandler::openUri,
                                    track = viewModel::trackReceipt,
                                )
                            } finally {
                                isOpeningReceipt = false
                            }
                        }
                    }
                },
                onChangePaymentMethod = {
                    navigator.recoverTo(startDestination)
                    graphEntry.viewModelStore.clear()
                },
                onCopy = {},
                onOpenUrl = uriHandler::openUri,
            ),
        )
    }
}

private suspend fun openReceipt(
    receiptUrl: String,
    receiptDownloader: ReceiptDownloader,
    receiptSharer: ReceiptSharer,
    openUri: (String) -> Unit,
    track: (StatusScreenReceiptOutcome) -> Unit,
) {
    var generated = false
    try {
        val file = receiptDownloader.download(receiptUrl)
        generated = true
        track(if (file != null) StatusScreenReceiptOutcome.Success else StatusScreenReceiptOutcome.Failure)
        val shared = file?.let(receiptSharer::share) == true
        if (!shared) openUri(receiptUrl)
    } catch (cancellation: CancellationException) {
        if (!generated) track(StatusScreenReceiptOutcome.Cancelled)
        throw cancellation
    }
}
