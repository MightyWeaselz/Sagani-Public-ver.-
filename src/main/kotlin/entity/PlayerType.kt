package entity

import kotlinx.serialization.Serializable

/**
 * The [PlayerType] enum class represents the different types of players that can play the game.
 */
@Serializable
enum class PlayerType {
    LOCAL_PLAYER,
    RANDOM_BOT,
    REMOTE_PLAYER,
    SMART_BOT
}