package com.jeff.startproject.ui.myclipboard

import android.content.ClipData
import android.content.ClipDescription.MIMETYPE_TEXT_HTML
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.Html
import androidx.lifecycle.lifecycleScope
import com.jeff.startproject.databinding.ActivityClipboardBinding
import com.log.JFLog
import com.ui.base.BaseActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// https://developer.android.com/guide/topics/text/copy-paste

class ClipboardActivity : BaseActivity<ActivityClipboardBinding>() {
    override fun getViewBinding(): ActivityClipboardBinding {
        return ActivityClipboardBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnCopy.setOnClickListener {
            copyToClipboard()
        }

        binding.btnPaste.setOnClickListener {
            pasteClipboard()
        }

        lifecycleScope.launch {
            delay(1000L)

            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = clipboard.primaryClip?.getItemAt(0)
            JFLog.d("ClipData[0]: ${clipData?.text}")
        }
    }

    private fun copyToClipboard() {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip: ClipData = ClipData.newPlainText("simple text", binding.edit.text.toString())
        clipboard.setPrimaryClip(clip)
    }

    private fun pasteClipboard() {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        if (clipboard.hasPrimaryClip()) {
            if (clipboard.primaryClipDescription?.hasMimeType(MIMETYPE_TEXT_PLAIN) == true) {
                val item = clipboard.primaryClip?.getItemAt(0)
                if (item == null) {
                    binding.textClipboard.text = "Primary clip null"
                } else {
                    var pasteData = item.text
                    if (!pasteData.isNullOrBlank()) {
                        binding.textClipboard.text = pasteData
                    }
                }
            } else if (clipboard.primaryClipDescription?.hasMimeType(MIMETYPE_TEXT_HTML) == true) {
                val item = clipboard.primaryClip?.getItemAt(0)
                if (item == null) {
                    binding.textClipboard.text = "Primary clip null"
                } else {
                    // item.text 取得文字部分
                    var pasteData = item.htmlText
                    if (!pasteData.isNullOrBlank()) {
                        binding.textClipboard.text = Html.fromHtml(pasteData, Html.FROM_HTML_MODE_COMPACT)
                    }
                }
            } else {
                binding.textClipboard.text = clipboard.primaryClipDescription.toString()
            }
        } else {
            binding.textClipboard.text = "No primary clip"
        }
    }
}
