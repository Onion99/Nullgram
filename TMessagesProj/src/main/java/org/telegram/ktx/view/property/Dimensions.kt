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

import android.content.Context
import android.view.View
import org.telegram.ui.view.helper.UIHelper

@Suppress("unused")
inline fun Context.dip(value: Int): Int = UIHelper.dpI(value)



/** Converts Device Independent Pixels to pixels. Returns an `Int` or a `Float` based on [value]'s type. */
@Suppress("unused")
inline fun Context.dip(value: Float): Float = UIHelper.dpF(value)


/** Converts Device Independent Pixels to pixels. Returns an `Int` or a `Float` based on [value]'s type. */
inline fun View.dip(value: Int): Int = context.dip(value)

/** Converts Device Independent Pixels to pixels. Returns an `Int` or a `Float` based on [value]'s type. */
inline fun View.dip(value: Float): Float = context.dip(value)


/** Converts Device Independent Pixels to pixels. Returns an `Int` or a `Float` based on [value]'s type. */
inline fun Context.dp(value: Int): Int = dip(value)

/** Converts Device Independent Pixels to pixels. Returns an `Int` or a `Float` based on [value]'s type. */
inline fun Context.dp(value: Float): Float = dip(value)

/** Converts Device Independent Pixels to pixels. Returns an `Int` or a `Float` based on [value]'s type. */
inline fun View.dp(value: Int): Int = context.dip(value)

/** Converts Device Independent Pixels to pixels. Returns an `Int` or a `Float` based on [value]'s type. */
inline fun View.dp(value: Float): Float = context.dip(value)
