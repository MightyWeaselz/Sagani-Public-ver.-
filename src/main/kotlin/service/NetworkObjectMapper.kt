package service

import edu.udo.cs.sopra.ntf.*
import entity.PlayerType
import entity.PokaniGame
import entity.components.Position
import entity.components.Rotation
import entity.move.AbstractMove
import entity.move.moves.IntermezzoMove
import entity.move.moves.PlaceMove
import entity.move.moves.SkipMove
import entity.move.moves.WildcardMove

/**
 * Exception to be thrown when a Move cannot be mapped to a Network Message
 */
class CanNotMapLocalObjectException(s: String) : IllegalArgumentException(s)

/**
 * Exception to be thrown when a Tile cannot be properly mapped
 */
class CanNotMapNetworkObjectException(s: String) : IllegalArgumentException(s)

// TO NETWORK
fun Rotation.mapToNetworkRotation(): Orientation {
    return when (this) {
        Rotation.UP -> Orientation.NORTH
        Rotation.RIGHT -> Orientation.EAST
        Rotation.DOWN -> Orientation.SOUTH
        Rotation.LEFT -> Orientation.WEST
        else -> {
            throw CanNotMapLocalObjectException("Rotation $this cannot be mapped to a network rotation")
        }
    }
}

fun AbstractMove.createNetworkMessage(game: PokaniGame, player: entity.Player): TurnMessage {
    val checksum = TurnChecksum(
        player.score,
        player.currentDiscs,
        game.isIntermezzo, //TODO
        game.endConditionsMet //TODO
    )
    when (this) {
        is PlaceMove -> {
            return TurnMessage(
                MoveType.OFFER_DISPLAY,
                TilePlacement(
                    baseTileToPlace.tileID,
                    position.x,
                    position.y,
                    rotation.mapToNetworkRotation()
                ),
                checksum
            )
        }

        is IntermezzoMove -> {
            return TurnMessage(
                MoveType.INTERMEZZO,
                TilePlacement(
                    baseTileToPlace.tileID,
                    position.x,
                    position.y,
                    rotation.mapToNetworkRotation()
                ),
                checksum
            )
        }

        is WildcardMove -> {
            return TurnMessage(
                MoveType.DRAW_PILE,
                TilePlacement(
                    baseTileToPlace.tileID,
                    position.x,
                    position.y,
                    rotation.mapToNetworkRotation()
                ),
                checksum
            )
        }

        is SkipMove -> {
            return TurnMessage(
                MoveType.SKIP,
                null,
                checksum
            )
        }

        else -> {
            throw CanNotMapLocalObjectException("Move $this cannot be mapped to a network message")
        }
    }
}

// TO LOCAL

fun Orientation.mapToRoation(): Rotation {
    return when (this) {
        Orientation.NORTH -> Rotation.UP
        Orientation.EAST -> Rotation.RIGHT
        Orientation.SOUTH -> Rotation.DOWN
        Orientation.WEST -> Rotation.LEFT
        else -> {
            throw CanNotMapNetworkObjectException("Orientation $this cannot be mapped to a rotation")
        }
    }
}

fun TilePlacement.mapToPosition(): Position {
    return Position(posX, posY)
}

fun TurnMessage.mapToMove(game: PokaniGame): AbstractMove {
    return when (type) {
        MoveType.OFFER_DISPLAY -> PlaceMove(
            tilePlacement?.mapToPosition()
                ?: throw CanNotMapNetworkObjectException("No tilePlacement on OFFER_DISPLAY move"),
            tilePlacement?.orientation?.mapToRoation()
                ?: throw CanNotMapNetworkObjectException("No tilePlacement on OFFER_DISPLAY move"),
            game.offerDisplay.firstOrNull { it.tileID == (tilePlacement?.tileId ?: 0) }
                ?: throw CanNotMapNetworkObjectException("tilePlacement id is not valid"),
            game.moveID,
            game.currentPlayer
        )

        MoveType.DRAW_PILE -> WildcardMove(
            tilePlacement?.mapToPosition() ?: Position(0, 0),
            tilePlacement?.orientation?.mapToRoation() ?: Rotation.UP,
            game.drawPile.firstOrNull() ?: throw CanNotMapNetworkObjectException("draw stack empty"),
            game.moveID,
            game.currentPlayer
        )

        MoveType.SKIP -> SkipMove(
            game.moveID,
            game.currentPlayer
        )

        MoveType.INTERMEZZO -> IntermezzoMove(
            tilePlacement?.mapToPosition() ?: Position(0, 0),
            tilePlacement?.orientation?.mapToRoation() ?: Rotation.UP,
            game.intermezzoDisplay.firstOrNull { it.tileID == (tilePlacement?.tileId ?: 0) }
                ?: throw CanNotMapNetworkObjectException("tilePlacement id is not valid"),
            game.moveID,
            game.currentPlayer
        )
    }
}

fun Player.mapToLocalPlayer(client: PokaniNetworkClient): entity.Player {
    return entity.Player(
        name,
        color,
        if (client.playerName == name) client.playerType else PlayerType.REMOTE_PLAYER
    )
}