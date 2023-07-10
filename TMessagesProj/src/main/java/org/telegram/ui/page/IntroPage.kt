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

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.graphics.SurfaceTexture
import android.graphics.drawable.BitmapDrawable
import android.opengl.GLES20
import android.opengl.GLUtils
import android.os.Looper
import android.os.Parcelable
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import org.telegram.ktx.view.dsl.link.add
import org.telegram.ktx.view.dsl.link.frameLayout
import org.telegram.ktx.view.dsl.link.textView
import org.telegram.ktx.view.dsl.params.horizontalMargin
import org.telegram.ktx.view.dsl.params.lParams
import org.telegram.ktx.view.dsl.params.wrapContent
import org.telegram.ktx.view.dsl.textureView
import org.telegram.messenger.AndroidUtilities
import org.telegram.messenger.ApplicationLoader
import org.telegram.messenger.DispatchQueue
import org.telegram.messenger.EmuDetector
import org.telegram.messenger.GenericProvider
import org.telegram.messenger.Intro
import org.telegram.messenger.LocaleController
import org.telegram.messenger.R
import org.telegram.ui.ActionBar.Theme
import org.telegram.ui.base.BaseComposeViewPage
import org.telegram.ktx.view.dsl.viewPager
import org.telegram.ktx.view.property.dip
import org.telegram.ktx.view.property.gravityCenter
import org.telegram.ktx.view.property.gravityCenterHorizontal
import javax.microedition.khronos.egl.EGL10
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.egl.EGLContext
import javax.microedition.khronos.egl.EGLDisplay
import javax.microedition.khronos.egl.EGLSurface
import javax.microedition.khronos.opengles.GL10

const val INTRO_PAGE_ICON_WIDTH   = 200
const val INTRO_PAGE_ICON_HEIGHT  = 150
const val EGL_CONTEXT_CLIENT_VERSION = 0x3098
const val EGL_OPENGL_ES2_BIT = 4

@Suppress("DEPRECATION")
class IntroPage : BaseComposeViewPage() {

    private var titles = emptyArray<String>()
    private var messages = emptyArray<String>()

    override fun onFragmentCreate(): Boolean {
        titles = arrayOf(
            LocaleController.getString("Page1Title", R.string.Page1Title),
            LocaleController.getString("Page2Title", R.string.Page2Title),
            LocaleController.getString("Page3Title", R.string.Page3Title),
            LocaleController.getString("Page5Title", R.string.Page5Title),
            LocaleController.getString("Page4Title", R.string.Page4Title),
            LocaleController.getString("Page6Title", R.string.Page6Title)
        )
        messages = arrayOf(
            LocaleController.getString("Page1Message", R.string.Page1Message),
            LocaleController.getString("Page2Message", R.string.Page2Message),
            LocaleController.getString("Page3Message", R.string.Page3Message),
            LocaleController.getString("Page5Message", R.string.Page5Message),
            LocaleController.getString("Page4Message", R.string.Page4Message),
            LocaleController.getString("Page6Message", R.string.Page6Message)
        )
        return true
    }

    override fun initComposeView(composeView: ComposeView) {
        composeView.setContent {
            IntroView()
        }
    }

