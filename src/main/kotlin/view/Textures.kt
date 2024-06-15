package view

import tools.aqua.bgw.visual.ImageVisual
import view.gamescene.GameScene
import javax.imageio.ImageIO

/**
 * Helper Class which holds all the used Textures of the View layer in order to not clutter the Scenes
 */
class Textures {
    val mainRoute = ImageVisual(
        ImageIO.read(
            this::class.java.getResource(
                "/ScoreboardScene/Routes/CustomMapV6.png"
            )
        )
    )
    val backgroundTexture = ImageVisual(
        ImageIO.read(
            this::class.java.getResource(
                "/ScoreboardScene/ScoreboardBackgroundTexture.jpg"
            )
        )
    )

    val cynthiaFullImage = ImageVisual(
        ImageIO.read(
            this::class.java.getResource(
                "/ScoreboardScene/Cynthia(yellow).png"
            )
        )
    )
    val berthaFullImage = ImageVisual(
        ImageIO.read(
            this::class.java.getResource(
                "/ScoreboardScene/Bertha(grey).png"
            )
        )
    )
    val flintFullImage = ImageVisual(
        ImageIO.read(
            this::class.java.getResource(
                "/ScoreboardScene/Flint(red).png"
            )
        )
    )
    val lucianFullImage = ImageVisual(
        ImageIO.read(
            this::class.java.getResource(
                "/ScoreboardScene/Lucian(blue).png"
            )
        )
    )

    val flintForwardStill = ImageVisual(
        ImageIO.read(
            this::class.java.getResource(
                "/ScoreboardScene/CharsFacingForward/FireGuy_Forward_Still.png"
            )
        )
    )
    val lucianForwardStill = ImageVisual(
        ImageIO.read(
            this::class.java.getResource(
                "/ScoreboardScene/CharsFacingForward/WaterGuy_Forward_Still.PNG"
            )
        )
    )
    val cynthiaForwardStill = ImageVisual(
        ImageIO.read(
            this::class.java.getResource(
                "/ScoreboardScene/CharsFacingForward/BlondeGirl_Forward_Still.png"
            )
        )
    )
    val berthaForwardStill = ImageVisual(
        ImageIO.read(
            this::class.java.getResource(
                "/ScoreboardScene/CharsFacingForward/GreyGirl_Forward_Still.png"
            )
        )
    )

    val flintBackwardsStill = ImageVisual(
        ImageIO.read(
            this::class.java.getResource(
                "/ScoreboardScene/CharsFacingBackwards/Flint(red)FacingBackwardsStill.png"
            )
        )
    )
    val lucianBackwardsStill = ImageVisual(
        ImageIO.read(
            this::class.java.getResource(
                "/ScoreboardScene/CharsFacingBackwards/Lucian(blue)FacingBackwardsStill.png"
            )
        )
    )
    val cynthiaBackwardsStill = ImageVisual(
        ImageIO.read(
            this::class.java.getResource(
                "/ScoreboardScene/CharsFacingBackwards/Cynthia(yellow)FacingBackwardsStill.png"
            )
        )
    )
    val berthaBackwardsStill = ImageVisual(
        ImageIO.read(
            this::class.java.getResource(
                "/ScoreboardScene/CharsFacingBackwards/Bertha(grey)FacingBackwardsStill.png"
            )
        )
    )

    val flintLeftStill = ImageVisual(
        ImageIO.read(
            this::class.java.getResource(
                "/ScoreboardScene/CharsFacingLeft/Flint(red)FacingLeftStill.png"
            )
        )
    )
    val lucianLeftStill = ImageVisual(
        ImageIO.read(
            this::class.java.getResource(
                "/ScoreboardScene/CharsFacingLeft/Lucian(blue)FacingLeftStill.png"
            )
        )
    )
    val cynthiaLeftStill = ImageVisual(
        ImageIO.read(
            this::class.java.getResource(
                "/ScoreboardScene/CharsFacingLeft/Cynthia(yellow)FacingLeftStill.png"
            )
        )
    )
    val berthaLeftStill = ImageVisual(
        ImageIO.read(
            this::class.java.getResource(
                "/ScoreboardScene/CharsFacingLeft/Bertha(grey)FacingLeftStill.png"
            )
        )
    )

    val flintRightStill = ImageVisual(
        ImageIO.read(
            this::class.java.getResource(
                "/ScoreboardScene/CharsFacingRight/Flint(red)FacingRightStill.png"
            )
        )
    )
    val lucianRightStill = ImageVisual(
        ImageIO.read(
            this::class.java.getResource(
                "/ScoreboardScene/CharsFacingRight/Lucian(blue)FacingRightStill.png"
            )
        )
    )
    val cynthiaRightStill = ImageVisual(
        ImageIO.read(
            this::class.java.getResource(
                "/ScoreboardScene/CharsFacingRight/Cynthia(yellow)FacingRightStill.png"
            )
        )
    )
    val berthaRightStill = ImageVisual(
        ImageIO.read(
            this::class.java.getResource(
                "/ScoreboardScene/CharsFacingRight/Bertha(grey)FacingRightStill.png"
            )
        )
    )

