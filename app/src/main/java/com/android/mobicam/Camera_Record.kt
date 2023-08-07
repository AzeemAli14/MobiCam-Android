package com.android.mobicam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_camera_record.addVideoFab

class Camera_Record : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_record)

        addVideoFab.setOnClickListener{
            startActivity(Intent(this, AddVideoActivity::class.java))
        }
    }
}