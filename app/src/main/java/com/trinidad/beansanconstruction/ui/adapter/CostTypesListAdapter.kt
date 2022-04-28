package com.trinidad.beansanconstruction.ui.adapter

import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.moufans.lib_base.base.adapter.BaseAdapter
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.api.bean.CarInfoSelectDataBean
import com.trinidad.beansanconstruction.api.bean.RoleDataBean
import com.trinidad.beansanconstruction.databinding.ItemCostTypesListBinding
import com.trinidad.beansanconstruction.databinding.ItemPeopleManagementListBinding
import com.trinidad.beansanconstruction.databinding.ItemRepairShopListBinding
import com.trinidad.beansanconstruction.databinding.ItemRoleListBinding

class CostTypesListAdapter(layoutResId: Int = R.layout.item_cost_types_list, data: MutableList<CarInfoSelectDataBean.RecordsBean> = mutableListOf()) : BaseAdapter<CarInfoSelectDataBean.RecordsBean, ItemCostTypesListBinding>(layoutResId, data) {

    override fun convert(holder: BaseDataBindingHolder<ItemCostTypesListBinding>, position: Int, item: CarInfoSelectDataBean.RecordsBean) {
        holder.dataBinding?.apply {

        }
    }
}