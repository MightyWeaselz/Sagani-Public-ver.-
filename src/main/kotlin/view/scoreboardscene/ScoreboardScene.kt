package view.scoreboardscene

import edu.udo.cs.sopra.ntf.Color
import entity.Player
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.gamecomponentviews.TokenView
import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.visual.Visual
import view.Character
import view.PokaniApplication
import view.Refreshable
import view.Textures
import view.gamescene.GameScene
import kotlin.math.floor

/**
 * the [ScoreboardScene] is the Scene in which the Characters can be seen progressing according to the Players scores
 * during a live game. This Scene can be called by pressing the tab key in the [GameScene].
 */
class ScoreboardScene(
    private val pokaniApplication: PokaniApplication
) : BoardGameScene(1920, 1080), Refreshable {

    private val textures = Textures()

    val completeRouteTokenList = mutableListOf<TokenView?>()
    val facingUpRouteTokenList = mutableListOf<TokenView?>()
    val facingRightRouteTokenList = mutableListOf<TokenView?>()
    val facingLeftRouteTokenList = mutableListOf<TokenView?>()
    val facingScreenRouteTokenList = mutableListOf<TokenView?>()

    private var maximumWinningPoints = -1.0

    val mainTokenGrid = GridPane<TokenView>(
        posX = 416,
        posY = 0,
        columns = 32,
        rows = 32,
        spacing = 0,
        layoutFromCenter = false
    ).apply {
        setRowHeights(34)
        setColumnWidths(34)
    }

    private val mainRouteView = TokenView(
        posX = 416,
        posY = 0,
        width = 1088,
        height = 1088,
        visual = textures.mainRoute
    )

    private val player1Label = Label(
        width = 300,
        height = 50,
        text = "",
        font = Font(size = 40, family = "Bauhaus 93")
    )

    private val player1PointsLabel = Label(
        posX = 260,
        posY = 280,
        width = 150,
        height = 50,
        text = "",
        font = Font(size = 40, family = "Bauhaus 93")
    ).apply { this.isVisible = false }

    private val player1Grid = GridPane<ComponentView>(
        posX = 50,
        posY = 20,
        columns = 2,
        rows = 2,
        spacing = 20,
        layoutFromCenter = false
    )

    private val player2Label = Label(
        width = 300,
        height = 50,
        text = "",
        font = Font(size = 40, family = "Bauhaus 93")
    )

    private val player2PointsLabel = Label(
        posX = 1510,
        posY = 280,
        width = 150,
        height = 50,
        text = "",
        font = Font(size = 40, family = "Bauhaus 93")
    ).apply { this.isVisible = false }

    private val player2Grid = GridPane<ComponentView>(
        posX = 1600,
        posY = 20,
        columns = 2,
        rows = 2,
        spacing = 20,
        layoutFromCenter = false
    )

    private val player3Label = Label(
        width = 300,
        height = 50,
        text = "",
        font = Font(size = 40, family = "Bauhaus 93")
    )

    private val player3PointsLabel = Label(
        posX = 260,
        posY = 800,
        width = 150,
        height = 50,
        text = "",
        font = Font(size = 40, family = "Bauhaus 93")
    ).apply { this.isVisible = false }

    private val player3Grid = GridPane<ComponentView>(
        posX = 50,
        posY = 550,
        columns = 2,
        rows = 2,
        spacing = 20,
        layoutFromCenter = false
    )

    private val player4Label = Label(
        posX = 1510,
        posY = 800,
        width = 300,
        height = 50,
        text = "",
        font = Font(size = 40, family = "Bauhaus 93")
    )

    private val player4PointsLabel = Label(
        posX = 1510,
        posY = 800,
        width = 150,
        height = 50,
        text = "",
        font = Font(size = 40, family = "Bauhaus 93")
    ).apply { this.isVisible = false }

    private val player4Grid = GridPane<ComponentView>(
        posX = 1600,
        posY = 550,
        columns = 2,
        rows = 2,
        spacing = 20,
        layoutFromCenter = false
    )

    private val allPlayersGrids = listOf(player1Grid, player2Grid, player3Grid, player4Grid)
    private val allPlayerPointsLabels =
        listOf(player1PointsLabel, player2PointsLabel, player3PointsLabel, player4PointsLabel)

    /**
     * the starting Positions of the Players, when the players have 0 Points
     */
    val startingPositions = mutableListOf<TokenView>()

    /**
     * the current player Positions. We need to save them separately in order to prune them when the players "move"
     */
    var playersPosition = mutableListOf<TokenView>()

    private val scoreboardSceneGridBuilder = ScoreboardSceneGridBuilder(this)

    init {
        background = textures.backgroundTexture
        addComponents(
            mainRouteView,
            mainTokenGrid,
            player1Grid,
            player2Grid,
            player3Grid,
            player4Grid,
            player1PointsLabel,
            player2PointsLabel,
            player3PointsLabel,
            player4PointsLabel,
        )


        scoreboardSceneGridBuilder.initializeTileGrid()
        scoreboardSceneGridBuilder.defineStartingPositions()
    }

    /**
     * initializes the Labels of the Points, names and Images at the sides
     */
    private fun initializePlayerLabels() {
        allPlayersGrids.forEach { it.isVisible = false }
        allPlayerPointsLabels.forEach { it.isVisible = false }
        val currentGame = pokaniApplication.rootService.game
        player1Label.text = currentGame.players[0].name
        player1Grid[0, 1] = getCharFromColor(currentGame.players[0].color).highResImageTokenView
        player1Grid[0, 0] = player1Label
        player1PointsLabel.text = "${currentGame.players[0].score}/${maximumWinningPoints}"
        player1PointsLabel.isVisible = true
        player1Grid.isVisible = true

        player2Label.text = currentGame.players[1].name
        player2Grid[0, 1] = getCharFromColor(currentGame.players[1].color).highResImageTokenView
        player2Grid[0, 0] = player2Label
        player2PointsLabel.text = "${currentGame.players[1].score}/${maximumWinningPoints}"
        player2PointsLabel.isVisible = true
        player2Grid.isVisible = true

        if (currentGame.players.size >= 3) {
            player3Label.text = currentGame.players[2].name
            player3Grid[0, 1] = getCharFromColor(currentGame.players[2].color).highResImageTokenView
            player3Grid[0, 0] = player3Label
            player3PointsLabel.text = "${currentGame.players[2].score}/${maximumWinningPoints}"
            player3PointsLabel.isVisible = true
            player3Grid.isVisible = true
        }
        if (currentGame.players.size >= 4) {
            player4Label.text = currentGame.players[3].name
            player4Grid[0, 1] = getCharFromColor(currentGame.players[3].color).highResImageTokenView
            player4Grid[0, 0] = player4Label
            player4PointsLabel.text = "${currentGame.players[3].score}/${maximumWinningPoints}"
            player4PointsLabel.isVisible = true
            player4Grid.isVisible = true
        }
    }

    /**
     * updates the player Labels for the current points of the players
     */
    private fun updatePlayerLabels() {
        val currentGame = pokaniApplication.rootService.game
        player1PointsLabel.text = "${currentGame.players[0].score}/${maximumWinningPoints.toInt()}"
        player2PointsLabel.text = "${currentGame.players[1].score}/${maximumWinningPoints.toInt()}"
        if (currentGame.players.size >= 3) {
            player3PointsLabel.text = "${currentGame.players[2].score}/${maximumWinningPoints.toInt()}"
        }
        if (currentGame.players.size >= 4) {
            player4PointsLabel.text = "${currentGame.players[3].score}/${maximumWinningPoints.toInt()}"
        }
    }

    /**
     * main function, which updates the characters Positions, according to the [maximumWinningPoints]
     * and the Position in which the characters should be
     */
    private fun updatePlayersCharactersPosition() {
        val currentGame = pokaniApplication.rootService.game
        playersPosition.forEach {
            it.visual = Visual.EMPTY
        }
        currentGame.players.forEachIndexed { index, it ->
            if (it.score <= 0) {
                val currentPlayers = currentGame.players
                startingPositions[currentPlayers.indexOf(it)].visual =
                    getCharFromColor(it.color).facingScreenImage
                playersPosition[index] = startingPositions[currentPlayers.indexOf(it)]
            } else {
                if (it.score >= maximumWinningPoints) {
                    val currentPlayersPosition = completeRouteTokenList[149]
                    val currentPlayersCharacter = getCharFromColor(it.color)
                    completeRouteTokenList[149]?.visual =
                        getCorrectVisual(currentPlayersPosition, currentPlayersCharacter)
                    if (currentPlayersPosition != null) {
                        playersPosition[index] = currentPlayersPosition
                    }
                } else {
                    val currentPlayersPosition =
                        completeRouteTokenList[floor(150.0 / maximumWinningPoints * it.score.toDouble()).toInt() - 1]
                    val currentPlayersCharacter = getCharFromColor(it.color)

                    completeRouteTokenList[floor(
                        150.0 / maximumWinningPoints * it.score.toDouble()
                    ).toInt() - 1]?.visual =
                        getCorrectVisual(currentPlayersPosition, currentPlayersCharacter)
                    if (currentPlayersPosition != null) {
                        playersPosition[index] = currentPlayersPosition
                    }
                }
            }
        }
    }

    /**
     * helper function which returns the correct Image, given the [currentPositionToken] and the [character],
     * since the Route has different sections that require a left-or-right facing Image.
     *
     * @param currentPositionToken the Token of the Position that the character will be placed
     * @param character the character of the Player for which the correct Image should be retrieved
     *
     * @return returns the [ImageVisual] of the correct character and stance
     */
    private fun getCorrectVisual(currentPositionToken: TokenView?, character: Character): ImageVisual {
        return when {
            facingUpRouteTokenList.contains(currentPositionToken) -> {
                character.facingUpImage
            }

            facingRightRouteTokenList.contains(currentPositionToken) -> {
                character.facingRightImage
            }

            facingLeftRouteTokenList.contains(currentPositionToken) -> {
                character.facingLeftImage
            }

            else -> {
                character.facingScreenImage
            }
        }
    }

    /**
     * helper function that returns the [Character] associated to the given [Color], since the players only store
     * their color for the use of the Network.
     *
     * @param color the Color of the Player
     *
     * @return returns the [Character] associated with the Color
     */
    private fun getCharFromColor(color: Color): Character {
        return when (color) {
            Color.GREY -> {
                Character.BERTHA
            }

            Color.BROWN -> {
                Character.LUCIAN
            }

            Color.WHITE -> {
                Character.FLINT
            }

            Color.BLACK -> {
                Character.CYNTHIA
            }
        }
    }

    /**
     * initialization function to set the [maximumWinningPoints] for the associated player count
     */
    private fun setMaximumWinningPoints() {
        val currentGame = pokaniApplication.rootService.game
        when (currentGame.players.size) {
            2 -> {
                maximumWinningPoints = 75.0
            }

            3 -> {
                maximumWinningPoints = 60.0
            }

            4 -> {
                maximumWinningPoints = 45.0
            }

            else -> {
            }
        }
    }

    override fun refreshOnPlaceTile() {
        updatePlayersCharactersPosition()
        updatePlayerLabels()
    }

    override fun refreshOnUndo() {
        updatePlayersCharactersPosition()
        updatePlayerLabels()
    }

    override fun refreshOnRedo(executeMovePlayer: Player) {
        updatePlayersCharactersPosition()
        updatePlayerLabels()
    }

    override fun refreshOnStartGame() {
        setMaximumWinningPoints()
        initializePlayerLabels()
        updatePlayerLabels()
        scoreboardSceneGridBuilder.fillRouteIntoLists()
        updatePlayersCharactersPosition()
    }

    override fun refreshOnEndGame() {
        updatePlayerLabels()
        updatePlayersCharactersPosition()
    }
}