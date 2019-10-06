package me.araib.module.music.player

interface Music {
    val id: String
    val name: String
    val albumArt: String
    val path: String
    val musicType: MusicType
    val soundLevel: Int
}