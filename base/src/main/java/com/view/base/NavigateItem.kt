package com.view.base

import android.os.Bundle

sealed class NavigateItem {
    object Up : NavigateItem()
    data class PopBackStack(val fragmentId: Int, val inclusive: Boolean) : NavigateItem()
    data class Destination(val action: Int, val bundle: Bundle?) : NavigateItem()
}