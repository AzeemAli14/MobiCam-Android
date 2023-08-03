package com.android.mobicam

import android.app.DownloadManager
import android.app.ProgressDialog
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storageMetadata
import java.util.*
import kotlin.collections.ArrayList

class AdapterVideo (
    private var context: Context,
    private var videoArrayList: ArrayList<ModelVideo>?
    ) : RecyclerView.Adapter<AdapterVideo.HolderVideo>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderVideo {
        val view = LayoutInflater.from(context).inflate(R.layout.player_video, parent, false)
        return HolderVideo(view)
    }

    override fun onBindViewHolder(holder: HolderVideo, position: Int) {
        val modelVideo = videoArrayList!![position]

        val id: String? = modelVideo.id
        val title: String? = modelVideo.title
        val timestamp: String? = modelVideo.timestamp
        val videoUri: String? = modelVideo.videoUri

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp!!.toLong()
        val formattedDateTime = android.text.format.DateFormat.format("dd/MM/yyyy K:mm a", calendar).toString()
        
        holder.titleTv.text = title
        holder.timeTv.text = formattedDateTime
        setVideoUrl(modelVideo, holder)
    }

    private fun setVideoUrl(modelVideo: ModelVideo, holder: HolderVideo) {
        holder.progressBar.visibility = View.VISIBLE

        val videoUrl: String? = modelVideo.videoUri

        //MediaController for play/pause/stop
        val mediaController = MediaController(context)
        mediaController.setAnchorView(holder.videoView)
        val videoUri = Uri.parse(videoUrl)

        holder.videoView.setMediaController(mediaController)
        holder.videoView.setVideoURI(videoUri)
        holder.videoView.requestFocus()

        holder.videoView.setOnPreparedListener { mediaPlayer ->
            //video is prepared to play
            mediaPlayer.start()

            //delete click Listener
            holder.deleteFab.setOnClickListener {
                deleteVideo(modelVideo)
            }

            //download click Listener
            holder.downloadFab.setOnClickListener {
                downloadVideo(modelVideo)
            }
        }
        holder.videoView.setOnInfoListener(MediaPlayer.OnInfoListener { mp, what, extra ->
            when(what){
                MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START -> {
                    //rendering start
                    holder.progressBar.visibility = View.VISIBLE
                    return@OnInfoListener true
                }

                MediaPlayer.MEDIA_INFO_BUFFERING_START -> {
                    //buffering start
                    holder.progressBar.visibility = View.VISIBLE
                    return@OnInfoListener true
                }
                MediaPlayer.MEDIA_INFO_BUFFERING_END -> {
                    //buffering end
                    holder.progressBar.visibility = View.GONE
                    return@OnInfoListener true
                }
            }
            false
        })
        holder.videoView.setOnCompletionListener { mediaPLayer ->
            mediaPLayer.start()
        }
    }

    private fun downloadVideo(modelVideo: ModelVideo) {
        val videoUrl = modelVideo.videoUri!!

        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(videoUrl)
        storageReference.metadata
            .addOnSuccessListener { storageMetadata ->
                val fileName = storageMetadata.name
                val fileType = storageMetadata.contentType
                val fileDirectory = Environment.DIRECTORY_DOWNLOADS

                val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

                val uri = Uri.parse(videoUrl)

                val request = DownloadManager.Request(uri)

                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

                request.setDestinationInExternalPublicDir("$fileDirectory", "$fileName.mp4")

                downloadManager.enqueue(request)
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteVideo(modelVideo: ModelVideo) {
        val progressDialog: ProgressDialog = ProgressDialog(context)
        progressDialog.setTitle("Please Wait...")
        progressDialog.setMessage("Deleting Video...")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()

        val videoId = modelVideo.id
        val videoUrl = modelVideo.videoUri!!

        val storageReference = FirebaseStorage.getInstance().getReference(videoUrl)
        storageReference.delete()
            .addOnSuccessListener {
                val databaseReference = FirebaseDatabase.getInstance().getReference("Videos")
                databaseReference.child(videoUrl)
                    .removeValue()
                    .addOnSuccessListener {
                        progressDialog.dismiss()
                        Toast.makeText(context, "Deleted Successfully...", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        progressDialog.dismiss()
                        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
    }

    override fun getItemCount(): Int {
        return videoArrayList!!.size
    }

    class HolderVideo(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var videoView:VideoView = itemView.findViewById(R.id.vView)
        var titleTv:TextView = itemView.findViewById(R.id.titleTv)
        var timeTv:TextView = itemView.findViewById(R.id.timeTv)
        var progressBar:ProgressBar = itemView.findViewById(R.id.pBar)
        var downloadFab:FloatingActionButton = itemView.findViewById(R.id.downloadFab)
        var deleteFab:FloatingActionButton = itemView.findViewById(R.id.deleteFab)
    }
}