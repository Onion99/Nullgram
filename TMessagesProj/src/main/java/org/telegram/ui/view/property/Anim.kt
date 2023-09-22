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

package org.telegram.ui.view.property

import android.view.animation.Interpolator
import org.telegram.ui.Components.CubicBezierInterpolator

// Sine
val easeOutSine: Interpolator = CubicBezierInterpolator(0.39, 0.575, 0.565, 1.0)
val easeInOutSine: Interpolator = CubicBezierInterpolator(0.445, 0.05, 0.55, 0.95)

// Quad
val easeInQuad: Interpolator = CubicBezierInterpolator(0.55, 0.085, 0.68, 0.53)
val easeOutQuad: Interpolator = CubicBezierInterpolator(0.25, 0.46, 0.45, 0.94)
val easeInOutQuad: Interpolator = CubicBezierInterpolator(0.455, 0.03, 0.515, 0.955)
