package view

import entity.Player
import entity.PlayerType
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.TextField
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import java.awt.Color

/**
 * The main Scene of the local lobby creation
 *
 * @param pokaniApplication the current PokaniApplication that holds the reference to this Scene
 */
class LocalGameNewMenu(private val pokaniApplication: PokaniApplication) : MenuScene(1920, 1080), Refreshable {
    private val textures = Textures()
    val positionLogic = LocalGameNewMenuPositionLogic(this)

    private val listOfColors = listOf<Color>(Color.BLACK, Color.DARK_GRAY, Color.ORANGE, Color.LIGHT_GRAY)
    private val robotImageGroup = listOf(textures.robotOff, textures.robotOn, textures.robotHard)
    private val playerTypes = listOf(PlayerType.LOCAL_PLAYER, PlayerType.RANDOM_BOT, PlayerType.SMART_BOT)
    private var colorOfPlayer1 = 0
    private var colorOfPlayer2 = 1
    private var colorOfPlayer3 = 2
    private var colorOfPlayer4 = 3
    private var robotSituationOfPlayer1 = 0
    private var robotSituationOfPlayer2 = 0
    private var robotSituationOfPlayer3 = 0
    private var robotSituationOfPlayer4 = 0

    private var randomPlayers = false

    /*Color Selections*/
    private val player1ColorButton = Button(
        posX = 1189, posY = 224, width = 71, height = 71,
        visual = ColorVisual(listOfColors[colorOfPlayer1])
    ).apply {
        onMouseClicked = {
            if (colorOfPlayer1 < 3) {
                colorOfPlayer1++
                this.visual = ColorVisual(listOfColors[colorOfPlayer1])
            } else {
                colorOfPlayer1 = 0
                this.visual = ColorVisual(listOfColors[colorOfPlayer1])
            }
        }
    }

    private val player2ColorButton = Button(
        posX = 1189, posY = 327, width = 71, height = 71,
        visual = ColorVisual(listOfColors[colorOfPlayer2])
    ).apply {
        onMouseClicked = {
            if (colorOfPlayer2 < 3) {
                colorOfPlayer2++
                this.visual = ColorVisual(listOfColors[colorOfPlayer2])
            } else {
                colorOfPlayer2 = 0
                this.visual = ColorVisual(listOfColors[colorOfPlayer2])
            }
        }
    }

    private val player3ColorButton = Button(
        posX = 1189, posY = 430, width = 71, height = 71,
        visual = ColorVisual(listOfColors[colorOfPlayer3])
    ).apply {
        onMouseClicked = {
            if (colorOfPlayer3 < 3) {
                colorOfPlayer3++
                this.visual = ColorVisual(listOfColors[colorOfPlayer3])
            } else {
                colorOfPlayer3 = 0
                this.visual = ColorVisual(listOfColors[colorOfPlayer3])
            }
        }
    }

    private val player4ColorButton = Button(
        posX = 1189, posY = 533, width = 71, height = 71,
        visual = ColorVisual(listOfColors[colorOfPlayer4])
    ).apply {
        onMouseClicked = {
            if (colorOfPlayer4 < 3) {
                colorOfPlayer4++
                this.visual = ColorVisual(listOfColors[colorOfPlayer4])
            } else {
                colorOfPlayer4 = 0
                this.visual = ColorVisual(listOfColors[colorOfPlayer4])
            }
        }
    }

    private val showInformationOnHover = Label(
        posX = -500, posY = -500, width = 150, height = 35, text = "",
        font = Font(size = 20, family = "Bauhaus 93"), visual = ColorVisual.LIGHT_GRAY
    )

    /**
     * gets the NFT [Color] from the given Index
     *
     * @param index the index that indicates what Color to retiurn
     * @return [Color]
     */
    private fun getColorFromSelection(index: Int): edu.udo.cs.sopra.ntf.Color {
        return when (index) {
            0 -> {
                edu.udo.cs.sopra.ntf.Color.BLACK
            }

            1 -> {
                edu.udo.cs.sopra.ntf.Color.WHITE
            }

            2 -> {
                edu.udo.cs.sopra.ntf.Color.BROWN
            }

            else -> {
                edu.udo.cs.sopra.ntf.Color.GREY
            }
        }
    }

