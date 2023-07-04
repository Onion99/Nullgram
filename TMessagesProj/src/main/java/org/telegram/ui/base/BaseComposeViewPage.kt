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

package org.telegram.ui.base

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Text
import androidx.compose.ui.platform.ComposeView
import androidx.core.view.ViewCompat
import androidx.core.view.doOnAttach
import androidx.core.view.doOnLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.lifecycle.ViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.findViewTreeSavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import org.telegram.messenger.ApplicationLoader
import org.telegram.ui.ActionBar.BaseFragment

abstract class BaseComposeViewPage : BaseFragment(), SavedStateRegistryOwner {

    private val savedStateRegistryController = SavedStateRegistryController.create(this)
    private val lifecycleRegistry = LifecycleRegistry(this)


    override fun createView(context: Context): View = ComposeView(context).apply {
        parentActivity.window.decorView.findViewById<ViewGroup>(android.R.id.content)?.let { decorView ->
            if(ViewTreeLifecycleOwner.get(decorView) == null){
                ViewTreeLifecycleOwner.set(decorView,context as AppCompatActivity)
            }
            if(ViewTreeViewModelStoreOwner.get(decorView) == null){
                ViewTreeViewModelStoreOwner.set(decorView, context as AppCompatActivity)
            }
            if(decorView.findViewTreeSavedStateRegistryOwner() == null){
                decorView.setViewTreeSavedStateRegistryOwner(context as AppCompatActivity)
            }
        }
        actionBar.setAddToContainer(false)
        initComposeView(this)
    }

    abstract fun initComposeView(composeView:ComposeView)


    override fun getLifecycle(): Lifecycle = lifecycleRegistry

    override val savedStateRegistry: SavedStateRegistry = savedStateRegistryController.savedStateRegistry
}
