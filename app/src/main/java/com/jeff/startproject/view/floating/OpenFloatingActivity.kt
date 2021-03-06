package com.jeff.startproject.view.floating

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import com.jeff.startproject.MyApplication
import com.jeff.startproject.R
import com.jeff.startproject.databinding.ActivityFloatingBinding
import com.jeff.startproject.widget.floating.*
import com.jeff.startproject.widget.floating.draggable.MovingDraggable
import com.jeff.startproject.widget.floating.draggable.SpringDraggable
import com.view.base.BaseActivity

class OpenFloatingActivity : BaseActivity<ActivityFloatingBinding>() {

    override fun getViewBinding() = ActivityFloatingBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
            startActivityForResult(intent, 0)
        }

        binding.btnOpen1.setOnClickListener {
            val intent = Intent(this, FloatingWindowService::class.java)
            startService(intent)
        }

        binding.btnClose1.setOnClickListener {
            val intent = Intent(this, FloatingWindowService::class.java)
            stopService(intent)
        }

        binding.btnOpen2.setOnClickListener {
            FloatingWindowUtil.createFloatingWindow(this, MovingDraggable())
        }

        binding.btnOpen3.setOnClickListener {
            FloatingWindowUtil.createFloatingWindow(this, SpringDraggable())
        }

        binding.btnOpen4.setOnClickListener {
            FloatingWindowUtil.createFloatingWindowWithHorizontalDraggable(this)
        }

        binding.btnOpen5.setOnClickListener {
            val floatingLayout = layoutInflater.inflate(R.layout.view_floating, null)
            floatingLayout.findViewById<View>(R.id.btn).setOnClickListener {
                windowManager.removeView(floatingLayout)
            }
            FloatingWindowUtil.createFloatingWindow(this, false, floatingLayout)
        }

        binding.btnOpen6.setOnClickListener {
            val view = StringFloatingMessageToast(this)
                .setContent("Hello!!")
                .setDuration(2000)
                .setCallback(object : FloatingMessageToast.FloatingMessageToastCallback {
                    override fun onCloseToast(view: View) {
                        FloatingWindowManager.clearSpecificView(MyApplication.self, view)
                    }
                }).show()
            FloatingWindowManager.addFloating(view)
        }

        binding.btnOpen7.setOnClickListener {
            FloatingWindowManager.hideAllMessage(this)
        }

        binding.btnOpen8.setOnClickListener {
            FloatingWindowManager.showAllFloating(this)
        }

        binding.btnOpen9.setOnClickListener {
            FloatingWindowManager.clearAllMessage(this)
        }
    }
}