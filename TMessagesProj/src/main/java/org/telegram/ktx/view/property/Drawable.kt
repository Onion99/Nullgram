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

package org.telegram.ktx.view.property

//noinspection SuspiciousImport
import android.R
import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.StateListDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.os.Build
import android.util.StateSet
import android.view.View

fun View.createSimpleSelectorRoundRectDrawable(rad:Int, defaultColor: Int, pressedColor: Int):Drawable = createSimpleSelectorRoundRectDrawable(rad, defaultColor, pressedColor, pressedColor)
@SuppressLint("ObsoleteSdkInt")
fun View.createSimpleSelectorRoundRectDrawable(rad:Int, defaultColor: Int, pressedColor: Int, maskColor: Int): Drawable{
    val defaultDrawable = ShapeDrawable(
        RoundRectShape(
            floatArrayOf(rad.toFloat(), rad.toFloat(), rad.toFloat(), rad.toFloat(), rad.toFloat(), rad.toFloat(), rad.toFloat(), rad.toFloat()),
            null,
            null
        )
    )
    defaultDrawable.paint.color = defaultColor
    val pressedDrawable = ShapeDrawable(
        RoundRectShape(
            floatArrayOf(rad.toFloat(), rad.toFloat(), rad.toFloat(), rad.toFloat(), rad.toFloat(), rad.toFloat(), rad.toFloat(), rad.toFloat()),
            null,
            null
        )
    )
    pressedDrawable.paint.color = maskColor
    return if (Build.VERSION.SDK_INT >= 21) {
        val colorStateList = ColorStateList(arrayOf(StateSet.WILD_CARD), intArrayOf(pressedColor))
        RippleDrawable(colorStateList, defaultDrawable, pressedDrawable)
    } else {
        val stateListDrawable = StateListDrawable()
        stateListDrawable.addState(intArrayOf(R.attr.state_pressed), pressedDrawable)
        stateListDrawable.addState(intArrayOf(R.attr.state_selected), pressedDrawable)
        stateListDrawable.addState(StateSet.WILD_CARD, defaultDrawable)
        stateListDrawable
    }
}
