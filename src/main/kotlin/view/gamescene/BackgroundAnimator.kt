package view.gamescene

import tools.aqua.bgw.animation.DelayAnimation
import tools.aqua.bgw.animation.SequentialAnimation
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.visual.CompoundVisual
import view.Textures

/**
 * The Class that handles the Animation of the Background in the [GameScene]
 *
 * @param gameScene the Scene of which the background will be animated
 */
class BackgroundAnimator(private val gameScene: GameScene) {

    private val texture = Textures()

    private val backgroundFrames = texture.gameSceneBackgroundFrames()

    private val testPane: Pane<CardView> = Pane(0, 0, 1920, 1080)

    private var animating = false

    /**
     * Adds 24 Panes as the Background of the GameScene
     */
    init {
        testPane.visual = CompoundVisual(backgroundFrames)
        gameScene.addComponents(testPane)
    }

    /**
     * Starts the animation.
     */
    fun startAnimiation() {
        animating = true
        animate(0)
    }

    /**
     * Stops the animation.
     */
    fun stopAnimation() {
        animating = false
    }

    /**
     * Sets the current frame of the animation visible and hides all other frames.
     */
    private fun animate(frame: Int) {
        if (!animating) {
            return
        }
        if (frame > backgroundFrames.size - 1) {
            animate(0)
            return
        }

        gameScene.playAnimation(
            SequentialAnimation(
                DelayAnimation(0).apply {
                    onFinished = {
                        (testPane.visual as CompoundVisual).children.forEachIndexed { index, singleLayerVisual ->
                            if (index == frame) {
                                singleLayerVisual.transparency = 1.0
                            } else {
                                singleLayerVisual.transparency = 0.0
                            }
                        }
                    }
                },
                DelayAnimation(100).apply {
                    onFinished = {
                        animate(frame + 1)
                    }
                }
            )
        )
    }
}