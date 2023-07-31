package com.android.mobicam.display

import android.annotation.SuppressLint
import android.content.Context
import androidx.fragment.app.Fragment
import com.android.mobicam.display.customcomponents.VideoViewWebRTC

@SuppressLint("StaticFieldLeak")
object Display : Fragment() {
    private var connection: ILocalConnection = NetworkServiceConnection

    fun init(
        context: Context?,
        videoView: VideoViewWebRTC
    ) {
        if (context != null) {
            connection.init(context)
        }
        videoView.init()
        CameraDataListener.init(connection)
    }

    fun connect(context: Context?) {
        connection.connect(context)
    }

    fun disconnect(context: Context?) {
        connection.disconnect(context)
    }
}