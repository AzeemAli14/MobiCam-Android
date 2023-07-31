package com.android.mobicam.customcomponents

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.android.mobicam.utils.ProgressEvents

class StartCameraButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : Button(context, attrs, defStyleAttr) {

    init {
        setOnTouchListener(OnTouchListener())
        show()
    }

    inner class OnTouchListener() : View.OnTouchListener {
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    ProgressEvents.onNext(ProgressEvents.Events.StartCamera)
                }
            }
            return false
        }
    }
}
