package service

import entity.PokaniGame
import view.Refreshable

/**
 * The [RootService] class is the root of all services.
 * It is used to access all services from one place.
 * @property gridService the [GridService]
 * @property pokaniService the [PokaniService]
 * @property tileService the [TileService]
 */
class RootService {
    val gridService = GridService(this)
    val pokaniService = PokaniService(this)
    val tileService = TileService()
    val aiService = AIService(this)
    val networkService = NetworkService(this)
    val musicService = MusicService()

    /**
     * The [PokaniGame] instance
     */
    lateinit var game: PokaniGame

    /**
     * Registers the refreshable event listeners to the [GridService] and [PokaniService]
     * @param newRefreshable the new refreshable to add
     */
    private fun addRefreshable(newRefreshable: Refreshable) {
        gridService.addRefreshable(newRefreshable)
        pokaniService.addRefreshable(newRefreshable)
        networkService.addRefreshable(newRefreshable)
    }

    /**
     * Registers the refreshable event listeners to the [GridService] and [PokaniService]
     * @param newRefreshables the new refreshables to add
     */
    fun addRefreshables(vararg newRefreshables: Refreshable) {
        newRefreshables.forEach { addRefreshable(it) }
    }

}
