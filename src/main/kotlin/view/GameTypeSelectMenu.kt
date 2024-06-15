package view

import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import java.awt.Color
import javax.imageio.ImageIO

/**
 * [MenuScene] that allows to choose whether the User want to play an Online or a Local game
 */
class GameTypeSelectMenu : MenuScene(1920, 1080, ColorVisual(88, 88, 88)), Refreshable {
    private val backGround = ImageVisual(
        ImageIO.read(
            LocalGameNewMenu::class.java.getResource
                ("/start_scene/back.png")
        )
    )

    val button = ImageVisual(
        ImageIO.read(
            LocalGameNewMenu::class.java.getResource
                ("/start_scene/MenuButton.png")
        )
    )

    private val headlineLabel = Label(
        width = 409,
        height = 142,
        posX = 753,
        posY = -15,
        text = "Pokani",
        font = Font(size = 128, color = Color.BLACK, family = "Bauhaus 93")
    )

    private val square = Label(
        width = 1803,
        height = 895,
        posX = 58,
        posY = 133,
        visual = ColorVisual(42, 42, 42)
    )

    val onlineButton = Button(
        width = 591,
        height = 245,
        posX = 280,
        posY = 418,
        visual = button,
        font = Font(128, Color.WHITE, family = "Bauhaus 93"),
        text = "Online"
    )

    val localButton = Button(
        width = 591,
        height = 245,
        posX = 1049,
        posY = 418,
        visual = button,
        font = Font(128, Color.WHITE, family = "Bauhaus 93"),
        text = "Local"
    )

    init {
        background = backGround
        square.opacity = 0.6
        localButton.opacity = 0.7
        onlineButton.opacity = 0.7
        opacity = 1.0
        addComponents(
            square,
            localButton,
            onlineButton,
            headlineLabel,
        )
    }
}