package com.jeff.startproject.ui.appmanager

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
import com.facebook.shimmer.ShimmerFrameLayout
import com.jeff.startproject.R
import com.jeff.startproject.databinding.ItemViewPackageInfoBinding
import com.jeff.startproject.ui.appmanager.enums.AppType
import com.jeff.startproject.ui.appmanager.enums.IconType
import com.jeff.startproject.ui.appmanager.enums.ViewType
import com.jeff.startproject.ui.appmanager.vo.AppViewData
import com.jeff.startproject.ui.appmanager.vo.PassAppInfo
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class AppAdapter(private val packageManager: PackageManager) : ListAdapter<AppViewData, RecyclerView.ViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<AppViewData>() {
            override fun areItemsTheSame(oldItem: AppViewData, newItem: AppViewData): Boolean {
                return when (oldItem.viewType == newItem.viewType) {
                    true -> when (oldItem.viewType) {
                        ViewType.Loading -> true
                        ViewType.Normal -> oldItem.appInfo?.packageName == newItem.appInfo?.packageName && (oldItem.appType == newItem.appType)
                    }
                    false -> false
                }
            }

            override fun areContentsTheSame(oldItem: AppViewData, newItem: AppViewData): Boolean {
                return oldItem.appInfo?.labelRes == newItem.appInfo?.labelRes
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

    var iconType = IconType.Icon
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType.ordinal
    }

    private fun inflateWithLayout(viewGroup: ViewGroup, @LayoutRes layoutRes: Int): View {
        val layoutInflater = LayoutInflater.from(viewGroup.context)
        return layoutInflater.inflate(layoutRes, viewGroup, false)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType.Loading.ordinal -> LoadingViewHolder(inflateWithLayout(parent, R.layout.item_view_package_loading))
            else -> ViewHolder(inflateWithLayout(parent, R.layout.item_view_package_info))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> holder.bind(getItem(position))
            else -> Unit
        }
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        when (holder) {
            is LoadingViewHolder -> holder.isShimmer = true
            else -> Unit
        }

        super.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        when (holder) {
            is LoadingViewHolder -> holder.isShimmer = false
            else -> Unit
        }

        super.onViewDetachedFromWindow(holder)
    }

    inner class LoadingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val root = itemView as ShimmerFrameLayout

        var isShimmer: Boolean = true
            set(value) {
                if (field != value) {
                    field = value
                    if (field) root.startShimmer() else root.stopShimmer()
                }
            }
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

        private var appType: AppType? = null
        private var packageName: String? = null

        @SuppressLint("SetTextI18n")
        fun bind(customAppInfo: AppViewData) {
            appType = customAppInfo.appType

            val appInfo = customAppInfo.appInfo
            if (appInfo != null) {
                packageName = appInfo.packageName
                binding.icon.setImageDrawable(
                    when (iconType) {
                        IconType.Logo -> appInfo.loadLogo(packageManager)
                        IconType.Icon -> appInfo.loadIcon(packageManager)
                        IconType.Unbadged -> appInfo.loadUnbadgedIcon(packageManager)
                        IconType.Banner -> appInfo.loadBanner(packageManager)
                    }
                )

                binding.titleLabel.text = appInfo.loadLabel(packageManager)
                binding.packageLabel.text = appInfo.packageName
                binding.descLabel.text = appInfo.loadDescription(packageManager)
            }

            val pkgInfo = customAppInfo.pkgInfo
            if (pkgInfo != null) {
                binding.versionLabel.text = "${pkgInfo.versionName} (${pkgInfo.longVersionCode})"
            }

            when (appType) {
                AppType.NonSystem -> colorTrashNonSystem
                AppType.System -> colorTrashSystem
                AppType.Recent -> colorTrashRecent
                else -> null
            }?.let {
                ImageViewCompat.setImageTintList(binding.btnRemove, it)
            }

            binding.btnRemove.visibility = when (appType) {
                AppType.Recent -> View.VISIBLE
                else -> if (AppManagerViewModel.enableRemove) View.VISIBLE else View.GONE
            }
        }
    }
}