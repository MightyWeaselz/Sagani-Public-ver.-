package view


import entity.PlayerType
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.TextField
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import java.awt.Color
import javax.imageio.ImageIO

/**#
 * This [MenuScene] is shown when the User want to play an Online Game. He can Input his Name, the SessionID and his
 * [PlayerType].
 */
class OnlineMenu(private val pokaniApplication: PokaniApplication) :
    MenuScene(1920, 1080, ColorVisual(88, 88, 88)),
    Refreshable {

    private val join = ImageVisual(
        ImageIO.read(
            LocalGameNewMenu::class.java.getResource
                ("/start_scene/MenuButton.png")
        )
    )

    private val back = ImageVisual(
        ImageIO.read(
            LocalGameNewMenu::class.java.getResource
                ("/start_scene/return.png")
        )
    )

    private val backGround = ImageVisual(
        ImageIO.read(
            LocalGameNewMenu::class.java.getResource
                ("/start_scene/pika.jpg")
        )
    )

    private val robotOn = ImageVisual(
        ImageIO.read(
            LocalGameNewMenu::class.java.getResource
                ("/start_scene/wakeup.png")
        )
    )

    private val robotOff = ImageVisual(
        ImageIO.read(
            LocalGameNewMenu::class.java.getResource
                ("/start_scene/sleeped_kabigon.png")
        )
    )

    private val robotHard = ImageVisual(
        ImageIO.read(
            LocalGameNewMenu::class.java.getResource
                ("/start_scene/botHardDifficulty.png")
        )
    )

    private val robotImageGroup = listOf(robotOff, robotOn, robotHard)
    private var robotSituation = 0

    private val showInformationOnHover = Label(
        posX = -500,
        posY = -500,
        width = 150,
        height = 35,
        text = "",
        font = Font(size = 20, family = "Bauhaus 93"),
        visual = ColorVisual.LIGHT_GRAY
    )

    private val headlineLabel = Label(
        width = 800,
        height = 142,
        posX = 560,
        posY = 0,
        text = "Online Game",
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

    private val playerNameLabel = Label(
        width = 417,
        height = 77,
        posX = 355,
        posY = 327,
        font = Font(64, Color.WHITE),
        text = "Player Name :"
    )

    val playerName = TextField(
        width = 621,
        height = 91,
        posX = 797,
        posY = 327,
        font = Font(58, Color.BLACK),
        text = ""
    ).apply {
        onKeyTyped = {
            joinGroupButtonDisable()
        }
    }

    private val sessionIdLabel = Label(
        width = 335,
        height = 77,
        posX = 445,
        posY = 555,
        font = Font(58, Color.WHITE),
        text = "SessionID :"
    )

    private val sessionId = TextField(
        width = 621,
        height = 91,
        posX = 797,
        posY = 555,
        font = Font(58, Color.BLACK),
        text = ""
    ).apply {
        onKeyTyped = {
            joinGroupButtonDisable()
        }
    }

    val joinGroupButton = Button(
        width = 500,
        height = 231,
        posX = 1035,
        posY = 721,
        visual = join,
        text = "Join",
        font = Font(90, Color.ORANGE)
    ).apply {
        this.isDisabled = true
    }

    val hostGroupButton = Button(
        width = 500,
        height = 231,
        posX = 385,
        posY = 721,
        visual = join,
        text = "Host",
        font = Font(90, Color.ORANGE)
    ).apply {
        this.isDisabled = true
    }

    private val playerAIButton = Button(
        width = 91,
        height = 91,
        posX = 1440,
        posY = 340,
        visual = robotImageGroup[robotSituation]
    ).apply {
        onMouseClicked = {
            if (robotSituation < 2) {
                robotSituation++
                this.visual = robotImageGroup[robotSituation]
            } else {
                robotSituation = 0
                this.visual = robotImageGroup[robotSituation]
            }
            showCorrectTextToDisplay(robotSituation)
        }
        onMouseEntered = {
            showInformationOnHover.apply {
                isVisible = true
                posX = 1261.0
                posY = 300.0
                showCorrectTextToDisplay(robotSituation)
            }
        }
        onMouseExited = {
            showInformationOnHover.isVisible = false
        }
    }

    private fun showCorrectTextToDisplay(playerState: Int) {
        showInformationOnHover.text = when (playerState) {
            0 -> "Human Player"
            1 -> "Easy Bot"
            else -> "Hard Bot"
        }
    }

    private fun getPlayerTypeByState(playerState: Int): PlayerType {
        return when (playerState) {
            0 -> PlayerType.LOCAL_PLAYER
            1 -> PlayerType.RANDOM_BOT
            else -> PlayerType.SMART_BOT
        }
    }

    private fun joinGroupButtonDisable() {
        joinGroupButton.isDisabled = sessionId.text.isBlank() || playerName.text.isBlank()
        hostGroupButton.isDisabled = sessionId.text.isBlank() || playerName.text.isBlank()
    }

    fun joinGroup() {
        println(getPlayerTypeByState(robotSituation))
        pokaniApplication.rootService.networkService.joinGame(
            playerName.text, sessionId.text, getPlayerTypeByState(robotSituation)
        )
        joinGroupButton.isDisabled = true
        hostGroupButton.isDisabled = true
    }

    fun hostGroup() {
        print("Hosting")
        pokaniApplication.rootService.networkService.hostGame(
            playerName.text, sessionId.text,
            getPlayerTypeByState(robotSituation)
        )
        joinGroupButton.isDisabled = true
        hostGroupButton.isDisabled = true
    }

    init {
        opacity = 1.0
        square.opacity = 0.6
        background = backGround
        addComponents(
            square,
            headlineLabel,
            backButton,
            joinGroupButton,
            hostGroupButton,
            playerName,
            playerNameLabel,
            sessionId,
            sessionIdLabel,
            playerAIButton
        )
    }
}