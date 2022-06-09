package com.trinidad.beansanconstruction.ui.adapter

import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.moufans.lib_base.base.adapter.BaseAdapter
import com.moufans.lib_base.utils.span.AndroidSpan
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.api.bean.CarInfoSelectDataBean
import com.trinidad.beansanconstruction.databinding.ItemChooseCarListBinding
import com.trinidad.beansanconstruction.databinding.ItemMechanicalDesList1Binding
import com.trinidad.beansanconstruction.databinding.ItemMechanicalDesListBinding

class MechanicalDesContentListAdapter(layoutResId: Int, data: MutableList<String> = mutableListOf()) : BaseAdapter<String, ItemMechanicalDesList1Binding>(layoutResId, data) {

    var count = 10

    override fun convert(holder: BaseDataBindingHolder<ItemMechanicalDesList1Binding>, position: Int, item: String) {
        holder.dataBinding?.apply {
            mContentTextView.text = if (position % count == 0) item else "¥$item"
            mContentTextView.setTextColor(Color.parseColor(if (position % count == 0) "#703C1F" else "#000000"))
            mContentTextView.textSize = if (position % count == 0) 15f else 13f
            mRightView.visibility = if (position % count == 0) View.GONE else View.VISIBLE
        }
    }

    private fun setMyText(textView: TextView, title: String, content: String) {
        textView.text = AndroidSpan().drawCommonSpan(title).drawForegroundColor(content, Color.parseColor("#ff333333")).spanText
    }
}