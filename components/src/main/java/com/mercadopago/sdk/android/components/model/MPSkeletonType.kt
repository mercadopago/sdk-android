package com.mercadopago.sdk.android.components.model

import androidx.compose.ui.graphics.Shape

/**
 * Tipo do [com.mercadopago.sdk.android.components.MPSkeleton] — define a forma
 * (shape) do placeholder de acordo com o que ele representa.
 *
 * Os nomes seguem o componente equivalente do iOS (`MPSkeletonView.SkeletonType`):
 * [Row], [Squared] e [Rounded]. Quando nenhum tipo semântico servir, use [Custom]
 * passando um [Shape] próprio. Para avatares/ícones com tamanho pré-definido, use
 * [Thumbnail].
 */
sealed class MPSkeletonType {
    /**
     * Linha de texto — cantos levemente arredondados (token `shape.small`).
     * Default para títulos, descrições e parágrafos.
     */
    data object Row : MPSkeletonType()

    /**
     * Retângulo arredondado — cantos médios (token `shape.medium`).
     * Usado para banners, inputs e botões.
     */
    data object Squared : MPSkeletonType()

    /**
     * Totalmente arredondado / circular (token `shape.full`).
     * Usado para chips e pílulas.
     */
    data object Rounded : MPSkeletonType()

    /**
     * Forma customizada — para casos que fogem dos tipos semânticos.
     * @param shape forma a ser aplicada ao skeleton
     */
    data class Custom(val shape: Shape) : MPSkeletonType()

    /**
     * Thumbnail de avatar/ícone — define forma **e** tamanho pré-definidos a partir
     * do [MPSkeletonThumbnailType], sem depender do `Modifier` do chamador.
     * @param type variante (tamanho + forma) do thumbnail
     */
    data class Thumbnail(
        val type: MPSkeletonThumbnailType = MPSkeletonThumbnailType.LargeRounded,
    ) : MPSkeletonType()
}
