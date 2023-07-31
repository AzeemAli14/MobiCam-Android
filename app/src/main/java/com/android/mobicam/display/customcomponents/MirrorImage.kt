package com.android.mobicam.display.customcomponents

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.android.mobicam.utils.ProgressEvents
import com.android.mobicam.customcomponents.Button

class MirrorImage @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : Button(context, attrs, defStyleAttr) {

    private var rotationAngle = 0f

    init {
        setOnTouchListener(OnTouchListener())
    }

    inner class OnTouchListener() : View.OnTouchListener {
        @SuppressLint("ClickableViewAccessibility")
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    ProgressEvents.onNext(ProgressEvents.Events.ToggleMirror)
                    rotationAngle = if (rotationAngle == 0f) 180f else 0f
                    animate().rotation(rotationAngle).start()
                }
            }
            return false
        }
    }
}
