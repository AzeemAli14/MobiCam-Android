package com.android.mobicam.camera.customcomponents

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.android.mobicam.IHideableLayout

class CameraLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr), IHideableLayout {

    init {
        hide()
    }

    override fun show() {
        visibility = View.VISIBLE
    }

    override fun hide() {
        visibility = View.GONE
    }
}
