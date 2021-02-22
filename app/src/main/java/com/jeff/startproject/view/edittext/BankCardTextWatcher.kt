package com.jeff.startproject.view.edittext

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.log.JFLog

class BankCardTextWatcher(private val editText: EditText) : TextWatcher {

    init {
        editText.addTextChangedListener(this)
    }

    // TODO: 不建議使用
    protected fun finalize() {
        JFLog.d("GC")
    }

    private var lastString = ""

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val startTime = System.currentTimeMillis()
        val cleanString = s.toString().replace(" ", "")

        val sb = StringBuilder(cleanString)

        if (cleanString.length > 4) {
            var d = 0
            for (i in 4..cleanString.length step 4) {
                sb.insert(i + d, " ")
                d += 1
            }
        }

        if (cleanString.length % 4 == 0) {
            sb.append(' ')
        }

        val resultString = sb.toString()

        if (s != null && !resultString.contentEquals(s)) {
            editText.removeTextChangedListener(this)
            val sbLength = resultString.length
            val newSelectionStart =
                (sbLength - lastString.length)
                    .let {
                        val startSel = start + count
                        when {
                            it > 1 -> startSel + 1
                            it < -1 -> {
                                if (lastString.last() == ' ') {
                                    startSel
                                } else {
                                    startSel - 1
                                }
                            }
                            else -> startSel
                        }
                    }.let {
                        when {
                            it < 0 -> 0
                            it > sbLength -> sbLength
                            else -> it
                        }
                    }

            editText.setText(resultString)
            editText.setSelection(newSelectionStart)

            editText.addTextChangedListener(this)
        }

        lastString = resultString

        JFLog.d("Spend: ${System.currentTimeMillis() - startTime} ns")
    }

    override fun afterTextChanged(editable: Editable?) {
    }
}