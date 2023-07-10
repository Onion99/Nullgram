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

package org.telegram.ktx.view.dsl

import android.content.Context
import android.view.TextureView
import android.view.View
import androidx.viewpager.widget.ViewPager

typealias NewViewRef<V> = (Context) -> V

inline fun <V: View> Context.view(createView: NewViewRef<V>, initView: V.() -> Unit = {}):V{
    return createView(this).apply(initView)
}

inline fun Context.viewPager(initView: ViewPager.() -> Unit = {}):ViewPager{
    return ViewPager(this).apply(initView)
}

inline fun Context.textureView(initView: TextureView.() -> Unit = {}): TextureView {
    return TextureView(this).apply(initView)
}
