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

package org.telegram.ui.view.cutomview.text

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ViewSwitcher

class TextViewSwitcher @JvmOverloads constructor (context: Context,attrs: AttributeSet? = null) : ViewSwitcher(context,attrs) {
    fun setText(text: CharSequence) {
        setText(text, true)
    }

    fun setText(text: CharSequence, animated: Boolean) {
        if (!TextUtils.equals(text, currentView.text)) {
            if (animated) {
                nextView.text = text
                showNext()
            } else {
                currentView.text = text
            }
        }
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        require(child is TextView)
        super.addView(child, index, params)
    }

    override fun getCurrentView(): TextView {
        return super.getCurrentView() as TextView
    }

    override fun getNextView(): TextView {
        return super.getNextView() as TextView
    }

    fun invalidateViews() {
        currentView.invalidate()
        nextView.invalidate()
    }
}
