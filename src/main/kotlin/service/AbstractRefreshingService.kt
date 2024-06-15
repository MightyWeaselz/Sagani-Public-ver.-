package service

import tools.aqua.bgw.core.BoardGameScene
import view.Refreshable

/**
 * Abstract service class that handles multiples [Refreshable]s (usually UI elements, such as
 * specialized [BoardGameScene] classes/instances) which are notified
 * of changes to refresh via the [onAllRefreshables] method.
 */
abstract class AbstractRefreshingService {

    /**
     * Abstract class to make a contact point between
     * Service and GUI to Update Information
     */
    private val refreshables = mutableListOf<Refreshable>()

    /**
     * Adds refreshables to the List of Classes that need refresh calls
     */
    fun addRefreshable(newRefreshable: Refreshable) {
        refreshables += newRefreshable
    }

    /**
     * makes a Refresh call on all refreshables in the List for a
     * specific Action that was made and changes the View
     */
    fun onAllRefreshables(method: Refreshable.() -> Unit) {
        refreshables.forEach { it.method() }
    }
}