    val deleteButtons = listOf(
        Button(width = 91, height = 91, posX = 1403, posY = 215, visual = textures.cancelPlayer),
        Button(width = 91, height = 91, posX = 1403, posY = 317, visual = textures.cancelPlayer),
        Button(width = 91, height = 91, posX = 1403, posY = 420, visual = textures.cancelPlayer),
        Button(width = 91, height = 91, posX = 1403, posY = 523, visual = textures.cancelPlayer)
    )

    private val headlineLabel = Label(
        width = 760, height = 142, posX = 585, posY = 0, text = "New Game",
        font = Font(size = 128, color = Color.BLACK, family = "Bauhaus 93")
    )

    private val square = Label(
        width = 1803, height = 895, posX = 58, posY = 133,
        visual = ColorVisual(42, 42, 42),
    )

    val backButton = Button(
        width = 116, height = 116, text = "", font = Font(64, Color.ORANGE),
        posX = 58, posY = 133, visual = textures.backButton
    )

    /**
     * changes the text to be displayed when hovering over the Ai Button selection
     *
     * @param playerState the state of what the Button is in to display the correct Text
     */
    private fun showCorrectTextToDisplay(playerState: Int) {
        showInformationOnHover.text = when (playerState) {
            0 -> "Human Player"
            1 -> "Easy Bot"
            else -> "Hard Bot"
        }
    }

    private val player1AIButton = Button(
        width = 91, height = 91, posX = 1291, posY = 215,
        visual = robotImageGroup[robotSituationOfPlayer1]
    ).apply {
        onMouseClicked = {
            if (robotSituationOfPlayer1 < 2) {
                robotSituationOfPlayer1++
                this.visual = robotImageGroup[robotSituationOfPlayer1]
            } else {
                robotSituationOfPlayer1 = 0
                this.visual = robotImageGroup[robotSituationOfPlayer1]
            }
            showCorrectTextToDisplay(robotSituationOfPlayer1)
        }
        onMouseEntered = {
            showInformationOnHover.apply {
                isVisible = true
                posX = 1261.0
                posY = 300.0
                showCorrectTextToDisplay(robotSituationOfPlayer1)
            }
        }
        onMouseExited = {
            showInformationOnHover.isVisible = false
        }
    }


    val player1Input = TextField(
        width = 621, height = 91, posX = 649, posY = 215,
        font = Font(40, Color.BLACK), text = ""
    ).apply {
        onKeyTyped = {
            positionLogic.ifCanChangePosition()
            positionLogic.ifCanChangeColor()
        }
    }

    private val player2AIButton = Button(
        width = 91, height = 91, posX = 1291, posY = 317,
        visual = robotImageGroup[robotSituationOfPlayer2]
    ).apply {
        onMouseClicked = {
            if (robotSituationOfPlayer2 < 2) {
                robotSituationOfPlayer2++
                this.visual = robotImageGroup[robotSituationOfPlayer2]
            } else {
                robotSituationOfPlayer2 = 0
                this.visual = robotImageGroup[robotSituationOfPlayer2]
            }
            showCorrectTextToDisplay(robotSituationOfPlayer2)
        }
        onMouseEntered = {
            showInformationOnHover.apply {
                isVisible = true
                posX = 1261.0
                posY = 402.0
                showCorrectTextToDisplay(robotSituationOfPlayer2)
            }
        }
        onMouseExited = {
            showInformationOnHover.isVisible = false
        }
    }


    val player2Input = TextField(
        width = 621, height = 91, posX = 649, posY = 317,
        font = Font(40, Color.BLACK), text = ""
    ).apply {
        onKeyTyped = {
            /*playButton.isDisabled = p1Input.text.isBlank() || this.text.isBlank()*/
            if (this.text.isBlank()) {
                positionLogic.pruneNameOrder(1)
            } else {
                if (player1Input.text.isNotBlank()) {
                    playButton.isDisabled = false
                    playButton.visual = textures.openedBall
                    playButton.width = 293.50
                    playButton.posX = 813.25
                }
                player3Input.isVisible = true
                player3AIButton.isVisible = true
                deleteButtons[1].isVisible = true
                positionLogic.ifCanChangePosition()
                positionLogic.ifCanChangeColor()
            }
        }
    }

