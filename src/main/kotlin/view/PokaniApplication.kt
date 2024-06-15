package view

import VERSION
import service.RootService
import tools.aqua.bgw.core.BoardGameApplication
import view.gamescene.*
import view.scoreboardscene.ScoreboardScene

/**
 * Main class of the application. Holds the reference to every Scene.
 */
class PokaniApplication : BoardGameApplication("Pokani v$VERSION"), Refreshable {

    val rootService = RootService()

    val gameScene = GameScene(this)

    val gameSceneUIUpdater = GameSceneUIUpdater(this)

    val gameSceneContainerUiUpdater = GameSceneContainerUiUpdater(this)

    val gameSceneGetters = GameSceneGetters(this)

    val gameSceneDragAndDropHandler = GameSceneDragAndDropHandler(this)

    val gameSceneAnimator = BackgroundAnimator(gameScene)

    val scoreboardScene = ScoreboardScene(this)

    val gameTypeSelectMenu = GameTypeSelectMenu().apply {
        onlineButton.onMouseClicked = {
            showGameScene(gameScene)
            hideMenuScene()
            this@PokaniApplication.showMenuScene(onlineMenu)
        }
        localButton.onMouseClicked = {
            hideMenuScene()
            this@PokaniApplication.showMenuScene(localGameSelectionMenu)
        }
    }

    private val localGameSelectionMenu = LocalGameSelectionMenu().apply {
        loadGameButton.onMouseClicked = {
            rootService.pokaniService.loadGame()
            showGameScene(gameScene)
            hideMenuScene()
        }
        newGameButton.onMouseClicked = {
            hideMenuScene()
            this@PokaniApplication.showMenuScene(localGameNewMenu)
        }
    }

    private val localGameNewMenu = LocalGameNewMenu(this)

    private val onlineMenu = OnlineMenu(this).apply {
        joinGroupButton.onMouseClicked = {
            joinGroup()
            this@PokaniApplication.showMenuScene(onlineJoinLobbyMenu)
            onlineJoinLobbyMenu.player1Input.text = this.playerName.text
        }
        hostGroupButton.onMouseClicked = {
            hostGroup()
            this@PokaniApplication.showMenuScene(onlineHostLobbyMenu)
            onlineHostLobbyMenu.player1Input.text = this.playerName.text
        }
    }

    val onlineHostLobbyMenu = OnlineHostLobbyMenu(this)

    val onlineJoinLobbyMenu = OnlineJoinLobbyMenu()

    val gameScenePauseMenu = GameScenePauseMenu(this)

    val gameOverMenu = GameOverMenu(this).apply {
        continueButton.onMouseClicked = {
            hideMenuScene()
        }
        mainMenuButton.onMouseClicked = {
            hideMenuScene()
            this@PokaniApplication.showMenuScene(gameTypeSelectMenu)
            if (gameScene.isOnlineGame) {
                rootService.networkService.disconnect()
            }
            onlineHostLobbyMenu.listOfInputs.forEach {
                it.text = ""
            }
            onlineJoinLobbyMenu.listOfInputs.forEach {
                it.text = ""
            }
        }
    }

    private val eventHandler = RefreshHandler(this)


    init {
        rootService.addRefreshables(
            this,
            scoreboardScene,
            gameScene,
            localGameNewMenu,
            onlineMenu,
            onlineHostLobbyMenu,
            onlineJoinLobbyMenu,
            eventHandler,
            gameScenePauseMenu,
            gameOverMenu
        )
        localGameSelectionMenu.backButton.onMouseClicked = {
            this@PokaniApplication.showMenuScene(gameTypeSelectMenu)
        }
        localGameNewMenu.backButton.onMouseClicked = {
            this@PokaniApplication.showMenuScene(localGameSelectionMenu)
        }
        onlineHostLobbyMenu.apply {
            backButton.onMouseClicked = {
                this@PokaniApplication.showMenuScene(onlineMenu)
                rootService.networkService.disconnect()
                listOfInputs.forEach {
                    it.text = ""
                }
            }
        }
        onlineJoinLobbyMenu.apply {
            backButton.onMouseClicked = {
                this@PokaniApplication.showMenuScene(onlineMenu)
                rootService.networkService.disconnect()
                listOfInputs.forEach {
                    it.text = ""
                }
            }
        }
        onlineMenu.backButton.onMouseClicked = {
            this@PokaniApplication.showMenuScene(gameTypeSelectMenu)
        }
        showMenuScene(gameTypeSelectMenu, 0)
    }

    override fun refreshOnStartGame() {
        showGameScene(gameScene)
        this.hideMenuScene(0)
    }

    override fun refreshOnEndGame() {
        showGameScene(scoreboardScene)
        gameSceneContainerUiUpdater.hideTileModifyOverlay()
        gameSceneUIUpdater.disableSelections()
        showMenuScene(gameOverMenu)
    }

}

