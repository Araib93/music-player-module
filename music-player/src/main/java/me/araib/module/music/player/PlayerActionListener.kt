package me.araib.module.music.player

import android.os.Bundle

interface PlayerActionListener {
    fun onPlayerAction(vararg bundle: Bundle)
}