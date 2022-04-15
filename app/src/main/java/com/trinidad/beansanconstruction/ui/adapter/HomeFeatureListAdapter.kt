package com.trinidad.beansanconstruction.ui.adapter

import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.moufans.lib_base.base.adapter.BaseAdapter
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.databinding.ItemHomeFeatureListBinding
import com.trinidad.beansanconstruction.ui.bean.FeatureListBean

class HomeFeatureListAdapter(layoutResId: Int = R.layout.item_home_feature_list, data: MutableList<FeatureListBean> = mutableListOf()) : BaseAdapter<FeatureListBean, ItemHomeFeatureListBinding>(layoutResId, data) {
    override fun convert(holder: BaseDataBindingHolder<ItemHomeFeatureListBinding>, position: Int, item: FeatureListBean) {
        holder.dataBinding?.apply {
            mFeatureIconImageView.setImageResource(item.resId)
            mFeatureTitleTextView.text = item.title
        }

    }
}