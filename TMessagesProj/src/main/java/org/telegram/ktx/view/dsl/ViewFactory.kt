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
import android.util.AttributeSet
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.CheckBox
import android.widget.CheckedTextView
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.MultiAutoCompleteTextView
import android.widget.RadioButton
import android.widget.RatingBar
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.AttrRes
import org.telegram.collections.forEachReversedByIndex
import org.telegram.exceptions.illegalArg
import java.lang.reflect.Constructor

interface ViewFactory {
    companion object {
        val appInstance: ViewFactory = ViewFactoryImpl.appInstance
    }

    operator fun <V : View> invoke(clazz: Class<out V>, context: Context): V
    fun <V : View> getThemeAttributeStyledView(
        clazz: Class<out V>,
        context: Context,
        @Suppress("UNUSED_PARAMETER") attrs: AttributeSet?,
        @AttrRes styleThemeAttribute: Int
    ): V
}

inline fun <reified V : View> ViewFactory.getThemeAttrStyledView(
    context: Context,
    attrs: AttributeSet?,
    @AttrRes styleThemeAttribute: Int
): V = getThemeAttributeStyledView(V::class.java, context, attrs, styleThemeAttribute)


typealias ViewInstantiator = (Class<out View>, Context) -> View?
typealias ThemeAttrStyledViewInstantiator = (Class<out View>, Context, Int) -> View?

class ViewFactoryImpl : ViewFactory {
    companion object {
        val appInstance = ViewFactoryImpl()
    }

    fun add(factory: ViewInstantiator) {
        viewInstantiators.add(factory)
    }

    fun addForThemeAttrStyled(factory: ThemeAttrStyledViewInstantiator) {
        themeAttrStyledViewInstantiators.add(factory)
    }

    override operator fun <V : View> invoke(clazz: Class<out V>, context: Context): V {
        viewInstantiators.forEachReversedByIndex { factory ->
            @Suppress("UNCHECKED_CAST")
            factory(clazz, context)?.let { view ->
                check(clazz.isInstance(view)) {
                    "Expected type $clazz but got ${view.javaClass}! Faulty factory: $factory"
                }
                return view as V
            }
        }
        illegalArg("No factory found for this type: $clazz")
    }

    override fun <V : View> getThemeAttributeStyledView(
        clazz: Class<out V>,
        context: Context,
        @Suppress("UNUSED_PARAMETER") attrs: AttributeSet?,
        @AttrRes styleThemeAttribute: Int
    ): V {
        themeAttrStyledViewInstantiators.forEachReversedByIndex { factory ->
            factory(clazz, context, styleThemeAttribute)?.let { view ->
                check(clazz.isInstance(view)) {
                    "Expected type $clazz but got ${view.javaClass}! Faulty factory: $factory"
                }
                @Suppress("UNCHECKED_CAST")
                return view as V
            }
        }
        illegalArg("No factory found for this type: $clazz")
    }

    private val viewInstantiators: MutableList<ViewInstantiator> = mutableListOf(::instantiateView)
    private val themeAttrStyledViewInstantiators: MutableList<ThemeAttrStyledViewInstantiator> =
        mutableListOf(::instantiateThemeAttrStyledView)
}

private inline fun <reified V : View> instantiateView(
    clazz: Class<out V>,
    context: Context
): V? = when (clazz) {
    TextView::class.java -> TextView(context)
    Button::class.java -> Button(context)
    ImageView::class.java -> ImageView(context)
    EditText::class.java -> EditText(context)
    Spinner::class.java -> Spinner(context)
    ImageButton::class.java -> ImageButton(context)
    CheckBox::class.java -> CheckBox(context)
    RadioButton::class.java -> RadioButton(context)
    CheckedTextView::class.java -> CheckedTextView(context)
    AutoCompleteTextView::class.java -> AutoCompleteTextView(context)
    MultiAutoCompleteTextView::class.java -> MultiAutoCompleteTextView(context)
    RatingBar::class.java -> RatingBar(context)
    SeekBar::class.java -> SeekBar(context)
    else -> clazz.viewConstructor().newInstance(context)
} as V?

private inline fun <reified V : View> instantiateThemeAttrStyledView(
    clazz: Class<out V>,
    context: Context,
    @AttrRes styleThemeAttribute: Int
): V? = when (clazz) {
    TextView::class.java -> TextView(context, null, styleThemeAttribute)
    Button::class.java -> Button(context, null, styleThemeAttribute)
    ImageView::class.java -> ImageView(context, null, styleThemeAttribute)
    EditText::class.java -> EditText(context, null, styleThemeAttribute)
    Spinner::class.java -> Spinner(context, null, styleThemeAttribute)
    ImageButton::class.java -> ImageButton(context, null, styleThemeAttribute)
    CheckBox::class.java -> CheckBox(context, null, styleThemeAttribute)
    RadioButton::class.java -> RadioButton(context, null, styleThemeAttribute)
    CheckedTextView::class.java -> CheckedTextView(context, null, styleThemeAttribute)
    AutoCompleteTextView::class.java -> AutoCompleteTextView(context, null, styleThemeAttribute)
    MultiAutoCompleteTextView::class.java -> {
        MultiAutoCompleteTextView(context, null, styleThemeAttribute)
    }
    RatingBar::class.java -> RatingBar(context, null, styleThemeAttribute)
    SeekBar::class.java -> SeekBar(context, null, styleThemeAttribute)
    else -> clazz.themeAttrStyledViewConstructor().newInstance(context, null, styleThemeAttribute)
} as V?

@Suppress("UNCHECKED_CAST")
private fun <V : View> Class<V>.viewConstructor(): Constructor<V> {
    return cachedViewConstructors[this] as Constructor<V>?
        ?: getConstructor(Context::class.java).also { cachedViewConstructors[this] = it }
}

@Suppress("UNCHECKED_CAST")
private fun <V : View> Class<out V>.themeAttrStyledViewConstructor(): Constructor<out V> {
    return cachedThemeAttrStyledViewConstructors[this] as Constructor<V>?
        ?: getConstructor(Context::class.java, AttributeSet::class.java, Int::class.java).also {
            cachedThemeAttrStyledViewConstructors[this] = it
        }
}

private val cachedViewConstructors by lazy(LazyThreadSafetyMode.PUBLICATION) {
    mutableMapOf<Class<out View>, Constructor<out View>>()
}
private val cachedThemeAttrStyledViewConstructors by lazy(LazyThreadSafetyMode.PUBLICATION) {
    mutableMapOf<Class<out View>, Constructor<out View>>()
}

fun Context.hasThemeAttributes(themeAttributes: IntArray): Boolean {
    val a = obtainStyledAttributes(themeAttributes)
    for (i in themeAttributes.indices) {
        if (a.hasValue(i).not()) {
            a.recycle()
            return false
        }
    }
    a.recycle()
    return true
}
