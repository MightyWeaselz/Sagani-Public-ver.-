package service

import java.io.IOException
import javax.sound.sampled.*

/**
 * MusicService class containing the music player
 */
class MusicService : AbstractRefreshingService() {

    private val tracks = HashMap<String, String>()
    private val random = true

    private var currentTrackTitle = ""
    private var currentTrack: Clip? = null


    /**
     * Initializes MusicService
     */
    init {
        tracks["Accumula Town"] = "/music/Accumula Town.wav"
        tracks["Anville Town"] = "/music/Anville Town.wav"
        tracks["Driftveil City"] = "/music/Driftveil City.wav"
        tracks["Nimbasa City"] = "/music/Nimbasa City.wav"
        tracks["Nuvema Town"] = "/music/Nuvema Town.wav"
        tracks["Pokémon Gym"] = "/music/Pokémon Gym.wav"
        tracks["Undella Town"] = "/music/Undella Town.wav"
    }

    /**
     * Plays a track
     *
     * @param index Index of the track to play
     */
    private fun playTrack(index: Int) {
        try {
            val filePath = tracks.entries.elementAt(index).value
            val audioInputStream: AudioInputStream = AudioSystem.getAudioInputStream(
                MusicService::class.java.getResource(filePath)
            )
            val clip: Clip = AudioSystem.getClip()
            clip.open(audioInputStream)
            val gainControl: FloatControl = clip.getControl(FloatControl.Type.MASTER_GAIN) as FloatControl
            gainControl.value = -30f
            clip.start()
            clip.addLineListener { event ->
                if (event.type == LineEvent.Type.STOP) {
                    clip.close()
                    currentTrackTitle = if (random) {
                        val randomNumber = (0 until tracks.size).random()
                        playTrack(randomNumber)
                        "Current Track: ${tracks.entries.elementAt(randomNumber).key}"
                    } else if (index < tracks.size - 1) {
                        playTrack(index + 1)
                        "Current Track: ${tracks.entries.elementAt(tracks.size - 1).key}"
                    } else {
                        playTrack(0)
                        "Current Track: ${tracks.entries.elementAt(0).key}"

                    }
                }
            }
            currentTrack = clip
        } catch (ioException: IOException) {
            println(ioException.message)
        }
    }

    /**
     * Plays a random track, defaults to track 0
     */
    fun playTrack() {
        currentTrackTitle = if (random) {
            val randomNumber = (0 until tracks.size).random()
            playTrack(randomNumber)
            "Current Track: ${tracks.entries.elementAt(randomNumber).key}"
        } else {
            playTrack(0)
            "Current Track: ${tracks.entries.elementAt(0).key}"
        }
    }
}