    private var currentViewPagerPage = 0
    private var currentDate: Long = 0
    private var eglThread: EGLThread? = null
    private lateinit var viewPager: ViewPager
    private lateinit var bottomPages: IntroViewPagerIndicator
    @Preview
    @Composable
    private fun IntroView() {
        Box(Modifier.fillMaxSize()) {
            AndroidView(
                factory = {
                    it.viewPager {
                        viewPager = this
                        adapter = IntroAdapter()
                        pageMargin = 0
                        offscreenPageLimit = 1
                        addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                                bottomPages.setPageOffset(position, positionOffset)
                                if(measuredWidth == 0) return
                                val offset = (position * measuredWidth + positionOffsetPixels - currentViewPagerPage * measuredWidth) / measuredWidth
                                Intro.setScrollOffset(offset.toFloat())
                            }

                            override fun onPageSelected(position: Int) {
                                currentViewPagerPage = position
                            }
                            override fun onPageScrollStateChanged(state: Int) {}
                        })
                    }
                })
            AndroidView(
                modifier = Modifier
                    .padding(top = 40.dp)
                    .align(Alignment.Center)
                    .width((INTRO_VIEW_PAGER_INDICATOR_VIEW_MARGIN_WIDTH * titles.size - INTRO_VIEW_PAGER_INDICATOR_VIEW_WIDTH).dp)
                    .height(INTRO_VIEW_PAGER_INDICATOR_VIEW_WIDTH.dp),
                factory = {
                    IntroViewPagerIndicator(it, viewPager, titles.size).apply {
                        bottomPages = this
                    }
                })
            AndroidView(
                modifier = Modifier.padding(top = 149.dp).size(INTRO_PAGE_ICON_WIDTH.dp, INTRO_PAGE_ICON_HEIGHT.dp).align(Alignment.TopCenter),
                factory = {
                    it.textureView{
                        surfaceTextureListener = object : TextureView.SurfaceTextureListener{
                            override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
                                if(eglThread == null){
                                    eglThread = EGLThread(surface)
                                    with(eglThread!!){
                                        setSurfaceTextureSize(width, height)
                                        postRunnable {
                                            val time = (System.currentTimeMillis() - currentDate) / 1000.0f
                                            Intro.setPage(currentViewPagerPage)
                                            Intro.setDate(time)
                                            Intro.onDrawFrame(0)
                                            if(isAlive && eglDisplay != null && eglSurface != null){
                                                egl10.eglSwapBuffers(eglDisplay,eglSurface)
                                            }
                                        }
                                        postRunnable(drawRunnable)
                                    }
                                }
                            }

                            override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
                                eglThread?.setSurfaceTextureSize(width, height)
                            }

                            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                                eglThread?.shutdown()
                                eglThread = null
                                return true
                            }

                            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {}
                        }
                    }
                }
            )
        }
    }

    override fun isLightStatusBar(): Boolean {
        val color = Theme.getColor(Theme.key_windowBackgroundWhite, null, true)
        return ColorUtils.calculateLuminance(color) > 0.7f
    }

    inner class EGLThread(private val surfaceTexture: SurfaceTexture) : DispatchQueue("EGLThread") {
        lateinit var egl10: EGL10
        var eglDisplay: EGLDisplay? = null
        private var eglConfig: EGLConfig? = null
        private var eglContext: EGLContext? = null
        var eglSurface: EGLSurface? = null
        private var hasInit = false
        private val textures = IntArray(24)
        private var maxRefreshRate = 0f
        private var lastDrawFrame: Long = 0
        private val telegramMaskProvider = GenericProvider<Void, Bitmap> { _ ->
            val size = context.dip(INTRO_PAGE_ICON_HEIGHT)
            val bitmap = Bitmap.createBitmap( context.dip(INTRO_PAGE_ICON_WIDTH), size, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            canvas.drawColor(Theme.getColor(Theme.key_windowBackgroundWhite))
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.CLEAR))
            canvas.drawCircle(bitmap.width / 2f, bitmap.height / 2f, size / 2f, paint)
            return@GenericProvider bitmap
        }

        private fun initGL(): Boolean {
            egl10 = EGLContext.getEGL() as EGL10
            eglDisplay = egl10.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY)
            if (eglDisplay === EGL10.EGL_NO_DISPLAY) {
                finish()
                return false
            }
            val version = IntArray(2)
            if (!egl10.eglInitialize(eglDisplay, version)) {
                finish()
                return false
            }
            val configsCount = IntArray(1)
            val configs = arrayOfNulls<EGLConfig>(1)
            val configSpec: IntArray = if (EmuDetector.with(parentActivity).detect()) {
                intArrayOf(
                    EGL10.EGL_RED_SIZE, 8,
                    EGL10.EGL_GREEN_SIZE, 8,
                    EGL10.EGL_BLUE_SIZE, 8,
                    EGL10.EGL_ALPHA_SIZE, 8,
                    EGL10.EGL_DEPTH_SIZE, 24,
                    EGL10.EGL_NONE
                )
            } else {
                intArrayOf(
                    EGL10.EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT,
                    EGL10.EGL_RED_SIZE, 8,
                    EGL10.EGL_GREEN_SIZE, 8,
                    EGL10.EGL_BLUE_SIZE, 8,
                    EGL10.EGL_ALPHA_SIZE, 8,
                    EGL10.EGL_DEPTH_SIZE, 24,
                    EGL10.EGL_STENCIL_SIZE, 0,
                    EGL10.EGL_SAMPLE_BUFFERS, 1,
                    EGL10.EGL_SAMPLES, 2,
                    EGL10.EGL_NONE
                )
            }
            eglConfig = if (!egl10.eglChooseConfig(eglDisplay, configSpec, configs, 1, configsCount)) {
                finish()
                return false
            } else if (configsCount[0] > 0) {
                configs[0]
            } else {
                finish()
                return false
            }
            val attribList = intArrayOf(EGL_CONTEXT_CLIENT_VERSION, 2, EGL10.EGL_NONE)
            eglContext = egl10.eglCreateContext(eglDisplay, eglConfig, EGL10.EGL_NO_CONTEXT, attribList)
            if (eglContext == null) {
                finish()
                return false
            }
            eglSurface = egl10.eglCreateWindowSurface(eglDisplay, eglConfig, surfaceTexture, null)
            if (eglSurface == null || eglSurface === EGL10.EGL_NO_SURFACE) {
                finish()
                return false
            }
            if (!egl10.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)) {
                finish()
                return false
            }
            GLES20.glGenTextures(23, textures, 0)
            loadTexture(R.drawable.intro_fast_arrow_shadow, 0)
            loadTexture(R.drawable.intro_fast_arrow, 1)
            loadTexture(R.drawable.intro_fast_body, 2)
            loadTexture(R.drawable.intro_fast_spiral, 3)
            loadTexture(R.drawable.intro_ic_bubble_dot, 4)
            loadTexture(R.drawable.intro_ic_bubble, 5)
            loadTexture(R.drawable.intro_ic_cam_lens, 6)
            loadTexture(R.drawable.intro_ic_cam, 7)
            loadTexture(R.drawable.intro_ic_pencil, 8)
            loadTexture(R.drawable.intro_ic_pin, 9)
            loadTexture(R.drawable.intro_ic_smile_eye, 10)
            loadTexture(R.drawable.intro_ic_smile, 11)
            loadTexture(R.drawable.intro_ic_videocam, 12)
            loadTexture(R.drawable.intro_knot_down, 13)
            loadTexture(R.drawable.intro_knot_up, 14)
            loadTexture(R.drawable.intro_powerful_infinity_white, 15)
            loadTexture(R.drawable.intro_powerful_infinity, 16)
            loadTexture(R.drawable.intro_powerful_mask, 17, Theme.getColor(Theme.key_windowBackgroundWhite), false)
            loadTexture(R.drawable.intro_powerful_star, 18)
            loadTexture(R.drawable.intro_private_door, 19)
            loadTexture(R.drawable.intro_private_screw, 20)
            loadTexture(R.drawable.intro_tg_plane, 21)
            loadProviderTexture({
                val paint = Paint(Paint.ANTI_ALIAS_FLAG)
                paint.color = -0xd35a20 // It's logo color, it should not be colored by the theme
                val size = AndroidUtilities.dp(INTRO_PAGE_ICON_HEIGHT.toFloat())
                val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint)
                bitmap
            }, 22)
            loadProviderTexture(telegramMaskProvider, 23)
            updateTelegramTextures()
            updatePowerfulTextures()
            Intro.setPrivateTextures(textures[19], textures[20])
            Intro.setFreeTextures(textures[14], textures[13])
            Intro.setFastTextures(textures[2], textures[3], textures[1], textures[0])
            Intro.setIcTextures(textures[4], textures[5], textures[6], textures[7], textures[8], textures[9], textures[10], textures[11], textures[12])
            Intro.onSurfaceCreated()
            currentDate = System.currentTimeMillis() - 1000
            return true
        }

        private fun updateTelegramTextures() {
            Intro.setTelegramTextures(textures[22], textures[21], textures[23])
        }

        private fun updatePowerfulTextures() {
            Intro.setPowerfulTextures(textures[17], textures[18], textures[16], textures[15])
        }

        fun finish() {
            egl10.eglMakeCurrent(eglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT)
            egl10.eglDestroySurface(eglDisplay, eglSurface)
            eglSurface = null
            egl10.eglDestroyContext(eglDisplay, eglContext)
            eglContext = null
            egl10.eglTerminate(eglDisplay)
            eglDisplay = null
        }

        val drawRunnable: Runnable = object : Runnable {
            override fun run() {
                if (!hasInit) {
                    return
                }
                val current = System.currentTimeMillis()
                if (eglContext != egl10.eglGetCurrentContext() || eglSurface != egl10.eglGetCurrentSurface(EGL10.EGL_DRAW)) {
                    if (!egl10.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)) {
                        return
                    }
                }
                val deltaDrawMs = (current - lastDrawFrame).coerceAtMost(16).toInt()
                val time: Float = (current - currentDate) / 1000.0f
                Intro.setPage(currentViewPagerPage)
                Intro.setDate(time)
                Intro.onDrawFrame(deltaDrawMs)
                egl10.eglSwapBuffers(eglDisplay, eglSurface)
                lastDrawFrame = current
                if (maxRefreshRate == 0f) {
                    val windowManager = ApplicationLoader.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                    val display = windowManager.defaultDisplay
                    val rates = display.supportedRefreshRates
                    var maxRate = 0f
                    for (rate in rates) {
                        if (rate > maxRate) {
                            maxRate = rate
                        }
                    }
                    maxRefreshRate = maxRate
                }
                val drawMs = System.currentTimeMillis() - current
                postRunnable(this, ((1000 / maxRefreshRate).toLong() - drawMs).coerceAtLeast(0))
            }
        }

        private fun loadProviderTexture(bitmapProvider: GenericProvider<Void, Bitmap>, index: Int, rebind: Boolean = false) {
            if (rebind) {
                GLES20.glDeleteTextures(1, textures, index)
                GLES20.glGenTextures(1, textures, index)
            }
            val bitmap = bitmapProvider.provide(null)
            GLES20.glBindTexture(GL10.GL_TEXTURE_2D, textures[index])
            GLES20.glTexParameteri(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR)
            GLES20.glTexParameteri(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR)
            GLES20.glTexParameteri(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE)
            GLES20.glTexParameteri(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE)
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0)
            bitmap.recycle()
        }

        private fun loadTexture(resId: Int, index: Int, tintColor: Int = 0, rebind: Boolean = false) {
            val drawable = ContextCompat.getDrawable(context, resId)
            if (drawable is BitmapDrawable) {
                if (rebind) {
                    GLES20.glDeleteTextures(1, textures, index)
                    GLES20.glGenTextures(1, textures, index)
                }
                val bitmap = drawable.bitmap
                GLES20.glBindTexture(GL10.GL_TEXTURE_2D, textures[index])
                GLES20.glTexParameteri(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR)
                GLES20.glTexParameteri(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR)
                GLES20.glTexParameteri(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE)
                GLES20.glTexParameteri(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE)
                if (tintColor != 0) {
                    val tempBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
                    val canvas = Canvas(tempBitmap)
                    val tempPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
                    tempPaint.setColorFilter(PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN))
                    canvas.drawBitmap(bitmap, 0f, 0f, tempPaint)
                    GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, tempBitmap, 0)
                    tempBitmap.recycle()
                } else {
                    GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0)
                }
            }
        }

        fun shutdown() {
            postRunnable {
                finish()
                val looper = Looper.myLooper()
                looper?.quit()
            }
        }

        fun setSurfaceTextureSize(width: Int, height: Int) {
            Intro.onSurfaceChanged(width, height, (width / 150f).coerceAtMost(height / 150f), 0)
        }

        override fun run() {
            hasInit = initGL()
            super.run()
        }
    }


    inner class IntroAdapter : PagerAdapter() {
        override fun getCount(): Int {
            return titles.size
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val contentLayout = container.context.frameLayout {
                val headerTextView  = textView {
                    setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText))
                    textSize = 26f
                    gravity = gravityCenter
                    text = titles[position]
                }
                val messageTextView = textView {
                    setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText3))
                    textSize = 15f
                    gravity = gravityCenter
                    text = AndroidUtilities.replaceTags(messages[position])
                }
                add(headerTextView,lParams(wrapContent,wrapContent,gravityCenterHorizontal){ topMargin = dip(316) ; horizontalMargin = dip(20)})
                add(messageTextView,lParams(wrapContent,wrapContent,gravityCenterHorizontal){ topMargin = dip(366)})
            }
            container.addView(contentLayout, 0)
            return contentLayout
        }

        override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
            container.removeView(any as View)
        }

        override fun isViewFromObject(view: View, any: Any): Boolean = view == any

        override fun restoreState(arg0: Parcelable?, arg1: ClassLoader?) {}
        override fun saveState(): Parcelable? = null
    }
}

