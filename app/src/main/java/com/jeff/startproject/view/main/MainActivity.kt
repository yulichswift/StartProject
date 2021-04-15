package com.jeff.startproject.view.main

import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.PopupWindow
import androidx.activity.viewModels
import androidx.core.view.children
import androidx.core.widget.NestedScrollView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.jeff.startproject.MyApplication
import com.jeff.startproject.R
import com.jeff.startproject.databinding.ActivityMainBinding
import com.jeff.startproject.view.LiveDataAdvActivity
import com.jeff.startproject.view.adbcmd.AdbCmdActivity
import com.jeff.startproject.view.blur.BlurActivity
import com.jeff.startproject.view.chain.ChainActivity
import com.jeff.startproject.view.datastructure.DataStructureActivity
import com.jeff.startproject.view.db.DbActivity
import com.jeff.startproject.view.draw.DrawActivity
import com.jeff.startproject.view.edittext.EditTextActivity
import com.jeff.startproject.view.encrypt.EncryptActivity
import com.jeff.startproject.view.eventbus.EventBusActivity
import com.jeff.startproject.view.eventbus.LiveEventBusActivity
import com.jeff.startproject.view.file.FileActivity
import com.jeff.startproject.view.floating.OpenFloatingActivity
import com.jeff.startproject.view.flowcontrol.FlowControlActivity
import com.jeff.startproject.view.gson.GsonActivity
import com.jeff.startproject.view.list.DragActivity
import com.jeff.startproject.view.login.LoginActivity
import com.jeff.startproject.view.navigation.NavigationActivity
import com.jeff.startproject.view.others.CustomActivity
import com.jeff.startproject.view.others.OverrideTransitionActivity
import com.jeff.startproject.view.pager.PagerActivity
import com.jeff.startproject.view.preferences.PreferencesActivity
import com.jeff.startproject.view.runtimeexec.RuntimeExecActivity
import com.jeff.startproject.view.sample.SampleActivity
import com.jeff.startproject.view.sort.DataSortActivity
import com.jeff.startproject.view.spec.SpecActivity
import com.jeff.startproject.view.text.TextActivity
import com.jeff.startproject.view.ui.SeekBarActivity
import com.jeff.startproject.view.vector.VectorActivity
import com.jeff.startproject.view.websocket.WebSocketActivity
import com.jeff.startproject.widget.CustomView3
import com.view.base.BaseActivity
import com.yulichswift.roundedview.widget.RoundedTextView
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

