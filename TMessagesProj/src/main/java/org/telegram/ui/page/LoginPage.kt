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

package org.telegram.ui.page

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.view.animation.AnimationUtils
import androidx.annotation.IntDef
import org.telegram.ktx.view.dsl.link.add
import org.telegram.ktx.view.dsl.link.horizontalLayout
import org.telegram.ktx.view.dsl.link.imageView
import org.telegram.ktx.view.dsl.link.outlineTextContainerView
import org.telegram.ktx.view.dsl.link.textView
import org.telegram.ktx.view.dsl.link.textViewSwitcher
import org.telegram.ktx.view.dsl.link.verticalLayout
import org.telegram.ktx.view.dsl.params.endMargin
import org.telegram.ktx.view.dsl.params.horizontalMargin
import org.telegram.ktx.view.dsl.params.horizontalPadding
import org.telegram.ktx.view.dsl.params.lParams
import org.telegram.ktx.view.dsl.params.matchParent
import org.telegram.ktx.view.dsl.params.verticalPadding
import org.telegram.ktx.view.dsl.params.wrapContent
import org.telegram.ktx.view.property.gravityCenter
import org.telegram.ktx.view.property.gravityCenterHorizontal
import org.telegram.ktx.view.property.gravityCenterVertical
import org.telegram.ktx.view.property.gravityStartCenter
import org.telegram.messenger.LocaleController
import org.telegram.messenger.R
import org.telegram.ui.ActionBar.Theme
import org.telegram.ui.base.BaseDslViewPage
import org.telegram.ui.view.helper.dip
import org.telegram.ui.view.property.FONT_MEDIUM
import org.telegram.ui.view.property.appFont
import org.telegram.ui.view.property.easeInOutQuad

class LoginPage: BaseDslViewPage(){

    @PageMode
    private var activityMode = MODE_LOGIN
    override fun initView(context: Context): View = context.verticalLayout {
        // content - title
        add(textView {
            text = LocaleController.getString(if (activityMode == MODE_CHANGE_PHONE_NUMBER) R.string.ChangePhoneNewNumber else R.string.YourNumber)
            gravity  = gravityCenter
            textSize = 18f
            typeface = appFont(FONT_MEDIUM)
            setLineSpacing(dip(2f),1f)
        },lParams(matchParent,wrapContent,gravityCenterHorizontal){
            horizontalMargin = dip(32)
        })
        // content -tip
        add(textView {
            text = LocaleController.getString(if (activityMode == MODE_CHANGE_PHONE_NUMBER) R.string.ChangePhoneHelp else R.string.StartText)
            gravity  = gravityCenter
            textSize = 14f
            typeface = appFont(FONT_MEDIUM)
            setLineSpacing(dip(2f),1f)
        },lParams(matchParent,wrapContent,gravityCenterHorizontal){
            horizontalMargin = dip(32)
        })

        add(outlineTextContainerView {
            add(horizontalLayout {
                gravity = gravityCenterVertical
                // country text change view
                add(textViewSwitcher {
                    setFactory {
                        textView {
                            horizontalPadding = dip(16)
                            verticalPadding = dip(12)
                            textSize = 16f
                            setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText))
                            setHintTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteHintText))
                            maxLines = 1
                            isSingleLine = true
                            ellipsize = TextUtils.TruncateAt.END
                            gravityStartCenter
                        }
                    }
                    inAnimation = AnimationUtils.loadAnimation(context, R.anim.text_in).apply {
                        interpolator = easeInOutQuad
                    }
                },lParams(0,wrapContent, weight = 1f))
                // country right enter image
                add(imageView { setImageResource(R.drawable.msg_inputarrow) },lParams(dip(24),dip(24),gravityCenterVertical){
                    endMargin = dip(14)
                })
            },lParams(matchParent,matchParent))
        },lParams {  })
    }
}

private const val MODE_LOGIN = 0
private const val MODE_CANCEL_ACCOUNT_DELETION = 1
private const val MODE_CHANGE_PHONE_NUMBER = 2
private const val MODE_CHANGE_LOGIN_EMAIL = 3
@Retention(AnnotationRetention.SOURCE)
@IntDef(value = [MODE_LOGIN, MODE_CANCEL_ACCOUNT_DELETION, MODE_CHANGE_PHONE_NUMBER, MODE_CHANGE_LOGIN_EMAIL])
annotation class PageMode
