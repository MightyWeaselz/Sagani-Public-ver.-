package view

import entity.Player
import entity.PlayerType
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.TextField
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.event.KeyEvent
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import java.awt.Color
import javax.imageio.ImageIO

/**
 * The Menu that will be displayed when the User plays an Online Game and is the Host.
 * The Scene will show the joined Players and the option to change their order and color.
 */
class OnlineHostLobbyMenu(
    private val pokaniApplication: PokaniApplication
) : MenuScene(1920, 1080, ColorVisual(88, 88, 88)), Refreshable {

    private var randomPlayers = false

    private val textures = Textures()

    private val back = ImageVisual(
        ImageIO.read(
            LocalGameNewMenu::class.java.getResource
                ("/start_scene/return.png")
        )
    )

    private val closedBall = ImageVisual(
        ImageIO.read(
            LocalGameNewMenu::class.java.getResource
                ("/start_scene/closed_ball.png")
        )
    )

    private val openedBall = ImageVisual(
        ImageIO.read(
            LocalGameNewMenu::class.java.getResource
                ("/start_scene/opened_ball.png")
        )
    )

    private val backGround = ImageVisual(
        ImageIO.read(
            LocalGameNewMenu::class.java.getResource
                ("/start_scene/hostback.png")
        )
    )

    private val directionDown = ImageVisual(
        ImageIO.read(
            LocalGameNewMenu::class.java.getResource
                ("/start_scene/dreieckdown.png")
        )
    )

    private val directionUp = ImageVisual(
        ImageIO.read(
            LocalGameNewMenu::class.java.getResource
                ("/start_scene/dreieck.png")
        )
    )

    /*Players Inputs*/
    val player1Input = TextField(
        width = 621,
        height = 91,
        posX = 649,
        posY = 215,
        font = Font(40, Color.BLACK),
        text = ""
    ).apply {
        isDisabled = true
    }

    private val player2Input = TextField(
        width = 621,
        height = 91,
        posX = 649,
        posY = 317,
        font = Font(40, Color.BLACK),
        text = ""
    ).apply {
        onKeyTyped = {
            /*playButton.isDisabled = p1Input.text.isBlank() || this.text.isBlank()*/
            if (this.text.isBlank()) {
                pruneNameOrder(1)
            } else {
                ifCanChangePosition()
                ifCanChangeColor()
                if (player1Input.text.isNotBlank()) {
                    enableStarting()
                }
                player3Input.isVisible = true
            }
        }
        isDisabled = true
    }

    private val player3Input = TextField(
        width = 621,
        height = 91,
        posX = 649,
        posY = 420,
        font = Font(40, Color.BLACK),
        text = ""
    ).apply {
        onKeyTyped = {
            if (this.text.isBlank()) {
                pruneNameOrder(2)
            } else {
                ifCanChangePosition()
                ifCanChangeColor()
                player4Input.isVisible = true
            }
        }
        isDisabled = true
    }

    private val player4Input = TextField(
        width = 621,
        height = 91,
        posX = 649,
        posY = 523,
        font = Font(40, Color.BLACK),
        text = ""
    ).apply {
        onKeyTyped = {
            ifCanChangePosition()
            ifCanChangeColor()
        }
        isDisabled = true
    }
    /*Players Inputs*/

    val listOfInputs = listOf(player1Input, player2Input, player3Input, player4Input)

    /*Color Selections*/
    private val listOfColors = listOf<Color>(Color.BLACK, Color.DARK_GRAY, Color.ORANGE, Color.LIGHT_GRAY)
    private var colorOfPlayer1 = 0
    private var colorOfPlayer2 = 0
    private var colorOfPlayer3 = 0
    private var colorOfPlayer4 = 0

    private val colorButtonOfPlayer1 = Button(
        posX = 1189,
        posY = 224,
        width = 71,
        height = 71,
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

    private val colorButtonOfPlayer2 = Button(
        posX = 1189,
        posY = 327,
        width = 71,
        height = 71,
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

    private val colorButtonOfPlayer3 = Button(
        posX = 1189,
        posY = 430,
        width = 71,
        height = 71,
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

    private val colorButtonOfPlayer4 = Button(
        posX = 1189,
        posY = 533,
        width = 71,
        height = 71,
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

    /*Color Selections*/
    /*normal things*/
    private val headlineLabel = Label(
        width = 409,
        height = 142,
        posX = 755,
        posY = 0,
        text = "Lobby",
        font = Font(size = 128, color = Color.BLACK)
    )

    private val square = Label(
        width = 1803,
        height = 895,
        posX = 58,
        posY = 133,
        visual = ColorVisual(42, 42, 42)
    )

    val backButton = Button(
        width = 116,
        height = 116,
        text = "",
        font = Font(64, Color.ORANGE),
        posX = 58,
        posY = 133,
        visual = back
    )

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

    private fun getPlayerType(name: String): PlayerType {
        return if (name == pokaniApplication.rootService.networkService.client!!.playerName) {
            pokaniApplication.rootService.networkService.client!!.playerType
        } else {
            PlayerType.REMOTE_PLAYER
        }
    }

    private val playButton = Button(
        width = 232,
        height = 232,
        posX = 844,
        posY = 721,
        visual = closedBall,
        font = Font(128, Color.WHITE),
        text = ""
    ).apply {
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
                        name = player1Input.text,
                        color = getColorFromSelection(colorOfPlayer1),
                        playerType = getPlayerType(player1Input.text)
                    )
                )
            }
            if (player2Input.text.isNotBlank()) {
                players.add(
                    Player(
                        name = player2Input.text,
                        color = getColorFromSelection(colorOfPlayer2),
                        playerType = getPlayerType(player2Input.text)
                    )
                )

            }
            if (player3Input.text.isNotBlank()) {
                players.add(
                    Player(
                        name = player3Input.text,
                        color = getColorFromSelection(colorOfPlayer3),
                        playerType = getPlayerType(player3Input.text)
                    )
                )
            }
            if (player4Input.text.isNotBlank()) {
                players.add(
                    Player(
                        name = player4Input.text,
                        color = getColorFromSelection(colorOfPlayer4),
                        playerType = getPlayerType(player4Input.text)
                    )
                )
            }

            this@OnlineHostLobbyMenu.pokaniApplication.showGameScene(pokaniApplication.gameScene)
            if (randomPlayers) {
                players.shuffle()
            }
            this@OnlineHostLobbyMenu.pokaniApplication.rootService.pokaniService.initializeGame(players)
            this@OnlineHostLobbyMenu.pokaniApplication.hideMenuScene()

            listOfInputs.forEach {
                it.text = ""
            }
        }
    }
    /*normal things*/

    private fun enableStarting() {
        playButton.isDisabled = false
        playButton.visual = openedBall
        playButton.width = 293.50
        playButton.posX = 813.25
    }

    /*change position buttons*/
    private val positionChangeButtonOfPlayer1 = Button(
        width = 91,
        height = 91,
        posX = 534,
        posY = 214,
        visual = directionDown
    ).apply { onMouseClicked = { changeP(0) } }

    private val positionChangeButtonOfPlayer2 = Button(
        width = 91,
        height = 91,
        posX = 534,
        posY = 317,
        visual = directionUp
    ).apply { onMouseClicked = { changeP(1) } }

    private val positionChangeButtonOfPlayer3 = Button(
        width = 91,
        height = 91,
        posX = 534,
        posY = 420,
        visual = directionUp
    ).apply { onMouseClicked = { changeP(2) } }

    private val positionChangeButtonOfPlayer4 = Button(
        width = 91,
        height = 91,
        posX = 534,
        posY = 523,
        visual = directionUp
    ).apply { onMouseClicked = { changeP(3) } }

    private val positionChangeButtonOfPlayer2Up = Button(
        width = 91,
        height = 40,
        posX = 534,
        posY = 317,
        visual = directionUp
    ).apply { onMouseClicked = { changeP(4) } }

    private val positionChangeButtonOfPlayer2Down = Button(
        width = 91,
        height = 40,
        posX = 534,
        posY = 368,
        visual = directionDown
    ).apply { onMouseClicked = { changeP(5) } }

    private val positionChangeButtonOfPlayer3Up = Button(
        width = 91,
        height = 40,
        posX = 534,
        posY = 420,
        visual = directionUp
    ).apply { onMouseClicked = { changeP(6) } }

    private val positionChangeButtonOfPlayer3Down = Button(
        width = 91,
        height = 40,
        posX = 534,
        posY = 471,
        visual = directionDown
    ).apply { onMouseClicked = { changeP(7) } }

    /*change position buttons*/

    val listOfColorButtons = listOf(
        colorButtonOfPlayer1, colorButtonOfPlayer2,
        colorButtonOfPlayer3, colorButtonOfPlayer4
    )

    private val listOfChangePButtons = listOf(
        positionChangeButtonOfPlayer1, positionChangeButtonOfPlayer2, positionChangeButtonOfPlayer3,
        positionChangeButtonOfPlayer4, positionChangeButtonOfPlayer2Up, positionChangeButtonOfPlayer2Down,
        positionChangeButtonOfPlayer3Up, positionChangeButtonOfPlayer3Down
    )

    /**
     * shows the correct "change Position Buttons" depending on how many Players are in the Lobby
     */
    fun ifCanChangePosition() {
        for (i in 0..3) {
            if (listOfInputs[i].text.isBlank()) {
                listOfChangePButtons[i].isVisible = false
                if (i == 1) {
                    listOfChangePButtons[4].isVisible = false
                    listOfChangePButtons[5].isVisible = false
                }
                if (i == 2) {
                    listOfChangePButtons[6].isVisible = false
                    listOfChangePButtons[7].isVisible = false
                }
            } else if (i == 1 || i == 2) {
                val j = if (i == 1) 4 else 6
                val isVisible = listOfInputs[i + 1].text.isNotBlank()
                listOfChangePButtons[j + 1].isVisible = isVisible
                listOfChangePButtons[i].isVisible = !isVisible
                listOfChangePButtons[j].isVisible = isVisible
            } else {
                listOfChangePButtons[i].isVisible = true
            }
        }
    }

    private fun hasSameColor(): Boolean {
        if (player3Input.text.isBlank()) {
            return colorOfPlayer1 == colorOfPlayer2
        }
        if (player4Input.text.isBlank()) {
            return colorOfPlayer1 == colorOfPlayer2 || colorOfPlayer1 == colorOfPlayer3 ||
                    colorOfPlayer2 == colorOfPlayer3
        }
        if (player4Input.text.isNotBlank()) {
            return colorOfPlayer1 == colorOfPlayer2 || colorOfPlayer1 == colorOfPlayer3 ||
                    colorOfPlayer1 == colorOfPlayer4 || colorOfPlayer2 == colorOfPlayer3 ||
                    colorOfPlayer2 == colorOfPlayer4 || colorOfPlayer3 == colorOfPlayer4
        }
        return false
    }

    private fun hasSameName(): Boolean {
        if (player3Input.text.isBlank()) {
            return player1Input.text == player2Input.text
        }
        if (player4Input.text.isBlank()) {
            return player1Input.text == player2Input.text || player2Input.text == player3Input.text
                    || player1Input.text == player3Input.text
        }
        if (player4Input.text.isNotBlank()) {
            return player1Input.text == player2Input.text || player1Input.text == player3Input.text
                    || player1Input.text == player4Input.text || player2Input.text == player3Input.text
                    || player2Input.text == player4Input.text || player3Input.text == player4Input.text
        }
        return false
    }

    private fun changeP(index: Int) {
        if (index == 0) {
            listOfInputs[0].text = listOfInputs[1].text.also { listOfInputs[1].text = listOfInputs[0].text }
            listOfColorButtons[0].visual = listOfColorButtons[1].visual.also {
                listOfColorButtons[1].visual = listOfColorButtons[0].visual
            }
        }
        if (index in 1..3) {
            listOfInputs[index].text = listOfInputs[index - 1].text.also {
                listOfInputs[index - 1].text = listOfInputs[index].text
            }
            listOfColorButtons[index].visual = listOfColorButtons[index - 1].visual.also {
                listOfColorButtons[index - 1].visual = listOfColorButtons[index].visual
            }

        }
        if (index == 4) {
            changeP(1)
        }
        if (index == 6) {
            changeP(2)
        }
        if (index == 5) {
            changeP(2)
        }
        if (index == 7) {
            changeP(3)
        }
    }

    fun ifCanChangeColor() {
        for (i in 0..3) {
            listOfColorButtons[i].isVisible = listOfInputs[i].text.isNotBlank()
        }
    }

    fun pruneNameOrder(index: Int) {
        for (i in index until 3) {
            listOfInputs[i].text = listOfInputs[i + 1].text
        }
        listOfInputs[3].text = ""
        repeat(3) {
            if (listOfInputs[it].text.isBlank() && it > 0) {
                listOfInputs[it + 1].isVisible = false

            }
        }
        if (player1Input.text.isNotBlank() && player2Input.text.isNotBlank()) {
            playButton.isDisabled = false
            playButton.visual = openedBall
            playButton.width = 293.50
            playButton.posX = 813.25
        } else {
            playButton.isDisabled = true
            playButton.width = 232.00
            playButton.height = 232.00
            playButton.posX = 844.00
            playButton.posY = 721.00
            playButton.visual = closedBall
        }
        ifCanChangePosition()
        ifCanChangeColor()
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
        square.opacity = 0.6
        opacity = 1.0
        background = backGround
        listOfColorButtons.forEach { it.isVisible = false }
        player3Input.isVisible = false
        player4Input.isVisible = false
        listOfChangePButtons.forEach { it.isVisible = false }

        player1Input.onKeyTyped = {
            playButton.isDisabled = player1Input.text.isBlank() || player2Input.text.isBlank()
            if (!playButton.isDisabled) {
                playButton.visual = openedBall
                playButton.width = 293.50
                playButton.posX = 813.25
            }
            if (player1Input.text.isBlank()) {
                pruneNameOrder(0)
            }
        }

        addComponents(
            square,
            positionChangeButtonOfPlayer1,
            positionChangeButtonOfPlayer2,
            positionChangeButtonOfPlayer3,
            positionChangeButtonOfPlayer4,
            positionChangeButtonOfPlayer2Up,
            positionChangeButtonOfPlayer2Down,
            positionChangeButtonOfPlayer3Up,
            positionChangeButtonOfPlayer3Down,
            headlineLabel,
            player1Input,
            player2Input,
            player3Input,
            player4Input,
            colorButtonOfPlayer1,
            colorButtonOfPlayer2,
            colorButtonOfPlayer3,
            colorButtonOfPlayer4,
            playButton,
            backButton,
            randomPlayerOrderCheckBox,
            randomPlayerOrderLabel
        )
    }

    override fun refreshOnOnlinePlayerJoin(s: String) {
        val playerInputs = listOf(player2Input, player3Input, player4Input)

        playerInputs.forEach { playerInput ->
            if (playerInput.text.isEmpty()) {
                playerInput.text = s
                playerInput.onKeyTyped?.let {
                    it(KeyEvent(isAltDown = false, isControlDown = false, isShiftDown = false))
                }
                ifCanChangePosition()
                ifCanChangeColor()
                return
            }
        }
    }

    override fun refreshOnOnlinePlayerLeft(s: String) {
        val playerInputs = listOf(player1Input, player2Input, player3Input, player4Input)

        playerInputs.forEach { playerInput ->
            if (playerInput.text == s) {
                playerInput.text = ""
                playerInput.onKeyTyped?.let {
                    it(KeyEvent(isAltDown = false, isControlDown = false, isShiftDown = false))
                }
                ifCanChangePosition()
                ifCanChangeColor()
                return
            }
        }
    }
}