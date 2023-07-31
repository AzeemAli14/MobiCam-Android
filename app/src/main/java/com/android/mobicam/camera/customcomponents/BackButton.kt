package com.android.mobicam.camera.customcomponents

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.android.mobicam.camera.Camera
import com.android.mobicam.utils.ProgressEvents

class BackButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : com.google.android.material.button.MaterialButton(context, attrs, defStyleAttr) {

    init {
        setOnTouchListener(OnTouchListener())
    }

    inner class OnTouchListener() : View.OnTouchListener {
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            when (event?.action) {
                MotionEvent.ACTION_UP -> {
                    Camera.disconnect()
                    ProgressEvents.onNext(ProgressEvents.Events.ShowMainScreen)
                }
            }
            return false
        }
    }
}
