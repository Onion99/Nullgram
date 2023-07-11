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

package org.telegram.ui.drawable

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.view.View
import androidx.core.graphics.ColorUtils
import org.telegram.ui.view.helper.UIHelper

class ViewFlickerDrawable {

    var size =0
    var rectTmp = RectF()
    private var gradientShader:Shader
    private var gradientShader2:Shader
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintOutline = Paint(Paint.ANTI_ALIAS_FLAG)
    private var matrix = Matrix()
    constructor():this(64,204)
    constructor(alphaLevelFirst:Int,alphaLevelSecond:Int){
        size = UIHelper.dpI(160)
        gradientShader = LinearGradient(0f, 0f, size.toFloat(), 0f, intArrayOf(Color.TRANSPARENT, ColorUtils.setAlphaComponent(Color.WHITE, alphaLevelFirst), Color.TRANSPARENT), null, Shader.TileMode.CLAMP)
        gradientShader2 = LinearGradient(0f, 0f, size.toFloat(), 0f, intArrayOf(Color.TRANSPARENT, ColorUtils.setAlphaComponent(Color.WHITE, alphaLevelSecond), Color.TRANSPARENT), null, Shader.TileMode.CLAMP)
        paint.shader = gradientShader
        paintOutline.run {
            shader = gradientShader2
            style = Paint.Style.STROKE
            strokeWidth= UIHelper.dpF(2f)
        }
    }


    fun draw(canvas: Canvas, rectF: RectF, rad: Float, view: View?) {
        update(view)
        canvas.drawRoundRect(rectF, rad, rad, paint)
        canvas.drawRoundRect(rectF, rad, rad, paintOutline)
    }

    var parentWidth = 0
    var repeatProgress = 1.2f
    private  var repeatEnabled = true
    private  var progress = 0f
    private  var lastUpdateTime: Long = 0
    private  var animationSpeedScale = 1f
    private fun update(view: View?) {
        if (repeatEnabled || progress < 1) {
            view?.invalidate()
            val currentTime = System.currentTimeMillis()
            if (lastUpdateTime != 0L) {
                val dt: Long = currentTime - lastUpdateTime
                if (dt > 10) {
                    progress += dt / 1200f * animationSpeedScale
                    if (progress > repeatProgress) {
                        progress = 0f
                    }
                    lastUpdateTime = currentTime
                }
            } else {
                lastUpdateTime = currentTime
            }
        }
        val x: Float = (parentWidth + size * 2) * progress - size
        matrix.reset()
        matrix.setTranslate(x, 0f)
        gradientShader.setLocalMatrix(matrix)
        gradientShader2.setLocalMatrix(matrix)
    }
}
