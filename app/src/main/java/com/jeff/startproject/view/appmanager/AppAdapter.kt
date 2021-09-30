package com.jeff.startproject.view.appmanager

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jeff.startproject.R
import com.jeff.startproject.databinding.ItemViewPackageInfoBinding
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class AppAdapter(private val packageManager: PackageManager) : ListAdapter<CustomApplicationInfo, RecyclerView.ViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<CustomApplicationInfo>() {
            override fun areItemsTheSame(oldItem: CustomApplicationInfo, newItem: CustomApplicationInfo): Boolean {
                // 不比對 AppType 當 recyclerView 更新時, 會移動到相同 App 的位置, 物件不會更新.
                return (oldItem.appInfo.packageName == newItem.appInfo.packageName) && (oldItem.appType == newItem.appType)
            }

            override fun areContentsTheSame(oldItem: CustomApplicationInfo, newItem: CustomApplicationInfo): Boolean {
                return oldItem.appInfo.labelRes == newItem.appInfo.labelRes
            }
        }
    }

    private val colorTrashNonSystem = ColorStateList.valueOf(Color.RED)
    private val colorTrashSystem = ColorStateList.valueOf(Color.parseColor("#ffd800"))
    private val colorTrashRecent = ColorStateList.valueOf(Color.parseColor("#888888"))

    private val onRowSelected = MutableSharedFlow<PassAppInfo>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    fun onRowSelected(): SharedFlow<PassAppInfo> = onRowSelected

    private val onRowRemove = MutableSharedFlow<PassAppInfo>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    fun onClickedRemove(): SharedFlow<PassAppInfo> = onRowRemove

    var iconType = AppManagerViewModel.IconType.Icon
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private fun inflateWithLayout(viewGroup: ViewGroup, @LayoutRes layoutRes: Int): View {
        val layoutInflater = LayoutInflater.from(viewGroup.context)
        return layoutInflater.inflate(layoutRes, viewGroup, false)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = inflateWithLayout(parent, R.layout.item_view_package_info)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(getItem(position))
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemViewPackageInfoBinding.bind(view)

        init {
            binding.root.setOnClickListener {
                val appType = appType
                val packageName = packageName
                if (appType != null && packageName != null) {
                    onRowSelected.tryEmit(PassAppInfo(appType, packageName))
                }
            }

            binding.btnRemove.setOnClickListener {
                val appType = appType
                val packageName = packageName
                if (appType != null && packageName != null) {
                    onRowRemove.tryEmit(PassAppInfo(appType, packageName))
                }
            }
        }

        private var appType: AppManagerViewModel.AppType? = null
        private var packageName: String? = null

        @SuppressLint("SetTextI18n")
        fun bind(customAppInfo: CustomApplicationInfo) {
            appType = customAppInfo.appType
            val appInfo = customAppInfo.appInfo
            packageName = appInfo.packageName

            binding.icon.setImageDrawable(
                when (iconType) {
                    AppManagerViewModel.IconType.Logo -> appInfo.loadLogo(packageManager)
                    AppManagerViewModel.IconType.Icon -> appInfo.loadIcon(packageManager)
                    AppManagerViewModel.IconType.Unbadged -> appInfo.loadUnbadgedIcon(packageManager)
                    AppManagerViewModel.IconType.Banner -> appInfo.loadBanner(packageManager)
                }
            )

            binding.titleLabel.text = appInfo.loadLabel(packageManager)
            binding.packageLabel.text = appInfo.packageName
            binding.descLabel.text = appInfo.loadDescription(packageManager)

            when (appType) {
                AppManagerViewModel.AppType.NonSystem -> colorTrashNonSystem
                AppManagerViewModel.AppType.System -> colorTrashSystem
                AppManagerViewModel.AppType.Recent -> colorTrashRecent
                else -> null
            }?.let {
                ImageViewCompat.setImageTintList(binding.btnRemove, it)
            }

            binding.btnRemove.visibility = when (appType) {
                AppManagerViewModel.AppType.Recent -> View.VISIBLE
                else -> if (AppManagerViewModel.enableRemove) View.VISIBLE else View.GONE
            }
        }
    }
}