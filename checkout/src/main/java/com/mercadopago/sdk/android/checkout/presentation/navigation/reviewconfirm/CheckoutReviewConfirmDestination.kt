package com.mercadopago.sdk.android.checkout.presentation.navigation.reviewconfirm

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.mercadopago.sdk.android.checkout.core.model.internal.toPaymentData
import com.mercadopago.sdk.android.checkout.domain.callback.CheckoutCallbackHolder
import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.checkout.presentation.reviewconfirm.ReviewConfirmScreen
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.ReviewConfirmViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun CheckoutReviewConfirmDestination(
    param: CheckoutReviewConfirmParam,
    actions: CheckoutReviewConfirmActions,
) {
    val viewModel: ReviewConfirmViewModel = koinViewModel {
        parametersOf(param.reviewContext.params, param.checkoutConfiguration)
    }
    val viewEvent by viewModel.viewEvent.collectAsState()
    val checkoutType = param.checkoutConfiguration?.checkoutType
    var actionHandled by remember(param.reviewContext) { mutableStateOf(false) }
    val dispatchAction: (ReviewConfirmNavigationAction) -> Unit = { action ->
        if (!actionHandled) {
            actionHandled = true
            handleReviewConfirmAction(
                action = action,
                param = param,
                actions = actions,
            )
        }
    }

    LaunchedEffect(viewEvent) {
        val event = viewEvent ?: return@LaunchedEffect
        viewModel.onViewEventConsumed()
        dispatchAction(
            ReviewConfirmNavigationPolicy.resolve(
                event = event,
                checkoutType = checkoutType,
                origin = param.reviewContext.origin,
            ),
        )
    }

    ReviewConfirmScreen(
        viewModel = viewModel,
        onBackClick = {
            dispatchAction(
                ReviewConfirmNavigationPolicy.resolveBack(param.reviewContext.origin),
            )
        },
    )
}

private fun handleReviewConfirmAction(
    action: ReviewConfirmNavigationAction,
    param: CheckoutReviewConfirmParam,
    actions: CheckoutReviewConfirmActions,
) {
    when (action) {
        is ReviewConfirmNavigationAction.NavigateUp -> actions.onNavigateUp(action.origin)
        ReviewConfirmNavigationAction.ReturnToPaymentSelector -> actions.onReturnToPaymentSelector()
        ReviewConfirmNavigationAction.ReturnToPaymentSelectorWithGenericError ->
            actions.onReturnToPaymentSelectorWithGenericError()
        is ReviewConfirmNavigationAction.FinishWithSuccess -> actions.onFinishCheckout(
            MercadoPagoCheckoutResult.Success(
                action.output.toPaymentData(param.checkoutConfiguration, param.reviewContext.params),
            ),
        )
        is ReviewConfirmNavigationAction.FinishWithError -> actions.onFinishCheckout(
            MercadoPagoCheckoutResult.Error(action.error),
        )
        ReviewConfirmNavigationAction.FinishWithCardTransactionCancellation -> {
            val cancellationContext = param.cardPaymentViewModel?.reviewConfirmCancellationContext()
            if (cancellationContext != null) {
                actions.onFinishCheckout(MercadoPagoCheckoutResult.UserCancelled(cancellationContext))
            } else {
                actions.onNavigateUp(param.reviewContext.origin)
            }
        }
        ReviewConfirmNavigationAction.FinishForEmailChange -> actions.onFinishForEmailChange(
            CheckoutCallbackHolder.emailChangeCallbackOrNull(),
        )
    }
}
