package me.araib.module.music.player

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import androidx.core.net.toUri
import androidx.core.os.bundleOf

class MusicPlayer(private val context: Context, private val callback: PlayerActionListener) :
    IMusicPlayer {
    override fun play(music: Music, exclusive: Boolean) {
        if (isPlaying(music)) throw MediaPlayerException(MediaPlayerError.ALREADY_PLAYING)
        val playerActions = ArrayList<Bundle>()
        if (exclusive) {
            mediaPlayers.entries.forEach { mediaPlayer ->
                playerActions.add(
                    bundleOf(
                        "id" to music.id,
                        "action" to MusicAction.STOPPED
                    )
                )
                mediaPlayer.value.stop()
                mediaPlayers.remove(mediaPlayer.key)
            }
        }

        val mediaPlayer = when (music.musicType) {
            MusicType.LOCAL -> {
                MediaPlayer().apply {
                    val audioAttributes =
                        AudioAttributes.Builder()
                            .setFlags(AudioManager.STREAM_MUSIC)
                            .build()
                    setVolume(music.soundLevel / 100F, music.soundLevel / 100F)
                    setAudioAttributes(audioAttributes)
                    setDataSource(context.applicationContext, music.path.toUri())
                    prepare()
                }
            }
            MusicType.ASSET -> {
                MediaPlayer.create(context, music.path.toInt())
            }
            MusicType.ONLINE -> {
                MediaPlayer().apply {
                    val audioAttributes =
                        AudioAttributes.Builder()
                            .setFlags(AudioManager.STREAM_MUSIC)
                            .build()
                    setAudioAttributes(audioAttributes)
                    setDataSource(music.path)
                    prepare()
                }
            }
        }
        mediaPlayer.start()
        mediaPlayers[music.id] = mediaPlayer
        playerActions.add(
            bundleOf(
                "id" to music.id,
                "action" to MusicAction.PLAYED
            )
        )
        callback.onPlayerAction(
            *playerActions.toTypedArray()
        )
    }

    override fun pause(music: Music) {
        return pause(music.id)
    }

    override fun pause(id: String) {
        val mediaPlayer =
            mediaPlayers[id] ?: throw MediaPlayerException(MediaPlayerError.UNABLE_TO_PAUSE)
        mediaPlayer.pause()
        callback.onPlayerAction(
            bundleOf(
                "id" to id,
                "action" to MusicAction.PAUSED
            )
        )
    }

    override fun resume(music: Music) {
        return resume(music.id)
    }

    override fun resume(id: String) {
        val mediaPlayer =
            mediaPlayers[id] ?: throw MediaPlayerException(MediaPlayerError.UNABLE_TO_RESUME)
        mediaPlayer.start()
        callback.onPlayerAction(
            bundleOf(
                "id" to id,
                "action" to MusicAction.RESUMED
            )
        )
    }

    override fun stop() {
        val playerActions = ArrayList<Bundle>()
        mediaPlayers.entries.forEach { mediaPlayer ->
            playerActions.add(
                bundleOf(
                    "id" to mediaPlayer.key,
                    "action" to MusicAction.STOPPED
                )
            )
            mediaPlayer.value.stop()
            mediaPlayers.remove(mediaPlayer.key)
        }
        callback.onPlayerAction(
            *playerActions.toTypedArray()
        )
    }

    override fun stop(music: Music) {
        return stop(music.id)
    }

    override fun stop(id: String) {
        val mediaPlayer =
            mediaPlayers[id] ?: throw MediaPlayerException(MediaPlayerError.UNABLE_TO_STOP)
        mediaPlayer.stop()
        callback.onPlayerAction(
            bundleOf(
                "id" to id,
                "action" to MusicAction.STOPPED
            )
        )
    }

    override fun changeVolume(music: Music, volume: Int) {
        changeVolume(music.id, volume)
    }

    override fun changeVolume(id: String, volume: Int) {
        val mediaPlayer =
            mediaPlayers[id] ?: throw MediaPlayerException(MediaPlayerError.UNABLE_TO_STOP)
        mediaPlayer.setVolume(volume / 100F, volume / 100F)
        callback.onPlayerAction(
            bundleOf(
                "id" to id,
                "action" to MusicAction.VOLUME_CHANGED,
                "volume" to volume
            )
        )
    }

    override fun mute(music: Music) {
        mute(music.id)
    }

    override fun mute(id: String) {
        val mediaPlayer =
            mediaPlayers[id] ?: throw MediaPlayerException(MediaPlayerError.UNABLE_TO_MUTE)
        mediaPlayer.setVolume(0F, 0F)
        callback.onPlayerAction(
            bundleOf(
                "id" to id,
                "action" to MusicAction.VOLUME_CHANGED,
                "volume" to 0
            )
        )
    }

    override fun isPlaying(music: Music): Boolean {
        return isPlaying(music.id)
    }

    override fun isPlaying(id: String): Boolean {
        return mediaPlayers[id]?.isPlaying ?: false
    }

    override fun whatIsPlaying(): List<String> {
        val currentlyPlaying = ArrayList<String>()
        mediaPlayers.entries.forEach { mediaPlayer ->
            if (mediaPlayer.value.isPlaying)
                currentlyPlaying.add(mediaPlayer.key)
        }
        return currentlyPlaying
    }

    private val mediaPlayers: HashMap<String, MediaPlayer> = HashMap()
}