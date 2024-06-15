package view.gamescene

import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.CompoundVisual
import tools.aqua.bgw.visual.ImageVisual
import view.LocalGameNewMenu
import view.PokaniApplication
import view.Refreshable
import view.Textures
import java.awt.Color
import javax.imageio.ImageIO

/**
 * [MenuScene] representing the pause menu of the game. It can be called by pressing Esc in the [GameScene]
 */
class GameScenePauseMenu(private val pokaniApplication: PokaniApplication) : MenuScene(800, 1080), Refreshable {

    private val textures = Textures()

    private val backGround = ImageVisual(
        ImageIO.read(
            LocalGameNewMenu::class.java.getResource
                ("/pause.jpg")
        )
    )

    val button = ImageVisual(
        ImageIO.read(
            LocalGameNewMenu::class.java.getResource
                ("/start_scene/MenuButton.png")
        )
    )

    private val headlineLabel = Label(
        width = 600,
        height = 120,
        posX = 100,
        posY = 0,
        text = "Game Paused",
        font = Font(size = 80, color = Color.WHITE, family = "Bauhaus 93")
    )

    val saveButton = Button(
        width = 350,
        height = 200,
        posX = 50,
        posY = 218,
        visual = button,
        font = Font(40, Color.WHITE, family = "Bauhaus 93"),
        text = "Save Game"
    )

    val saveDialog = Label(
        posX = 50,
        posY = 415,
        width = 700,
        height = 80,
        font = Font(30, Color.WHITE, family = "Bauhaus 93"),
        text = ""
    )

    val continueButton = Button(
        width = 350,
        height = 200,
        posX = 400,
        posY = 218,
        visual = button,
        font = Font(40, Color.WHITE, family = "Bauhaus 93"),
        text = "Continue"
    )

    val mainMenuButton = Button(
        width = 350,
        height = 200,
        posX = 225,
        posY = 500,
        visual = button,
        font = Font(40, Color.WHITE, family = "Bauhaus 93"),
        text = "Main Menu"
    )

    val simDelayLabel = Label(
        posX = 150,
        posY = 900,
        width = 500,
        height = 100,
        text = "SimDelay: ${pokaniApplication.gameScene.simDelay}ms",
        font = Font(size = 50, family = "Bauhaus 93", color = Color.WHITE),
        visual = ColorVisual(0, 0, 0, 100),
        alignment = Alignment.CENTER
    )

    private val simDelayDownButton: Label = Label(
        posX = 42, posY = 800, width = 210, height = 80,
        font = Font(size = 40, family = "Bauhaus 93", color = Color.WHITE),
        alignment = Alignment.CENTER, text = "➖",
        visual = CompoundVisual(ColorVisual(0, 0, 0, 100), textures.delayUp)
    ).apply {
        onMouseClicked = {
            if (pokaniApplication.gameScene.simDelay <= 200) {
                pokaniApplication.gameScene.simDelay = 200
            } else {
                pokaniApplication.gameScene.simDelay -= 200
            }
            simDelayLabel.text = "SimDelay: ${pokaniApplication.gameScene.simDelay}ms"
        }
    }

    private val simDelayResetButton: Label = Label(
        posX = 294, posY = 800, width = 210, height = 80,
        font = Font(size = 40, family = "Bauhaus 93", color = Color.WHITE),
        alignment = Alignment.CENTER, text = "\uD83D\uDD01",
        visual = CompoundVisual(ColorVisual(0, 0, 0, 100), textures.delayReset)
    ).apply {
        onMouseClicked = {
            pokaniApplication.gameScene.simDelay = 1000
            simDelayLabel.text = "SimDelay: ${pokaniApplication.gameScene.simDelay}ms"
        }
    }

    private val simDelayUpButton: Label = Label(
        posX = 546, posY = 800, width = 210, height = 80,
        font = Font(size = 40, family = "Bauhaus 93", color = Color.WHITE),
        alignment = Alignment.CENTER, text = "➕",
        visual = CompoundVisual(ColorVisual(0, 0, 0, 100), textures.delayDown)
    ).apply {
        onMouseClicked = {
            if (pokaniApplication.gameScene.simDelay >= 10000) {
                pokaniApplication.gameScene.simDelay = 10000
            } else {
                pokaniApplication.gameScene.simDelay += 200
            }
            simDelayLabel.text = "SimDelay: ${pokaniApplication.gameScene.simDelay}ms"
        }
    }

    init {
        saveDialog.isVisible = false
        background = backGround
        addComponents(
            mainMenuButton,
            saveButton,
            headlineLabel,
            continueButton,
            saveDialog,
            simDelayLabel,
            simDelayUpButton,
            simDelayResetButton,
            simDelayDownButton,
        )
    }

    /**
     * shows the Label with the message in response to clicking the [saveButton]
     */
    fun showSaveDialog() {
        if (pokaniApplication.gameScene.isOnlineGame) {
            saveDialog.font = Font(30, Color.RED, family = "Bauhaus 93")
            saveDialog.text = "Cannot save the game in an Online Lobby!"
        } else {
            saveDialog.font = Font(30, Color.GREEN, family = "Bauhaus 93")
            saveDialog.text = "Successfully saved the Game!"
        }
        saveDialog.isVisible = true
    }

}