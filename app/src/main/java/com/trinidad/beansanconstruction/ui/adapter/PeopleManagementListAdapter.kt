package com.trinidad.beansanconstruction.ui.adapter

import android.graphics.Color
import android.widget.TextView
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.moufans.lib_base.base.adapter.BaseAdapter
import com.moufans.lib_base.utils.ImageLoader
import com.moufans.lib_base.utils.span.AndroidSpan
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.api.bean.LogonLogSelectDataBean
import com.trinidad.beansanconstruction.databinding.ItemPeopleManagementListBinding

class PeopleManagementListAdapter(layoutResId: Int = R.layout.item_people_management_list, data: MutableList<LogonLogSelectDataBean.RecordsBean> = mutableListOf()) : BaseAdapter<LogonLogSelectDataBean.RecordsBean, ItemPeopleManagementListBinding>(layoutResId, data) {

    override fun convert(holder: BaseDataBindingHolder<ItemPeopleManagementListBinding>, position: Int, item: LogonLogSelectDataBean.RecordsBean) {
        holder.dataBinding?.apply {
            setMyText(mUserNameTextView, "姓名：", item.name ?: "")
            setMyText(mUserPhoneTextView, "电话：", item.phone ?: "")
            setMyText(mUseRoleTextView, "角色：", item.codename ?: "")
            setMyText(mUseIdTextView, "用户ID：", item.id ?: "")
            setMyText(mApplyTimeTextView, "申请时间：", item.createTime ?: "")
            ImageLoader.setImage(item.picUrl, mUserIconImageView, defaultImageResId = R.mipmap.ic_default_icon)
            // 审核状态（0/审核中，1/拒绝，2/通过）
            val deleted = item.deleted
            mLeftRTextView.apply {
                isEnabled = deleted != "2"
                text = when (deleted) {
                    "0" -> {
                        "拒绝申请"
                    }
                    "1" -> {
                        "已拒绝"
                    }
                    "2" -> {
                        "拒绝申请"
                    }
                    else -> {
                        ""
                    }

                }
            }
            mRightRTextView.apply {
                isEnabled = deleted != "1"
                text = when (deleted) {
                    "0" -> {
                        "通过审核"
                    }
                    "1" -> {
                        "通过审核"
                    }
                    "2" -> {
                        "已审核"
                    }
                    else -> {
                        ""
                    }

                }
            }
        }
    }

    private fun setMyText(textView: TextView, title: String, content: String) {
        textView.text = AndroidSpan().drawCommonSpan(title).drawForegroundColor(content, Color.parseColor("#ff333333")).spanText
    }
}