    private val player3AIButton = Button(
        width = 91, height = 91, posX = 1291, posY = 420,
        visual = robotImageGroup[robotSituationOfPlayer3]
    ).apply {
        onMouseClicked = {
            if (robotSituationOfPlayer3 < 2) {
                robotSituationOfPlayer3++
                this.visual = robotImageGroup[robotSituationOfPlayer3]
            } else {
                robotSituationOfPlayer3 = 0
                this.visual = robotImageGroup[robotSituationOfPlayer3]
            }
            showCorrectTextToDisplay(robotSituationOfPlayer3)
        }
        onMouseEntered = {
            showInformationOnHover.apply {
                isVisible = true
                posX = 1261.0
                posY = 505.0
                showCorrectTextToDisplay(robotSituationOfPlayer3)
            }
        }
        onMouseExited = {
            showInformationOnHover.isVisible = false
        }
    }


    private val player3Input = TextField(
        width = 621, height = 91, posX = 649, posY = 420,
        font = Font(40, Color.BLACK), text = ""
    ).apply {
        onKeyTyped = {
            if (this.text.isBlank()) {
                positionLogic.pruneNameOrder(2)
            } else {
                player4Input.isVisible = true
                player4AIButton.isVisible = true
                deleteButtons[2].isVisible = true
                positionLogic.ifCanChangePosition()
                positionLogic.ifCanChangeColor()
            }
        }
    }

    private val player4AIButton = Button(
        width = 91, height = 91, posX = 1291, posY = 523,
        visual = robotImageGroup[robotSituationOfPlayer4]
    ).apply {
        onMouseClicked = {
            if (robotSituationOfPlayer4 < 2) {
                robotSituationOfPlayer4++
                this.visual = robotImageGroup[robotSituationOfPlayer4]
            } else {
                robotSituationOfPlayer4 = 0
                this.visual = robotImageGroup[robotSituationOfPlayer4]
            }
            showCorrectTextToDisplay(robotSituationOfPlayer4)
        }
        onMouseEntered = {
            showInformationOnHover.apply {
                isVisible = true
                posX = 1261.0
                posY = 608.0
                showCorrectTextToDisplay(robotSituationOfPlayer4)
            }
        }
        onMouseExited = {
            showInformationOnHover.isVisible = false
        }
    }

    private val player4Input = TextField(
        width = 621, height = 91, posX = 649, posY = 523,
        font = Font(40, Color.BLACK), text = ""
    ).apply {
        onKeyTyped = {
            if (this.text.isNotBlank()) {
                deleteButtons[3].isVisible = true
            }
            positionLogic.ifCanChangePosition()
            positionLogic.ifCanChangeColor()
        }
    }

    val playButton = Button(
        width = 232, height = 232, posX = 844, posY = 721,
        visual = textures.closedBall, font = Font(128, Color.WHITE), text = ""
    ).apply {
        this.isDisabled = true
        onMouseClicked = {
            require(!hasSameColor()) {
                "The players should have different colors"
            }
            require(!hasSameName()) {
                "The players should have different name"
            }
        }
    }

