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
import android.view.TextureView
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.StyleRes
import org.telegram.ktx.view.dsl.NO_THEME
import org.telegram.ktx.view.dsl.view
import org.telegram.ui.Components.RLottieImageView
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

inline fun Context.textView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: TextView.() -> Unit = {}
): TextView = view(id, theme, initView)

inline fun View.textView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: TextView.() -> Unit = {}
): TextView = context.textView(id, theme, initView)


inline fun Context.button(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: Button.() -> Unit = {}
): Button = view(id, theme, initView)

inline fun View.button(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: Button.() -> Unit = {}
): Button = context.button(id, theme, initView)

inline fun Context.imageView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: ImageView.() -> Unit = {}
): ImageView = view(id, theme, initView)

inline fun View.imageView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: ImageView.() -> Unit = {}
): ImageView = context.imageView(id, theme, initView)


inline fun Context.editText(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: EditText.() -> Unit = {}
): EditText = view(id, theme, initView)

inline fun View.editText(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: EditText.() -> Unit = {}
): EditText = context.editText(id, theme, initView)

inline fun Context.textureView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: TextureView.() -> Unit = {}
): TextureView = view(id, theme, initView)

inline fun View.textureView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: TextureView.() -> Unit = {}
): TextureView = context.textureView(id, theme, initView)

inline fun Context.rLottieImageView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: RLottieImageView.() -> Unit = {}
): RLottieImageView = view(id, theme, initView)

inline fun View.rLottieImageView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: RLottieImageView.() -> Unit = {}
): RLottieImageView = context.rLottieImageView(id, theme, initView)
