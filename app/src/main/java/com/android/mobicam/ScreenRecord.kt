package com.android.mobicam

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.MediaRecorder
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.SparseIntArray
import android.view.Surface
import android.view.View
import android.widget.Toast
import android.widget.ToggleButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_screen_record.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

class ScreenRecord : AppCompatActivity() {
    private var screenDensity:Int = 0
    private var projectManager: MediaProjectionManager?=null
    private var mediaProjection: MediaProjection?=null
    private var virtualDisplay: VirtualDisplay?=null
    private var mediaProjectionCallback: MediaProjectionCallback?=null
    private var mediaRecorder: MediaRecorder?=null


    internal var videoUri: String=""

    companion object {
        private val REQUEST_CODE = 1000
        private val REQUEST_PERMISSION = 1001
        private var DISPLAY_WIDTH = 700
        private var DISPLAY_HEIGHT = 1280
        private val ORIENTATION = SparseIntArray()

        init {
            ORIENTATION.append(Surface.ROTATION_0, 90)
            ORIENTATION.append(Surface.ROTATION_90, 0)
            ORIENTATION.append(Surface.ROTATION_180, 270)
            ORIENTATION.append(Surface.ROTATION_270, 180)
        }
    }

    inner class MediaProjectionCallback: MediaProjection.Callback() {
        override fun onStop() {
            if(toggleButton.isChecked) {
                toggleButton.isChecked = false
                mediaRecorder!!.stop()
                mediaRecorder!!.reset()
            }
            mediaProjection = null
            stopScreenRecord()
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_record)

        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        screenDensity = metrics.densityDpi
        mediaRecorder = MediaRecorder()
        projectManager = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager

        DISPLAY_HEIGHT = metrics.heightPixels
        DISPLAY_WIDTH = metrics.widthPixels

        toggleButton.setOnClickListener {v ->
            if (
                ContextCompat.checkSelfPermission(
                    this@ScreenRecord, android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) + ContextCompat.checkSelfPermission(
                    this@ScreenRecord, android.Manifest.permission.RECORD_AUDIO
                ) != PackageManager.PERMISSION_GRANTED
            )
            {
                if(ActivityCompat.shouldShowRequestPermissionRationale(
                    this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        this, android.Manifest.permission.RECORD_AUDIO)
                )
                {
                    toggleButton.isChecked = false
                    Snackbar.make(
                        rootLayout, "Permissions", Snackbar.LENGTH_INDEFINITE).setAction("ENABLE") {
                        ActivityCompat.requestPermissions(
                            this@ScreenRecord,
                            arrayOf(
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                android.Manifest.permission.RECORD_AUDIO
                            ),
                            REQUEST_PERMISSION
                        )
                    }.show()
                }
                else{
                    ActivityCompat.requestPermissions(
                        this@ScreenRecord,
                        arrayOf(
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.RECORD_AUDIO
                        ),
                        REQUEST_PERMISSION
                    )
                }
            }
            else {
                startRecording(v)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_PERMISSION -> {
                if (grantResults.size >0 && grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    startRecording(toggleButton)
                }
                else {
//                    toggleButton.isChecked = false
//                    Snackbar.make(
//                        rootLayout, "Permissions", Snackbar.LENGTH_INDEFINITE).setAction("ENABLE") {
//                        val intent = Intent()
//                        intent.action = Settings.ACTION_APPLICATION_SETTINGS
//                        intent.addCategory(Intent.CATEGORY_DEFAULT)
//                        intent.data = Uri.parse("package:$packageName")
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
//                        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
//                        startActivity(intent)
//                    }.show()
                    startRecording(toggleButton)
                }
                return
            }
        }
    }

    private fun startRecording(v: View?) {
        if ((v as ToggleButton).isChecked) {
            initRecorder()
            screenShare()
        }
        else {
            mediaRecorder!!.stop()
            mediaRecorder!!.reset()
            stopScreenRecord()

            videoView.visibility = View.VISIBLE
            videoView.setVideoURI(Uri.parse(videoUri))
            videoView.start()
        }
    }

    private fun screenShare() {
        if (
            mediaProjection == null
        )
        {
            startActivityForResult(projectManager!!.createScreenCaptureIntent(), REQUEST_CODE)
            return
        }
        virtualDisplay = createVirtualDisplay()
        mediaRecorder!!.start()
    }

    private fun createVirtualDisplay(): VirtualDisplay? {
        return mediaProjection!!.createVirtualDisplay(
            "ScreenRecord", DISPLAY_WIDTH, DISPLAY_HEIGHT, screenDensity,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            mediaRecorder!!.surface, null, null
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode != REQUEST_CODE) {
            return
        }

        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(this, "Screen cast permission denied", Toast.LENGTH_SHORT).show()
            return
        }

            mediaProjectionCallback = MediaProjectionCallback()
            mediaProjection = projectManager?.getMediaProjection(resultCode, data!!)
            mediaProjection!!.registerCallback(mediaProjectionCallback, null)
            virtualDisplay = createVirtualDisplay()
            mediaRecorder!!.start()
    }

