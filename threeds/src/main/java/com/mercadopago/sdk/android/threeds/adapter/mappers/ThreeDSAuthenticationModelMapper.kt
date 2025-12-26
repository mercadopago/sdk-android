package com.mercadopago.sdk.android.threeds.adapter.mappers

import com.mercadopago.sdk.android.coremethods.domain.provider.models.ThreeDSAuthenticationModel
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSAuthenticationModel

/**
 * Mapper to convert between core-methods ThreeDSAuthenticationModel and threeds MPThreeDSAuthenticationModel.
 * This allows the adapter to translate between the two module's domain models.
 */
internal object ThreeDSAuthenticationModelMapper {
    /**
     * Converts MPThreeDSAuthenticationModel (threeds model) to ThreeDSAuthenticationModel (core-methods model).
     *
     * @param mpModel The MPThreeDSAuthenticationModel to convert
     * @return The converted ThreeDSAuthenticationModel
     */
    fun toThreeDSAuthenticationModel(
        mpModel: MPThreeDSAuthenticationModel,
    ): ThreeDSAuthenticationModel {
        return ThreeDSAuthenticationModel(
            threeDSServerTransID = mpModel.threeDSServerTransID,
            acsReferenceNumber = mpModel.acsReferenceNumber,
            dsTransID = mpModel.dsTransID,
            acsTransID = mpModel.acsTransID,
            acsSignedContent = mpModel.acsSignedContent,
        )
    }

    /**
     * Converts ThreeDSAuthenticationModel (core-methods model) to MPThreeDSAuthenticationModel (threeds model).
     *
     * @param model The ThreeDSAuthenticationModel to convert
     * @return The converted MPThreeDSAuthenticationModel
     */
    fun toMPThreeDSAuthenticationModel(
        model: ThreeDSAuthenticationModel,
    ): MPThreeDSAuthenticationModel {
        return MPThreeDSAuthenticationModel(
            threeDSServerTransID = model.threeDSServerTransID,
            acsReferenceNumber = model.acsReferenceNumber,
            dsTransID = model.dsTransID,
            acsTransID = model.acsTransID,
            acsSignedContent = model.acsSignedContent,
        )
    }
}
