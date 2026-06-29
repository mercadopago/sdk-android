package com.mercadopago.sdk.android.domain.model

/**
 * Represents the supported countries in the Mercado Pago SDK.
 * This enum defines the ISO 3166-1 alpha-3 country codes for all supported regions,
 * allowing for country-specific payment processing and localization.
 * Each country code corresponds to a specific market where Mercado Pago operates.
 *
 * Example:
 * ```kotlin
 * // Get the country code for Argentina
 * val myCountryCode = CountryCode.ARG
 *
 * // Use in SDK initialization
 * MercadoPagoSDK.initialize(
 *     context = context,
 *     publicKey = "YOUR_PUBLIC_KEY",
 *     countryCode = myCountryCode
 * )
 * ```
 */
enum class CountryCode {
    /**
     * Argentina (ISO 3166-1 alpha-3: ARG)
     * Supports payment methods specific to the Argentine market.
     */
    ARG,

    /**
     * Brazil (ISO 3166-1 alpha-3: BRA)
     * Supports payment methods specific to the Brazilian market.
     */
    BRA,

    /**
     * Chile (ISO 3166-1 alpha-3: CHL)
     * Supports payment methods specific to the Chilean market.
     */
    CHL,

    /**
     * Colombia (ISO 3166-1 alpha-3: COL)
     * Supports payment methods specific to the Colombian market.
     */
    COL,

    /**
     * Mexico (ISO 3166-1 alpha-3: MEX)
     * Supports payment methods specific to the Mexican market.
     */
    MEX,

    /**
     * Costa Rica (ISO 3166-1 alpha-3: CRI)
     * Supports payment methods specific to the Costa Rican market.
     */
    CRI,

    /**
     * Peru (ISO 3166-1 alpha-3: PER)
     * Supports payment methods specific to the Peruvian market.
     */
    PER,

    /**
     * Ecuador (ISO 3166-1 alpha-3: ECU)
     * Supports payment methods specific to the Ecuadorian market.
     */
    ECU,

    /**
     * Dominican Republic (ISO 3166-1 alpha-3: DOM)
     * Supports payment methods specific to the Dominican market.
     */
    DOM,

    /**
     * Uruguay (ISO 3166-1 alpha-3: URY)
     * Supports payment methods specific to the Uruguayan market.
     */
    URY,

    /**
     * Venezuela (ISO 3166-1 alpha-3: VEN)
     * Supports payment methods specific to the Venezuelan market.
     */
    VEN,

    /**
     * Panama (ISO 3166-1 alpha-3: PAN)
     * Supports payment methods specific to the Panamanian market.
     */
    PAN,

    /**
     * Bolivia (ISO 3166-1 alpha-3: BOL)
     * Supports payment methods specific to the Bolivian market.
     */
    BOL,

    /**
     * Paraguay (ISO 3166-1 alpha-3: PRY)
     * Supports payment methods specific to the Paraguayan market.
     */
    PRY,

    /**
     * Guatemala (ISO 3166-1 alpha-3: GTM)
     * Supports payment methods specific to the Guatemalan market.
     */
    GTM,

    /**
     * Honduras (ISO 3166-1 alpha-3: HND)
     * Supports payment methods specific to the Honduran market.
     */
    HND,

    /**
     * El Salvador (ISO 3166-1 alpha-3: SLV)
     * Supports payment methods specific to the Salvadoran market.
     */
    SLV,

    /**
     * Nicaragua (ISO 3166-1 alpha-3: NIC)
     * Supports payment methods specific to the Nicaraguan market.
     */
    NIC,

    /**
     * Cuba (ISO 3166-1 alpha-3: CUB)
     * Supports payment methods specific to the Cuban market.
     */
    CUB
}
