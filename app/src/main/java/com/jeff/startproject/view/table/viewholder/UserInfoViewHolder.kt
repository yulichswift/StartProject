package com.jeff.startproject.view.table.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_user_info.view.*

class UserInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val ivAvater = itemView.iv_avatar
    val tvName = itemView.tv_name
}