    /**
     * adds the Logic associated to the [playButton] to start the game when the correct conditions are met
     */
    private fun addPlayButtonEvent() {
        playButton.apply {
            this.isDisabled = true
            onMouseClicked = {
                require(!hasSameColor()) {
                    "The players should have different colors"
                }
                require(!hasSameName()) {
                    "The players should have different name"
                }
                val players = ArrayDeque<Player>()
                if (player1Input.text.isNotBlank()) {
                    players.add(
                        Player(
                            name = player1Input.text, color = getColorFromSelection(colorOfPlayer1),
                            playerType = playerTypes[robotSituationOfPlayer1]
                        )
                    )
                }
                if (player2Input.text.isNotBlank()) {
                    players.add(
                        Player(
                            name = player2Input.text, color = getColorFromSelection(colorOfPlayer2),
                            playerType = playerTypes[robotSituationOfPlayer2]
                        )
                    )
                }
                if (player3Input.text.isNotBlank()) {
                    players.add(
                        Player(
                            name = player3Input.text, color = getColorFromSelection(colorOfPlayer3),
                            playerType = playerTypes[robotSituationOfPlayer3]
                        )
                    )
                }
                if (player4Input.text.isNotBlank()) {
                    players.add(
                        Player(
                            name = player4Input.text, color = getColorFromSelection(colorOfPlayer4),
                            playerType = playerTypes[robotSituationOfPlayer4]
                        )
                    )
                }

                pokaniApplication.showGameScene(pokaniApplication.gameScene)
                if (randomPlayers) {
                    players.shuffle()
                }
                pokaniApplication.rootService.pokaniService.initializeGame(players)
                pokaniApplication.hideMenuScene()
            }
        }
    }

    /*change position buttons*/
    private val positionChangeButtonOfPlayer1 = Button(
        width = 91, height = 91, posX = 534, posY = 214,
        visual = textures.directionDown
    ).apply { onMouseClicked = { positionLogic.changePosition(0) } }

    private val positionChangeButtonOfPlayer2 = Button(
        width = 91, height = 91, posX = 534, posY = 317,
        visual = textures.directionUp
    ).apply { onMouseClicked = { positionLogic.changePosition(1) } }

    private val positionChangeButtonOfPlayer3 = Button(
        width = 91, height = 91, posX = 534, posY = 420,
        visual = textures.directionUp
    ).apply { onMouseClicked = { positionLogic.changePosition(2) } }

    private val positionChangeButtonOfPlayer4 = Button(
        width = 91, height = 91, posX = 534, posY = 523,
        visual = textures.directionUp
    ).apply { onMouseClicked = { positionLogic.changePosition(3) } }

    private val positionChangeButtonOfPlayer2Up = Button(
        width = 91, height = 40, posX = 534, posY = 317,
        visual = textures.directionUp
    ).apply { onMouseClicked = { positionLogic.changePosition(4) } }

    private val positionChangeButtonOfPlayer2Down = Button(
        width = 91, height = 40, posX = 534, posY = 368,
        visual = textures.directionDown
    ).apply { onMouseClicked = { positionLogic.changePosition(5) } }

    private val positionChangeButtonOfPlayer3Up = Button(
        width = 91, height = 40, posX = 534, posY = 420,
        visual = textures.directionUp
    ).apply { onMouseClicked = { positionLogic.changePosition(6) } }

    private val positionChangeButtonOfPlayer3Down = Button(
        width = 91, height = 40, posX = 534, posY = 471,
        visual = textures.directionDown
    ).apply { onMouseClicked = { positionLogic.changePosition(7) } }


    /*change position buttons*/
    val listOfColorButtons = listOf(
        player1ColorButton,
        player2ColorButton,
        player3ColorButton,
        player4ColorButton
    )
    val listOfInputs = listOf(player1Input, player2Input, player3Input, player4Input)
    val listOfAIButtons = listOf(player1AIButton, player2AIButton, player3AIButton, player4AIButton)
    val listOfChangePButtons = listOf(
        positionChangeButtonOfPlayer1, positionChangeButtonOfPlayer2,
        positionChangeButtonOfPlayer3, positionChangeButtonOfPlayer4,
        positionChangeButtonOfPlayer2Up, positionChangeButtonOfPlayer2Down,
        positionChangeButtonOfPlayer3Up, positionChangeButtonOfPlayer3Down
    )

