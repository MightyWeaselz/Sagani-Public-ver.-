package view.customComponent

import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.visual.ColorVisual

/**
 * CustomCornerStonesComponent is used to force the desired pane size.
 * @constructor Creates a [CustomCornerStonesComponent] with the given position.
 * @param posX the x coordinate of the placeholder
 * @param posY the y coordinate of the placeholder
 */
class CustomCornerStonesComponent(posX: Int, posY: Int) : CardView(
    posX = posX,
    posY = posY,
    height = 100,
    width = 100,
    front = ColorVisual(0, 0, 0),
    back = ColorVisual(0, 0, 0),
) {
    init {
        this.isVisible = false
    }
}