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
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.core.model.internal.getOrderId
import com.mercadopago.sdk.android.checkout.core.model.internal.startsWithPayment
import com.mercadopago.sdk.android.checkout.core.model.internal.toCheckoutType
import com.mercadopago.sdk.android.checkout.domain.callback.CheckoutCallbackHolder
import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.Screen
import com.mercadopago.sdk.android.checkout.domain.usecase.ValidateCVUseCase
import com.mercadopago.sdk.android.checkout.presentation.cardpayment.CardPaymentScreen
import com.mercadopago.sdk.android.checkout.presentation.cvv.CVVScreen
import com.mercadopago.sdk.android.checkout.presentation.installments.InstallmentsScreen
import com.mercadopago.sdk.android.checkout.presentation.loading.LoadingScreen
import com.mercadopago.sdk.android.checkout.presentation.paymentbrick.PaymentBrickScreen
import com.mercadopago.sdk.android.checkout.presentation.state.CardPaymentViewEvent
import com.mercadopago.sdk.android.checkout.presentation.state.CheckoutDestination
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentViewEvent
import com.mercadopago.sdk.android.checkout.presentation.state.PaymentBrickViewEvent
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.CVVViewModel
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.CardPaymentViewModel
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.InstallmentsViewModel
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.PaymentBrickViewModel
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.rememberPCIFieldState
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
    var pendingCVVEvent by remember { mutableStateOf<PaymentBrickViewEvent.NavigateToCVV?>(null) }
    var pendingPaymentBrickViewModel by remember { mutableStateOf<PaymentBrickViewModel?>(null) }

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

        composable<CheckoutDestination.Payment> {
            PaymentBrickScreenDestination(
                checkoutConfiguration = checkoutConfiguration,
                onNavigateToForm = { navController.navigate(CheckoutDestination.Form) },
                onNavigateToCVV = { event, vm ->
                    pendingCVVEvent = event
                    pendingPaymentBrickViewModel = vm
                    navController.navigate(CheckoutDestination.CVV)
                },
                onNavigateToInstallments = { event ->
                    pendingInstallmentData = event.installmentData
                    pendingPaymentData = MPPaymentData.Payment(
                        orderId = "",
                        orderStatus = "",
                        transactionAmount = null,
                        paymentMethodId = "",
                        paymentTypeId = "",
                        payer = null,
                        installment = null,
                        issuerId = event.optionId,
                    )
                    navController.navigate(CheckoutDestination.Installment)
                },
            )
        }

        composable<CheckoutDestination.CVV> {
            val cvvEvent = pendingCVVEvent ?: return@composable
            val paymentBrickVm = pendingPaymentBrickViewModel
            CVVScreenDestination(
                cvvEvent = cvvEvent,
                onCVVConfirmed = { securityCodeState ->
                    if (paymentBrickVm != null) {
                        // A28: tokenize CVV then route to installments or process
                        paymentBrickVm.processPaymentMethodWithTokenization(
                            optionId = cvvEvent.optionId,
                            securityCodeState = securityCodeState,
                        )
                        navController.popBackStack()
                    }
                },
                onBackPressed = { navController.popBackStack() },
            )
        }

        composable<CheckoutDestination.Form> {
            val graphEntry = remember { navController.getBackStackEntry<CheckoutGraph>() }
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

        composable<CheckoutDestination.Installment> {
            val installmentData = pendingInstallmentData ?: return@composable
            val paymentData = pendingPaymentData ?: return@composable
            val graphEntry = remember { navController.getBackStackEntry<CheckoutGraph>() }
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
    }
}

@Composable
private fun PaymentBrickScreenDestination(
    checkoutConfiguration: CheckoutConfiguration?,
    onNavigateToForm: () -> Unit,
    onNavigateToCVV: (PaymentBrickViewEvent.NavigateToCVV, PaymentBrickViewModel) -> Unit,
    onNavigateToInstallments: (PaymentBrickViewEvent.NavigateToInstallmentsFromCard) -> Unit,
) {
    val paymentBrickViewModel: PaymentBrickViewModel = koinViewModel {
        parametersOf(checkoutConfiguration)
    }
    val viewEvent by paymentBrickViewModel.viewEvent.collectAsState()

    LaunchedEffect(viewEvent) {
        when (val event = viewEvent) {
            is PaymentBrickViewEvent.OnOptionSelected -> {
                onNavigateToForm()
                paymentBrickViewModel.onViewEventConsumed()
            }

            is PaymentBrickViewEvent.NavigateToCVV -> {
                onNavigateToCVV(event, paymentBrickViewModel)
                paymentBrickViewModel.onViewEventConsumed()
            }

            is PaymentBrickViewEvent.NavigateToOfflineSelector -> {
                // TODO(A22): navigate to offline method selector screen with event.options
                paymentBrickViewModel.onViewEventConsumed()
            }

            is PaymentBrickViewEvent.NavigateToInstallmentsFromCard -> {
                onNavigateToInstallments(event)
                paymentBrickViewModel.onViewEventConsumed()
            }

            null -> Unit
        }
    }

    PaymentBrickScreen(viewModel = paymentBrickViewModel)
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
                when {
                    event.payment is MPPaymentData.CardTransaction &&
                        event.installment.quotas.size == 1 ->
                        cardPaymentViewModel.onInstallmentConfirmed(
                            event.installment.quotas.first().installments ?: 1,
                        )
                    event.installment.quotas.isNotEmpty() -> {
                        cardPaymentViewModel.markScreenPresented(Screen.INSTALLMENTS)
                        onNavigateToInstallments(event.installment, event.payment)
                    }
                    else ->
                        CheckoutCallbackHolder.notify(MercadoPagoCheckoutResult.Success(event.payment))
                }
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

@Composable
private fun CVVScreenDestination(
    cvvEvent: PaymentBrickViewEvent.NavigateToCVV,
    onCVVConfirmed: (PCIFieldState) -> Unit,
    onBackPressed: () -> Unit,
) {
    val cvvViewModel = remember(cvvEvent) {
        CVVViewModel(
            securityCodeScreen = cvvEvent.securityCodeScreen,
            cvvExpectedLength = cvvEvent.cvvExpectedLength,
            validateCVUseCase = ValidateCVUseCase(),
        )
    }
    val cvvPCIState = rememberPCIFieldState()
    CVVScreen(
        viewModel = cvvViewModel,
        cvvPCIState = cvvPCIState,
        onBackPressed = onBackPressed,
        onConfirm = { onCVVConfirmed(cvvPCIState) },
    )
}
