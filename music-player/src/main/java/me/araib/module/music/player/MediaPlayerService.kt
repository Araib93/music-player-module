package me.araib.module.music.player

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.IBinder

class MediaPlayerService : Service(), PlayerActionListener {
    override fun onPlayerAction(vararg bundle: Bundle) {

    }

    private val musicPlayer: MusicPlayer by lazy { MusicPlayer(this, this) }
    override fun onBind(intent: Intent?): IBinder? {
    }
}