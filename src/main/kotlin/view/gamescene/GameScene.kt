package view.gamescene

import edu.udo.cs.sopra.ntf.Color
import entity.Player
import entity.PlayerType
import entity.components.BaseTile
import entity.components.Position
import entity.components.Rotation
import entity.components.Tile
import entity.move.moves.IntermezzoMove
import entity.move.moves.PlaceMove
import entity.move.moves.WildcardMove
import service.TileFactory
import service.TileImageLoader
import tools.aqua.bgw.components.container.CardStack
import tools.aqua.bgw.components.container.LinearLayout
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.layoutviews.CameraPane
import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.UIComponent
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.CompoundVisual
import tools.aqua.bgw.visual.ImageVisual
import view.PokaniApplication
import view.Refreshable
import view.Textures
import view.customComponent.CustomTileComponent
import view.customComponent.DragPlaceholderComponent
import view.customComponent.Origin

/**
 * GameScene is the scene where the game is played.
 * @constructor Creates a [GameScene] with the given [PokaniApplication].
 * @param pokaniApplication the [PokaniApplication] to which this [GameScene] belongs
 */
class GameScene(private val pokaniApplication: PokaniApplication) : BoardGameScene(1920, 1080), Refreshable {

    /**
     * The [Player]s of the game.
     */
    val tiles = ArrayDeque<CustomTileComponent>()

    private val rootService = pokaniApplication.rootService

    private val textures = Textures()

    val possiblePositions = mutableMapOf<Position, DragPlaceholderComponent>()

    var isOnlineGame = false

    var simDelay = 1000

    val displayLayout: LinearLayout<CardView> = LinearLayout(
        posX = 1350, posY = 0, width = 560, height = 140,
        alignment = Alignment.CENTER_RIGHT, spacing = 10,
    )

    val intermezzoLayout: LinearLayout<CardView> = LinearLayout(
        posX = 1460, posY = 140, width = 450, height = 140,
        alignment = Alignment.CENTER_RIGHT, spacing = 10,
    )

    val playerCams = ArrayDeque<CameraPane<Pane<CardView>>>()

    val playerPanes = ArrayDeque<Pane<CardView>>()

    val rotateLeftButton = CardView(
        width = 35, height = 35,
        back = textures.rotateLeftButton, front = textures.rotateLeftButton
    ).apply {
        isDisabled = true
        isVisible = false
    }

    val rotateRightButton = CardView(
        width = 35, height = 35,
        back = textures.rotateRightButton, front = textures.rotateRightButton
    ).apply {
        isDisabled = true
        isVisible = false
    }

    val confirmButton = CardView(
        width = 25, height = 25,
        back = textures.confirmButton, front = textures.confirmButton
    ).apply {
        isDisabled = true
        isVisible = false
    }

    val cancelButton = CardView(
        width = 25, height = 25,
        back = textures.cancelButton, front = textures.cancelButton
    ).apply {
        isDisabled = true
        isVisible = false
    }

    val currentInformationGrid = GridPane<UIComponent>(
        columns = 1, rows = 2, spacing = 0, layoutFromCenter = false,
    )

    val currentPlayerLabel = Label(
        width = 650, height = 80, text = "",
        font = Font(size = 50, family = "Bauhaus 93", color = java.awt.Color.WHITE),
        alignment = Alignment.CENTER_LEFT
    )

    val currentRoundLabel = Label(
        width = 650, height = 80, text = "",
        font = Font(size = 50, family = "Bauhaus 93", color = java.awt.Color.WHITE),
        alignment = Alignment.CENTER_LEFT
    )

    val centerButton: Label = Label(
        posX = 1500, posY = 420, width = 420, height = 80,
        font = Font(size = 40, family = "Bauhaus 93", color = java.awt.Color.WHITE),
        alignment = Alignment.CENTER, text = "Center Camera",
        visual = textures.bigButton
    ).apply { onMouseClicked = { pokaniApplication.gameSceneContainerUiUpdater.centerCameras() } }

