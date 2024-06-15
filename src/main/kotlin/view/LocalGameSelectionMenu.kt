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
 * [MenuScene] that allows the option of choosing whether the saved game should be loaded or
 * a new local game should be started
 */
class LocalGameSelectionMenu : MenuScene(1920, 1080, ColorVisual(88, 88, 88)), Refreshable {
    val back = ImageVisual(
        ImageIO.read(
            LocalGameNewMenu::class.java.getResource
                ("/start_scene/return.png")
        )
    )

    val backGround = ImageVisual(
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
        posX = 755,
        posY = 0,
        text = "Local",
        font = Font(size = 128, color = Color.BLACK, family = "Bauhaus 93")
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

    val newGameButton = Button(
        width = 591,
        height = 245,
        posX = 280,
        posY = 418,
        visual = button,
        font = Font(64, Color.WHITE, family = "Bauhaus 93"),
        text = "New Game"
    )

    val loadGameButton = Button(
        width = 591,
        height = 245,
        posX = 1049,
        posY = 418,
        visual = button,
        font = Font(64, Color.WHITE, family = "Bauhaus 93"),
        text = "Load Game"
    )

    init {
        background = backGround
        newGameButton.opacity = 0.7
        loadGameButton.opacity = 0.7
        square.opacity = 0.6
        opacity = 1.0
        addComponents(
            square,
            headlineLabel,
            newGameButton,
            loadGameButton,
            backButton

        )
    }
}