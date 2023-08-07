package com.android.mobicam.customcomponents

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.android.mobicam.AddVideoActivity
import com.android.mobicam.HomeActivity
import com.android.mobicam.RecordingActivity
import com.android.mobicam.ScreenRecord
import com.android.mobicam.VideosActivity
import kotlin.system.exitProcess

class ExitButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : Button(context, attrs, defStyleAttr) {

    init {
        setOnTouchListener(OnTouchListener())
//        setOnClickListener(OnLongClickListener())
    }

    inner class OnTouchListener : View.OnTouchListener {
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            (context as Activity).let {
                val intent = Intent (it, VideosActivity::class.java)
                it.startActivity(intent)
            }
            return true
        }
    }

//    inner class OnLongClickListener : View.OnLongClickListener {
//        fun setOnLongClickListener(v: View?, event: MotionEvent?): Boolean {
//            (context as Activity).let {
//                val intent = Intent (it, ScreenRecord::class.java)
//                it.startActivity(intent)
//            }
//            return true
//        }
//    }

}
