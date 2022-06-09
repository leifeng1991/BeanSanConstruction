package com.trinidad.beansanconstruction.ui.adapter

import android.graphics.Color
import android.widget.TextView
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.moufans.lib_base.base.adapter.BaseAdapter
import com.moufans.lib_base.utils.span.AndroidSpan
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.api.bean.CarProjectSelectDataBean
import com.trinidad.beansanconstruction.databinding.ItemCostQueryListBinding

class CostQueryListAdapter(layoutResId: Int = R.layout.item_cost_query_list, data: MutableList<CarProjectSelectDataBean.RecordsBean> = mutableListOf()) : BaseAdapter<CarProjectSelectDataBean.RecordsBean, ItemCostQueryListBinding>(layoutResId, data) {

    override fun convert(holder: BaseDataBindingHolder<ItemCostQueryListBinding>, position: Int, item: CarProjectSelectDataBean.RecordsBean) {
        holder.dataBinding?.apply {
            setMyText(mProjectTextView, "项目：", item.pname ?: "")
            setMyText(mCostTypesTextView, "费用类型：", item.ctname ?: "")
            setMyText(mConstructionUnitCostTextView, "费用单价：", "¥${item.unitPrice} 元${item.pattern}")
            setMyText(mQuantityTextView, "数量：", item.number ?: "")
            mCostOfSubtotalTextView.text = AndroidSpan().drawCommonSpan("费用小计：").drawForegroundColor("¥${item.sumPrice ?: "0"}", Color.parseColor("#E02020")).spanText
            setMyText(mCreateTimeTextView, "创建日期：", item.createTime ?: "")
        }
    }

    private fun setMyText(textView: TextView, title: String, content: String) {
        textView.text = AndroidSpan().drawCommonSpan(title).drawForegroundColor(content, Color.parseColor("#ff333333")).spanText
    }
}