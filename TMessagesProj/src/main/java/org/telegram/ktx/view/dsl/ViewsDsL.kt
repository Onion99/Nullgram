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

import android.annotation.SuppressLint
import android.content.Context
import android.view.ContextThemeWrapper
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.StyleRes
import androidx.core.view.get
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

const val NO_THEME = 0
private const val VIEW_FACTORY = "splitties:views.dsl:viewfactory"

@Suppress("NOTHING_TO_INLINE")
inline fun Context.withTheme(theme: Int) = ContextThemeWrapper(this, theme)
@Suppress("NOTHING_TO_INLINE")
inline fun Context.wrapCtxIfNeeded(theme: Int): Context = if (theme == NO_THEME) this else withTheme(theme)

typealias NewViewRef<V> = (Context) -> V
inline fun <V: View> Context.view(createView: NewViewRef<V>, initView: V.() -> Unit = {}):V = createView(this).apply(initView)

inline fun <V : View> Context.view(
    createView: NewViewRef<V>,
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: V.() -> Unit = {}
): V {
    return createView(wrapCtxIfNeeded(theme)).also { it.id = id }.apply(initView)
}

inline fun <reified V : View> Context.view(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: V.() -> Unit = {}
): V =  viewFactory(V::class.java, wrapCtxIfNeeded(theme)).also { it.id = id }.apply(initView)

val Context.viewFactory: ViewFactory
    @SuppressLint("WrongConstant")
    get() = try { getSystemService(VIEW_FACTORY) as ViewFactory? ?: ViewFactory.appInstance
    } catch (t: Throwable) {
        ViewFactory.appInstance
    }


