package company.tap.tapcardformkit.open.web_wrapper.presentation.nfc_activity.nfcbottomsheet

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.nfc.NfcAdapter
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

/**
 * A phone outline with a pulsing dot where this device's NFC antenna sits, so people put the
 * card in the right spot instead of hunting for it. Phones only read a card held over a small
 * area of the back; held a few centimetres off, the card is not detected at all.
 *
 * Drawn as the user sees the phone (screen facing them), which matches how the position is
 * reported: millimetres from the top-left corner in portrait. The dot marks the spot on the
 * back, behind the screen.
 *
 * The position comes from [NfcAdapter.getNfcAntennaInfo] (Android 14+). Use [bind]; it returns
 * false when the device reports nothing, and the caller should then leave the view hidden.
 */
class NfcAntennaGuideView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private var deviceWidthMm = 0f
    private var deviceHeightMm = 0f
    private var antennaXmm = 0f
    private var antennaYmm = 0f

    private val density = resources.displayMetrics.density
    private val outline = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 1.5f * density
    }
    private val dot = Paint(Paint.ANTI_ALIAS_FLAG)
    private val ring = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 1.5f * density
    }
    private val body = RectF()
    private var pulse = 0f
    private val pulser = ValueAnimator.ofFloat(0f, 1f).apply {
        duration = 1400
        repeatCount = ValueAnimator.INFINITE
        interpolator = LinearInterpolator()
        addUpdateListener { pulse = it.animatedValue as Float; invalidate() }
    }

    /** Outline and dot colour; the pulse ring uses the same colour, fading out. */
    fun setColor(color: Int) {
        outline.color = Color.argb(150, Color.red(color), Color.green(color), Color.blue(color))
        dot.color = color
        ring.color = color
        invalidate()
    }

    /** Reads the antenna position from [adapter]; false when the device does not report one. */
    fun bind(adapter: NfcAdapter?): Boolean {
        if (adapter == null || Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE) return false
        val info = runCatching { adapter.nfcAntennaInfo }.getOrNull() ?: return false
        val antenna = info.availableNfcAntennas.firstOrNull() ?: return false
        if (info.deviceWidth <= 0 || info.deviceHeight <= 0) return false
        deviceWidthMm = info.deviceWidth.toFloat()
        deviceHeightMm = info.deviceHeight.toFloat()
        antennaXmm = antenna.locationX.toFloat().coerceIn(0f, deviceWidthMm)
        antennaYmm = antenna.locationY.toFloat().coerceIn(0f, deviceHeightMm)
        invalidate()
        return true
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        pulser.start()
    }

    override fun onDetachedFromWindow() {
        pulser.cancel()
        super.onDetachedFromWindow()
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (visibility == VISIBLE && isAttachedToWindow) pulser.start() else pulser.cancel()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (deviceWidthMm <= 0f) return
        val pad = outline.strokeWidth
        val availW = width - paddingLeft - paddingRight - 2 * pad
        val availH = height - paddingTop - paddingBottom - 2 * pad
        // Keep the real device proportions, centred in the view.
        val scale = minOf(availW / deviceWidthMm, availH / deviceHeightMm)
        val w = deviceWidthMm * scale
        val h = deviceHeightMm * scale
        val left = paddingLeft + pad + (availW - w) / 2
        val top = paddingTop + pad + (availH - h) / 2
        body.set(left, top, left + w, top + h)
        val corner = w * 0.16f
        canvas.drawRoundRect(body, corner, corner, outline)

        val cx = left + antennaXmm * scale
        val cy = top + antennaYmm * scale
        val r = w * 0.11f
        ring.alpha = ((1f - pulse) * 255).toInt()
        canvas.drawCircle(cx, cy, r * (1f + pulse * 1.4f), ring)
        canvas.drawCircle(cx, cy, r, dot)
    }
}
