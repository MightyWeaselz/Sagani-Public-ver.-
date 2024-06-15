package view.customComponent

import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import javax.imageio.ImageIO

/**
 * DragPlaceholderComponent is a custom component creating a placeholder for a possible tile locations.
 * @constructor Creates a [DragPlaceholderComponent] with the given position.
 * @param posX the x coordinate of the placeholder
 * @param posY the y coordinate of the placeholder
 */
class DragPlaceholderComponent(posX: Int, posY: Int) : CardView(
    posX = posX,
    posY = posY,
    height = 100,
    width = 100,
    front = ColorVisual(0, 0, 0),
    back = ColorVisual(0, 0, 0),
) {
    init {
        val placeholder = ImageVisual(
            ImageIO.read(
                DragPlaceholderComponent::class.java.getResource(
                    "/placeholderWhite.png"
                )
            )
        )
        frontVisual = placeholder
        backVisual = placeholder
    }
}