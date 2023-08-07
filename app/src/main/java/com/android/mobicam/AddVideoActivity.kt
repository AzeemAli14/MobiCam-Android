@file:Suppress("DEPRECATION")

package com.android.mobicam

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.MediaController
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_add_video.*

class AddVideoActivity : AppCompatActivity() {

//    private lateinit var actionBar: ActionBar
    private val VIDEO_PICK_GALLERY_CODE = 100
    private val VIDEO_PICK_CAMERA_CODE = 101
    private val CAMERA_REQUEST_CODE = 102
    private lateinit var cameraPermissions:Array<String>
    private lateinit var progressBar: ProgressBar
    private var videoUri: Uri? = null
    private lateinit var progressDialog: ProgressDialog
    private var title:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_video)

//        actionBar = supportActionBar!!
//        actionBar.title = "Add Video"
//        actionBar.setDisplayHomeAsUpEnabled(true)
//        actionBar.setDisplayShowHomeEnabled(true)

        cameraPermissions = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Uploading video...")
        progressDialog.setCanceledOnTouchOutside(false)


        uploadVideoBtn.setOnClickListener {
            title = titleEt.text.toString().trim()
            if(TextUtils.isEmpty(title)){
                Toast.makeText(this, "Please enter title", Toast.LENGTH_SHORT).show()
            }
            else if (videoUri == null){
                Toast.makeText(this, "Please select video", Toast.LENGTH_SHORT).show()
            }
            else {
                uploadVideosFirebase()
            }
        }

        pickVideoFab.setOnClickListener {
            videoPickDialog()
        }
    }

    private fun uploadVideosFirebase() {
        progressDialog.show()
        val timestamp = ""+System.currentTimeMillis()
        val filePathAndName = "Videos/video_$timestamp"

        val storageRef = FirebaseStorage.getInstance().getReference(filePathAndName)
        storageRef.putFile(videoUri!!)
            .addOnSuccessListener {
            taskSnapshot ->
            val uriTask = taskSnapshot.storage.downloadUrl
            while (!uriTask.isSuccessful);
            val downloadUri = uriTask.result
            if (uriTask.isSuccessful){
                val hashMap = HashMap<String, Any>()
                hashMap["id"] = "$timestamp"
                hashMap["title"] = "$title"
                hashMap["timestamp"] = "$timestamp"
                hashMap["videoUri"] = "$downloadUri"

                val ref = FirebaseDatabase.getInstance().getReference("Videos")
                ref.child(timestamp)
                    .setValue(hashMap)
                    .addOnSuccessListener {
                        progressDialog.dismiss()
                        Toast.makeText(this, "Video uploaded successfully", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {e ->
                        progressDialog.dismiss()
                        Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .addOnFailureListener {e ->
                progressDialog.dismiss()
                Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setVideoToVideoView() {
        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)
        videoView.setVideoURI(videoUri)
        videoView.requestFocus()
        videoView.setOnPreparedListener {
            videoView.pause()
        }
    }

    private fun videoPickDialog() {
        val options = arrayOf("Camera", "Gallery")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pick Video")
            .setItems(options) { dialog, i ->
                if (i == 0) {
                    if (!checkCameraPermission()) {
                        requestCameraPermission()
                    }
                    else {
                        videoPickCamera()
                    }
                } else {
                    videoPickGallery()
                }
            }
            .show()

    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            cameraPermissions,
            CAMERA_REQUEST_CODE
        )
    }

    private fun checkCameraPermission(): Boolean {
        val result1 = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        val result2 = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        return result1 && result2
    }

    private fun videoPickGallery() {
        val intent = Intent()
        intent.type = "video/*"
        intent.action = Intent.ACTION_GET_CONTENT

        @Suppress("DEPRECATION")
        startActivityForResult(
            Intent.createChooser(
                intent, "Select Video"
            ), VIDEO_PICK_GALLERY_CODE
        )
    }

    private fun videoPickCamera() {
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        @Suppress("DEPRECATION")
        startActivityForResult(intent, VIDEO_PICK_CAMERA_CODE)
    }

    override fun onSupportNavigateUp(): Boolean {
        @Suppress("DEPRECATION")
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isNotEmpty()) {
                    val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED
                    if (cameraAccepted && storageAccepted) {
                        videoPickCamera()
                    }
                    else {
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            if(requestCode == VIDEO_PICK_CAMERA_CODE) {
                videoUri = data!!.data
                setVideoToVideoView()
            }
            else if (requestCode == VIDEO_PICK_GALLERY_CODE) {
                videoUri = data!!.data
                setVideoToVideoView()
            }
        }
        else {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
        }
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)
    }
}