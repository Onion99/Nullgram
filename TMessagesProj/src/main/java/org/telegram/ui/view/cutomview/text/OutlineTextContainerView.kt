/*
 * Copyright (C) 2019-2023 qwq233 <qwq233@qwq2333.top>
 * https://github.com/qwq233/Nullgram
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this software.
 *  If not, see
 * <https://www.gnu.org/licenses/>
 */

package org.telegram.ui.view.cutomview.text

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Region
import android.text.TextPaint
import android.util.AttributeSet
import android.widget.EditText
import android.widget.FrameLayout
import androidx.core.graphics.ColorUtils
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import org.telegram.ui.ActionBar.Theme
import org.telegram.ui.view.helper.dip
import org.telegram.ui.view.helper.dipFI
import org.telegram.ui.view.property.anim.SimpleFloatPropertyCompat

private const val PADDING_LEFT = 14f
private const val PADDING_TEXT = 4f
private const val SPRING_MULTIPLIER = 100f
private val selectionProgressProperty = SimpleFloatPropertyCompat("selectionProgress", object : SimpleFloatPropertyCompat.Getter<OutlineTextContainerView> {
    override fun get(obj: OutlineTextContainerView): Float {
        return obj.selectionProgress
    }
}, object : SimpleFloatPropertyCompat.Setter<OutlineTextContainerView> {
    override fun set(obj: OutlineTextContainerView, value: Float) {
        obj.selectionProgress = value
        if (!obj.forceUseCenter) {
            obj.outlinePaint.strokeWidth = obj.strokeWidthRegular + (obj.strokeWidthSelected - obj.strokeWidthRegular) * obj.selectionProgress
            obj.updateColor()
        }
        obj.invalidate()
    }
}).setMultiplier(SPRING_MULTIPLIER)

private val errorProgressProperty = SimpleFloatPropertyCompat("errorProgress", object : SimpleFloatPropertyCompat.Getter<OutlineTextContainerView> {
    override fun get(obj: OutlineTextContainerView): Float {
        return obj.errorProgress
    }
}, object : SimpleFloatPropertyCompat.Setter<OutlineTextContainerView> {
    override fun set(obj: OutlineTextContainerView, value: Float) {
        obj.errorProgress = value
        obj.invalidate()
    }
}).setMultiplier(SPRING_MULTIPLIER)

class OutlineTextContainerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs) {


    private val rect = RectF()
    private var mText = ""
    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private val selectionSpring = SpringAnimation(this, selectionProgressProperty)
    private val errorSpring = SpringAnimation(this, errorProgressProperty)
    private var attachedEditText: EditText?= null
    var selectionProgress = 0f
    val outlinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    var errorProgress = 0f
    val strokeWidthRegular = 2.coerceAtLeast(dipFI(0.5f)).toFloat()
    val strokeWidthSelected = dip(1.5f)
    var forceUseCenter = false
        private set

    init {
        setWillNotDraw(false)
        textPaint.textSize = dip(16f)
        outlinePaint.style = Paint.Style.STROKE
        outlinePaint.strokeCap = Paint.Cap.ROUND
        outlinePaint.strokeWidth = strokeWidthRegular
        updateColor()
        setPadding(0,dipFI(6f), 0, 0)
    }

    fun setForceUseCenter(forceUseCenter: Boolean) {
        this.forceUseCenter = forceUseCenter
        invalidate()
    }

    fun attachEditText(attachedEditText: EditText) {
        this.attachedEditText = attachedEditText
        invalidate()
    }

    fun setText(text: String) {
        mText = text
        invalidate()
    }

    private fun setColor(color: Int) {
        outlinePaint.color = color
        invalidate()
    }

