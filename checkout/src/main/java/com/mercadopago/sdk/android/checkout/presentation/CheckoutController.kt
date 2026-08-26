package com.mercadopago.sdk.android.checkout.presentation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.withFrameNanos
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mercadopago.sdk.android.checkout.core.model.MPCardType
import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.core.model.internal.buildProcessOrderParamsForMethodSelection
import com.mercadopago.sdk.android.checkout.core.model.internal.getOnEmailChangeRequested
import com.mercadopago.sdk.android.checkout.core.model.internal.getOrderId
import com.mercadopago.sdk.android.checkout.core.model.internal.startsWithPayment
import com.mercadopago.sdk.android.checkout.core.model.internal.toCheckoutType
import com.mercadopago.sdk.android.checkout.core.model.internal.toPaymentData
import com.mercadopago.sdk.android.checkout.domain.callback.CheckoutCallbackHolder
import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.checkout.domain.exception.ErrorCode
import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.MPUserCancelledContext
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.MethodSelectionScreenData
import com.mercadopago.sdk.android.checkout.domain.model.OrderProcessOutput
import com.mercadopago.sdk.android.checkout.domain.model.Screen
import com.mercadopago.sdk.android.checkout.domain.model.SelectionDisplayType
import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams
import com.mercadopago.sdk.android.checkout.presentation.cardpayment.CardPaymentScreen
import com.mercadopago.sdk.android.checkout.presentation.cvv.SecurityCodeScreen
import com.mercadopago.sdk.android.checkout.presentation.extensions.popBackStackToPayment
import com.mercadopago.sdk.android.checkout.presentation.extensions.rememberCheckoutGraphEntry
import com.mercadopago.sdk.android.checkout.presentation.extensions.toPlainAmountString
import com.mercadopago.sdk.android.checkout.presentation.installments.InstallmentsScreen
import com.mercadopago.sdk.android.checkout.presentation.loading.LoadingScreen
import com.mercadopago.sdk.android.checkout.presentation.methodselection.MethodSelectionScreenDestination
import com.mercadopago.sdk.android.checkout.presentation.paymentbrick.PaymentBrickScreen
import com.mercadopago.sdk.android.checkout.presentation.reviewconfirm.ReviewConfirmScreen
import com.mercadopago.sdk.android.checkout.presentation.state.CardPaymentViewEvent
import com.mercadopago.sdk.android.checkout.presentation.state.CheckoutDestination
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentViewEvent
import com.mercadopago.sdk.android.checkout.presentation.state.PaymentBrickViewEvent
import com.mercadopago.sdk.android.checkout.presentation.state.ReviewConfirmViewEvent
import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeScreenConfig
import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeViewEvent
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.CardPaymentViewModel
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.CheckoutCoordinatorViewModel
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.InstallmentsViewModel
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.PaymentBrickViewModel
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.ReviewConfirmViewModel
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.SecurityCodeViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Suppress("LongMethod", "CyclomaticComplexMethod")
@Composable
internal fun CheckoutController(
    checkoutConfiguration: CheckoutConfiguration?,
    navController: NavHostController = rememberNavController(),
) {
    val coordinatorViewModel: CheckoutCoordinatorViewModel = koinViewModel()
    val coordinatorState by coordinatorViewModel.state.collectAsState()
    val graphEntry = navController.rememberCheckoutGraphEntry()

    val startDestination: CheckoutDestination =
        if (checkoutConfiguration.startsWithPayment()) {
            CheckoutDestination.Payment
        } else {
            CheckoutDestination.Form
        }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        route = CheckoutGraph::class,
        enterTransition = { slideInHorizontally { it } },
        exitTransition = { slideOutHorizontally { it } },
    ) {
        composable<CheckoutDestination.Loading> {
            LoadingScreen()
            LaunchedEffect(Unit) {
                navController.navigate(CheckoutDestination.PaymentBrick) {
                    popUpTo(CheckoutDestination.Loading) { inclusive = true }
                }
            }
        }

        composable<CheckoutDestination.Payment> { _ ->
            val graphEntry = graphEntry ?: return@composable
            val paymentBrickViewModel: PaymentBrickViewModel = koinViewModel(
                viewModelStoreOwner = graphEntry,
            ) { parametersOf(checkoutConfiguration) }
            if (coordinatorState.reviewConfirmLoadFailed) {
                LaunchedEffect(Unit) {
                    paymentBrickViewModel.showLoadFailureMessage()
                    coordinatorViewModel.clearReviewConfirmLoadFailed()
                }
            }
            PaymentBrickScreenDestination(
                paymentBrickViewModel = paymentBrickViewModel,
                onNavigateToForm = { navController.navigate(CheckoutDestination.Form) },
                onNavigateToSecurityCode = { config ->
                    coordinatorViewModel.setSecurityCodeConfig(config)
                    navController.navigate(CheckoutDestination.SecurityCode)
                },
                onNavigateToReviewConfirm = { params ->
                    coordinatorViewModel.setReviewConfirmParams(params)
                    navController.navigate(CheckoutDestination.ReviewConfirm)
                },
                onNavigateToOfflineSelector = { screenData ->
                    coordinatorViewModel.setMethodSelectionData(screenData)
                    navController.navigate(CheckoutDestination.OfflineMethodSelector)
                },
            )
        }

        composable<CheckoutDestination.SecurityCode> { _ ->
            val config = coordinatorState.securityCodeConfig ?: return@composable
            val graphEntry = graphEntry ?: return@composable
            val paymentBrickViewModel: PaymentBrickViewModel = koinViewModel(
                viewModelStoreOwner = graphEntry,
            ) { parametersOf(checkoutConfiguration) }
            LaunchedEffect(Unit) {
                paymentBrickViewModel.markScreenPresented(Screen.SECURITY_CODE)
            }
            SecurityCodeScreenDestination(
                config = config,
                onTokenSuccess = { cardId, token ->
                    navController.popBackStack()
                    paymentBrickViewModel.processOrder(cardId = cardId, token = token)
                },
                onTokenError = {
                    navController.popBackStack()
                    paymentBrickViewModel.onTokenError()
                },
                onUserCancelled = {
                    navController.popBackStack()
                },
            )
        }

        composable<CheckoutDestination.Form> { _ ->
            val graphEntry = graphEntry ?: return@composable
            val cardPaymentViewModel: CardPaymentViewModel = koinViewModel(
                viewModelStoreOwner = graphEntry,
            ) { parametersOf(checkoutConfiguration) }
            CardFormScreenDestination(
                cardPaymentViewModel = cardPaymentViewModel,
                onNavigateToInstallments = { installmentData, paymentData ->
                    coordinatorViewModel.setInstallmentData(installmentData, paymentData)
                    navController.navigate(CheckoutDestination.Installment)
                },
                onNavigateToReviewConfirm = { params ->
                    coordinatorViewModel.setReviewConfirmParams(params)
                    navController.navigate(CheckoutDestination.ReviewConfirm)
                },
            )
        }

        composable<CheckoutDestination.ReviewConfirm> {
            val params = coordinatorState.reviewConfirmParams ?: run {
                navController.popBackStack()
                return@composable
            }
            ReviewConfirmScreenDestination(
                params = params,
                checkoutConfiguration = checkoutConfiguration,
                onBackClick = {
                    coordinatorViewModel.clearReviewConfirmParams()
                    navController.popBackStack()
                },
                onDismiss = {
                    coordinatorViewModel.clearReviewConfirmParams()
                    navController.popBackStack()
                },
                onNavigateBack = {
                    coordinatorViewModel.clearReviewConfirmParams()
                    navController.popBackStack()
                },
                onNavigateToPayment = {
                    coordinatorViewModel.clearReviewConfirmParams()
                    coordinatorViewModel.notifyReviewConfirmLoadFailed()
                    navController.popBackStackToPayment()
                },
            )
        }

        composable<CheckoutDestination.Installment> { _ ->
            val installmentData = coordinatorState.installmentData ?: return@composable
            val paymentData = coordinatorState.paymentData ?: return@composable
            val graphEntry = graphEntry ?: return@composable
            val cardPaymentViewModel: CardPaymentViewModel = koinViewModel(
                viewModelStoreOwner = graphEntry,
            ) { parametersOf(checkoutConfiguration) }
            val cardPaymentViewState by cardPaymentViewModel.viewState.collectAsState()
            val cardPaymentViewEvent by cardPaymentViewModel.viewEvent.collectAsState()

            LaunchedEffect(cardPaymentViewEvent) {
                when (val event = cardPaymentViewEvent) {
                    is CardPaymentViewEvent.OnPaymentConfirmed -> {
                        cardPaymentViewModel.onViewEventConsumed()
                        cardPaymentViewModel.markScreenPresented(Screen.CARD_FORM)
                        coordinatorViewModel.setReviewConfirmParams(event.params)
                        navController.navigate(CheckoutDestination.ReviewConfirm)
                    }
                    else -> Unit
                }
            }

            InstallmentsScreenDestination(
                installmentData = installmentData,
                paymentData = paymentData,
                checkoutType = checkoutConfiguration.toCheckoutType(),
                orderId = checkoutConfiguration?.getOrderId().orEmpty(),
                onInstallmentConfirmed = { installments ->
                    cardPaymentViewModel.onInstallmentConfirmed(installments)
                },
                isLoading = cardPaymentViewState.isLoading,
                onBackClick = { navController.popBackStack() },
            )
        }

        composable<CheckoutDestination.OfflineMethodSelector> { _ ->
            val screenData = coordinatorState.methodSelectionData ?: return@composable
            val graphEntry = graphEntry ?: return@composable
            val paymentBrickViewModel: PaymentBrickViewModel = koinViewModel(
                viewModelStoreOwner = graphEntry,
            ) { parametersOf(checkoutConfiguration) }
            MethodSelectionScreenDestination(
                screenData = screenData,
                onOptionSelected = { option ->
                    coordinatorState.methodSelectionData?.let { currentData ->
                        val totalAmount = currentData.footer?.totalAmount.orEmpty().toPlainAmountString()
                        checkoutConfiguration.buildProcessOrderParamsForMethodSelection(
                            option = option,
                            amount = totalAmount,
                        )?.let { params ->
                            if (currentData.selectionType == SelectionDisplayType.Chevron) {
                                coordinatorViewModel.setReviewConfirmParams(params)
                                navController.navigate(CheckoutDestination.ReviewConfirm)
                            } else {
                                paymentBrickViewModel.processOrder(params)
                            }
                        }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }
    }
}

@Composable
private fun PaymentBrickScreenDestination(
    paymentBrickViewModel: PaymentBrickViewModel,
    onNavigateToForm: () -> Unit,
    onNavigateToSecurityCode: (SecurityCodeScreenConfig) -> Unit,
    onNavigateToReviewConfirm: (ProcessOrderParams) -> Unit,
    onNavigateToOfflineSelector: (MethodSelectionScreenData) -> Unit,
) {
    val viewEvent by paymentBrickViewModel.viewEvent.collectAsState()

    LaunchedEffect(viewEvent) {
        when (val event = viewEvent) {
            is PaymentBrickViewEvent.OnOptionSelected -> {
                onNavigateToForm()
                paymentBrickViewModel.onViewEventConsumed()
            }

            is PaymentBrickViewEvent.OnSecurityCodeRequired -> {
                paymentBrickViewModel.onViewEventConsumed()
                onNavigateToSecurityCode(event.config)
            }

            is PaymentBrickViewEvent.OnPaymentReadyForReview -> {
                paymentBrickViewModel.onViewEventConsumed()
                withFrameNanos { }
                onNavigateToReviewConfirm(event.params)
            }

            is PaymentBrickViewEvent.OnFailure -> {
                paymentBrickViewModel.onViewEventConsumed()
                CheckoutCallbackHolder.notify(MercadoPagoCheckoutResult.Error(event.error))
            }

            is PaymentBrickViewEvent.OnUserCancelled -> {
                paymentBrickViewModel.onViewEventConsumed()
                CheckoutCallbackHolder.notify(MercadoPagoCheckoutResult.UserCancelled(event.context))
            }

            is PaymentBrickViewEvent.OnOfflineMethodSelected -> {
                paymentBrickViewModel.onViewEventConsumed()
                paymentBrickViewModel.markScreenPresented(Screen.OFFLINE_METHOD_SELECTOR)
                onNavigateToOfflineSelector(event.screenData)
            }

            null -> Unit
        }
    }

    PaymentBrickScreen(viewModel = paymentBrickViewModel)
}

@Composable
private fun SecurityCodeScreenDestination(
    config: SecurityCodeScreenConfig,
    onTokenSuccess: (cardId: String, token: String) -> Unit,
    onTokenError: () -> Unit,
    onUserCancelled: () -> Unit,
) {
    val viewModel: SecurityCodeViewModel = koinViewModel { parametersOf(config) }
    val viewEvent by viewModel.viewEvent.collectAsState()

    LaunchedEffect(viewEvent) {
        when (val event = viewEvent) {
            is SecurityCodeViewEvent.OnTokenSuccess -> {
                viewModel.onViewEventConsumed()
                onTokenSuccess(event.cardId, event.token)
            }

            is SecurityCodeViewEvent.OnTokenError -> {
                viewModel.onViewEventConsumed()
                onTokenError()
            }

            is SecurityCodeViewEvent.OnUserCancelled -> {
                viewModel.onViewEventConsumed()
                onUserCancelled()
            }

            null -> Unit
        }
    }

    SecurityCodeScreen(viewModel = viewModel)
}

@Composable
private fun CardFormScreenDestination(
    cardPaymentViewModel: CardPaymentViewModel,
    onNavigateToInstallments: (MPInstallmentData, MPPaymentData) -> Unit,
    onNavigateToReviewConfirm: (ProcessOrderParams) -> Unit = {},
) {
    val viewEvent by cardPaymentViewModel.viewEvent.collectAsState()

    LaunchedEffect(viewEvent) {
        when (val event = viewEvent) {
            is CardPaymentViewEvent.OnSuccess -> {
                cardPaymentViewModel.onViewEventConsumed()
                handleCardPaymentSuccess(event, cardPaymentViewModel, onNavigateToInstallments)
            }

            is CardPaymentViewEvent.OnPaymentConfirmed -> {
                cardPaymentViewModel.onViewEventConsumed()
                cardPaymentViewModel.markScreenPresented(Screen.CARD_FORM)
                onNavigateToReviewConfirm(event.params)
            }

            is CardPaymentViewEvent.OnFailure -> {
                cardPaymentViewModel.onViewEventConsumed()
                CheckoutCallbackHolder.notify(MercadoPagoCheckoutResult.Error(event.error))
            }

            is CardPaymentViewEvent.OnUserCancelled -> {
                cardPaymentViewModel.onViewEventConsumed()
                CheckoutCallbackHolder.notify(MercadoPagoCheckoutResult.UserCancelled(event.context))
            }

            is CardPaymentViewEvent.OnBackPressed -> {
                cardPaymentViewModel.onViewEventConsumed()
                CheckoutCallbackHolder.notify(MercadoPagoCheckoutResult.UserCancelled(event.context))
            }

            null -> Unit
        }
    }

    CardPaymentScreen(viewModel = cardPaymentViewModel)
}

private fun handleCardPaymentSuccess(
    event: CardPaymentViewEvent.OnSuccess,
    viewModel: CardPaymentViewModel,
    onNavigateToInstallments: (MPInstallmentData, MPPaymentData) -> Unit,
) {
    when {
        event.payment is MPPaymentData.CardTransaction &&
            MPCardType.fromString(event.payment.paymentTypeId) == MPCardType.CREDIT &&
            event.installment.quotas.isNotEmpty() -> {
            viewModel.markScreenPresented(Screen.INSTALLMENTS)
            onNavigateToInstallments(event.installment, event.payment)
        }
        event.payment is MPPaymentData.CardTransaction &&
            event.installment.quotas.size == 1 ->
            viewModel.onInstallmentConfirmed(
                event.installment.quotas.first().installments ?: 1,
            )
        event.installment.quotas.isNotEmpty() -> {
            viewModel.markScreenPresented(Screen.INSTALLMENTS)
            onNavigateToInstallments(event.installment, event.payment)
        }
        event.payment is MPPaymentData.CardSave ->
            CheckoutCallbackHolder.notify(MercadoPagoCheckoutResult.Success(event.payment))
        else ->
            viewModel.onInvalidInstallmentData(
                MercadoPagoCheckoutError.UnknownError(
                    code = ErrorCode.UNKNOWN,
                    messageError = "CardTransaction received with no installment quotas",
                    localized = "checkout",
                    throwable = null,
                ),
            )
    }
}

@Suppress("LongParameterList")
@Composable
private fun ReviewConfirmScreenDestination(
    params: ProcessOrderParams,
    checkoutConfiguration: CheckoutConfiguration?,
    onBackClick: () -> Unit,
    onDismiss: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToPayment: () -> Unit,
) {
    val viewModel: ReviewConfirmViewModel = koinViewModel {
        parametersOf(params, checkoutConfiguration)
    }
    val viewEvent by viewModel.viewEvent.collectAsState()

    LaunchedEffect(viewEvent) {
        val event = viewEvent ?: return@LaunchedEffect
        viewModel.onViewEventConsumed()
        when (event) {
            is ReviewConfirmViewEvent.OnPaymentSuccess -> {
                onDismiss()
                notifyReviewConfirmPaymentSuccess(checkoutConfiguration, params, event.output)
            }
            is ReviewConfirmViewEvent.OnPaymentError -> {
                onDismiss()
                CheckoutCallbackHolder.notify(MercadoPagoCheckoutResult.Error(event.error))
            }
            is ReviewConfirmViewEvent.OnLoadFailure -> {
                if (checkoutConfiguration.startsWithPayment()) {
                    onNavigateToPayment()
                } else {
                    onDismiss()
                    CheckoutCallbackHolder.notify(MercadoPagoCheckoutResult.Error(event.error))
                }
            }
            is ReviewConfirmViewEvent.OnModifyPaymentMethod -> {
                val checkoutType = checkoutConfiguration?.checkoutType
                when {
                    checkoutType is MPCheckoutType.CardTransaction -> {
                        onNavigateBack()
                        CheckoutCallbackHolder.notify(
                            MercadoPagoCheckoutResult.UserCancelled(
                                MPUserCancelledContext.Payment(screens = listOf(Screen.REVIEW_AND_CONFIRM)),
                            ),
                        )
                    }
                    checkoutType is MPCheckoutType.Payment -> {
                        onDismiss()
                    }
                    else -> onDismiss()
                }
            }
            is ReviewConfirmViewEvent.OnModifyEmail -> {
                checkoutConfiguration.getOnEmailChangeRequested()?.invoke()
                onDismiss()
            }
        }
    }

    ReviewConfirmScreen(
        viewModel = viewModel,
        onBackClick = onBackClick,
    )
}

private fun notifyReviewConfirmPaymentSuccess(
    checkoutConfiguration: CheckoutConfiguration?,
    params: ProcessOrderParams,
    output: OrderProcessOutput,
) {
    CheckoutCallbackHolder.notify(
        MercadoPagoCheckoutResult.Success(output.toPaymentData(checkoutConfiguration, params)),
    )
}

@Suppress("LongParameterList")
@Composable
private fun InstallmentsScreenDestination(
    installmentData: MPInstallmentData,
    paymentData: MPPaymentData,
    checkoutType: String,
    orderId: String,
    onInstallmentConfirmed: (Int) -> Unit,
    isLoading: Boolean,
    onBackClick: () -> Unit,
) {
    val installmentsViewModel: InstallmentsViewModel = koinViewModel {
        parametersOf(installmentData, paymentData, checkoutType, orderId)
    }
    val viewEvent by installmentsViewModel.viewEvent.collectAsState()

    LaunchedEffect(viewEvent) {
        when (val event = viewEvent) {
            is InstallmentViewEvent.OnSuccess -> {
                installmentsViewModel.onViewEventConsumed()
                when (paymentData) {
                    is MPPaymentData.CardTransaction -> onInstallmentConfirmed(event.installment)
                    is MPPaymentData.CardSave -> {
                        CheckoutCallbackHolder.notify(MercadoPagoCheckoutResult.Success(paymentData))
                    }
                    is MPPaymentData.Payment -> onInstallmentConfirmed(event.installment)
                }
            }

            is InstallmentViewEvent.OnFailure -> {
                installmentsViewModel.onViewEventConsumed()
                CheckoutCallbackHolder.notify(MercadoPagoCheckoutResult.Error(event.error))
            }

            is InstallmentViewEvent.OnUserCancelled -> {
                installmentsViewModel.onViewEventConsumed()
                CheckoutCallbackHolder.notify(MercadoPagoCheckoutResult.UserCancelled(event.context))
            }

            null -> Unit
        }
    }

    InstallmentsScreen(
        viewModel = installmentsViewModel,
        isLoading = isLoading,
        onBackClick = {
            installmentsViewModel.onBackPressed()
            onBackClick()
        },
    )
}