    val rotateLeftButton = ImageVisual(
        ImageIO.read(
            this::class.java.getResource(
                "/rotateLeftButton.png"
            )
        )
    )
    val rotateRightButton = ImageVisual(
        ImageIO.read(
            this::class.java.getResource(
                "/rotateRightButton.png"
            )
        )
    )
    val confirmButton = ImageVisual(
        ImageIO.read(
            this::class.java.getResource(
                "/confirmButton.png"
            )
        )
    )
    val cancelButton = ImageVisual(
        ImageIO.read(
            this::class.java.getResource(
                "/cancelButton.png"
            )
        )
    )
    val discs = ImageVisual(
        ImageIO.read(
            this::class.java.getResource(
                "/PokaniDiscs.png"
            )
        )
    )

    val closedBall = ImageVisual(
        ImageIO.read(
            this::class.java.getResource
                ("/start_scene/closed_ball.png")
        )
    )
    val openedBall = ImageVisual(
        ImageIO.read(
            this::class.java.getResource
                ("/start_scene/opened_ball.png")
        )
    )
    val localGameNewMenuBackGround = ImageVisual(
        ImageIO.read(
            this::class.java.getResource
                ("/start_scene/localnewback.jpg")
        )
    )
    val backButton = ImageVisual(
        ImageIO.read(
            this::class.java.getResource
                ("/start_scene/return.png")
        )
    )
    val robotOn = ImageVisual(
        ImageIO.read(
            this::class.java.getResource
                ("/start_scene/wakeup.png")
        )
    )
    val robotOff = ImageVisual(
        ImageIO.read(
            this::class.java.getResource
                ("/start_scene/sleeped_kabigon.png")
        )
    )
    val robotHard = ImageVisual(
        ImageIO.read(
            this::class.java.getResource
                ("/start_scene/botHardDifficulty.png")
        )
    )
    val cancelPlayer = ImageVisual(
        ImageIO.read(
            this::class.java.getResource
                ("/start_scene/kreuz.png")
        )
    )
    val directionDown = ImageVisual(
        ImageIO.read(
            this::class.java.getResource
                ("/start_scene/dreieckdown.png")
        )
    )
    val directionUp = ImageVisual(
        ImageIO.read(
            this::class.java.getResource
                ("/start_scene/dreieck.png")
        )
    )

    /**
     * loads all the used Frames of the [GameScene]s Background
     */
    fun gameSceneBackgroundFrames(): MutableList<ImageVisual> {
        val gameSceneBackgrounds = mutableListOf<ImageVisual>()
        for (i in 1..24) {
            gameSceneBackgrounds.add(
                ImageVisual(
                    ImageIO.read(
                        this::class.java.getResource(
                            "/gamebackground/$i.jpg"
                        )
                    )
                )
            )
        }
        return gameSceneBackgrounds
    }

    val activeBorder = ImageVisual(
        ImageIO.read(
            this::class.java.getResource
                ("/activeBorder.png")
        )
    )

    val offerBorder = ImageVisual(
        ImageIO.read(
            this::class.java.getResource
                ("/OfferBorder.png")
        )
    )

    val intermezzoBorder = ImageVisual(
        ImageIO.read(
            this::class.java.getResource
                ("/IntermezzoBorder.png")
        )
    )

    val drawPileBorder = ImageVisual(
        ImageIO.read(
            this::class.java.getResource
                ("/DrawPileBorder.png")
        )
    )

    val bigButton = ImageVisual(
        ImageIO.read(
            this::class.java.getResource
                ("/BigButton.png")
        )
    )

    val smolButton = ImageVisual(
        ImageIO.read(
            this::class.java.getResource
                ("/SmolButton.png")
        )
    )

    val error = ImageVisual(
        ImageIO.read(
            this::class.java.getResource
                ("/Error.png")
        )
    )

    val menuButton = ImageVisual(
        ImageIO.read(
            this::class.java.getResource
                ("/start_scene/MenuButton.png")
        )
    )

    val delayUp = ImageVisual(
        ImageIO.read(
            this::class.java.getResource
                ("/DelayUp.png")
        )
    )
    val delayReset = ImageVisual(
        ImageIO.read(
            this::class.java.getResource
                ("/DelayReset.png")
        )
    )
    val delayDown = ImageVisual(
        ImageIO.read(
            this::class.java.getResource
                ("/DelayDown.png")
        )
    )

    val checkBoxEmpty = ImageVisual(
        ImageIO.read(
            this::class.java.getResource
                ("/checkBoxEmpty.png")
        )
    )

}