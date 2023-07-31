package com.android.mobicam.customcomponents

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.android.mobicam.IHideableLayout

class MainLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) , IHideableLayout {

    init {
    }

    override fun show() {
        visibility = View.VISIBLE
    }

    override fun hide() {
        visibility = View.GONE
    }
}
