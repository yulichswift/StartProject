package com.jeff.startproject.ui.main

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
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
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
import com.jeff.startproject.databinding.LayoutMainContentBinding
import com.jeff.startproject.ui.adbcmd.AdbCmdActivity
import com.jeff.startproject.ui.appmanager.AppManagerActivity
import com.jeff.startproject.ui.blur.BlurActivity
import com.jeff.startproject.ui.bottomsheet.BottomSheetActivity
import com.jeff.startproject.ui.bottomsheet.BottomSheetActivity2
import com.jeff.startproject.ui.chain.ChainActivity
import com.jeff.startproject.ui.chart.ChartActivity
import com.jeff.startproject.ui.colorpicker.ColorPickerActivity
import com.jeff.startproject.ui.datastructure.DataStructureActivity
import com.jeff.startproject.ui.db.DbActivity
import com.jeff.startproject.ui.draw.DrawActivity
import com.jeff.startproject.ui.edittext.EditTextActivity
import com.jeff.startproject.ui.encrypt.EncryptActivity
import com.jeff.startproject.ui.eventbus.EventBusActivity
import com.jeff.startproject.ui.eventbus.LiveEventBusActivity
import com.jeff.startproject.ui.file.FileActivity
import com.jeff.startproject.ui.floating.OpenFloatingActivity
import com.jeff.startproject.ui.flowcontrol.FlowControlActivity
import com.jeff.startproject.ui.gson.GsonActivity
import com.jeff.startproject.ui.list.DragActivity
import com.jeff.startproject.ui.livedata.LiveDataAdvActivity
import com.jeff.startproject.ui.login.GoogleSignInActivity
import com.jeff.startproject.ui.login.LoginActivity
import com.jeff.startproject.ui.myclipboard.ClipboardActivity
import com.jeff.startproject.ui.navigation.NavigationActivity
import com.jeff.startproject.ui.others.CustomActivity
import com.jeff.startproject.ui.others.OverrideTransitionActivity
import com.jeff.startproject.ui.pager.PagerActivity
import com.jeff.startproject.ui.preferences.PreferencesActivity
import com.jeff.startproject.ui.result.ResultActivity
import com.jeff.startproject.ui.runtimeexec.RuntimeExecActivity
import com.jeff.startproject.ui.sample.SampleActivity
import com.jeff.startproject.ui.sort.DataSortActivity
import com.jeff.startproject.ui.spec.SpecActivity
import com.jeff.startproject.ui.table.TableActivity
import com.jeff.startproject.ui.text.TextActivity
import com.jeff.startproject.ui.ui.BlankActivity
import com.jeff.startproject.ui.ui.SeekBarActivity
import com.jeff.startproject.ui.vector.VectorActivity
import com.jeff.startproject.ui.websocket.WebSocketActivity
import com.jeff.startproject.utils.SoftInputAssist
import com.jeff.startproject.widget.CustomView3
import com.ui.base.BaseActivity
import com.yulichswift.roundedview.widget.RoundedTextView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

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
    private lateinit var stubBinding: LayoutMainContentBinding

    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        SoftInputAssist(this, lifecycle, binding.root)

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

        stubBinding = LayoutMainContentBinding.bind(binding.viewStub.inflate())
        binding.nestedScrollView.setAlphaByScroll(binding.backgroundView)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                supervisorScope {
                    launch {
                        viewModel.getSearchFlow()
                            // flowWithLifecycle 為 repeatOnLifecycle 再封裝, 讓其感覺更直觀.
                            // .flowWithLifecycle(lifecycle, Lifecycle.State.RESUMED)
                            .debounce(500L)
                            .collectLatest {
                                it.takeIf { it.isNotBlank() }?.let(::filterBtn) ?: visibleAllBtn()
                            }
                    }
                }
            }
        }

        stubBinding.editTextSearch.customInsertionActionModeCallback =
            getActionModeCallback("Insertion paste")
        stubBinding.editTextSearch.customSelectionActionModeCallback =
            getActionModeCallback("Selection paste")
        stubBinding.editTextSearch.addTextChangedListener {
            viewModel.search(it.toString())
        }

        stubBinding.cardView.setOnClickListener {
            addViewToDecorView(it)
        }

        stubBinding.btnEventBus.setOnClickListener {
            Intent(this, EventBusActivity::class.java).also {
                startActivity(it)
            }
        }

        stubBinding.btnLiveEventBus.setOnClickListener {
            startActivity(Intent(this, LiveEventBusActivity::class.java))
        }

        stubBinding.btnEdit.setOnClickListener {
            startActivity(Intent(this, EditTextActivity::class.java))
        }

        stubBinding.btnRoom.setOnClickListener {
            startActivity(Intent(this, DbActivity::class.java))
        }

        stubBinding.btnFlow.setOnClickListener {
            startActivity(Intent(this, FlowControlActivity::class.java))
        }

        stubBinding.btnVector.setOnClickListener {
            startActivity(Intent(this, VectorActivity::class.java))
        }

        stubBinding.btnSample.setOnClickListener {
            startActivity(Intent(this, SampleActivity::class.java))
        }

        stubBinding.btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        stubBinding.btnChain.setOnClickListener {
            startActivity(Intent(this, ChainActivity::class.java))
        }

        stubBinding.btnWebsocket.setOnClickListener {
            startActivity(Intent(this, WebSocketActivity::class.java))
        }

        stubBinding.btnNavigation.setOnClickListener {
            startActivity(Intent(this, NavigationActivity::class.java))
        }

        stubBinding.btnDraw.setOnClickListener {
            startActivity(Intent(this, DrawActivity::class.java))
        }

        stubBinding.btnAdbCmd.setOnClickListener {
            startActivity(Intent(this, AdbCmdActivity::class.java))
        }

        stubBinding.btnRuntimeExec.setOnClickListener {
            startActivity(Intent(this, RuntimeExecActivity::class.java))
        }

        stubBinding.btnFile.setOnClickListener {
            startActivity(Intent(this, FileActivity::class.java))
        }

        stubBinding.btnPager.setOnClickListener {
            startActivity(Intent(this, PagerActivity::class.java))
        }

        stubBinding.btnDataStructure.setOnClickListener {
            startActivity(Intent(this, DataStructureActivity::class.java))
        }

        stubBinding.btnPreference.setOnClickListener {
            startActivity(Intent(this, PreferencesActivity::class.java))
        }

        stubBinding.btnBlur.setOnClickListener {
            startActivity(Intent(this, BlurActivity::class.java))
        }

        stubBinding.btnFloating.setOnClickListener {
            startActivity(Intent(this, OpenFloatingActivity::class.java))
        }

        stubBinding.btnVibrate.setOnClickListener {
            MyApplication.self.startVibrate()
        }

        stubBinding.btnTextView.setOnClickListener {
            startActivity(Intent(this, TextActivity::class.java))
        }

        stubBinding.btnSort.setOnClickListener {
            startActivity(Intent(this, DataSortActivity::class.java))
        }

        stubBinding.btnEncrypt.setOnClickListener {
            startActivity(Intent(this, EncryptActivity::class.java))
        }

        stubBinding.btnOverrideTransition.setOnClickListener {
            startActivity(Intent(this, OverrideTransitionActivity::class.java))
        }

        stubBinding.btnGson.setOnClickListener {
            startActivity(Intent(this, GsonActivity::class.java))
        }

        stubBinding.btnDrag.setOnClickListener {
            startActivity(Intent(this, DragActivity::class.java))
        }

        stubBinding.btnCustom.setOnClickListener {
            startActivity(Intent(this, CustomActivity::class.java))
        }

        stubBinding.btnSeekBar.setOnClickListener {
            startActivity(Intent(this, SeekBarActivity::class.java))
        }

        stubBinding.btnSpec.setOnClickListener {
            startActivity(Intent(this, SpecActivity::class.java))
        }

        stubBinding.btnLiveDataAdv.setOnClickListener {
            startActivity(Intent(this, LiveDataAdvActivity::class.java))
        }

        stubBinding.btnPopupMenu.setOnClickListener {
            showPopupWindow(stubBinding.btnPopupMenu, 0, 0)
            // showPopupWindowAtLocation(stubBinding.btnPopupMenu)
        }

        stubBinding.btnChart.setOnClickListener {
            startActivity(Intent(this, ChartActivity::class.java))
        }

        stubBinding.btnActivityResult.setOnClickListener {
            startActivity(Intent(this, ResultActivity::class.java))
        }

        stubBinding.btnColorPicker.setOnClickListener {
            startActivity(Intent(this, ColorPickerActivity::class.java))
        }

        stubBinding.btnPackageManager.setOnClickListener {
            startActivity(Intent(this, AppManagerActivity::class.java))
        }

        stubBinding.btnGoogleLogin.setOnClickListener {
            startActivity(Intent(this, GoogleSignInActivity::class.java))
        }

        stubBinding.btnClipboard.setOnClickListener {
            startActivity(Intent(this, ClipboardActivity::class.java))
        }

        stubBinding.btnTable.setOnClickListener {
            startActivity(Intent(this, TableActivity::class.java))
        }

        stubBinding.btnBottomSheet.setOnClickListener {
            startActivity(Intent(this, BottomSheetActivity::class.java))
        }

        stubBinding.btnBottomSheet2.setOnClickListener {
            startActivity(Intent(this, BottomSheetActivity2::class.java))
        }

        stubBinding.btnBlankActivity.setOnClickListener {
            startActivity(Intent(this, BlankActivity::class.java))
        }

        stubBinding.btnAdjacent.setOnClickListener {
            startActivity(Intent(this, BlankActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT or Intent.FLAG_ACTIVITY_NEW_TASK)
            })
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

    private fun visibleAllBtn() {
        (binding.nestedScrollView.getChildAt(0) as ViewGroup).children.forEach {
            it.visibility = View.VISIBLE
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
