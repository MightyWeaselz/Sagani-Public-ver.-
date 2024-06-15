package view

/**
 * The Logic Class of the [LocalGameNewMenu] Scene. Performs the changes in Position and Color of the Players
 */
class LocalGameNewMenuPositionLogic(private val scene: LocalGameNewMenu) {
    private val textures = Textures()

    /**
     * shows the correct "change Position Buttons" depending on how many Players are in the Lobby
     */
    fun ifCanChangePosition() {
        for (i in 0..3) {
            if (scene.listOfInputs[i].text.isBlank()) {
                scene.listOfChangePButtons[i].isVisible = false
                if (i == 1) {
                    scene.listOfChangePButtons[4].isVisible = false
                    scene.listOfChangePButtons[5].isVisible = false
                }
                if (i == 2) {
                    scene.listOfChangePButtons[6].isVisible = false
                    scene.listOfChangePButtons[7].isVisible = false
                }
            } else if (i == 1 || i == 2) {
                val j = if (i == 1) 4 else 6
                val isVisible = scene.listOfInputs[i + 1].text.isNotBlank()
                scene.listOfChangePButtons[j + 1].isVisible = isVisible
                scene.listOfChangePButtons[i].isVisible = !isVisible
                scene.listOfChangePButtons[j].isVisible = isVisible
            } else {
                scene.listOfChangePButtons[i].isVisible = true
            }
        }
    }

    /**
     * function to handle the changing of Positions of the Players
     *
     * @param index the Index of the Player to change Position
     */
    fun changePosition(index: Int) {
        when (index) {
            0 -> swap(0, 1)
            in 1..3 -> swap(index, index - 1)
            4 -> changePosition(1)
            5, 6 -> changePosition(2)
            7 -> changePosition(3)
        }
    }

    /**
     * function that together with [changePosition] handles and swaps the Players
     */
    private fun swap(i: Int, j: Int) {
        scene.listOfInputs[i].text = scene.listOfInputs[j].text.also {
            scene.listOfInputs[j].text = scene.listOfInputs[i].text
        }
        scene.listOfAIButtons[i].visual
        scene.listOfAIButtons[j].visual.also {
            scene.listOfAIButtons[j].visual = scene.listOfAIButtons[i].visual
        }
        scene.listOfColorButtons[i].visual
        scene.listOfColorButtons[j].visual.also {
            scene.listOfColorButtons[j].visual = scene.listOfColorButtons[i].visual
        }
    }

    /**
     * function that prunes the Player entries when text is empty or the delete Button is pressed
     *
     * @param index index of the Player entry to be deleted
     */
    fun pruneNameOrder(index: Int) {
        for (i in index until 3) {
            scene.listOfInputs[i].text = scene.listOfInputs[i + 1].text
            scene.listOfColorButtons[i].visual = scene.listOfColorButtons[i + 1].visual
            scene.listOfAIButtons[i].visual = scene.listOfAIButtons[i + 1].visual
        }
        scene.listOfInputs[3].text = ""
        repeat(3) {
            if (scene.listOfInputs[it].text.isBlank() && it > 0) {
                scene.listOfInputs[it + 1].isVisible = false
                scene.listOfAIButtons[it + 1].isVisible = false
                scene.deleteButtons[it + 1].isVisible = false
            }
        }
        checkIfNothing()
        if (scene.player1Input.text.isNotBlank() && scene.player2Input.text.isNotBlank()) {
            scene.playButton.isDisabled = false
            scene.playButton.visual = textures.openedBall
            scene.playButton.width = 293.50
            scene.playButton.posX = 813.25
        } else {
            scene.playButton.isDisabled = true
            scene.playButton.width = 232.00
            scene.playButton.height = 232.00
            scene.playButton.posX = 844.00
            scene.playButton.posY = 721.00
            scene.playButton.visual = textures.closedBall
        }
        scene.positionLogic.ifCanChangePosition()
        ifCanChangeColor()
    }

    /**
     * activates the Color Change Button if the Player entry gets filled
     */
    fun ifCanChangeColor() {
        scene.listOfColorButtons.forEachIndexed { index, button ->
            button.isVisible = scene.listOfInputs[index].text.isNotBlank()
        }
    }

    /**
     * deactivates the delete and Ai buttons of empty Player entries
     */
    private fun checkIfNothing() {
        for (i in 0..3) {
            if (scene.listOfInputs[i].text == "") {
                scene.deleteButtons[i].isVisible = false
                scene.listOfAIButtons[i].visual = textures.robotOff
            }
        }
    }
}