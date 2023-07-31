package com.android.mobicam.camera.customcomponents

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.android.mobicam.camera.FlashlightHandler
import com.android.mobicam.utils.ProgressEvents
import com.android.mobicam.customcomponents.Button

@SuppressLint("ServiceCast")
class Flashlight @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : Button(context, attrs, defStyleAttr) {

    init {
        setOnTouchListener(OnTouchListener())
    }

    inner class OnTouchListener() : View.OnTouchListener {
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    ProgressEvents.onNext(ProgressEvents.Events.ToggleFlashlight)
                    FlashlightHandler.toggleFlashlight(context)
                }
            }
            return false
        }
    }
}
