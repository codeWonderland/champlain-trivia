package us.cyosp.codewonderland.champlaintrivia.util

class Sound(val assetPath: String) {
    /*
    PLEASE NOTE THAT THIS IS A MODIFIED CLASS
    Author: The Big Nerd Ranch Guide, 3rd Ed., Ch. 21
    Source: https://www.bignerdranch.com/solutions/AndroidProgramming3e.zip
     */

    val name: String
    var soundId: Int? = null

    init {
        val components = assetPath.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val filename = components[components.size - 1]
        name = filename.replace(".wav", "")
    }
}