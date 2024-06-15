package view

import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import java.awt.Color
import javax.imageio.ImageIO

/**
 * [MenuScene] that appears when the Game is being ended, to show what Player won with how many Points in what Order.
 *
 * @param pokaniApplication the current [PokaniApplication]
 */
class GameOverMenu(private val pokaniApplication: PokaniApplication) : MenuScene(800, 1080), Refreshable {

    val backGround = ImageVisual(
        ImageIO.read(
            LocalGameNewMenu::class.java.getResource
                ("/End.png")
        )
    )

    val button = ImageVisual(
        ImageIO.read(
            LocalGameNewMenu::class.java.getResource
                ("/start_scene/MenuButton.png")
        )
    )

    private val square = Label(
        width = 800, height = 400, posX = 0, posY = 190,
        visual = ColorVisual(42, 42, 42),
    )

    private val headlineLabel = Label(
        width = 600,
        height = 120,
        posX = 100,
        posY = 0,
        text = "Game Over",
        font = Font(size = 80, color = Color.WHITE, family = "Bauhaus 93")
    )

    private val endGamePlayerScores = GridPane<Label>(
        posX = 400,
        posY = 390,
        columns = 3,
        rows = 5,
        spacing = 0,
        layoutFromCenter = true
    )

    private val playerRanks = Label(
        width = 200,
        height = 70,
        font = Font(size = 45, color = Color.WHITE),
        text = "Rank:"
    )

    private val playerNames = Label(
        width = 300,
        height = 70,
        font = Font(size = 45, color = Color.WHITE),
        text = "Name:"
    )

    private val playerPointLabel = Label(
        width = 200,
        height = 70,
        font = Font(size = 45, color = Color.WHITE),
        text = "Score:"
    )

    val mainMenuButton = Button(
        width = 350,
        height = 200,
        posX = 50,
        posY = 600,
        visual = button,
        font = Font(40, Color.WHITE, family = "Bauhaus 93"),
        text = "Main Menu"
    )

    val continueButton = Button(
        width = 350,
        height = 200,
        posX = 400,
        posY = 600,
        visual = button,
        font = Font(40, Color.WHITE, family = "Bauhaus 93"),
        text = "Continue"
    )

    init {
        square.opacity = 0.5
        background = backGround
        addComponents(
            square,
            endGamePlayerScores,
            continueButton,
            headlineLabel,
            mainMenuButton
        )
        endGamePlayerScores[0, 0] = playerRanks
        endGamePlayerScores[1, 0] = playerNames
        endGamePlayerScores[2, 0] = playerPointLabel
    }

    /**
     * A data class representing the results of the game.
     *
     * @property place The place of the player in the game.
     * @property name The name of the player.
     * @property score The score of the player.
     */
    data class Results(val place: Int, val name: String, val score: Int)

    /**
     * refreshes the point score screen with the
     * appropriate Players in accordance to their Rank/Points
     */
    override fun refreshOnEndGame() {
        val placeGroups = pokaniApplication.rootService.game.players.groupBy { it.score }
            .toSortedMap(compareByDescending { it })

        val sortedResult = placeGroups.flatMap { (score, group) ->
            group.sortedBy { it.name }.map {
                Results(placeGroups.keys.indexOf(score) + 1, it.name, it.score)
            }
        }
        sortedResult.forEachIndexed { index, results ->
            val playerRank = Label(
                width = 50, height = 70,
                font = Font(size = 45, color = Color.WHITE),
                text = "${results.place}"
            )
            val playerName = Label(
                width = 400, height = 70,
                font = Font(size = 45, color = Color.WHITE),
                text = results.name
            )
            val playerPoints = Label(
                width = 70, height = 70,
                font = Font(size = 45, color = Color.WHITE),
                text = "${results.score}"
            )
            endGamePlayerScores[0, index + 1] = playerRank
            endGamePlayerScores[1, index + 1] = playerName
            endGamePlayerScores[2, index + 1] = playerPoints
        }

    }

}