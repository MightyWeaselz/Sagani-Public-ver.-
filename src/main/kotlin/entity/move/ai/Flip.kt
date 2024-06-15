package entity.move.ai

import entity.components.Tile
import kotlinx.serialization.Serializable

/**
 * A [Flip] is a [satisfiedTile] that can be flipped by a concrete [satisfactionCause].
 * @property satisfiedTile the tile that will be flipped
 * @property satisfactionCause the tile that will cause the flip
 */
@Serializable
data class Flip(val satisfiedTile: Tile, val satisfactionCause: Tile) {
    override fun toString() = "${satisfactionCause.toString(false)} at ${
        satisfactionCause.position
    } would flip ${
        satisfiedTile.toString(false)
    } refunding the player ${satisfiedTile.price} discs"
}