package view


import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.event.KeyEvent
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import java.awt.Color
import javax.imageio.ImageIO

/**
 * This Scene is shown when a User joins an Online Lobby. He will be able to see the other joined players.
 */
class OnlineJoinLobbyMenu : MenuScene(1920, 1080, ColorVisual(88, 88, 88)), Refreshable {

    private val backGround = ImageVisual(
        ImageIO.read(
            LocalGameNewMenu::class.java.getResource
                ("/start_scene/joinback.jpg")
        )
    )

    val back = ImageVisual(
        ImageIO.read(
            LocalGameNewMenu::class.java.getResource
                ("/start_scene/return.png")
        )
    )

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

    /*Players Inputs*/
    val player1Input = Label(
        width = 621,
        height = 91,
        posX = 649,
        posY = 215,
        visual = ColorVisual.GRAY,
        font = Font(40, Color.BLACK),
        text = ""
    )

    private val player2Input = Label(
        width = 621,
        height = 91,
        posX = 649,
        posY = 317,
        visual = ColorVisual.GRAY,
        font = Font(40, Color.BLACK),
        text = ""
    )

    private val player3Input = Label(
        width = 621,
        height = 91,
        posX = 649,
        posY = 420,
        visual = ColorVisual.GRAY,
        font = Font(40, Color.BLACK),
        text = ""
    )

    private val player4Input = Label(
        width = 621,
        height = 91,
        posX = 649,
        posY = 523,
        visual = ColorVisual.GRAY,
        font = Font(40, Color.BLACK),
        text = ""
    )

    val listOfInputs = listOf(player1Input,player2Input,player3Input,player4Input)
    init {
        opacity = 1.0
        square.opacity = 0.6
        background = backGround

        addComponents(
            square,
            headlineLabel,
            player1Input,
            player2Input,
            player3Input,
            player4Input,
            backButton
        )
    }

    override fun refreshOnOnlinePlayerJoin(s: String) {
        if (player2Input.text.isEmpty()) {
            player2Input.text = s
            player2Input.onKeyTyped?.let { it(KeyEvent(isAltDown = false, isControlDown = false, isShiftDown = false)) }
            return
        }
        if (player3Input.text.isEmpty()) {
            player3Input.text = s
            player3Input.onKeyTyped?.let { it(KeyEvent(isAltDown = false, isControlDown = false, isShiftDown = false)) }
            return
        }
        if (player4Input.text.isEmpty()) {
            player4Input.text = s
            player4Input.onKeyTyped?.let { it(KeyEvent(isAltDown = false, isControlDown = false, isShiftDown = false)) }
            return
        }
    }

    override fun refreshOnOnlinePlayerLeft(s: String) {
        if (player1Input.text == s) {
            player1Input.text = ""
            player1Input.onKeyTyped?.let { it(KeyEvent(isAltDown = false, isControlDown = false, isShiftDown = false)) }
            return
        }
        if (player2Input.text == s) {
            player2Input.text = ""
            player2Input.onKeyTyped?.let { it(KeyEvent(isAltDown = false, isControlDown = false, isShiftDown = false)) }
            return
        }
        if (player3Input.text == s) {
            player3Input.text = ""
            player3Input.onKeyTyped?.let { it(KeyEvent(isAltDown = false, isControlDown = false, isShiftDown = false)) }
            return
        }
        if (player4Input.text == s) {
            player4Input.text = ""
            player4Input.onKeyTyped?.let { it(KeyEvent(isAltDown = false, isControlDown = false, isShiftDown = false)) }
            return
        }
    }
}