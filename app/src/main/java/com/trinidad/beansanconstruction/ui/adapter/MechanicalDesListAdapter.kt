package com.trinidad.beansanconstruction.ui.adapter

import android.graphics.Color
import android.widget.TextView
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.moufans.lib_base.base.adapter.BaseAdapter
import com.moufans.lib_base.utils.span.AndroidSpan
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.api.bean.CarInfoSelectDataBean
import com.trinidad.beansanconstruction.databinding.ItemChooseCarListBinding
import com.trinidad.beansanconstruction.databinding.ItemMechanicalDesListBinding

class MechanicalDesListAdapter(layoutResId: Int, data: MutableList<String> = mutableListOf()) : BaseAdapter<String, ItemMechanicalDesListBinding>(layoutResId, data) {

    override fun convert(holder: BaseDataBindingHolder<ItemMechanicalDesListBinding>, position: Int, item: String) {
        holder.dataBinding?.apply {
            mContentTextView.text = item
            mContentTextView.setBackgroundColor(Color.parseColor(if (position == 0) "#F7E1C9" else "#ffffff"))
            mContentTextView.textSize = if (position == 0) 15f else 13f
            mContentTextView.setTextColor(Color.parseColor(if (position == 0) "#703C1F" else "#000000"))
        }
    }

    private fun setMyText(textView: TextView, title: String, content: String) {
        textView.text = AndroidSpan().drawCommonSpan(title).drawForegroundColor(content, Color.parseColor("#ff333333")).spanText
    }
}