    val undoButton: Label = Label(
        posX = 1500, posY = 510, width = 205, height = 80,
        font = Font(size = 40, family = "Bauhaus 93", color = java.awt.Color.WHITE),
        alignment = Alignment.CENTER, text = "Undo",
        visual = textures.smolButton
    )

    val redoButton: Label = Label(
        posX = 1715, posY = 510, width = 205, height = 80,
        font = Font(size = 40, family = "Bauhaus 93", color = java.awt.Color.WHITE),
        alignment = Alignment.CENTER, text = "Redo",
        visual = textures.bigButton
    )

    val skipButton: Label = Label(
        posX = 1500, posY = 600, width = 420, height = 80,
        font = Font(size = 40, family = "Bauhaus 93", color = java.awt.Color.WHITE),
        alignment = Alignment.CENTER, text = "Skip",
        visual = textures.bigButton
    )

    val spectatingLabel: Label = Label(
        posX = 760, posY = 940, width = 400, height = 50, text = "Spectating: ",
        font = Font(size = 40, family = "Bauhaus 93", color = java.awt.Color.WHITE),
        alignment = Alignment.CENTER,
    )

    val offerDisplayBorder: Label = Label(
        posX = 1360, posY = 10, width = 560, height = 120,
        visual = CompoundVisual(textures.offerBorder)
    )

    val intermezzoBorder: Label = Label(
        posX = 1470, posY = 150, width = 450, height = 120,
        visual = CompoundVisual(textures.intermezzoBorder)
    )

    val drawPileBorder: Label = Label(
        posX = 1580, posY = 290, width = 340, height = 120,
        visual = CompoundVisual(textures.drawPileBorder)
    )

    val drawPiles = ArrayDeque<CardStack<CardView>>()
    val playerClusters = ArrayDeque<ArrayDeque<Label>>()

    val errorLabel = Label(
        posX = 640,
        posY = 360,
        width = 640,
        height = 360,
        text = "Error:\nYou can only send\na move when it's\n your turn!",
        alignment = Alignment.CENTER,
        font = Font(size = 50, color = java.awt.Color.RED, family = "Bauhaus 93"),
        isWrapText = true,
        visual = CompoundVisual(ColorVisual(0, 0, 0, 100), textures.error)
    ).apply { isVisible = false }

    init {
        TileFactory.getOriginalTiles().forEach {
            tiles.add(CustomTileComponent(it.element, it.value, it.tileID, it.connections))
        }
        currentInformationGrid.setCenterMode(Alignment.CENTER_LEFT)
        addComponents(
            displayLayout, intermezzoLayout, currentInformationGrid, centerButton, undoButton, redoButton,
            skipButton, spectatingLabel, offerDisplayBorder, intermezzoBorder, drawPileBorder,
            errorLabel
        )
        pokaniApplication.rootService.musicService.playTrack()
    }

    /**
     * clears all the UI Components in the [GameScene] of any entries
     */
    fun clearUIComponents() {
        playerPanes.clear()
        playerCams.clear()
        drawPiles.clear()
        playerClusters.forEach {
            it.forEach { label ->
                if (label.parent != null) {
                    label.removeFromParent()
                }
            }
        }
        playerClusters.clear()
        displayLayout.clear()
        intermezzoLayout.clear()
        tiles.forEach {
            if (it.parent != null) {
                it.removeFromParent()
            }
            it.rotation = 0.0
            it.showBack()
            it.origin = Origin.DRAW_PILE
            (it.frontVisual as CompoundVisual).children.forEach { visual ->
                visual.transparency = 1.0
            }
        }
    }