    private fun initRecorder() {
        try
        {
            mediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
            mediaRecorder!!.setVideoSource(MediaRecorder.VideoSource.SURFACE)
            mediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)

            videoUri = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
                .toString() + StringBuilder("/")
                .append("Screen_Recorder")
                .append(SimpleDateFormat("dd_MM_yyyy-hh_mm_ss").format(Date()))
                .append(".mp4")
                .toString()
            mediaRecorder!!.setOutputFile(videoUri)
            mediaRecorder!!.setVideoSize(DISPLAY_WIDTH, DISPLAY_HEIGHT)
            mediaRecorder!!.setVideoEncoder(MediaRecorder.VideoEncoder.H264)
            mediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            mediaRecorder!!.setVideoEncodingBitRate(512*1000)
            mediaRecorder!!.setVideoFrameRate(30)

            val rotation: Int = windowManager.defaultDisplay.rotation
            val orientation: Int = ORIENTATION.get(rotation + 90)
            mediaRecorder!!.setOrientationHint(orientation)
            mediaRecorder!!.prepare()
        }
        catch (e: IOException)
        {
            e.printStackTrace()
        }
//        try {
//            var recordFile = ("ScreenREC${System.currentTimeMillis()}.mp4")
//            mediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
//            mediaRecorder!!.setVideoSource(MediaRecorder.VideoSource.SURFACE)
//            mediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
//
//            val newPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) //.absolutePath
//            val folder = File(newPath, "ScreenREC/")
//            if (!folder.exists()) {
//                folder.mkdirs()
//            }
//            val file = File(folder, recordFile)
//            videoUri = file.absolutePath
//
//            mediaRecorder!!.setOutputFile(videoUri)
//            mediaRecorder!!.setVideoSize(DISPLAY_WIDTH, DISPLAY_HEIGHT)
//            mediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
//            mediaRecorder!!.setVideoEncoder(MediaRecorder.VideoEncoder.H264)
//            mediaRecorder!!.setVideoEncodingBitRate(512*1000)
//            mediaRecorder!!.setVideoFrameRate(30)
//
//            var rotation = windowManager.defaultDisplay.rotation
//            var orientation = ORIENTATION.get(rotation +90)
//            mediaRecorder!!.setOrientationHint(orientation)
//            mediaRecorder!!.prepare()
//        }
//        catch (e: IOException) {
//            e.printStackTrace()
//        }
    }

    private fun stopScreenRecord() {
        if (virtualDisplay == null) {
            return
            virtualDisplay!!.release()
            destroyMediaProjection()
        }
    }

    private fun destroyMediaProjection() {
        if (mediaProjection != null) {
            mediaProjection!!.unregisterCallback(mediaProjectionCallback)
            mediaProjection!!.stop()
            mediaProjection = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyMediaProjection()
    }
}