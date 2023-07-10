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

package org.telegram.ui.view.helper

import android.content.Context
import android.content.res.Configuration
import android.graphics.Point
import android.util.DisplayMetrics
import android.view.WindowManager
import org.telegram.messenger.AndroidUtilities
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor

@Suppress("DEPRECATION")
object UIHelper {
    var density = 1f
    // 屏幕刷新率
    var screenRefreshRate = 60f
    // 屏幕刷新时间
    private var screenRefreshTime = 1000 / screenRefreshRate
    // 显示器的尺寸和密度
    private var displayMetrics = DisplayMetrics()
    // 显示的大小（以像素为单位
    var displaySize = Point()


    // ------------------------------------------------------------------------
    // 检查系统屏幕配置,从而展示UI所需要的界面尺寸
    // ------------------------------------------------------------------------
    fun checkSystemDisplaySize(context: Context, newConfiguration: Configuration?){
        val oldDensity = density
        density = context.resources.displayMetrics.density
        val newDensity = density
        if(abs(oldDensity - newDensity) > 0.1){
            // todo density has change
        }
        val configuration = newConfiguration ?: context.resources.configuration
        // ---- 获取屏幕配置 ------
        val manger = context.getSystemService(Context.WINDOW_SERVICE) as? WindowManager
        manger?.run {
            val display = manger.defaultDisplay
            display?.run {
                getMetrics(displayMetrics)
                getSize(displaySize)
                screenRefreshRate = display.refreshRate
                screenRefreshTime = 1000 / screenRefreshRate
            }
        }
        if (configuration.screenWidthDp != Configuration.SCREEN_WIDTH_DP_UNDEFINED) {
            val newSize = ceil((configuration.screenWidthDp * density)).toInt()
            if (abs(displaySize.x - newSize) > 3) {
                displaySize.x = newSize
            }
        }
        if (configuration.screenHeightDp != Configuration.SCREEN_HEIGHT_DP_UNDEFINED) {
            val newSize = ceil((configuration.screenHeightDp * density)).toInt()
            if (abs(displaySize.y - newSize) > 3) {
                displaySize.y = newSize
            }
        }
    }

    // ------------------------------------------------------------------------
    // ui dp size
    // ------------------------------------------------------------------------
    fun dpI(value:Float):Int{
        if(value == 0F) return 0
        return floor((density * value)).toInt()
    }
    fun dpI(value:Int):Int{
        if(value == 0) return 0
        return floor((density * value)).toInt()
    }


    fun dpF(value:Float):Float{
        if(value == 0F) return 0F
        return floor((density * value))
    }
}
