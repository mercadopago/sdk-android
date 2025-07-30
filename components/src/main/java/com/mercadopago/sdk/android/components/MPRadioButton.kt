package com.mercadopago.sdk.android.components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme

private const val RADIO_GROUP = "RadioButton"

/**
 * @param selected whether this radio button is selected or not
 * @param onClick called when this radio button is clicked. If `null`, then this radio button will
 *   not be interactable, unless something else handles its input events and updates its state.
 * @param modifier the [Modifier] to be applied to this radio button
 * @param enabled controls the enabled state of this radio button. When `false`, this component will
 *   not respond to user input, and it will appear visually disabled and disabled to accessibility
 *   services.
 * @param error controls if the components its showing a error
 * @param interactionSource an optional hoisted [MutableInteractionSource] for observing and
 *   emitting [Interaction]s for this radio button. You can use this to change the radio button's
 *   appearance or preview the radio button in different states. Note that if `null` is provided,
 *   interactions will still happen internally.
 */
@Composable
fun MPRadioButton(
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    error: Boolean = false,
    interactionSource: MutableInteractionSource? = null
) {
    RadioButton(
        selected = selected,
        onClick = onClick,
        modifier = modifier.size(MercadoPagoTheme.spacing.xl),
        enabled = enabled,
        colors = RadioButtonColors(
            selectedColor = if (error) MercadoPagoTheme.color.text.negative else MercadoPagoTheme.color.text.accent,
            unselectedColor = if (error) MercadoPagoTheme.color.text.negative else MercadoPagoTheme.color.text.secondary,
            disabledSelectedColor = MercadoPagoTheme.color.text.disabled,
            disabledUnselectedColor = MercadoPagoTheme.color.text.disabled
        ),
        interactionSource = interactionSource
    )
}

@Preview(name = "Radio Button", group = RADIO_GROUP)
@Composable
internal fun MPRadioButtonPreview() {
    MercadoPagoTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White),
        ) {
            Row {
                MPRadioButton(
                    selected = false,
                )
                MPRadioButton(
                    selected = true,
                )
            }

            Row {
                MPRadioButton(
                    selected = false,
                    enabled = false
                )
                MPRadioButton(
                    selected = true,
                    enabled = false
                )
            }

            Row {
                MPRadioButton(
                    selected = false,
                    error = true

                )
                MPRadioButton(
                    selected = true,
                    error = true
                )
            }
        }
    }
}
