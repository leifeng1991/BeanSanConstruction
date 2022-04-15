package com.trinidad.beansanconstruction.ui.adapter

import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.moufans.lib_base.base.adapter.BaseAdapter
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.api.bean.ProjectListDataBean
import com.trinidad.beansanconstruction.databinding.ItemProjectListBinding

class ProjectListAdapter(private val isChoice: Boolean = false, layoutResId: Int = R.layout.item_project_list, data: MutableList<ProjectListDataBean> = mutableListOf()) : BaseAdapter<ProjectListDataBean, ItemProjectListBinding>(layoutResId, data) {
    override fun convert(holder: BaseDataBindingHolder<ItemProjectListBinding>, position: Int, item: ProjectListDataBean) {
        holder.dataBinding?.apply {
            // 名称
            mNameTitleTextView.text = item.name
            // 创建时间
            mCreateTimeTextView.text = item.createTime
            // 地址
            mAddressTextView.text = item.siteName
            mEditTextView.text = if (isChoice) "选择" else "编辑"
        }
    }
}