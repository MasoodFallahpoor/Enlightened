package ir.fallahpoor.enlightened.presentation.common

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import ir.fallahpoor.enlightened.R
import kotlinx.android.synthetic.main.try_again_layout.view.*

class TryAgainView : LinearLayout {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(
        context,
        attrs,
        0
    )

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(
        context,
        attrs,
        defStyleAttr,
        0
    )

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        orientation = LinearLayout.VERTICAL
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.try_again_layout, this, true)
    }

    fun setErrorMessage(errorMessage: String) {
        errorMessageTextView.text = errorMessage
    }

    fun setTryAgainButtonClickListener(clickListener: OnClickListener) {
        tryAgainButton.setOnClickListener(clickListener)
    }

}