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

import android.view.Gravity
import android.view.View

inline val View.gravityCenter: Int get() = Gravity.CENTER
inline val View.gravityCenterVertical: Int get() = Gravity.CENTER_VERTICAL
inline val View.gravityCenterHorizontal: Int get() = Gravity.CENTER_HORIZONTAL

inline val View.gravityVerticalCenter: Int get() = gravityCenterVertical
inline val View.gravityHorizontalCenter: Int get() = gravityCenterHorizontal

inline val View.gravityStart: Int get() = Gravity.START
inline val View.gravityTop: Int get() = Gravity.TOP
inline val View.gravityEnd: Int get() = Gravity.END
inline val View.gravityBottom: Int get() = Gravity.BOTTOM

inline val View.gravityStartBottom: Int get() = Gravity.START or Gravity.BOTTOM
inline val View.gravityStartTop: Int get() = Gravity.START or Gravity.TOP
inline val View.gravityEndBottom: Int get() = Gravity.END or Gravity.BOTTOM
inline val View.gravityEndTop: Int get() = Gravity.END or Gravity.TOP

inline val View.gravityBottomStart: Int get() = gravityStartBottom
inline val View.gravityTopStart: Int get() = gravityStartTop
inline val View.gravityBottomEnd: Int get() = gravityEndBottom
inline val View.gravityTopEnd: Int get() = gravityEndTop

inline val View.gravityStartCenter: Int get() = Gravity.START or Gravity.CENTER_VERTICAL
inline val View.gravityEndCenter: Int get() = Gravity.END or Gravity.CENTER_VERTICAL

inline val View.gravityTopCenter: Int get() = Gravity.TOP or Gravity.CENTER_HORIZONTAL
inline val View.gravityBottomCenter: Int get() = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
