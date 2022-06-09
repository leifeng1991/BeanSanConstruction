package com.trinidad.beansanconstruction.ui.adapter

import android.graphics.Color
import android.widget.TextView
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.moufans.lib_base.base.adapter.BaseAdapter
import com.moufans.lib_base.utils.span.AndroidSpan
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.api.bean.CostTypeSelectDataBean
import com.trinidad.beansanconstruction.api.bean.RepairSelectDataBean
import com.trinidad.beansanconstruction.databinding.ItemCostTypesListBinding

class CostTypesListAdapter(layoutResId: Int = R.layout.item_cost_types_list, data: MutableList<CostTypeSelectDataBean.RecordsBean> = mutableListOf()) : BaseAdapter<CostTypeSelectDataBean.RecordsBean, ItemCostTypesListBinding>(layoutResId, data) {

    override fun convert(holder: BaseDataBindingHolder<ItemCostTypesListBinding>, position: Int, item: CostTypeSelectDataBean.RecordsBean) {
        holder.dataBinding?.apply {
            // 费用名称
            mTitleTextView.text = item.name
            // 价格
            mPriceTextView.text = "¥${item.price}"
            // 计费单位
            mUnitTextView.text = item.pattern

            setMyText(mCreateTimeTextView, "创建日期：", item.createTime ?: "")
            setMyText(mCommissionRateTextView, "提成比例：", "${item.number ?: "0"}%")
        }
    }

    private fun setMyText(textView: TextView, title: String, content: String) {
        textView.text = AndroidSpan().drawCommonSpan(title).drawForegroundColor(content, Color.parseColor("#ff333333")).spanText
    }
}