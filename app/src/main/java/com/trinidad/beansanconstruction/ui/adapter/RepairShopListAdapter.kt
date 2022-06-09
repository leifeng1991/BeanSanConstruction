package com.trinidad.beansanconstruction.ui.adapter

import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.moufans.lib_base.base.adapter.BaseAdapter
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.api.bean.RepairSelectDataBean
import com.trinidad.beansanconstruction.databinding.ItemRepairShopListBinding

class RepairShopListAdapter(layoutResId: Int = R.layout.item_repair_shop_list, data: MutableList<RepairSelectDataBean.RecordsBean> = mutableListOf()) : BaseAdapter<RepairSelectDataBean.RecordsBean, ItemRepairShopListBinding>(layoutResId, data) {

    override fun convert(holder: BaseDataBindingHolder<ItemRepairShopListBinding>, position: Int, item: RepairSelectDataBean.RecordsBean) {
        holder.dataBinding?.apply {
            // 维修店名
            mShopNameTextView.text = item.name
            // 地址
            mShopAddressTextView.text = item.address
            // 联系人
            mShopUserTextView.text = item.person
            // 联系电话
            mShopPhoneTextView.text = item.phone
        }
    }
}