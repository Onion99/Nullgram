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

package org.telegram.ui.view.property.anim

import androidx.dynamicanimation.animation.FloatPropertyCompat

class SimpleFloatPropertyCompat<T>(name: String, private val getter: Getter<T>, private val setter: Setter<T>) : FloatPropertyCompat<T>(name) {
    var multiplier = 1f
        private set

    fun setMultiplier(multiplier: Float): SimpleFloatPropertyCompat<T> {
        this.multiplier = multiplier
        return this
    }

    override fun getValue(any: T): Float {
        return getter[any] * multiplier
    }

    override fun setValue(any: T, value: Float) {
        setter[any] = value / multiplier
    }

    interface Getter<T> {
        operator fun get(obj: T): Float
    }

    interface Setter<T> {
        operator fun set(obj: T, value: Float)
    }
}
