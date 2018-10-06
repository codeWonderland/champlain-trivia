package us.cyosp.codewonderland.champlaintrivia.util

import android.content.Context
import android.content.res.AssetManager
import android.media.AudioManager
import android.media.SoundPool
import android.util.Log

import java.io.IOException
import java.util.ArrayList

class BeatBox(context: Context) {
    /*
    PLEASE NOTE THAT THIS IS A MODIFIED CLASS
    Author: The Big Nerd Ranch Guide, 3rd Ed., Ch. 21
    Source: https://www.bignerdranch.com/solutions/AndroidProgramming3e.zip
     */

    private val mAssets: AssetManager
    private val mSounds = ArrayList<Sound>()
    private val mSoundPool: SoundPool

    val sounds: List<Sound>
        get() = mSounds

    init {
        mAssets = context.assets
        // This old constructor is deprecated, but we need it for
        // compatibility.

        mSoundPool = SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0)
        loadSounds()
    }

    fun play(sound: Sound) {
        val soundId = sound.soundId ?: return
        mSoundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f)
    }

    fun release() {
        mSoundPool.release()
    }

    private fun loadSounds() {
        val soundNames: Array<String>?
        try {
            soundNames = mAssets.list(SOUNDS_FOLDER)
            Log.i(TAG, "Found " + soundNames!!.size + " sounds")
        } catch (ioe: IOException) {
            Log.e(TAG, "Could not list assets", ioe)
            return
        }

        for (filename in soundNames) {
            try {
                val assetPath = "$SOUNDS_FOLDER/$filename"
                val sound = Sound(assetPath)
                load(sound)
                mSounds.add(sound)
            } catch (ioe: IOException) {
                Log.e(TAG, "Could not load sound $filename", ioe)
            }

        }
    }

    @Throws(IOException::class)
    private fun load(sound: Sound) {
        val assetFd = mAssets.openFd(sound.assetPath)
        val soundId = mSoundPool.load(assetFd, 1)
        sound.soundId = soundId
    }

    companion object {
        private val TAG = "BeatBox"

        private val SOUNDS_FOLDER = "sounds"
        private val MAX_SOUNDS = 5
    }
}