/*
 * https://developer.android.google.cn/kotlin/ktx
 *
 * https://developer.android.google.cn/kotlin/coroutines
 *
 * https://source.android.com/setup/contribute/code-style#follow-field-naming-conventions
 *
 * https://material.io/develop/android/components/material-card-view
 *
 * https://material.io/develop/android/components/material-button
 *
 * View binding:
 * https://developer.android.com/topic/libraries/view-binding
 * https://blog.csdn.net/u010976213/article/details/104501830
 *
 * CollapsingToolbarLayout:
 * https://www.jianshu.com/p/c6a6d08f4a2b
 * https://www.itread01.com/content/1549919007.html
 * app:layout_scrollFlags="scroll|exitUntilCollapsed|enterAlways"
 *
 * LiveData:
 * https://juejin.im/post/5cdff0de5188252f5e019bea
 */

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val viewModel: MainViewModel by viewModels()

    private val broadcastChannel = BroadcastChannel<String>(Channel.BUFFERED)
    fun getBroadcastChannelFlow() = broadcastChannel.asFlow()

    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.navigationBarColor = getColor(R.color.purple)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR

        binding.nestedScrollView.setAlphaByScroll(binding.backgroundView)

        lifecycleScope.launch {
            getBroadcastChannelFlow()
                    .debounce(500L)
                    .collectLatest {
                        filterBtn(it)
                    }
        }

        binding.editTextSearch.addTextChangedListener {
            broadcastChannel.offer(it.toString())
        }

        binding.cardView.setOnClickListener {
            addViewToDecorView(it)
        }

        binding.btnEventBus.setOnClickListener {
            Intent(this, EventBusActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnLiveEventBus.setOnClickListener {
            Intent(this, LiveEventBusActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnEdit.setOnClickListener {
            Intent(this, EditTextActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnRoom.setOnClickListener {
            Intent(this, DbActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnFlow.setOnClickListener {
            Intent(this, FlowControlActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnVector.setOnClickListener {
            Intent(this, VectorActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnSample.setOnClickListener {
            Intent(this, SampleActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnLogin.setOnClickListener {
            Intent(this, LoginActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnChain.setOnClickListener {
            Intent(this, ChainActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnWebsocket.setOnClickListener {
            Intent(this, WebSocketActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnNavigation.setOnClickListener {
            Intent(this, NavigationActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnDraw.setOnClickListener {
            Intent(this, DrawActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnAdbCmd.setOnClickListener {
            Intent(this, AdbCmdActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnRuntimeExec.setOnClickListener {
            Intent(this, RuntimeExecActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnFile.setOnClickListener {
            Intent(this, FileActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnPager.setOnClickListener {
            Intent(this, PagerActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnDataStructure.setOnClickListener {
            Intent(this, DataStructureActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnPreference.setOnClickListener {
            Intent(this, PreferencesActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnBlur.setOnClickListener {
            Intent(this, BlurActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnFloating.setOnClickListener {
            Intent(this, OpenFloatingActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnVibrate.setOnClickListener {
            MyApplication.self.startVibrate()
        }

        binding.btnTextView.setOnClickListener {
            Intent(this, TextActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnSort.setOnClickListener {
            Intent(this, DataSortActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnEncrypt.setOnClickListener {
            Intent(this, EncryptActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnOverrideTransition.setOnClickListener {
            Intent(this, OverrideTransitionActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnGson.setOnClickListener {
            Intent(this, GsonActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnDrag.setOnClickListener {
            Intent(this, DragActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnCustom.setOnClickListener {
            Intent(this, CustomActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnSeekBar.setOnClickListener {
            Intent(this, SeekBarActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnSpec.setOnClickListener {
            Intent(this, SpecActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnLiveDataAdv.setOnClickListener {
            Intent(this, LiveDataAdvActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnPopupMenu.setOnClickListener {
            showPopupWindow(binding.btnPopupMenu, 0, 0)
        }
    }

    private fun addViewToDecorView(view: View) {
        val rect = Rect().also { view.getGlobalVisibleRect(it) }
        val lp = FrameLayout.LayoutParams(rect.width(), rect.height())
        lp.setMargins(rect.left, rect.top, 0, 0)

        val customView = CustomView3(this)
        (window.decorView as ViewGroup).apply {
            addView(customView, lp)

            customView.btn.setOnClickListener {
                removeView(customView)
            }
        }
    }

    private fun NestedScrollView.setAlphaByScroll(view: View) {
        setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            view.alpha =
                    (scrollY - oldScrollY).let {
                        view.alpha - it / 300f
                    }.let {
                        when {
                            it > 1f -> 1f
                            it < .1f -> .1f
                            else -> it
                        }
                    }
        }
    }

    private fun filterBtn(key: String) {
        (binding.nestedScrollView.getChildAt(0) as ViewGroup).children.filter {
            it is RoundedTextView
        }.map {
            it as RoundedTextView
        }.forEach {
            when {
                key.isBlank() -> View.VISIBLE
                it.text.contains(key, true) -> View.VISIBLE
                else -> View.GONE
            }.let { result ->
                it.visibility = result
            }
        }
    }

    private fun showPopupWindow(anchor: View, xOffset: Int, yOffset: Int) {
        val rect = Rect().also { anchor.getGlobalVisibleRect(it) }
        PopupWindow(this).apply {
            width = rect.width()
            height = WindowManager.LayoutParams.WRAP_CONTENT
            contentView = CustomView3(this@MainActivity)
            isTouchable = true
            isFocusable = true
            isOutsideTouchable = true
            elevation = 9f
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setOnDismissListener {
            }
            showAsDropDown(anchor, xOffset, yOffset)
        }
    }
}
