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

package org.telegram.ktx.view.dsl.link

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.annotation.IdRes
import androidx.annotation.StyleRes
import androidx.core.view.get
import androidx.viewpager.widget.ViewPager
import org.telegram.ktx.view.dsl.NO_THEME
import org.telegram.ktx.view.dsl.view
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@Suppress("NOTHING_TO_INLINE")
inline fun <V : View> ViewGroup.add(
    view: V,
    lp: ViewGroup.LayoutParams
): V = view.also { addView(it, lp) }

inline val ViewGroup.lastChild: View get() = this[childCount - 1]

@Suppress("CONFLICTING_OVERLOADS")
@JvmName("addBefore")
fun <V : View> ViewGroup.add(
    view: V,
    lp: ViewGroup.LayoutParams,
    beforeChild: View
): V {
    val index = indexOfChild(beforeChild).also {
        check(it != -1) { "Value passed in beforeChild is not a child of the ViewGroup!" }
    }
    return view.also { addView(it, index, lp) }
}

@Suppress("CONFLICTING_OVERLOADS")
@JvmName("addAfter")
fun <V : View> ViewGroup.add(
    view: V,
    lp: ViewGroup.LayoutParams,
    afterChild: View
): V {
    val index = indexOfChild(afterChild).also {
        check(it != -1) { "Value passed in beforeChild is not a child of the ViewGroup!" }
    } + 1
    return view.also { addView(it, index, lp) }
}

inline val ViewGroup.matchLayoutPrams: ViewGroup.LayoutParams
    get() = ViewGroup.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT,)

inline fun Context.verticalLayout(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: LinearLayout.() -> Unit = {}
): LinearLayout = view(::LinearLayout, id, theme) {
    orientation = LinearLayout.VERTICAL
    initView()
}

inline fun View.verticalLayout(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: LinearLayout.() -> Unit = {}
): LinearLayout = context.verticalLayout(id, theme, initView)


inline fun Context.horizontalLayout(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: LinearLayout.() -> Unit = {}
): LinearLayout = view(::LinearLayout, id, theme, initView)

inline fun View.horizontalLayout(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: LinearLayout.() -> Unit = {}
): LinearLayout = context.horizontalLayout(id, theme, initView)


inline fun Context.frameLayout(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: FrameLayout.() -> Unit = {}
): FrameLayout = view(::FrameLayout, id, theme, initView)

inline fun View.frameLayout(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: FrameLayout.() -> Unit = {}
): FrameLayout = context.frameLayout(id, theme, initView)


inline fun Context.viewPager(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: ViewPager.() -> Unit = {}
): ViewPager = view(::ViewPager, id, theme, initView)

inline fun View.viewPager(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: ViewPager.() -> Unit = {}
): ViewPager = context.viewPager(id, theme, initView)

inline fun Context.scrollView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: ScrollView.() -> Unit = {}
): ScrollView = view(::ScrollView, id, theme, initView)

inline fun View.scrollView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: ScrollView.() -> Unit = {}
): ScrollView = context.scrollView(id, theme, initView)


