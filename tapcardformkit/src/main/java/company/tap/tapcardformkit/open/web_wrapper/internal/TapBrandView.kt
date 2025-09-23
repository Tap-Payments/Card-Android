package company.tap.tapcardformkit.open.web_wrapper.internal
import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import company.tap.tapcardformkit.R

import company.tap.taplocalizationkit.LocalizationManager


class TapBrandView : LinearLayout {

    val poweredByImage by lazy { findViewById<AppCompatImageView>(R.id.poweredByImage) }
    val outerConstraint by lazy { findViewById<ConstraintLayout>(R.id.outerConstraint) }
    val constraint by lazy { findViewById<CardView>(R.id.outerConstraint) }
    val backButtonLinearLayout by lazy { findViewById<LinearLayout>(R.id.back_btn_linear) }
    val imageBack by lazy { findViewById<ImageView>(R.id.image_back) }
    val backTitle by lazy { findViewById<TextView>(R.id.back_title) }

    @DrawableRes
    val logoIcon: Int =
        if (ThemeManager.currentTheme.isNotEmpty() && ThemeManager.currentTheme.contains("dark")) {
            R.drawable.poweredbytap2
        } else if (ThemeManager.currentTheme.isNotEmpty() && ThemeManager.currentTheme.contains("light")) {
           R.drawable.poweredbytap2
        } else R.drawable.poweredbytap2


    /**
     * Simple constructor to use when creating a TapHeader from code.
     *  @param con] ext The Context the view is running in, through which it can
     *  access the current theme, resources, etc.
     **/
    constructor(context: Context) : super(context)

    /**
     *  @param context The Context the view is running in, through which it can
     *  access the current theme, resources, etc.
     *  @param attrs The attributes of the XML Button tag being used to inflate the view.
     *
     */
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    /**
     *  @param context The Context the view is running in, through which it can
     *  access the current theme, resources, etc.
     *  @param attrs The attributes of the XML Button tag being used to inflate the view.
     * @param defStyleAttr The resource identifier of an attribute in the current theme
     * whose value is the the resource id of a style. The specified style’s
     * attribute values serve as default values for the button. Set this parameter
     * to 0 to avoid use of default values.
     */
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        inflate(context, R.layout.tap_brandview, this)
        poweredByImage.setImageResource(logoIcon)
        backTitle.text = resources.getString(R.string.back)

        // backTitle.setTextColor(loadAppThemManagerFromPath(AppColorTheme.PoweredByTapBackButtonLabelColor))
        //imageBack.backgroundTintList =
        //  ColorStateList.valueOf(loadAppThemManagerFromPath(AppColorTheme.PoweredByTapBackButtonIconColor))



        if (LocalizationManager.getLocale(context).language == "ar") {
            imageBack.rotation = 180f
            backTitle?.typeface = Typeface.createFromAsset(
                context?.assets, TapFont.tapFontType(
                    TapFont.TajawalMedium
                )
            )
        }else{
            backTitle?.typeface = Typeface.createFromAsset(
                context?.assets, TapFont.tapFontType(
                    TapFont.RobotoRegular
                )
            )
        }
    }



}