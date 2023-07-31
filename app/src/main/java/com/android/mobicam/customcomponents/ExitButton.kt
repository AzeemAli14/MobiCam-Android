package com.android.mobicam.customcomponents

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.android.mobicam.HomeActivity
import kotlin.system.exitProcess

class ExitButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : Button(context, attrs, defStyleAttr) {

    init {
        setOnTouchListener(OnTouchListener())
    }

    inner class OnTouchListener : View.OnTouchListener {
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            (context as Activity)?.let {
                val intent = Intent (it, HomeActivity::class.java)
                it.startActivity(intent)
            }
            return true
        }
    }
}
