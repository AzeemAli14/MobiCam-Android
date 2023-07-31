package com.android.mobicam.customcomponents

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.mobicam.IHideableLayout

class PreConnectLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), IHideableLayout {

    init {
        show()
    }

    override fun show() {
        visibility = View.VISIBLE
    }

    override fun hide() {
        visibility = View.GONE
    }
}
