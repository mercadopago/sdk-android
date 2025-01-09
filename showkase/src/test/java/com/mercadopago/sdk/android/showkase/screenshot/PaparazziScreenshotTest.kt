package com.mercadopago.sdk.android.showkase.screenshot

import androidx.activity.OnBackPressedDispatcher
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.Density
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.airbnb.android.showkase.models.Showkase
import com.android.ide.common.rendering.api.SessionParams.RenderingMode
import com.google.testing.junit.testparameterinjector.TestParameter
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import com.mercadopago.sdk.android.showkase.getMetadata
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class PaparazziScreenshotTest {

    object PreviewProvider : com.google.testing.junit.testparameterinjector.TestParameterValuesProvider() {
        override fun provideValues(context: Context?): List<ShowkaseTestPreview> {
            val metadata = Showkase.getMetadata()
            val components = metadata.componentList.map(::ShowkaseComponentTestPreview)
            val colors = metadata.colorList.map(::ShowkaseColorTestPreview)
            val typography = metadata.typographyList.map(::ShowkaseTypographyTestPreview)

            return components + colors + typography
        }
    }

    enum class BaseDeviceConfig(
        val deviceConfig: DeviceConfig,
    ) {
        NEXUS_5(DeviceConfig.NEXUS_5),
    }

    @get:Rule
    val paparazzi = Paparazzi(
        maxPercentDifference = 0.01,
        deviceConfig = BaseDeviceConfig.NEXUS_5.deviceConfig.copy(
            softButtons = false,
            screenHeight = 1,
        ),
        renderingMode = RenderingMode.SHRINK,
    )

    @Test
    fun snapshot(
        @TestParameter(valuesProvider = PreviewProvider::class) componentTestPreview: ShowkaseTestPreview,
        @TestParameter baseDeviceConfig: BaseDeviceConfig,
        @TestParameter(value = ["1.0", "1.5"]) fontScale: Float,
    ) {
        val densityScale = baseDeviceConfig.deviceConfig.density.dpiValue / 160f
        paparazzi.unsafeUpdateConfig(
            baseDeviceConfig.deviceConfig.copy(
                screenHeight = componentTestPreview.height?.let {
                    it * densityScale
                }?.toInt() ?: baseDeviceConfig.deviceConfig.screenHeight,
                screenWidth = componentTestPreview.width?.let {
                    it * densityScale
                }?.toInt() ?: baseDeviceConfig.deviceConfig.screenWidth,
            )
        )
        paparazzi.snapshot(name = "") {
            val lifecycleOwner = LocalLifecycleOwner.current
            CompositionLocalProvider(
                LocalInspectionMode provides true,
                LocalDensity provides Density(
                    density = LocalDensity.current.density,
                    fontScale = fontScale
                ),

                // Needed so that UI that uses it don't crash during screenshot tests
                LocalOnBackPressedDispatcherOwner provides object : OnBackPressedDispatcherOwner {
                    override val lifecycle: Lifecycle
                        get() = lifecycleOwner.lifecycle
                    override val onBackPressedDispatcher: OnBackPressedDispatcher
                        get() = OnBackPressedDispatcher()
                }
            ) {
                componentTestPreview.Content()
            }
        }
    }
}
