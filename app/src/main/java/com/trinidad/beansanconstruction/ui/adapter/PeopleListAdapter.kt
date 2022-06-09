package com.trinidad.beansanconstruction.ui.adapter

import android.graphics.Color
import android.widget.TextView
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.moufans.lib_base.base.adapter.BaseAdapter
import com.moufans.lib_base.utils.ImageLoader
import com.moufans.lib_base.utils.span.AndroidSpan
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.api.bean.RepairSelectDataBean
import com.trinidad.beansanconstruction.api.bean.UserSelectDataBean
import com.trinidad.beansanconstruction.databinding.ItemCostTypesListBinding
import com.trinidad.beansanconstruction.databinding.ItemPeopleListBinding
import com.trinidad.beansanconstruction.databinding.ItemProjectListBinding

class PeopleListAdapter(layoutResId: Int = R.layout.item_people_list, data: MutableList<UserSelectDataBean.RecordsBean> = mutableListOf()) : BaseAdapter<UserSelectDataBean.RecordsBean, ItemPeopleListBinding>(layoutResId, data) {

    override fun convert(holder: BaseDataBindingHolder<ItemPeopleListBinding>, position: Int, item: UserSelectDataBean.RecordsBean) {
        holder.dataBinding?.apply {
            setMyText(mUserNameTextView, "姓名：", item.name ?: "")
            setMyText(mUserPhoneTextView, "电话：", item.phone ?: "")
            setMyText(mUseRoleTextView, "角色：", item.code ?: "")
            setMyText(mUseIdTextView, "用户ID：", item.id ?: "")
            setMyText(mApplyTimeTextView, "申请时间：", item.createTime ?: "")
            ImageLoader.setImage(item.picUrl, mUserIconImageView, defaultImageResId = R.mipmap.ic_default_icon)
        }
    }

    private fun setMyText(textView: TextView, title: String, content: String) {
        textView.text = AndroidSpan().drawCommonSpan(title).drawForegroundColor(content, Color.parseColor("#ff333333")).spanText
    }
}