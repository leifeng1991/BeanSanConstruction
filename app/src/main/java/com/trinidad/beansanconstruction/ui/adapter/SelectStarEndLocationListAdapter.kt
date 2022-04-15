package com.trinidad.beansanconstruction.ui.adapter

import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.moufans.lib_base.base.adapter.BaseAdapter
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.api.bean.ProjectListDataBean
import com.trinidad.beansanconstruction.databinding.ItemSelectStartEndLocationListBinding

class SelectStarEndLocationListAdapter(layoutResId: Int = R.layout.item_select_start_end_location_list, data: MutableList<ProjectListDataBean> = mutableListOf()) : BaseAdapter<ProjectListDataBean, ItemSelectStartEndLocationListBinding>(layoutResId, data) {
    override fun convert(holder: BaseDataBindingHolder<ItemSelectStartEndLocationListBinding>, position: Int, item: ProjectListDataBean) {

    }
}