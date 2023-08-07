package com.android.mobicam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_videos.*

class VideosActivity : AppCompatActivity() {

    private lateinit var videoArrayList: ArrayList<ModelVideo>

    private lateinit var adapterVideo: AdapterVideo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_videos)

        title="Video"

        loadVideosFromFirebase()

        addVideoFab.setOnClickListener {
//            This will intent user to MainActivity
            startActivity(Intent(this, AddVideoActivity::class.java))
        }
    }

    private fun loadVideosFromFirebase() {
        videoArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Videos")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                videoArrayList.clear()
                for (ds in snapshot.children) {
                    //get data as model
                    val modelVideo = ds.getValue(ModelVideo::class.java)
                    //add to arrayList
                    videoArrayList.add(modelVideo!!)
                }

                //setup adapter
                adapterVideo = AdapterVideo(this@VideosActivity, videoArrayList)
                //set adapter to recyclerview
                videosRv.adapter = adapterVideo
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}