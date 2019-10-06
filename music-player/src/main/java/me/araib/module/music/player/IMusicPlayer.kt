package me.araib.module.music.player

import androidx.annotation.IntRange

interface IMusicPlayer {
    fun play(music: Music, exclusive: Boolean = false)
    fun pause(music: Music)
    fun pause(id: String)
    fun resume(music: Music)
    fun resume(id: String)
    fun stop()
    fun stop(music: Music)
    fun stop(id: String)
    fun changeVolume(music: Music, @IntRange(from = 0, to = 100) volume: Int)
    fun changeVolume(id: String, @IntRange(from = 0, to = 100) volume: Int)
    fun mute(music: Music)
    fun mute(id: String)

    fun isPlaying(music: Music): Boolean
    fun isPlaying(id: String): Boolean
    fun whatIsPlaying(): List<String>
}