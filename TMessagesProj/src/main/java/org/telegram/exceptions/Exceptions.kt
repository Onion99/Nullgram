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

package org.telegram.exceptions

import android.content.Intent

/** Throws an [IllegalStateException] with a message that includes [value]. */
fun unexpectedValue(value: Any?): Nothing = throw IllegalStateException("Unexpected value: $value")

/** Throws an [IllegalArgumentException] with the passed message. */
fun illegalArg(errorMessage: String? = null): Nothing = throw IllegalArgumentException(errorMessage)

/** Throws an [IllegalArgumentException] with the passed [argument]. */
fun illegalArg(
    argument: Any?
): Nothing = throw IllegalArgumentException("Illegal argument: $argument")

/** Throws an [UnsupportedOperationException] with the passed message. */
fun unsupported(
    errorMessage: String? = null
): Nothing = throw UnsupportedOperationException(errorMessage)

/** Throws an [UnsupportedOperationException] with the unsupported action name in the message. */
fun unsupportedAction(intent: Intent): Nothing = unsupported("Unsupported action: ${intent.action}")
