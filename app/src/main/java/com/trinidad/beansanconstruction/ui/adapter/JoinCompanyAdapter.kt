package com.trinidad.beansanconstruction.ui.adapter

import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.moufans.lib_base.base.adapter.BaseAdapter
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.api.bean.CompanyDataBean
import com.trinidad.beansanconstruction.databinding.ItemJoinCompanyBinding

class JoinCompanyAdapter(layoutResId: Int = R.layout.item_join_company, data: MutableList<CompanyDataBean> = mutableListOf()) : BaseAdapter<CompanyDataBean, ItemJoinCompanyBinding>(layoutResId, data) {
    override fun convert(holder: BaseDataBindingHolder<ItemJoinCompanyBinding>, position: Int, item: CompanyDataBean) {
        holder.dataBinding?.apply {
            mRootShadowLayout.shadowTop = if (position == 0) context.resources.getDimension(R.dimen.com_dp15) else context.resources.getDimension(R.dimen.com_dp7)
            mRootShadowLayout.shadowBottom = if (position == data.size - 1) context.resources.getDimension(R.dimen.com_dp15) else context.resources.getDimension(R.dimen.com_dp7)
            // 公司名称
            mCompanyNameTextView.text = item.name
            // 省份
            mCompanyCityTextView.text = item.province
        }
    }
}