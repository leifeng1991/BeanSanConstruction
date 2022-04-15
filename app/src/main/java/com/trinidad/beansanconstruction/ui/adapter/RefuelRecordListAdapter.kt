package com.trinidad.beansanconstruction.ui.adapter

import android.annotation.SuppressLint
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.moufans.lib_base.base.adapter.BaseAdapter
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.api.bean.OilPriceLogSelectDataBean
import com.trinidad.beansanconstruction.databinding.ItemRefuelRecordListBinding
import com.trinidad.beansanconstruction.utils.StringUtil

class RefuelRecordListAdapter(layoutResId: Int = R.layout.item_refuel_record_list, data: MutableList<OilPriceLogSelectDataBean.RecordsBean> = mutableListOf()) : BaseAdapter<OilPriceLogSelectDataBean.RecordsBean, ItemRefuelRecordListBinding>(layoutResId, data) {
    @SuppressLint("SetTextI18n")
    override fun convert(holder: BaseDataBindingHolder<ItemRefuelRecordListBinding>, position: Int, item: OilPriceLogSelectDataBean.RecordsBean) {
        holder.dataBinding?.apply {
            // 车牌
            mCarCardRTextView.text = item.carPlate
            // 车型
            mCarTypeRTextView.text = item.ctName
            // 加油量
            mFuelQuantityTextView.text = item.number
            // 油价
            mOilPriceTextView.text = "¥${StringUtil.saveTwoDecimal(item.price ?: "0")}"
            // 运输公司
            mTransportCompanyTextView.text = item.scName
            // 车队
            mMotorcadeTextView.text = item.mName
            // 车主
            mOwnersTextView.text = "车主：${item.personName}"
            // 创建时间
            mTimeTextView.text = item.createTime
            // 操作人
            mOperateTextView.text = "操作：${item.aname}"
            // 总价
            mTotalPriceTextView.text = "¥${StringUtil.saveTwoDecimal(item.money ?: "0")}"
        }
    }
}