    /**
     * creates all the used UI Components in the [GameScene]
     */
    fun createUIComponents() {
        pokaniApplication.gameSceneContainerUiUpdater.createPlayerPanesAndCams()
        val currentGame = rootService.game
        repeat(3) {
            drawPiles.add(
                CardStack(posX = 1590 + 110 * it, posY = 300, width = 100, height = 100, alignment = Alignment.CENTER)
            )
        }
        var pileIndex = 2
        var tileCounter = 1
        val reverseDrawPile = currentGame.drawPile.reversed()
        reverseDrawPile.forEach {
            if (tileCounter > 24) {
                pileIndex--
                tileCounter = 1
            }
            drawPiles[pileIndex].add(tiles[it.tileID - 1])
            tileCounter++
        }
        drawPiles.forEach {
            addComponents(it)
        }
        currentGame.offerDisplay.forEach {
            val tile = tiles[it.tileID - 1]
            displayLayout.add(tile)
            tile.showFront()
            tile.origin = Origin.OFFER_DISPLAY
        }
        currentInformationGrid[0, 0] = currentPlayerLabel
        currentInformationGrid[0, 1] = currentRoundLabel
        isOnlineGame = currentGame.players.any { it.playerType == PlayerType.REMOTE_PLAYER }
        createPlayerInfoClusters()
        setPlayerInfoClusters()
        displayPlayerInfoClusters()
        pokaniApplication.gameSceneDragAndDropHandler.addDragEvent()
    }

    /**
     * creates the Spectate Info Buttons at the Bottom of the [GameScene]
     */
    private fun createPlayerInfoClusters() {
        val playerSize = rootService.game.players.size
        repeat(playerSize) {
            val playerCluster = ArrayDeque<Label>()
            val playerIcon = Label(
                posX = 420 + (540 - 135 * playerSize) + 270 * it, posY = 1000, width = 40,
                height = 40, font = Font(size = 30, family = "Bauhaus 93", color = java.awt.Color.WHITE),
                alignment = Alignment.CENTER,
            )
            val playerScore = Label(
                posX = 460 + (540 - 135 * playerSize) + 270 * it, posY = 1000, width = 70,
                height = 40, font = Font(size = 30, family = "Bauhaus 93", color = java.awt.Color.WHITE),
                alignment = Alignment.CENTER,
            )
            val playerDisks = Label(
                posX = 530 + (540 - 135 * playerSize) + 270 * it, posY = 1000, width = 100,
                height = 40, font = Font(size = 30, family = "Bauhaus 93", color = java.awt.Color.WHITE),
                alignment = Alignment.CENTER,
            )
            val diskIcon = Label(posX = 630 + (540 - 135 * playerSize) + 270 * it, posY = 1000, width = 40, height = 40)
            val playerName = Label(
                posX = 420 + (540 - 135 * playerSize) + 270 * it, posY = 1040, width = 250,
                height = 40, font = Font(size = 30, family = "Bauhaus 93", color = java.awt.Color.WHITE),
                alignment = Alignment.CENTER,
            )
            playerCluster.add(playerIcon)
            playerCluster.add(playerScore)
            playerCluster.add(playerDisks)
            playerCluster.add(diskIcon)
            playerCluster.add(playerName)
            playerClusters.add(playerCluster)
        }
    }

    /**
     * Fills the Spectate Info Buttons from [createPlayerInfoClusters] with the current Players information
     */
    private fun setPlayerInfoClusters() {
        val currentGame = rootService.game
        currentGame.players.forEachIndexed { index, player ->
            val playerCluster = playerClusters[index]
            when (player.color) {
                Color.BROWN -> {
                    val compoundVisual = CompoundVisual(ColorVisual(150, 150, 150), textures.lucianForwardStill)
                    compoundVisual.children[0].transparency = 0.0
                    playerCluster[0].visual = compoundVisual
                }

                Color.BLACK -> {
                    val compoundVisual = CompoundVisual(ColorVisual(150, 150, 150), textures.cynthiaForwardStill)
                    compoundVisual.children[0].transparency = 0.0
                    playerCluster[0].visual = compoundVisual
                }

                Color.WHITE -> {
                    val compoundVisual = CompoundVisual(ColorVisual(150, 150, 150), textures.flintForwardStill)
                    compoundVisual.children[0].transparency = 0.0
                    playerCluster[0].visual = compoundVisual
                }

                Color.GREY -> {
                    val compoundVisual = CompoundVisual(ColorVisual(150, 150, 150), textures.berthaForwardStill)
                    compoundVisual.children[0].transparency = 0.0
                    playerCluster[0].visual = compoundVisual
                }
            }
            playerCluster[1].text = player.score.toString()
            playerCluster[2].text = "${player.currentDiscs} / ${player.totalDiscs}"
            val compoundVisual = CompoundVisual(ColorVisual(150, 150, 150), textures.discs)
            compoundVisual.children[0].transparency = 0.0
            playerCluster[3].visual = compoundVisual
            playerCluster[4].text = player.name

            playerCluster[0].onMouseClicked = {
                pokaniApplication.gameSceneUIUpdater.showPlayerCam(index)
            }
            playerCluster[1].onMouseClicked = {
                pokaniApplication.gameSceneUIUpdater.showPlayerCam(index)
            }
            playerCluster[2].onMouseClicked = {
                pokaniApplication.gameSceneUIUpdater.showPlayerCam(index)
            }
            playerCluster[4].onMouseClicked = {
                pokaniApplication.gameSceneUIUpdater.showPlayerCam(index)
            }
        }
    }

