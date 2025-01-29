package com.mercadopago.sdk.android.coremethods.ui.components.textfield.securityfield

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.StateRestorationTester
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.PCIFieldRobot
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCITextFieldTestTags
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.rememberPCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.securitycode.SecurityCodeFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.securitycode.SecurityCodeTextField

internal class SecurityCodeTextFieldRobot(
    composeRule: ComposeContentTestRule,
) : PCIFieldRobot(composeRule) {
    fun createSecurityField(
        enabled: Boolean = true,
        readOnly: Boolean = false,
        onEvent: (SecurityCodeFieldEvent) -> Unit = {},
        decorationBox: @Composable (innerTextField: @Composable () -> Unit) -> Unit =
            @Composable { innerTextField -> innerTextField() },
        stateRestorationTester: StateRestorationTester? = null,
    ) {
        testTag = PCITextFieldTestTags.SecurityCode.tag
        setContent(stateRestorationTester) {
            fieldState = rememberPCIFieldState()
            MaterialTheme {
                SecurityCodeTextField(
                    state = fieldState,
                    onEvent = onEvent,
                    enabled = enabled,
                    readOnly = readOnly,
                    decorationBox = decorationBox,
                )
            }
        }
    }
}

internal fun securityFieldRobot(
    composeTestRule: ComposeContentTestRule,
    block: SecurityCodeTextFieldRobot.() -> Unit,
) = SecurityCodeTextFieldRobot(composeTestRule).apply(block)
