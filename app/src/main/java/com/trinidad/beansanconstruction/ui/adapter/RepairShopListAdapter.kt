package com.trinidad.beansanconstruction.ui.adapter

import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.moufans.lib_base.base.adapter.BaseAdapter
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.api.bean.CarInfoSelectDataBean
import com.trinidad.beansanconstruction.api.bean.RoleDataBean
import com.trinidad.beansanconstruction.databinding.ItemPeopleManagementListBinding
import com.trinidad.beansanconstruction.databinding.ItemRepairShopListBinding
import com.trinidad.beansanconstruction.databinding.ItemRoleListBinding

class RepairShopListAdapter(layoutResId: Int = R.layout.item_repair_shop_list, data: MutableList<CarInfoSelectDataBean.RecordsBean> = mutableListOf()) : BaseAdapter<CarInfoSelectDataBean.RecordsBean, ItemRepairShopListBinding>(layoutResId, data) {

    override fun convert(holder: BaseDataBindingHolder<ItemRepairShopListBinding>, position: Int, item: CarInfoSelectDataBean.RecordsBean) {
        holder.dataBinding?.apply {

        }
    }
}