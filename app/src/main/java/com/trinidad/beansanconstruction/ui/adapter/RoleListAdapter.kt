package com.trinidad.beansanconstruction.ui.adapter

import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.moufans.lib_base.base.adapter.BaseAdapter
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.api.bean.RoleDataBean
import com.trinidad.beansanconstruction.databinding.ItemProjectListBinding
import com.trinidad.beansanconstruction.databinding.ItemRoleListBinding

class RoleListAdapter(layoutResId: Int = R.layout.item_role_list, data: MutableList<RoleDataBean> = mutableListOf()) : BaseAdapter<RoleDataBean, ItemRoleListBinding>(layoutResId, data) {

    var checkedId: String? = null

    override fun convert(holder: BaseDataBindingHolder<ItemRoleListBinding>, position: Int, item: RoleDataBean) {
        holder.dataBinding?.apply {
            mRTextView.text = item.name
            mRTextView.isSelected = (checkedId == item.code)
        }
    }
}