package com.mercadopago.sdk.android.example.presentation.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mercadopago.sdk.android.example.R
import com.mercadopago.sdk.android.example.navigation.SampleDestination
import com.mercadopago.sdk.android.example.navigation.SampleFeaturesNavigationList
import com.mercadopago.sdk.android.example.navigation.isRoute
import com.mercadopago.sdk.android.example.presentation.theme.MercadoPagoSampleTheme

@Composable
internal fun SampleHomeScreen(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val destination = currentBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            TopBar(
                destination = destination,
                onNavigateUp = navController::navigateUp,
            )
        },
    ) { paddingValues ->
        SampleNavHost(
            navController = navController,
            modifier = modifier.padding(paddingValues),
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TopBar(
    destination: String?,
    onNavigateUp: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(
                    id = SampleFeaturesNavigationList.find {
                        it.destination.isRoute(destination)
                    }?.title ?: R.string.app_name,
                )
            )
        },
        navigationIcon = {
            if (SampleDestination.Home.isRoute(destination).not()) {
                IconButton(onClick = onNavigateUp) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_back),
                        contentDescription = null,
                    )
                }
            }
        }
    )
}

@Preview
@Composable
fun TopBarPreview() {
    MercadoPagoSampleTheme {
        TopBar(
            destination = SampleDestination.SDKInstance.toString(),
            onNavigateUp = { },
        )
    }
}