    /**
     * adds the needed [playerClusters] to the [GameScene]
     */
    private fun displayPlayerInfoClusters() {
        rootService.game.players.forEachIndexed { index, _ ->
            playerClusters[index].forEach {
                addComponents(it)
            }
        }
    }

    /**
     * Returns a Tile to its [Origin] from where it was dragged
     *
     * @param currentTile the Tile that will be returned
     */
    private fun returnDraggedTile(currentTile: CustomTileComponent) {
        pokaniApplication.gameSceneGetters.getCurrentPlayerPane().remove(currentTile)
        when (currentTile.origin) {
            Origin.OFFER_DISPLAY -> {
                displayLayout.add(currentTile)
            }

            Origin.INTERMEZZO -> {
                intermezzoLayout.add(currentTile)
            }

            Origin.DRAW_PILE -> {
                pokaniApplication.gameSceneGetters.getActiveDrawPile().add(currentTile)
            }
        }
    }

    /**
     * this Method adds all the needed logic to the UI Buttons that appear when placing a Tile
     *
     * @param currentTile the Tile that is being placed
     * @param position the Position of the Tile that is being placed
     */
    fun addModifyButtonsEvent(currentTile: CustomTileComponent, position: Position) {
        repositionTileModifyOverlay(currentTile)
        rotateLeftButton.onMouseClicked = {
            currentTile.rotateTile(Rotation.LEFT)
            hidePossibleSatisfactions()
            showPossibleSatisfactions(currentTile, position)
        }
        rotateRightButton.onMouseClicked = {
            currentTile.rotateTile(Rotation.RIGHT)
            hidePossibleSatisfactions()
            showPossibleSatisfactions(currentTile, position)
        }
        confirmButton.onMouseClicked = {
            currentTile.height = 100.0
            currentTile.width = 100.0
            currentTile.posX += 10
            currentTile.posY += 10
            pokaniApplication.gameSceneContainerUiUpdater.hideTileModifyOverlay()
            pokaniApplication.gameSceneUIUpdater.enableSelection()
            val baseTile = pokaniApplication.gameSceneGetters.getTileInOptions(currentTile)
            if (baseTile != null) {
                generateAndSendMove(currentTile, position, baseTile)
            }
            hidePossibleSatisfactions()
        }
        cancelButton.onMouseClicked = {
            currentTile.height = 100.0
            currentTile.width = 100.0
            currentTile.posX = 0.0
            currentTile.posY = 0.0
            currentTile.setsTileRotation(Rotation.UP)
            pokaniApplication.gameSceneContainerUiUpdater.hideTileModifyOverlay()
            returnDraggedTile(currentTile)
            if (currentTile.origin == Origin.DRAW_PILE) {
                currentTile.isDraggable = true
                (pokaniApplication.gameScene.drawPileBorder.visual as CompoundVisual).children[0].transparency = 1.0
            } else {
                pokaniApplication.gameSceneUIUpdater.enableSelection()
            }
            hidePossibleSatisfactions()
        }
    }

