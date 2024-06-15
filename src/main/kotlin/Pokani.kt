import view.PokaniApplication

const val ORIGINAL_TILES_PATH = "tiles_colornames.csv"
const val SAVE_GAME_FILE_NAME = "savegame.json"
const val VERSION = "1.1.2"
const val SAVES_FOLDER = "saves"
const val OFFER_SIZE = 5
const val MAX_PLAYERS = 4
const val INTERMEZZO_SIZE = 4
const val MAX_CONNECTIONS = 4
const val SPLASH = """
█ ▄▄  ████▄ █  █▀ ██      ▄   ▄█ 
█   █ █   █ █▄█   █ █      █  ██ 
█▀▀▀  █   █ █▀▄   █▄▄█ ██   █ ██ 
█     ▀████ █  █  █  █ █ █  █ ▐█ 
 █            █      █ █  █ █  ▐ 
  ▀          ▀      █  █   ██    
                   ▀      v$VERSION
"""

fun main() {
    println(SPLASH)
    PokaniApplication().show()
    println("Application ended. Goodbye")
}