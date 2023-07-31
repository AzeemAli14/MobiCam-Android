package com.android.mobicam.display.customcomponents

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.android.mobicam.IHideableLayout

class DisplayLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr), IHideableLayout {

    override fun show() {
        visibility = View.VISIBLE
    }

    override fun hide() {
        visibility = View.GONE
    }
}
