package me.araib.module.music.player

class MediaPlayerException(var errorCode: MediaPlayerError) : Exception() {
    override fun getLocalizedMessage(): String? {
        return errorCode.message
    }
}

enum class MediaPlayerError(var code: Int, var message: String) {
    ALREADY_PLAYING(500, "Music is already playing"),
    UNABLE_TO_PAUSE(401, "Unable to pause music, music not playing"),
    UNABLE_TO_RESUME(402, "Unable to resume music, music never played"),
    UNABLE_TO_STOP(403, "Unable to stop music, music not playing"),
    UNABLE_TO_MUTE(404, "Unable to mute music, music not playing")
}