    /**
     * this method generates and sends the correct Move to the Service Layer
     *
     * @param currentTile the GUI Tile that will be placed
     * @param position the Position of the Tile to be placed
     * @param baseTile the Entity Tile that will be placed in the Entity Layer
     */
    private fun generateAndSendMove(currentTile: CustomTileComponent, position: Position, baseTile: BaseTile) {
        val currentGame = rootService.game
        val currentPlayer = currentGame.currentPlayer
        when (currentTile.origin) {
            Origin.OFFER_DISPLAY -> {
                val placeMove = PlaceMove(
                    position = position, rotation = currentTile.tileRotation,
                    baseTileToPlace = baseTile, moveID = currentGame.moveID, player = currentPlayer
                )
                rootService.pokaniService.executeMove(placeMove)
                pokaniApplication.gameSceneUIUpdater.disableSelections()
            }

            Origin.DRAW_PILE -> {
                val wildcardMove = WildcardMove(
                    position = position, rotation = currentTile.tileRotation,
                    baseTileToPlace = baseTile, moveID = currentGame.moveID, player = currentPlayer
                )
                rootService.pokaniService.executeMove(wildcardMove)
                pokaniApplication.gameSceneUIUpdater.disableSelections()
            }

            Origin.INTERMEZZO -> {
                val intermezzoMove = IntermezzoMove(
                    position = position, rotation = currentTile.tileRotation,
                    baseTileToPlace = baseTile, moveID = currentGame.moveID, player = currentPlayer
                )
                rootService.pokaniService.executeMove(intermezzoMove)
                pokaniApplication.gameSceneUIUpdater.disableSelections()
            }
        }
    }

    /**
     * Repositions the Tile Modify Overlay Buttons to the tile that is being placed
     *
     * @param tile the Tile that is being placed
     */
    private fun repositionTileModifyOverlay(tile: CustomTileComponent) {
        rotateLeftButton.posX = tile.posX - 30
        rotateLeftButton.posY = tile.posY - 20
        rotateLeftButton.rotation = 330.0
        rotateLeftButton.isDisabled = false
        rotateLeftButton.isVisible = true
        rotateRightButton.posX = tile.posX + 115
        rotateRightButton.posY = tile.posY - 20
        rotateRightButton.rotation = 30.0
        rotateRightButton.isDisabled = false
        rotateRightButton.isVisible = true
        confirmButton.posX = tile.posX - 25
        confirmButton.posY = tile.posY + 95
        confirmButton.isDisabled = false
        confirmButton.isVisible = true
        cancelButton.posX = tile.posX + 120
        cancelButton.posY = tile.posY + 95
        cancelButton.isDisabled = false
        cancelButton.isVisible = true
        rotateLeftButton.zIndex = 10000
        rotateRightButton.zIndex = 10000
        confirmButton.zIndex = 10000
        cancelButton.zIndex = 10000
    }

    /**
     * function to update the connections Visuals when placing, to show what connections will be satisfied by the
     * placement of the selected Tile.
     *
     * @param selectedTile the Tile that is being placed
     * @param position the Position of the Tile that is being placed
     */
    fun showPossibleSatisfactions(selectedTile: CustomTileComponent, position: Position) {
        val simTile = Tile(
            selectedTile.tileID,
            selectedTile.element,
            selectedTile.connections,
            position,
            selectedTile.tileRotation
        )
        val satisfactions = rootService.tileService.calculateSatisfactions(simTile, rootService.game.currentPlayer.grid)
        satisfactions.forEach {
            val tile = tiles[it.satisfiedTile.tileID - 1]
            val compoundLayers = ((tile.frontVisual as CompoundVisual).children).toMutableList()
            val tileImageLoader = TileImageLoader()
            compoundLayers.add(
                ImageVisual(
                    tileImageLoader.statisfiedArrowImageFor(
                        it.connection.element,
                        it.connection.rotation
                    )
                )
            )
            tile.frontVisual = CompoundVisual(compoundLayers)
        }
    }

    /**
     * function to hide the updated Visuals again, when placing the Card down finally
     */
    fun hidePossibleSatisfactions() {
        tiles.forEach {
            val compoundLayers = ((it.frontVisual as CompoundVisual).children).toMutableList()
            if (it.connections.size != compoundLayers.size) {
                it.frontVisual = CompoundVisual(compoundLayers.subList(0, it.connections.size + 1))
            }
        }
    }
}