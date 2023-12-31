package com.android.mobicam.camera

import android.content.Context

interface ILocalConnection {
    fun init(context: Context?)
    fun setDataCallback(dataCallback: IDataReceived?)
    fun connect(context: Context?)
    fun disconnect(context: Context?)
    fun isConnected(): Boolean
    fun sendMessage(message: String?)
    fun stop()
    fun start()
    val isVideoCapable: Boolean
}