    fun updateColor() {
        val textSelectionColor = ColorUtils.blendARGB(
            Theme.getColor(Theme.key_windowBackgroundWhiteHintText),
            Theme.getColor(Theme.key_windowBackgroundWhiteValueText),
            if (forceUseCenter) 0f else selectionProgress
        )
        textPaint.color = ColorUtils.blendARGB(textSelectionColor, Theme.getColor(Theme.key_text_RedBold), errorProgress)
        val selectionColor = ColorUtils.blendARGB(
            Theme.getColor(Theme.key_windowBackgroundWhiteInputField),
            Theme.getColor(Theme.key_windowBackgroundWhiteInputFieldActivated),
            if (forceUseCenter) 0f else selectionProgress
        )
        setColor(ColorUtils.blendARGB(selectionColor, Theme.getColor(Theme.key_text_RedBold), errorProgress))
    }

    @JvmOverloads
    fun animateSelection(newValue: Float, animate: Boolean = true) {
        if (!animate) {
            selectionProgress = newValue
            if (!forceUseCenter) {
                outlinePaint.strokeWidth = strokeWidthRegular + (strokeWidthSelected - strokeWidthRegular) * selectionProgress
            }
            updateColor()
            return
        }
        animateSpring(selectionSpring, newValue)
    }

    fun animateError(newValue: Float) {
        animateSpring(errorSpring, newValue)
    }

    private fun animateSpring(spring: SpringAnimation, newValue: Float) {
        var currentValue = newValue
        currentValue *= SPRING_MULTIPLIER
        if (spring.spring != null && currentValue == spring.spring.finalPosition) return
        spring.cancel()
        spring.setSpring(SpringForce(currentValue)
                .setStiffness(500f)
                .setDampingRatio(SpringForce.DAMPING_RATIO_NO_BOUNCY)
                .setFinalPosition(currentValue)).start()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas ?: return
        val textOffset = textPaint.textSize / 2f - dip(1.75f)
        val topY = paddingTop + textOffset
        val centerY = height / 2f + textPaint.textSize / 2f
        val useCenter = attachedEditText != null && attachedEditText!!.length() == 0 && !attachedEditText!!.hint.isNullOrEmpty() || forceUseCenter
        val textY = if (useCenter) topY + (centerY - topY) * (1f - selectionProgress) else topY
        val stroke = outlinePaint.strokeWidth

        val scaleX = if (useCenter) 0.75f + 0.25f * (1f - selectionProgress) else 0.75f
        val textWidth = textPaint.measureText(mText) * scaleX


        canvas.save()
        rect.set(paddingStart + dip(PADDING_LEFT - PADDING_TEXT), paddingTop + stroke , width - dip(PADDING_LEFT - PADDING_TEXT), height + stroke * 2)
        // 图像合成
        canvas.clipRect(rect,Region.Op.DIFFERENCE)
        rect.set(paddingLeft + stroke,paddingTop + stroke,width - stroke - paddingEnd,height - stroke -paddingBottom)
        canvas.drawRoundRect(rect,dip(6f),dip(6f),outlinePaint)
        canvas.restore()

        val left  = paddingLeft  + dip(PADDING_LEFT - PADDING_TEXT)
        val right = width - stroke - paddingEnd - dip(6f)
        val lineY = paddingTop + stroke

        val activeLeft = left + textWidth + dip(PADDING_LEFT - PADDING_TEXT)
        val fromLeft = left + textWidth / 2f
        canvas.drawLine(fromLeft + (activeLeft - fromLeft) * if (useCenter) selectionProgress else 1f, lineY, right, lineY, outlinePaint)


        val fromRight = left /2f + dip(PADDING_TEXT)
        canvas.drawLine(left, lineY, fromRight + (left - fromRight) * if (useCenter) selectionProgress else 1f, lineY, outlinePaint)

        canvas.save()
        canvas.scale(scaleX,scaleX,paddingStart + dip(PADDING_LEFT + PADDING_TEXT),textY)
        canvas.drawText(mText,paddingStart + dip(PADDING_LEFT),textY,textPaint)
        canvas.restore()
    }
}
