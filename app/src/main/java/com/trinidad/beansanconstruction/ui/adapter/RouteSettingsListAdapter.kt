package com.trinidad.beansanconstruction.ui.adapter

import android.annotation.SuppressLint
import android.view.View
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.moufans.lib_base.base.adapter.BaseAdapter
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.api.bean.TransportSelectDataBean
import com.trinidad.beansanconstruction.databinding.ItemRouteSettingsListBinding
import com.trinidad.beansanconstruction.utils.StringUtil

class RouteSettingsListAdapter(layoutResId: Int = R.layout.item_route_settings_list, data: MutableList<TransportSelectDataBean.RecordsListBean> = mutableListOf()) : BaseAdapter<TransportSelectDataBean.RecordsListBean, ItemRouteSettingsListBinding>(layoutResId, data) {
    @SuppressLint("SetTextI18n")
    override fun convert(holder: BaseDataBindingHolder<ItemRouteSettingsListBinding>, position: Int, item: TransportSelectDataBean.RecordsListBean) {
        holder.dataBinding?.apply {
            mRootShadowLayout.shadowBottom = if (position == data.size - 1) context.resources.getDimension(R.dimen.com_dp15) else context.resources.getDimension(R.dimen.com_dp10)
            // 起始点项目
            startTextView.text = item.startPoint
            // 终点倒土点
            endTextView.text = item.endPoint
            // 单价
            mUnitPriceTextView.text = "单价：¥${StringUtil.saveTwoDecimal(item.money ?: "0")}"
            // 起始点打卡范围
            mLeftClockInScopeTextView.text = "打卡范围：${item.startExtent}m"
            // 终点打卡范围
            mRightClockInScopeTextView.text = "打卡范围：${item.endExtent}m"
            mHaveOpenedRTextView.visibility = if (item.deleted == "0") View.GONE else View.VISIBLE
            mHaveClosedRTextView.visibility = if (item.deleted == "0") View.VISIBLE else View.GONE
        }
    }
}