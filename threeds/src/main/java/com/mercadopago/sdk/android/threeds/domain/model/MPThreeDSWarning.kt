package com.mercadopago.sdk.android.threeds.domain.model

data class MPThreeDSWarning(
    val id: String,
    val message: String,
    val severity: MPSeverity
)

enum class MPSeverity(grade: Int) {
    LOW(0),
    MEDIUM(1),
    HIGH(2),
    NONE(3), ;

    companion object {
        fun getWaningByGrade(grade: Int): MPSeverity {
            return when (grade) {
                0 -> LOW
                1 -> MEDIUM
                2 -> HIGH
                else -> NONE
            }
        }
    }
}
