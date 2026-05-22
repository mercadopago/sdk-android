package com.mercadopago.sdk.android.components.model

/**
 * Tipo (tamanho + forma) do MPSkeletonThumbnail, conforme variantes definidas no Figma.
 */
enum class MPSkeletonThumbnailType {
    /**
     * Quadrado com cantos arredondados (40dp · radius medium).
     * Usado para placeholders de ícones / app icon.
     */
    Squared,

    /**
     * Circular padrão (48dp).
     * Usado para placeholders de avatares.
     */
    LargeRounded,

    /**
     * Circular maior (56dp).
     * Usado para placeholders de avatares de destaque.
     */
    XLargeRounded,

    /**
     * Circular grande (64dp).
     * Usado para placeholders de avatares hero / topo de tela.
     */
    HugeRounded,
}
