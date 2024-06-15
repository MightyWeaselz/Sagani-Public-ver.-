package view

import edu.udo.cs.sopra.ntf.Color
import tools.aqua.bgw.components.gamecomponentviews.TokenView
import tools.aqua.bgw.visual.ImageVisual

private val textures = Textures()

private val cynthiaVisual = TokenView(
    visual = textures.cynthiaFullImage,
    width = 202,
    height = 416
)

private val berthaVisual = TokenView(
    visual = textures.berthaFullImage,
    width = 151,
    height = 412
)

private val flintVisual = TokenView(
    visual = textures.flintFullImage,
    width = 151,
    height = 412
)

private val lucianVisual = TokenView(
    visual = textures.lucianFullImage,
    width = 270,
    height = 405
)

/**
 * Enum Class for easier access to the textures of the game Characters for the Players
 */
enum class Character(
    val color: Color,
    val facingUpImage: ImageVisual,
    val facingRightImage: ImageVisual,
    val facingScreenImage: ImageVisual,
    val facingLeftImage: ImageVisual,
    val highResImageTokenView: TokenView
) {
    CYNTHIA(
        Color.BLACK,
        textures.cynthiaBackwardsStill,
        textures.cynthiaRightStill,
        textures.cynthiaForwardStill,
        textures.cynthiaLeftStill,
        cynthiaVisual
    ),
    FLINT(
        Color.WHITE,
        textures.flintBackwardsStill,
        textures.flintRightStill,
        textures.flintForwardStill,
        textures.flintLeftStill,
        flintVisual
    ),
    LUCIAN(
        Color.BROWN,
        textures.lucianBackwardsStill,
        textures.lucianRightStill,
        textures.lucianForwardStill,
        textures.lucianLeftStill,
        lucianVisual
    ),
    BERTHA(
        Color.GREY,
        textures.berthaBackwardsStill,
        textures.berthaRightStill,
        textures.berthaForwardStill,
        textures.berthaLeftStill,
        berthaVisual
    )
}