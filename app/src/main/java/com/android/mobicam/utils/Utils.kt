package com.android.mobicam.display

import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.media.AudioManager
import android.media.ToneGenerator
import android.net.wifi.WifiManager
import android.widget.Toast
import com.android.mobicam.MainActivity
import timber.log.Timber
import java.math.BigInteger
import java.net.InetAddress

object Utils {

    enum class TONE {
        PIP,
        ALERT,
        INTERCEPT
    }

    fun beep(tone: TONE = TONE.PIP, duration: Int = 150) {
        val toneGen = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
        val tgTone = when (tone) {
            TONE.INTERCEPT -> {
                ToneGenerator.TONE_CDMA_ABBR_INTERCEPT
            }
            TONE.ALERT -> {
                ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD
            }
            else -> {
                ToneGenerator.TONE_CDMA_PIP
            }
        }
        toneGen.startTone(tgTone, duration)
    }

    fun isMe(otherIP: String): Boolean {
        val myIP = getMyIP()
        val isSame = otherIP == myIP
        if (isSame) {
            Timber.i("Same IP address $myIP, $otherIP")
        }
        return isSame
    }

    fun getMyIP(): String? {
        val context: Context = MainActivity.applicationContext()
        val wm = context.applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        val longIp = wm.connectionInfo.ipAddress.toLong()
        val byteIp = BigInteger.valueOf(longIp).toByteArray().reversedArray()
        val ipAddr = InetAddress.getByAddress(byteIp).hostAddress
        return ipAddr
    }


    fun toast(context: Context, message: String) {
        Toast.makeText(
            context, message,
            Toast.LENGTH_LONG
        ).show()
    }
}
