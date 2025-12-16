package com.mercadopago.sdk.android.threeds.adapter.mappers

import com.mercadopago.sdk.android.coremethods.domain.provider.models.ThreeDSSeverity
import com.mercadopago.sdk.android.coremethods.domain.provider.models.ThreeDSWarning
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSSeverity
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSWarning

/**
 * Mapper to convert between core-methods ThreeDSWarning and threeds MPThreeDSWarning.
 * This allows the adapter to translate between the two module's domain models.
 */
internal object ThreeDSWarningMapper {
    /**
     * Converts MPThreeDSWarning (threeds model) to ThreeDSWarning (core-methods model).
     *
     * @param mpWarning The MPThreeDSWarning to convert
     * @return The converted ThreeDSWarning
     */
    fun toThreeDSWarning(mpWarning: MPThreeDSWarning): ThreeDSWarning {
        return ThreeDSWarning(
            id = mpWarning.id,
            message = mpWarning.message,
            severity = toThreeDSSeverity(mpWarning.severity),
        )
    }

    /**
     * Converts ThreeDSWarning (core-methods model) to MPThreeDSWarning (threeds model).
     *
     * @param warning The ThreeDSWarning to convert
     * @return The converted MPThreeDSWarning
     */
    fun toMPThreeDSWarning(warning: ThreeDSWarning): MPThreeDSWarning {
        return MPThreeDSWarning(
            id = warning.id,
            message = warning.message,
            severity = toMPThreeDSSeverity(warning.severity),
        )
    }

    /**
     * Converts a list of MPThreeDSWarning to a list of ThreeDSWarning.
     *
     * @param mpWarnings The list of MPThreeDSWarning to convert
     * @return The converted list of ThreeDSWarning
     */
    fun toThreeDSWarningList(mpWarnings: List<MPThreeDSWarning>): List<ThreeDSWarning> {
        return mpWarnings.map { toThreeDSWarning(it) }
    }

    /**
     * Converts a list of ThreeDSWarning to a list of MPThreeDSWarning.
     *
     * @param warnings The list of ThreeDSWarning to convert
     * @return The converted list of MPThreeDSWarning
     */
    fun toMPThreeDSWarningList(warnings: List<ThreeDSWarning>): List<MPThreeDSWarning> {
        return warnings.map { toMPThreeDSWarning(it) }
    }

    private fun toThreeDSSeverity(mpSeverity: MPThreeDSSeverity): ThreeDSSeverity {
        return when (mpSeverity) {
            MPThreeDSSeverity.LOW -> ThreeDSSeverity.LOW
            MPThreeDSSeverity.MEDIUM -> ThreeDSSeverity.MEDIUM
            MPThreeDSSeverity.HIGH -> ThreeDSSeverity.HIGH
            MPThreeDSSeverity.NONE -> ThreeDSSeverity.NONE
        }
    }

    private fun toMPThreeDSSeverity(severity: ThreeDSSeverity): MPThreeDSSeverity {
        return when (severity) {
            ThreeDSSeverity.LOW -> MPThreeDSSeverity.LOW
            ThreeDSSeverity.MEDIUM -> MPThreeDSSeverity.MEDIUM
            ThreeDSSeverity.HIGH -> MPThreeDSSeverity.HIGH
            ThreeDSSeverity.NONE -> MPThreeDSSeverity.NONE
        }
    }
}
