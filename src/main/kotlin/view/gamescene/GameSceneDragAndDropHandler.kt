package view.gamescene

import entity.components.Position
import tools.aqua.bgw.event.DragEvent
import tools.aqua.bgw.visual.CompoundVisual
import view.PokaniApplication
import view.customComponent.CustomTileComponent
import view.customComponent.DragPlaceholderComponent
import view.customComponent.Origin

/**
 * This Class handles all the Drag and Drop Events of the [GameScene]
 */
class GameSceneDragAndDropHandler(private val pokaniApplication: PokaniApplication) {

    /**
     * Adds Drag and Drop Events to all tiles.
     */
    fun addDragEvent() {
        pokaniApplication.gameScene.tiles.forEach { display ->
            display.onDragGestureStarted = { handleDragGestureStarted(display) }
            display.onDragGestureEnded = { _, _ -> hidePossiblePositions() }
        }
    }

    /**
     * hides the Positions in which a dragged Tile can be placed in, after a Tile was placed down
     */
    private fun hidePossiblePositions() {
        pokaniApplication.gameSceneGetters.getCurrentPlayerPane().removeAll(
            pokaniApplication.gameSceneGetters.getCurrentPlayerPane().filterIsInstance<DragPlaceholderComponent>()
        )
    }

    /**
     * Sets the drop acceptor for all possible positions.
     */
    private fun setDropAcceptorForPossiblePositions() {
        pokaniApplication.gameScene.possiblePositions.forEach { (position, cardView) ->
            cardView.dropAcceptor = { dragEvent ->
                handleDropEvent(dragEvent, position)
                true
            }
        }
    }

    /**
     * Shows all valid positions in which a Tile can be placed.
     *
     * @param coords the [Position]s in which a Tile can be placed
     */
    private fun showPossiblePositions(coords: List<Position>) {
        coords.forEach {
            val placeHolder = DragPlaceholderComponent(3996 + 100 * it.x, 2204 + 100 * -it.y)
            pokaniApplication.gameScene.possiblePositions[it] = placeHolder
            pokaniApplication.gameSceneGetters.getCurrentPlayerPane().add(placeHolder)
        }
    }

    /**
     * Handles the drag gesture started event.
     *
     * @param tile the Tile that is being dragged
     */
    private fun handleDragGestureStarted(tile: CustomTileComponent) {
        val currentGame = pokaniApplication.rootService.game
        val currentPlayer = currentGame.currentPlayer
        val validPositions = currentPlayer.let { player ->
            pokaniApplication.rootService.gridService.getValidPositions(player.grid)
        }
        if (tile.origin == Origin.DRAW_PILE) {
            tile.showFront()
            pokaniApplication.gameScene.displayLayout.forEach { it.isDraggable = false }
            (pokaniApplication.gameScene.offerDisplayBorder.visual as CompoundVisual).children[0].transparency = 0.0
        }
        showPossiblePositions(validPositions)
        setDropAcceptorForPossiblePositions()
    }

    /**
     * Handles the drop event.
     *
     * @param dragEvent the [DragEvent] that handles Tile that is being dragged
     * @param position the Position in which the dragged Tile is being placed
     */
    private fun handleDropEvent(dragEvent: DragEvent, position: Position) {
        val selectedTile = dragEvent.draggedComponent as CustomTileComponent
        pokaniApplication.gameSceneGetters.getCurrentPlayerPane().add(selectedTile)
        selectedTile.height = 120.0
        selectedTile.width = 120.0
        selectedTile.posX = (3986 + 100 * position.x).toDouble()
        selectedTile.posY = (2194 + 100 * -position.y).toDouble()
        if (selectedTile.origin == Origin.DRAW_PILE) {
            selectedTile.showFront()
        }
        pokaniApplication.gameSceneUIUpdater.disableSelections()
        pokaniApplication.gameScene.addModifyButtonsEvent(selectedTile, position)
        selectedTile.isDraggable = false
        pokaniApplication.gameScene.showPossibleSatisfactions(selectedTile, position)
    }
}