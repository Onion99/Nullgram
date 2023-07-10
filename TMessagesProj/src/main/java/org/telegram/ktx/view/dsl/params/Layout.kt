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

package org.telegram.ktx.view.dsl.params

import android.annotation.SuppressLint
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.Px
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

inline val View.matchParent
    get() = ViewGroup.LayoutParams.MATCH_PARENT

@Suppress("unused")
inline val View.wrapContent
    get() = ViewGroup.LayoutParams.WRAP_CONTENT

inline fun LinearLayout.lParams(
    width: Int = wrapContent,
    height: Int = wrapContent,
    initParams: LinearLayout.LayoutParams.() -> Unit = {}
): LinearLayout.LayoutParams {
    return LinearLayout.LayoutParams(width, height).apply(initParams)
}

inline fun LinearLayout.lParams(
    width: Int = wrapContent,
    height: Int = wrapContent,
    gravity: Int = -1,
    weight: Float = 0f,
    initParams: LinearLayout.LayoutParams.() -> Unit = {}
): LinearLayout.LayoutParams {
    return LinearLayout.LayoutParams(width, height).also {
        it.gravity = gravity
        it.weight = weight
    }.apply(initParams)
}

inline fun FrameLayout.lParams(
    width: Int = wrapContent,
    height: Int = wrapContent,
    @SuppressLint("InlinedApi")
    gravity: Int = FrameLayout.LayoutParams.UNSPECIFIED_GRAVITY,
    initParams: FrameLayout.LayoutParams.() -> Unit = {}
): FrameLayout.LayoutParams {
    return FrameLayout.LayoutParams(width, height).also {
        it.gravity = gravity
    }.apply(initParams)
}

@PublishedApi
internal const val NO_GETTER = "Property does not have a getter"
@PublishedApi
internal inline val noGetter: Nothing
    get() = throw UnsupportedOperationException(NO_GETTER)


inline var ViewGroup.MarginLayoutParams.horizontalMargin: Int
    @Deprecated(NO_GETTER, level = DeprecationLevel.HIDDEN) get() = noGetter
    set(@Px value) {
        leftMargin = value
        rightMargin = value
    }

inline var ViewGroup.MarginLayoutParams.verticalMargin: Int
    @Deprecated(NO_GETTER, level = DeprecationLevel.HIDDEN) get() = noGetter
    set(@Px value) {
        topMargin = value
        bottomMargin = value
    }

inline var ViewGroup.MarginLayoutParams.margin: Int
    @Deprecated(NO_GETTER, level = DeprecationLevel.HIDDEN) get() = noGetter
    set(@Px value) {
        leftMargin = value
        topMargin = value
        rightMargin = value
        bottomMargin = value
    }

inline var ViewGroup.MarginLayoutParams.startMargin: Int
    @SuppressLint("ObsoleteSdkInt")
    get() = if (Build.VERSION.SDK_INT >= 17) marginStart else leftMargin
    @SuppressLint("ObsoleteSdkInt")
    set(@Px value) {
        if (Build.VERSION.SDK_INT >= 17) marginStart = value
        else leftMargin = value
    }

inline var ViewGroup.MarginLayoutParams.endMargin: Int
    @SuppressLint("ObsoleteSdkInt")
    get() = if (Build.VERSION.SDK_INT >= 17) marginEnd else rightMargin
    @SuppressLint("ObsoleteSdkInt")
    set(@Px value) {
        if (Build.VERSION.SDK_INT >= 17) marginEnd = value
        else rightMargin = value
    }

