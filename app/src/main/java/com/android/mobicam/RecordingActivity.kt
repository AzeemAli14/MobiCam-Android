package com.android.mobicam

import android.hardware.display.VirtualDisplay
import android.media.MediaRecorder
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.SparseArray
import android.widget.VideoView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RecordingActivity : AppCompatActivity() {
    private val  REQUEST_CODE = 1000
    private val REQUEST_PERMISSION = 1001
    private lateinit var mediaProjectionManager: MediaProjectionManager
    private var mediaProjection: MediaProjection? = null
    private var virtualDisplay: VirtualDisplay? = null
    private lateinit var mediaProjectionCallBack: MediaProjectionCallBack

    private var mScreenDensity :Int ? = null
    private var DISPLAY_WIDTH = 720
    private var DISPLAY_WIDTH = 1280

    private var mediaRecorder: MediaRecorder? = null
    private lateinit var toggleBtn :FloatingActionButton

    var isChecked = false

    private lateinit var videoView: VideoView
    private var videoUri: String = ""
    private val ORIENTATION = SparseArray()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recording)
    }

    inner class MediaProjectionCallBack {

    }
}