    /**
     * check whether the Players have chosen the same Color
     *
     * @return returns a Boolean depending on the outcome
     */
    private fun hasSameColor(): Boolean {
        val player3Blank = player3Input.text.isBlank()
        val player4Blank = player4Input.text.isBlank()
        return when {
            player3Blank -> colorOfPlayer1 == colorOfPlayer2
            player4Blank -> colorOfPlayer1 == colorOfPlayer2 || colorOfPlayer1 == colorOfPlayer3 ||
                    colorOfPlayer2 == colorOfPlayer3

            else -> colorOfPlayer1 == colorOfPlayer2 || colorOfPlayer1 == colorOfPlayer3 ||
                    colorOfPlayer1 == colorOfPlayer4 || colorOfPlayer2 == colorOfPlayer3 ||
                    colorOfPlayer2 == colorOfPlayer4 || colorOfPlayer3 == colorOfPlayer4
        }
    }

    /**
     * checks whether players have the same Name
     *
     * @return returns a Boolean depending on the outcome
     */
    private fun hasSameName(): Boolean {
        val inputs = listOf(player1Input.text, player2Input.text, player3Input.text, player4Input.text).filter {
            it.isNotBlank()
        }
        return inputs.distinct().size != inputs.size
    }

    private val randomPlayerOrderCheckBox = Label(
        posX = 650,
        posY = 650,
        width = 60,
        height = 60,
        font = Font(size = 40, family = "Bauhaus 93", color = Color.WHITE),
        alignment = Alignment.CENTER_LEFT,
        visual = textures.checkBoxEmpty
    ).apply {
        onMouseClicked = {
            if (randomPlayers) {
                visual = textures.checkBoxEmpty
                randomPlayers = false
            } else {
                visual = textures.confirmButton
                randomPlayers = true
            }
        }
    }

    private val randomPlayerOrderLabel = Label (
        posX = 710,
        posY = 650,
        width = 420,
        height = 60,
        text = "Random player order",
        font = Font(size = 40, family = "Bauhaus 93", color = Color.WHITE),
        alignment = Alignment.CENTER,
        visual = ColorVisual(0,0,0, 100)
    )

    init {
        background = textures.localGameNewMenuBackGround
        opacity = 1.0
        square.opacity = 0.6
        listOfColorButtons.forEach { it.isVisible = false }
        listOfChangePButtons.forEach { it.isVisible = false }
        listOfAIButtons.forEachIndexed { index, button ->
            if (index >= 2) button.isVisible = false
        }
        deleteButtons.forEachIndexed { index, button ->
            button.isVisible = false
            button.onMouseClicked = {
                if (index == 4) {
                    player4Input.text = ""
                    deleteButtons[3].isVisible = false
                    positionLogic.ifCanChangePosition()
                    positionLogic.ifCanChangeColor()
                } else positionLogic.pruneNameOrder(index)
            }
        }
        positionChangeButtonOfPlayer2Up.isVisible = false
        positionChangeButtonOfPlayer2Down.isVisible = false
        positionChangeButtonOfPlayer3Up.isVisible = false
        positionChangeButtonOfPlayer3Down.isVisible = false

        player1Input.onKeyTyped = {
            playButton.isDisabled = player1Input.text.isBlank() || player2Input.text.isBlank()
            if (!playButton.isDisabled) {
                playButton.visual = textures.openedBall
                playButton.width = 293.50
                playButton.posX = 813.25
            }
            if (player1Input.text.isBlank()) {
                positionLogic.pruneNameOrder(0)
            } else {
                deleteButtons[0].isVisible = true
            }
        }

        addComponents(
            square, positionChangeButtonOfPlayer1, positionChangeButtonOfPlayer2,
            positionChangeButtonOfPlayer3, positionChangeButtonOfPlayer4, positionChangeButtonOfPlayer2Up,
            positionChangeButtonOfPlayer2Down, positionChangeButtonOfPlayer3Up, positionChangeButtonOfPlayer3Down,
            headlineLabel, player1AIButton, deleteButtons[0], player1Input, player2AIButton, deleteButtons[1],
            player2Input, player3AIButton, deleteButtons[2], player3Input, player4AIButton, deleteButtons[3],
            player4Input, playButton, backButton, player1ColorButton, player2ColorButton, player3ColorButton,
            player4ColorButton, showInformationOnHover, randomPlayerOrderCheckBox, randomPlayerOrderLabel
        )
        addPlayButtonEvent()
    }
}