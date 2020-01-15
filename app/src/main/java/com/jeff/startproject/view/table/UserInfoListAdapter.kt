package com.jeff.startproject.view.table

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.jeff.startproject.R
import com.jeff.startproject.model.api.user.UserItem
import com.jeff.startproject.view.table.viewholder.UserInfoViewHolder

class UserInfoListAdapter : PagedListAdapter<UserItem, UserInfoViewHolder>(diffCallback) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<UserItem>() {
            override fun areItemsTheSame(oldItem: UserItem, newItem: UserItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: UserItem, newItem: UserItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserInfoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(
            R.layout.item_user_info,
            parent,
            false
        )
        return UserInfoViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserInfoViewHolder, position: Int) {
        getItem(position).let {

            // user avatar
            Glide.with(holder.itemView.context)
                .load(it?.avatarUrl)
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.ivAvater)

            // user login
            it?.login.run {
                holder.tvName.text = it?.login
            }

            /*
            // user badge
            if (it!!.isSiteAdmin!!) {
                holder.badgeTextView.visibility = View.VISIBLE
            } else {
                holder.badgeTextView.visibility = View.GONE
            }
            */
        }
    }
}
