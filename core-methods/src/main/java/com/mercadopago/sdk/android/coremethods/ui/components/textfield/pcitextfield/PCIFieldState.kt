package com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

/**
 * This class holds the input data of secure fields. It makes sure the fields are PCI Compliant.
 * Use the [rememberPCIFieldState] to create a new instance of this class on compose.
 */
@Stable
class PCIFieldState internal constructor() {
    internal var input: String by mutableStateOf("")

    companion object {
        internal val Saver: Saver<PCIFieldState, String> = Saver<PCIFieldState, String>(
            save = { it.input },
            restore = { restored ->
                PCIFieldState().apply {
                    input = restored
                }
            },
        )
    }
}

/**
 * Create a new instance of PCIFieldState that lives as long as the composition. This is used for all PCI Fields.
 * You should have one for each field.
 */
@Composable
fun rememberPCIFieldState(): PCIFieldState {
    return rememberSaveable<PCIFieldState>(saver = PCIFieldState.Saver) {
        PCIFieldState()
    }
}
