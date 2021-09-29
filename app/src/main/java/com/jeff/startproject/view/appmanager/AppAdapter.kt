package com.jeff.startproject.view.appmanager

import android.annotation.SuppressLint
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jeff.startproject.R
import com.jeff.startproject.databinding.ItemViewPackageInfoBinding
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class AppAdapter(private val packageManager: PackageManager) : ListAdapter<ApplicationInfo, RecyclerView.ViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ApplicationInfo>() {
            override fun areItemsTheSame(oldItem: ApplicationInfo, newItem: ApplicationInfo): Boolean {
                return oldItem.packageName == newItem.packageName
            }

            override fun areContentsTheSame(oldItem: ApplicationInfo, newItem: ApplicationInfo): Boolean {
                return oldItem.labelRes == newItem.labelRes
            }
        }
    }

    private val onRowSelected = MutableSharedFlow<String>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    fun onRowSelected(): SharedFlow<String> = onRowSelected

    private val onRowRemove = MutableSharedFlow<String>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    fun onClickedRemove(): SharedFlow<String> = onRowRemove

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
                packageName?.also(onRowSelected::tryEmit)
            }

            binding.btnRemove.setOnClickListener {
                packageName?.also(onRowRemove::tryEmit)
            }
        }

        private var packageName: String? = null

        @SuppressLint("SetTextI18n")
        fun bind(appInfo: ApplicationInfo) {
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
        }
    }
}