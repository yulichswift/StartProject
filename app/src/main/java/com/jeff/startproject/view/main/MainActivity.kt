package com.jeff.startproject.view.main

import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.children
import androidx.core.widget.NestedScrollView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jeff.startproject.MyApplication
import com.jeff.startproject.R
import com.jeff.startproject.databinding.ActivityMainBinding
import com.jeff.startproject.view.adbcmd.AdbCmdActivity
import com.jeff.startproject.view.blur.BlurActivity
import com.jeff.startproject.view.chain.ChainActivity
import com.jeff.startproject.view.chart.ChartActivity
import com.jeff.startproject.view.colorpicker.ColorPickerActivity
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
import com.jeff.startproject.view.livedata.LiveDataAdvActivity
import com.jeff.startproject.view.login.LoginActivity
import com.jeff.startproject.view.navigation.NavigationActivity
import com.jeff.startproject.view.others.CustomActivity
import com.jeff.startproject.view.others.OverrideTransitionActivity
import com.jeff.startproject.view.pager.PagerActivity
import com.jeff.startproject.view.preferences.PreferencesActivity
import com.jeff.startproject.view.result.ResultActivity
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
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/*
 * https://developer.android.google.cn/kotlin/ktx
 *
 * https://developer.android.google.cn/kotlin/coroutines
 *
 * https://developer.android.com/kotlin/flow/stateflow-and-sharedflow
 *
 * https://medium.com/androiddevelopers/migrating-from-livedata-to-kotlins-flow-379292f419fb
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

    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                window.navigationBarColor = getColor(R.color.purple)
                ViewCompat.getWindowInsetsController(window.decorView)?.apply {
                    isAppearanceLightNavigationBars = true
                }
            }
            else -> {
                window.navigationBarColor = Color.WHITE
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            }
        }

        binding.nestedScrollView.setAlphaByScroll(binding.backgroundView)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.getSearchFlow()
                    // flowWithLifecycle 為 repeatOnLifecycle 再封裝, 讓其感覺更直觀.
                    // .flowWithLifecycle(lifecycle, Lifecycle.State.RESUMED)
                    .debounce(500L)
                    .collectLatest {
                        it.takeIf { it.isNotBlank() }?.let(::filterBtn)
                    }
            }
        }

        binding.editTextSearch.customInsertionActionModeCallback =
            getActionModeCallback("Insertion paste")
        binding.editTextSearch.customSelectionActionModeCallback =
            getActionModeCallback("Selection paste")
        binding.editTextSearch.addTextChangedListener {
            viewModel.search(it.toString())
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
            startActivity(Intent(this, LiveEventBusActivity::class.java))
        }

        binding.btnEdit.setOnClickListener {
            startActivity(Intent(this, EditTextActivity::class.java))
        }

        binding.btnRoom.setOnClickListener {
            startActivity(Intent(this, DbActivity::class.java))
        }

        binding.btnFlow.setOnClickListener {
            startActivity(Intent(this, FlowControlActivity::class.java))
        }

        binding.btnVector.setOnClickListener {
            startActivity(Intent(this, VectorActivity::class.java))
        }

        binding.btnSample.setOnClickListener {
            startActivity(Intent(this, SampleActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnChain.setOnClickListener {
            startActivity(Intent(this, ChainActivity::class.java))
        }

        binding.btnWebsocket.setOnClickListener {
            startActivity(Intent(this, WebSocketActivity::class.java))
        }

        binding.btnNavigation.setOnClickListener {
            startActivity(Intent(this, NavigationActivity::class.java))
        }

        binding.btnDraw.setOnClickListener {
            startActivity(Intent(this, DrawActivity::class.java))
        }

        binding.btnAdbCmd.setOnClickListener {
            startActivity(Intent(this, AdbCmdActivity::class.java))
        }

        binding.btnRuntimeExec.setOnClickListener {
            startActivity(Intent(this, RuntimeExecActivity::class.java))
        }

        binding.btnFile.setOnClickListener {
            startActivity(Intent(this, FileActivity::class.java))
        }

        binding.btnPager.setOnClickListener {
            startActivity(Intent(this, PagerActivity::class.java))
        }

        binding.btnDataStructure.setOnClickListener {
            startActivity(Intent(this, DataStructureActivity::class.java))
        }

        binding.btnPreference.setOnClickListener {
            startActivity(Intent(this, PreferencesActivity::class.java))
        }

        binding.btnBlur.setOnClickListener {
            startActivity(Intent(this, BlurActivity::class.java))
        }

        binding.btnFloating.setOnClickListener {
            startActivity(Intent(this, OpenFloatingActivity::class.java))
        }

        binding.btnVibrate.setOnClickListener {
            MyApplication.self.startVibrate()
        }

        binding.btnTextView.setOnClickListener {
            startActivity(Intent(this, TextActivity::class.java))
        }

        binding.btnSort.setOnClickListener {
            startActivity(Intent(this, DataSortActivity::class.java))
        }

        binding.btnEncrypt.setOnClickListener {
            startActivity(Intent(this, EncryptActivity::class.java))
        }

        binding.btnOverrideTransition.setOnClickListener {
            startActivity(Intent(this, OverrideTransitionActivity::class.java))
        }

        binding.btnGson.setOnClickListener {
            startActivity(Intent(this, GsonActivity::class.java))
        }

        binding.btnDrag.setOnClickListener {
            startActivity(Intent(this, DragActivity::class.java))
        }

        binding.btnCustom.setOnClickListener {
            startActivity(Intent(this, CustomActivity::class.java))
        }

        binding.btnSeekBar.setOnClickListener {
            startActivity(Intent(this, SeekBarActivity::class.java))
        }

        binding.btnSpec.setOnClickListener {
            startActivity(Intent(this, SpecActivity::class.java))
        }

        binding.btnLiveDataAdv.setOnClickListener {
            startActivity(Intent(this, LiveDataAdvActivity::class.java))
        }

        binding.btnPopupMenu.setOnClickListener {
            showPopupWindow(binding.btnPopupMenu, 0, 0)
            // showPopupWindowAtLocation(binding.btnPopupMenu)
        }

        binding.btnChart.setOnClickListener {
            startActivity(Intent(this, ChartActivity::class.java))
        }

        binding.btnActivityResult.setOnClickListener {
            startActivity(Intent(this, ResultActivity::class.java))
        }

        binding.btnColorPicker.setOnClickListener {
            startActivity(Intent(this, ColorPickerActivity::class.java))
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
                if (oldScrollY < scrollY) {
                    1f
                } else {
                    (oldScrollY - scrollY).let {
                        view.alpha - it / 300f
                    }
                }.let {
                    when {
                        it > 1f -> 1f
                        it < .5f -> .5f
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

    private fun showPopupWindowAtLocation(anchor: View) {
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
            showAtLocation(window.decorView, Gravity.TOP or Gravity.START, rect.left, rect.bottom)
        }
    }

    private fun getActionModeCallback(text: String) = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            when (item?.itemId) {
                android.R.id.paste -> {
                    Toast.makeText(this@MainActivity, text, Toast.LENGTH_SHORT).show()
                }
            }
            return false
        }

        override fun onDestroyActionMode(mode: ActionMode?) {

        }
    }
}
