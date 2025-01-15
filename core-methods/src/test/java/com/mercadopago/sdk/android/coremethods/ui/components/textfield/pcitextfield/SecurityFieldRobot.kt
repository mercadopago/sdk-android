package com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.StateRestorationTester
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.securitycode.SecurityCode
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.securitycode.SecurityCodeFieldEvent

internal class SecurityFieldRobot(
    composeRule: ComposeContentTestRule
) : PCITextFieldRobot(composeRule) {

    fun createSecurityField(
        enabled: Boolean = true,
        readOnly: Boolean = false,
        onEvent: (SecurityCodeFieldEvent) -> Unit = {},
        decorationBox: @Composable (innerTextField: @Composable () -> Unit) -> Unit =
            @Composable { innerTextField -> innerTextField() },
        stateRestorationTester: StateRestorationTester? = null,
    ) {
        setContent(stateRestorationTester) {
            fieldState = rememberPCIFieldState()
            MaterialTheme {
                SecurityCode(
                    state = fieldState,
                    onEvent = onEvent,
                    enabled = enabled,
                    readOnly = readOnly,
                    decorationBox = decorationBox
                )
            }
        }
    }
}

internal fun securityFieldRobot(
    composeTestRule: ComposeContentTestRule,
    block: SecurityFieldRobot.() -> Unit
) = SecurityFieldRobot(composeTestRule).apply(block)
