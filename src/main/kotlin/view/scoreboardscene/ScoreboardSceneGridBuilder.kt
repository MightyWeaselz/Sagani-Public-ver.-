package view.scoreboardscene

import tools.aqua.bgw.components.gamecomponentviews.TokenView
import tools.aqua.bgw.visual.Visual

/**
 * Helper Class of the [ScoreboardScene] in which the Grid is being build that the Player Characters walk along
 *
 * @param scoreboardScene the [ScoreboardScene] in which to build the Grid
 */
class ScoreboardSceneGridBuilder(private val scoreboardScene: ScoreboardScene) {

    /**
     * function that fills the whole Route of the Scene into the proper lists
     */
    fun fillRouteIntoLists() {
        fillListPart1()
        fillListPart2()
    }

    /**
     * function that fills first half Route of the Scene into the proper lists
     */
    private fun fillListPart1() {
        repeat(5) {
            scoreboardScene.completeRouteTokenList.add(scoreboardScene.mainTokenGrid[15, 29 - it])
            scoreboardScene.facingUpRouteTokenList.add(scoreboardScene.mainTokenGrid[15, 29 - it])
        }
        repeat(12) {
            scoreboardScene.completeRouteTokenList.add(scoreboardScene.mainTokenGrid[15 + it, 24])
            scoreboardScene.facingRightRouteTokenList.add(scoreboardScene.mainTokenGrid[15 + it, 24])
        }
        repeat(5) {
            scoreboardScene.completeRouteTokenList.add(scoreboardScene.mainTokenGrid[27, 24 - it])
            scoreboardScene.facingUpRouteTokenList.add(scoreboardScene.mainTokenGrid[27, 24 - it])
        }
        repeat(23) {
            scoreboardScene.completeRouteTokenList.add(scoreboardScene.mainTokenGrid[27 - it, 19])
            scoreboardScene.facingLeftRouteTokenList.add(scoreboardScene.mainTokenGrid[27 - it, 19])
        }
        repeat(6) {
            scoreboardScene.completeRouteTokenList.add(scoreboardScene.mainTokenGrid[4, 19 - it])
            scoreboardScene.facingUpRouteTokenList.add(scoreboardScene.mainTokenGrid[4, 19 - it])
        }
        repeat(3) {
            scoreboardScene.completeRouteTokenList.add(scoreboardScene.mainTokenGrid[4 + it, 13])
            scoreboardScene.facingRightRouteTokenList.add(scoreboardScene.mainTokenGrid[4 + it, 13])
        }
        repeat(2) {
            scoreboardScene.completeRouteTokenList.add(scoreboardScene.mainTokenGrid[7, 13 + it])
            scoreboardScene.facingScreenRouteTokenList.add(scoreboardScene.mainTokenGrid[7, 13 + it])
        }
        scoreboardScene.completeRouteTokenList.add(scoreboardScene.mainTokenGrid[7, 15])
        scoreboardScene.facingRightRouteTokenList.add(scoreboardScene.mainTokenGrid[7, 15])
        scoreboardScene.completeRouteTokenList.add(scoreboardScene.mainTokenGrid[8, 15])
        scoreboardScene.facingScreenRouteTokenList.add(scoreboardScene.mainTokenGrid[8, 15])
        repeat(3) {
            scoreboardScene.completeRouteTokenList.add(scoreboardScene.mainTokenGrid[8 + it, 16])
            scoreboardScene.facingRightRouteTokenList.add(scoreboardScene.mainTokenGrid[8 + it, 16])
        }
        repeat(4) {
            scoreboardScene.completeRouteTokenList.add(scoreboardScene.mainTokenGrid[11, 16 - it])
            scoreboardScene.facingUpRouteTokenList.add(scoreboardScene.mainTokenGrid[11, 16 - it])
        }
        repeat(2) {
            scoreboardScene.completeRouteTokenList.add(scoreboardScene.mainTokenGrid[11 + it, 12])
            scoreboardScene.facingRightRouteTokenList.add(scoreboardScene.mainTokenGrid[11 + it, 12])
        }
        scoreboardScene.completeRouteTokenList.add(scoreboardScene.mainTokenGrid[13, 12])
        scoreboardScene.facingScreenRouteTokenList.add(scoreboardScene.mainTokenGrid[13, 12])
        scoreboardScene.completeRouteTokenList.add(scoreboardScene.mainTokenGrid[13, 13])
        scoreboardScene.facingRightRouteTokenList.add(scoreboardScene.mainTokenGrid[13, 13])
        scoreboardScene.completeRouteTokenList.add(scoreboardScene.mainTokenGrid[14, 13])
        scoreboardScene.facingScreenRouteTokenList.add(scoreboardScene.mainTokenGrid[14, 13])
        repeat(2) {
            scoreboardScene.completeRouteTokenList.add(scoreboardScene.mainTokenGrid[14 + it, 14])
            scoreboardScene.facingRightRouteTokenList.add(scoreboardScene.mainTokenGrid[14 + it, 14])
        }

    }

