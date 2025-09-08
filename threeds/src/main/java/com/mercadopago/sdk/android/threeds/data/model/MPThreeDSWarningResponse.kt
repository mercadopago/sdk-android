package com.mercadopago.sdk.android.threeds.data.model

data class MPThreeDSWarningResponse(
    val id: String,
    val message: String,
    val severity: MPSeverityResponse
)

enum class MPSeverityResponse(grade: Int) {
    LOW(0),
    MEDIUM(1),
    HIGH(2),
    NONE(3), ;

    companion object {
        fun getWaningByGrade(grade: Int): MPSeverityResponse {
            return when (grade) {
                0 -> LOW
                1 -> MEDIUM
                2 -> HIGH
                else -> NONE
            }
        }
    }
}
