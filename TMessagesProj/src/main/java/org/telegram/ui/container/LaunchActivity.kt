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

package org.telegram.ui.container

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.Activity
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import org.telegram.messenger.AndroidUtilities
import org.telegram.messenger.NotificationCenter
import org.telegram.messenger.R
import org.telegram.ui.ActionBar.BaseFragment
import org.telegram.ui.ActionBar.DrawerLayoutContainer
import org.telegram.ui.ActionBar.INavigationLayout
import org.telegram.ui.ActionBar.Theme
import org.telegram.ui.Cells.DrawerProfileCell
import org.telegram.ui.Components.CubicBezierInterpolator
import org.telegram.ui.Components.LayoutHelper
import org.telegram.ui.Components.RLottieDrawable
import org.telegram.ui.page.IntroPage
import org.telegram.ui.view.helper.UIHelper

class LaunchActivity : AppCompatActivity(){

    private val mainFragmentsStack = ArrayList<BaseFragment>()

    private val evenObserver by lazy {
        NotificationCenter.NotificationCenterDelegate { id, account, args ->
            if (id == NotificationCenter.needSetDayNightTheme){
                actionBarLayout.animateThemedValues(args[0] as Theme.ThemeInfo, args[3] as Int , args[1] as Boolean, true, null);
                DrawerProfileCell.switchingTheme = false
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        preCreate()
        super.onCreate(savedInstanceState)
        initUiConfig()
        createView()
        initPrimaryPage()
    }

    private fun preCreate(){
        // splash anim
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            splashScreen.setOnExitAnimationListener {
                val animator = ObjectAnimator.ofFloat(it, View.ALPHA,1f,0f)
                animator.interpolator = CubicBezierInterpolator.DEFAULT
                animator.duration = 150L
                animator.addListener(object :AnimatorListenerAdapter(){
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        it.remove()
                    }
                })
                animator.start()
            }
        }
        // activity title
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        // theme
        setTheme(R.style.Theme_TMessages_Compose)
        // navigationBarColor
        runCatching {
            window.navigationBarColor = 0xff000000.toInt()
        }
        // Background
        window.setBackgroundDrawableResource(R.drawable.transparent)
    }

    private fun initUiConfig(){
        UIHelper.checkSystemDisplaySize(this,resources.configuration)
        AndroidUtilities.isInMultiwindow = isInMultiWindowMode
        AndroidUtilities.fillStatusBarHeight(this)
    }

    private lateinit var rippleAbove:View
    private lateinit var themeSwitchImageView:ImageView
    private var themeSwitchSunDrawable: RLottieDrawable ?= null
    private lateinit var themeSwitchSunView:View
    private lateinit var drawerLayoutContainer: DrawerLayoutContainer
    private lateinit var actionBarLayout: INavigationLayout
    private fun createView(){
        val frameLayout =  object :FrameLayout(this){
            override fun dispatchDraw(canvas: Canvas?) {
                super.dispatchDraw(canvas)

            }
        }
        setContentView(frameLayout,ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        /*themeSwitchImageView = ImageView(this)
        themeSwitchImageView.isVisible = false
        themeSwitchSunView = object :View(this){
            override fun onDraw(canvas: Canvas?) {
                super.onDraw(canvas)
                canvas?.run {
                    themeSwitchSunDrawable?.draw(this)
                    invalidate()
                }
            }
        }
        themeSwitchImageView.isVisible = false*/
//        frameLayout.addView(themeSwitchSunView, LayoutHelper.createFrame(48, 48F))
        actionBarLayout = INavigationLayout.newLayout(this)
/*        actionBarLayout.setDelegate(object :INavigationLayout.INavigationLayoutDelegate{
            override fun needAddFragmentToStack(fragment: BaseFragment?, layout: INavigationLayout?): Boolean {
                return true
            }
        })*/
        actionBarLayout.fragmentStack = mainFragmentsStack
        drawerLayoutContainer = object : DrawerLayoutContainer(this){}
        drawerLayoutContainer.parentActionBarLayout = actionBarLayout
        frameLayout.addView(actionBarLayout.view, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT.toFloat()))
        frameLayout.addView(drawerLayoutContainer, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT.toFloat()))
    }


    private fun initPrimaryPage(){
        actionBarLayout.addFragmentToStack(IntroPage())
        NotificationCenter.getGlobalInstance().addObserver(evenObserver,NotificationCenter.needSetDayNightTheme)
    }


    override fun onDestroy() {
        NotificationCenter.getGlobalInstance().removeObserver(evenObserver,NotificationCenter.needSetDayNightTheme)
        super.onDestroy()
    }
}