    /**
     * function that fills second half Route of the Scene into the proper lists
     */
    private fun fillListPart2() {
        repeat(2) {
            scoreboardScene.completeRouteTokenList.add(scoreboardScene.mainTokenGrid[16, 14 - it])
            scoreboardScene.facingUpRouteTokenList.add(scoreboardScene.mainTokenGrid[16, 14 - it])
        }
        repeat(3) {
            scoreboardScene.completeRouteTokenList.add(scoreboardScene.mainTokenGrid[16 + it, 12])
            scoreboardScene.facingRightRouteTokenList.add(scoreboardScene.mainTokenGrid[16 + it, 12])
        }
        scoreboardScene.completeRouteTokenList.add(scoreboardScene.mainTokenGrid[19, 12])
        scoreboardScene.facingScreenRouteTokenList.add(scoreboardScene.mainTokenGrid[19, 12])
        scoreboardScene.completeRouteTokenList.add(scoreboardScene.mainTokenGrid[19, 13])
        scoreboardScene.facingRightRouteTokenList.add(scoreboardScene.mainTokenGrid[19, 13])
        scoreboardScene.completeRouteTokenList.add(scoreboardScene.mainTokenGrid[20, 13])
        scoreboardScene.facingScreenRouteTokenList.add(scoreboardScene.mainTokenGrid[20, 13])
        repeat(2) {
            scoreboardScene.completeRouteTokenList.add(scoreboardScene.mainTokenGrid[20 + it, 14])
            scoreboardScene.facingRightRouteTokenList.add(scoreboardScene.mainTokenGrid[20 + it, 14])
        }
        repeat(2) {
            scoreboardScene.completeRouteTokenList.add(scoreboardScene.mainTokenGrid[22, 14 - it])
            scoreboardScene.facingUpRouteTokenList.add(scoreboardScene.mainTokenGrid[22, 14 - it])
        }
        repeat(3) {
            scoreboardScene.completeRouteTokenList.add(scoreboardScene.mainTokenGrid[22 + it, 12])
            scoreboardScene.facingRightRouteTokenList.add(scoreboardScene.mainTokenGrid[22 + it, 12])
        }
        repeat(2) {
            scoreboardScene.completeRouteTokenList.add(scoreboardScene.mainTokenGrid[25, 12 + it])
            scoreboardScene.facingScreenRouteTokenList.add(scoreboardScene.mainTokenGrid[25, 12 + it])
        }
        repeat(2) {
            scoreboardScene.completeRouteTokenList.add(scoreboardScene.mainTokenGrid[25 + it, 14])
            scoreboardScene.facingRightRouteTokenList.add(scoreboardScene.mainTokenGrid[25 + it, 14])
        }
        repeat(5) {
            scoreboardScene.completeRouteTokenList.add(scoreboardScene.mainTokenGrid[27, 14 - it])
            scoreboardScene.facingUpRouteTokenList.add(scoreboardScene.mainTokenGrid[27, 14 - it])
        }
        repeat(23) {
            scoreboardScene.completeRouteTokenList.add(scoreboardScene.mainTokenGrid[27 - it, 9])
            scoreboardScene.facingLeftRouteTokenList.add(scoreboardScene.mainTokenGrid[27 - it, 9])
        }
        repeat(5) {
            scoreboardScene.completeRouteTokenList.add(scoreboardScene.mainTokenGrid[4, 9 - it])
            scoreboardScene.facingUpRouteTokenList.add(scoreboardScene.mainTokenGrid[4, 9 - it])
        }
        repeat(23) {
            scoreboardScene.completeRouteTokenList.add(scoreboardScene.mainTokenGrid[4 + it, 4])
            scoreboardScene.facingRightRouteTokenList.add(scoreboardScene.mainTokenGrid[4 + it, 4])
        }
        repeat(3) {
            scoreboardScene.completeRouteTokenList.add(scoreboardScene.mainTokenGrid[27, 4 - it])
            scoreboardScene.facingUpRouteTokenList.add(scoreboardScene.mainTokenGrid[27, 4 - it])
        }
    }

    /**
     * initialization function which sets the Coordinates for the starting positions
     */
    fun defineStartingPositions() {
        scoreboardScene.mainTokenGrid[14, 30]?.let { scoreboardScene.startingPositions.add(it) }
        scoreboardScene.mainTokenGrid[15, 30]?.let { scoreboardScene.startingPositions.add(it) }
        scoreboardScene.mainTokenGrid[16, 30]?.let { scoreboardScene.startingPositions.add(it) }
        scoreboardScene.mainTokenGrid[17, 30]?.let { scoreboardScene.startingPositions.add(it) }
        scoreboardScene.playersPosition = scoreboardScene.startingPositions.toMutableList()
    }

    /**
     * function that fills the mainTokenGrid with empty [TokenView]s
     */
    fun initializeTileGrid() {
        repeat(32) { posX ->
            repeat(32) { posY ->
                scoreboardScene.mainTokenGrid[posX, posY] = getEmptyTokenView()
            }
        }
    }

    /**
     * function that returns an empty [TokenView]
     *
     * @return [TokenView]
     */
    private fun getEmptyTokenView(): TokenView {
        return TokenView(
            width = 31,
            height = 37,
            visual = Visual.EMPTY
        )
    }

}