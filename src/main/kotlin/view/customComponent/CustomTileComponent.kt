package view.customComponent

import entity.components.Connection
import entity.components.Element
import entity.components.Rotation
import service.TileImageLoader
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.CompoundVisual
import tools.aqua.bgw.visual.ImageVisual
import java.awt.Color

/**
 * CustomTileComponent is a custom component creating a tile with a given element, price and connections.
 * @constructor Creates a [CustomTileComponent] with the given [Element], price and [Connection].
 * @param element the [Element] of the tile
 * @param price the price of the tile
 * @param connections the [Connection]s of the tile
 */
class CustomTileComponent(val element: Element, val price: Int, val tileID: Int, val connections: List<Connection>) :
    CardView(front = ColorVisual(Color.WHITE), back = ColorVisual(Color.WHITE)) {


    private val tileImageLoader = TileImageLoader()

    var tileRotation: Rotation = Rotation.UP
    var origin = Origin.DRAW_PILE


    init {
        height = 100.0
        width = 100.0
        setVisuals(element, price, connections)
    }

    /**
     * Creates a CompoundVisual with the given [Element], price and [Connection] and sets the visuals of the tile.
     */
    private fun setVisuals(element: Element, price: Int, connections: List<Connection>) {
        val frontVisualItems: MutableList<ImageVisual> = mutableListOf()

        frontVisualItems.add(ImageVisual(tileImageLoader.frontImageFor(element, price)))

        frontVisualItems.addAll(connections.map {
            ImageVisual(tileImageLoader.arrowImageFor(it.element, it.rotation))
        })

        frontVisual = CompoundVisual(frontVisualItems)
        backVisual = ImageVisual(tileImageLoader.backImageFor(element, price))
    }

    fun rotateTile(entityRotation: Rotation) {
        rotate(entityRotation.ordinal * 45)
        tileRotation = tileRotation.rotate(entityRotation)
    }

    fun setsTileRotation(entityRotation: Rotation) {
        rotation = (entityRotation.ordinal * 45).toDouble()
        tileRotation = entityRotation
    }
}