const val INTRO_VIEW_PAGER_INDICATOR_VIEW_WIDTH = 5f
const val INTRO_VIEW_PAGER_INDICATOR_VIEW_OVAL_WIDTH = 5f /2
const val INTRO_VIEW_PAGER_INDICATOR_VIEW_MARGIN_WIDTH = 11f
@SuppressLint("ViewConstructor")
class IntroViewPagerIndicator(context: Context, private val viewPager: ViewPager, private val pagesCount: Int) : View(context) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var progress = 0f
    private var scrollPosition = 0
    private var currentPage = 0
    private val rect = RectF()
    private var colorKey = -1
    private var selectedColorKey = -1

    fun setPageOffset(position: Int, offset: Float) {
        progress = offset
        scrollPosition = position
        invalidate()
    }

    fun setColor(key: Int, selectedKey: Int) {
        colorKey = key
        selectedColorKey = selectedKey
    }

    override fun onDraw(canvas: Canvas) {
        //  draw per dot color (no current )
        if (colorKey >= 0) {
            paint.color = Theme.getColor(colorKey) and 0x00ffffff or -0x4c000000
        } else {
            paint.color = if (Theme.getCurrentTheme().isDark) -0xaaaaab else -0x444445
        }
        //  draw per dot (no current )
        var pageIndexViewWidth = 0F
        currentPage = viewPager.currentItem
        for (pagePosition in 0 until pagesCount) {
            if (pagePosition == currentPage) continue
            pageIndexViewWidth = pagePosition * dip(INTRO_VIEW_PAGER_INDICATOR_VIEW_MARGIN_WIDTH)
            rect.set(pageIndexViewWidth,0f,(pageIndexViewWidth + dip(INTRO_VIEW_PAGER_INDICATOR_VIEW_WIDTH)),dip(INTRO_VIEW_PAGER_INDICATOR_VIEW_WIDTH))
            canvas.drawRoundRect(rect, dip(INTRO_VIEW_PAGER_INDICATOR_VIEW_OVAL_WIDTH), dip(INTRO_VIEW_PAGER_INDICATOR_VIEW_OVAL_WIDTH), paint)
        }
        //  draw current dot color
        if (selectedColorKey >= 0) {
            paint.color = Theme.getColor(selectedColorKey)
        } else {
            paint.color = -0xd35a20
        }
        //  draw current dot
        pageIndexViewWidth = currentPage * dip(INTRO_VIEW_PAGER_INDICATOR_VIEW_MARGIN_WIDTH)
        if (progress != 0f) {
            if (scrollPosition >= currentPage) {
                rect[pageIndexViewWidth, 0f, pageIndexViewWidth + dip(INTRO_VIEW_PAGER_INDICATOR_VIEW_WIDTH) + dip(INTRO_VIEW_PAGER_INDICATOR_VIEW_MARGIN_WIDTH) * progress] = dip(INTRO_VIEW_PAGER_INDICATOR_VIEW_WIDTH)
            } else {
                rect[pageIndexViewWidth - dip(INTRO_VIEW_PAGER_INDICATOR_VIEW_MARGIN_WIDTH) * (1.0f - progress), 0f, (pageIndexViewWidth + dip(INTRO_VIEW_PAGER_INDICATOR_VIEW_WIDTH))] = dip(INTRO_VIEW_PAGER_INDICATOR_VIEW_WIDTH)
            }
        } else {
            rect[pageIndexViewWidth, 0f, (pageIndexViewWidth + dip(INTRO_VIEW_PAGER_INDICATOR_VIEW_WIDTH))] = dip(INTRO_VIEW_PAGER_INDICATOR_VIEW_WIDTH)
        }
        canvas.drawRoundRect(rect, dip(INTRO_VIEW_PAGER_INDICATOR_VIEW_OVAL_WIDTH), dip(INTRO_VIEW_PAGER_INDICATOR_VIEW_OVAL_WIDTH), paint)
    }
}
