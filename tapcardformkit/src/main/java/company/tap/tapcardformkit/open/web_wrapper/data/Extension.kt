package company.tap.tapcardformkit

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import androidx.annotation.DimenRes
import androidx.annotation.RequiresApi
import androidx.core.os.postDelayed
import com.google.gson.Gson
import company.tap.tapcardformkit.open.web_wrapper.data.network.model.ThreeDsResponse
import company.tap.tapcardformkit.open.web_wrapper.data.rawFolderRefrence
import java.util.*


fun Context.px(@DimenRes dimen: Int): Int = resources.getDimension(dimen).toInt()

fun Context.dp(@DimenRes dimen: Int): Float = px(dimen) / resources.displayMetrics.density

fun Context.getDimensionsInDp(dimension: Int): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dimension.toFloat(),
        this.resources?.displayMetrics
    ).toInt()

}

fun Context.getAssetFile(filename: String): Int {
    Log.e("packagename", this.packageName.toString())
    return resources.getIdentifier(
        filename,
        rawFolderRefrence,
        this.packageName
    )
}

fun String.getModelFromJson(): ThreeDsResponse {
    return Gson().fromJson(this, ThreeDsResponse::class.java)
}

fun getRandomNumbers(length: Int): String {
    val allowedChars = ('0'..'9')
    return (1..length)
        .map { allowedChars.random() }.shuffled()
        .joinToString("")
}

fun getRandomTrx(length: Int=23): String {

    val letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    val prefix = "tck_LV"
    return prefix+(1..length)
        .map { letters.random() }.shuffled()
        .joinToString("")
}

fun getDeviceLocale(): Locale? {
    val defaultLocale = Resources.getSystem().configuration.locales.get(0);
    return defaultLocale

}

fun Context.getDeviceTheme(): String {
    return when(resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES){
        true ->"dark"
        false->"light"
    }
}
fun <T> List<T>?.jointToStringForUrl(): String? {
    return this?.joinToString()?.replace(", ", "%22%2C%22")
}

/**
 * function to get query data and decode it
 */
@RequiresApi(Build.VERSION_CODES.O)
fun Uri.getQueryParameterFromUri(keyValue: String?): String {
    val decodedBytes = String(Base64.getDecoder().decode(this.getQueryParameter(keyValue)),Charsets.UTF_8)

    return decodedBytes
}



fun getScreenHeight(): Int {
    return Resources.getSystem().getDisplayMetrics().heightPixels
}
fun Context.twoThirdHeightView(): Double {
    return getDeviceSpecs().first.times(2.15) / 3
}
fun Context.getDeviceSpecs(): Pair<Int, Int> {
    val displayMetrics = DisplayMetrics()
    (this.getActivity())?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
    val height = displayMetrics.heightPixels
    val width = displayMetrics.widthPixels
    val pair: Pair<Int, Int> = Pair(height, width)
    return pair
}


fun Context.getActivity(): Activity? {
    return when (this) {
        is Activity -> this
        is ContextWrapper -> this.baseContext.getActivity()
        else -> null
    }
}
fun doAfterSpecificTime(time: Long = 1000L, execute: () -> Unit) =
    Handler(Looper.getMainLooper()).postDelayed(time) {
        execute.invoke()
    }