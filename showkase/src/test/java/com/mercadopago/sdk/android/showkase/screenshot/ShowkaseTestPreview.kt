package com.mercadopago.sdk.android.showkase.screenshot

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.android.showkase.models.ShowkaseBrowserColor
import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
import com.airbnb.android.showkase.ui.padding4x
import java.util.Locale

interface ShowkaseTestPreview {

    @Composable
    fun Content()

    val height: Int?
    val width: Int?
}

class ShowkaseComponentTestPreview(
    private val showkaseBrowserComponent: ShowkaseBrowserComponent
) : ShowkaseTestPreview {
    @Composable
    override fun Content() = showkaseBrowserComponent.component()
    override fun toString(): String = showkaseBrowserComponent
        .componentKey
        .removeRange(
            startIndex = 0,
            endIndex = showkaseBrowserComponent.componentKey.indexOf(string = "_", startIndex = 1) + 1
        )
        .replace("_null", "")
    override val height: Int? = showkaseBrowserComponent.heightDp
    override val width: Int? = showkaseBrowserComponent.widthDp
}

class ShowkaseColorTestPreview(
    private val showkaseBrowserColor: ShowkaseBrowserColor
) : ShowkaseTestPreview {
    @Composable
    override fun Content() {
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(showkaseBrowserColor.color)
        )
    }

    override val height: Int? = null
    override val width: Int? = null

    override fun toString(): String = "${showkaseBrowserColor.colorGroup}_${showkaseBrowserColor.colorName}"
}

class ShowkaseTypographyTestPreview(
    private val showkaseBrowserTypography: ShowkaseBrowserTypography
) : ShowkaseTestPreview {
    @Composable
    override fun Content() {
        BasicText(
            text = showkaseBrowserTypography.typographyName.replaceFirstChar {
                it.titlecase(Locale.getDefault())
            },
            modifier = Modifier.padding(padding4x),
            style = showkaseBrowserTypography.textStyle
        )
    }

    override val height: Int? = null
    override val width: Int? = null

    override fun toString(): String =
        "${showkaseBrowserTypography.typographyGroup}_${showkaseBrowserTypography.typographyName}"
}
