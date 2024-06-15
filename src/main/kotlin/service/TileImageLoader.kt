package service

import entity.components.Element
import entity.components.Rotation
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

private const val FRONT_IMG = "/Eggs.png"
private const val BACK_IMG = "/Back.png"
private const val ARROWS = "/Arrows.png"
private const val ARROWS_SATIS = "/Arrows_Satis.png"
private const val IMG_HEIGHT = 100
private const val IMG_WIDTH = 100

/**
 * TileImageLoader, loads the different image components needed for creating a tile.
 * A tile consists of a front, a back and arrows.
 */
class TileImageLoader {

    /**
     * Loads the different image components needed for creating a tile.
     */
    private val front: BufferedImage = ImageIO.read(TileImageLoader::class.java.getResource(FRONT_IMG))
    private val back: BufferedImage = ImageIO.read(TileImageLoader::class.java.getResource(BACK_IMG))
    private val arrow: BufferedImage = ImageIO.read(TileImageLoader::class.java.getResource(ARROWS))
    private val arrowSatis: BufferedImage = ImageIO.read(TileImageLoader::class.java.getResource(ARROWS_SATIS))

    /**
     * Provides the front image for the given [Element] and [price]
     */
    fun frontImageFor(element: Element, price: Int): BufferedImage =
        getImageByCoordinates(front, convertPriceToCord(price), element.ordinal)

    /**
     * Provides the back image for the given [Element] and [price]
     */
    fun backImageFor(element: Element, price: Int): BufferedImage =
        getImageByCoordinates(back, convertPriceToCord(price), element.ordinal)

    /**
     * Provides the arrow image for the given [Element] and [Rotation]
     */
    fun arrowImageFor(element: Element, rotation: Rotation): BufferedImage =
        getImageByCoordinates(arrow, rotation.ordinal, element.ordinal)

    fun statisfiedArrowImageFor(element: Element, rotation: Rotation): BufferedImage =
        getImageByCoordinates(arrowSatis, rotation.ordinal, element.ordinal)

    /**
     * Provides the image for the given [x] and [y] coordinates
     */
    private fun getImageByCoordinates(image: BufferedImage, x: Int, y: Int) = image.getSubimage(
        x * IMG_WIDTH,
        y * IMG_HEIGHT,
        IMG_WIDTH,
        IMG_HEIGHT
    )

    /**
     * Converts the price to the corresponding column in the image
     */
    private fun convertPriceToCord(price: Int): Int =
        when (price) {
            1 -> 0
            3 -> 1
            6 -> 2
            else -> 3
        }
}