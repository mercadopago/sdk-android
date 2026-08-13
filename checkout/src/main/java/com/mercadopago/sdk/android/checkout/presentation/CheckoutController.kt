package com.mercadopago.sdk.android.checkout.presentation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mercadopago.sdk.android.checkout.core.model.MPCardType
import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.core.model.internal.getOrderId
import com.mercadopago.sdk.android.checkout.core.model.internal.startsWithPayment
import com.mercadopago.sdk.android.checkout.core.model.internal.toCheckoutType
import com.mercadopago.sdk.android.checkout.domain.callback.CheckoutCallbackHolder
import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.checkout.domain.exception.ErrorCode
import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.MPUserCancelledContext
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.MethodSelectionScreenData
import com.mercadopago.sdk.android.checkout.domain.model.Screen
import com.mercadopago.sdk.android.checkout.domain.model.SelectionDisplayType
import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams
import com.mercadopago.sdk.android.checkout.presentation.cardpayment.CardPaymentScreen
import com.mercadopago.sdk.android.checkout.presentation.cvv.SecurityCodeScreen
import com.mercadopago.sdk.android.checkout.presentation.installments.InstallmentsScreen
import com.mercadopago.sdk.android.checkout.presentation.loading.LoadingScreen
import com.mercadopago.sdk.android.checkout.presentation.methodselection.MethodSelectionScreenDestination
import com.mercadopago.sdk.android.checkout.presentation.paymentbrick.PaymentBrickScreen
import com.mercadopago.sdk.android.checkout.presentation.state.CardPaymentViewEvent
import com.mercadopago.sdk.android.checkout.presentation.state.CheckoutDestination
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentViewEvent
import com.mercadopago.sdk.android.checkout.presentation.state.PaymentBrickViewEvent
import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeScreenConfig
import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeViewEvent
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.CardPaymentViewModel
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.InstallmentsViewModel
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.PaymentBrickViewModel
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.SecurityCodeViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Suppress("LongMethod")
@Composable
internal fun CheckoutController(
    checkoutConfiguration: CheckoutConfiguration?,
    navController: NavHostController = rememberNavController(),
) {
    var pendingInstallmentData by remember { mutableStateOf<MPInstallmentData?>(null) }
    var pendingPaymentData by remember { mutableStateOf<MPPaymentData?>(null) }
    var pendingSecurityCodeConfig by remember { mutableStateOf<SecurityCodeScreenConfig?>(null) }
    var pendingMethodSelectionData by remember { mutableStateOf<MethodSelectionScreenData?>(null) }

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

        composable<CheckoutDestination.Payment> { backStackEntry ->
            val graphEntry = remember(backStackEntry) { navController.getBackStackEntry<CheckoutGraph>() }
            val paymentBrickViewModel: PaymentBrickViewModel = koinViewModel(
                viewModelStoreOwner = graphEntry,
            ) { parametersOf(checkoutConfiguration) }
            PaymentBrickScreenDestination(
                paymentBrickViewModel = paymentBrickViewModel,
                onNavigateToForm = { navController.navigate(CheckoutDestination.Form) },
                onNavigateToSecurityCode = { config ->
                    pendingSecurityCodeConfig = config
                    navController.navigate(CheckoutDestination.SecurityCode)
                },
                onNavigateToOfflineSelector = { screenData ->
                    pendingMethodSelectionData = screenData
                    navController.navigate(CheckoutDestination.OfflineMethodSelector)
                },
            )
        }

        composable<CheckoutDestination.SecurityCode> { backStackEntry ->
            val config = pendingSecurityCodeConfig ?: return@composable
            val graphEntry = remember(backStackEntry) { navController.getBackStackEntry<CheckoutGraph>() }
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
                    paymentBrickViewModel.processPaymentMethodWithToken(cardId, token)
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

        composable<CheckoutDestination.Form> { backStackEntry ->
            val graphEntry = remember(backStackEntry) { navController.getBackStackEntry<CheckoutGraph>() }
            val cardPaymentViewModel: CardPaymentViewModel = koinViewModel(
                viewModelStoreOwner = graphEntry,
            ) { parametersOf(checkoutConfiguration) }
            CardFormScreenDestination(
                cardPaymentViewModel = cardPaymentViewModel,
                onNavigateToInstallments = { installmentData, paymentData ->
                    pendingInstallmentData = installmentData
                    pendingPaymentData = paymentData
                    navController.navigate(CheckoutDestination.Installment)
                },
            )
        }

        composable<CheckoutDestination.Installment> { backStackEntry ->
            val installmentData = pendingInstallmentData ?: return@composable
            val paymentData = pendingPaymentData ?: return@composable
            val graphEntry = remember(backStackEntry) { navController.getBackStackEntry<CheckoutGraph>() }
            val cardPaymentViewModel: CardPaymentViewModel = koinViewModel(
                viewModelStoreOwner = graphEntry,
            ) { parametersOf(checkoutConfiguration) }
            val cardPaymentViewState by cardPaymentViewModel.viewState.collectAsState()

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

        composable<CheckoutDestination.OfflineMethodSelector> { backStackEntry ->
            val screenData = pendingMethodSelectionData ?: return@composable
            val graphEntry = remember(backStackEntry) { navController.getBackStackEntry<CheckoutGraph>() }
            val paymentBrickViewModel: PaymentBrickViewModel = koinViewModel(
                viewModelStoreOwner = graphEntry,
            ) { parametersOf(checkoutConfiguration) }
            MethodSelectionScreenDestination(
                screenData = screenData,
                onOptionSelected = { option ->
                    val currentData = pendingMethodSelectionData ?: return@MethodSelectionScreenDestination
                    if (currentData.selectionType == SelectionDisplayType.Chevron) {
                        // ReviewConfirm destination not yet available (separate feature)
                    } else {
                        paymentBrickViewModel.processOrder(
                            ProcessOrderParams(
                                orderId = checkoutConfiguration?.toCheckoutType()?.let {
                                    (it as? MPCheckoutType.Payment)?.order?.orderId
                                }.orEmpty(),
                                clientToken = checkoutConfiguration?.toCheckoutType()?.let {
                                    (it as? MPCheckoutType.Payment)?.order?.clientToken
                                }.orEmpty(),
                                paymentMethodId = option.id,
                                paymentMethodType = "ticket",
                                token = "",
                                installments = 0,
                                amount = currentData.footer?.totalAmount.orEmpty(),
                            ),
                        )
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                    CheckoutCallbackHolder.notify(
                        MercadoPagoCheckoutResult.UserCancelled(
                            MPUserCancelledContext.Payment(
                                screens = listOf(Screen.OFFLINE_METHOD_SELECTOR),
                            ),
                        ),
                    )
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
) {
    val viewEvent by cardPaymentViewModel.viewEvent.collectAsState()

    LaunchedEffect(viewEvent) {
        when (val event = viewEvent) {
            is CardPaymentViewEvent.OnSuccess -> {
                cardPaymentViewModel.onViewEventConsumed()
                handleCardPaymentSuccess(event, cardPaymentViewModel, onNavigateToInstallments)
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
