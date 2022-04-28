package com.trinidad.beansanconstruction.ui.adapter

import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.moufans.lib_base.base.adapter.BaseAdapter
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.api.bean.RepairCostSelectDataBean
import com.trinidad.beansanconstruction.api.bean.RoleDataBean
import com.trinidad.beansanconstruction.databinding.ItemMaintenanceOrderReviewListBinding
import com.trinidad.beansanconstruction.databinding.ItemPeopleManagementListBinding
import com.trinidad.beansanconstruction.databinding.ItemRepairShopListBinding
import com.trinidad.beansanconstruction.databinding.ItemRoleListBinding

class MaintenanceOrderReviewListAdapter(layoutResId: Int = R.layout.item_maintenance_order_review_list, data: MutableList<RepairCostSelectDataBean.RecordsBean> = mutableListOf()) : BaseAdapter<RepairCostSelectDataBean.RecordsBean, ItemMaintenanceOrderReviewListBinding>(layoutResId, data) {

    override fun convert(holder: BaseDataBindingHolder<ItemMaintenanceOrderReviewListBinding>, position: Int, item: RepairCostSelectDataBean.RecordsBean) {
        holder.dataBinding?.apply {
            // 维修店名
            mShopNameTextView.text = item.repairName
            // 维修日期
            mMaintainDateTextView.text = item.wxDate
            // 申请时间
            mApplicationDateTextView.text = item.createTime
            // 申请人
            mProposerTextView.text = item.aname
            // 车牌/编号
            mNumberPlateTextView.text = item.carPlate
            // 内容
            mApplyContentTextView.text = item.content
            // 金额
            mPriceTextView.text = "¥${item.money}"
            // 审核状态（0/拒绝，1/审核中，2/通过）
            val deleted = item.deleted
            mLeftRTextView.apply {
                isEnabled = deleted != "2"
                text = when (deleted) {
                    "0" -> {
                        "已拒绝"
                    }
                    "1" -> {
                        "拒绝申请"
                    }
                    "2" -> {
                        "拒绝"
                    }
                    else -> {
                        ""
                    }

                }
            }
            mRightRTextView.apply {
                isEnabled = deleted != "0"
                text = when (deleted) {
                    "0" -> {
                        "通过审核"
                    }
                    "1" -> {
                        "通过审核"
                    }
                    "2" -> {
                        "已审核"
                    }
                    else -> {
                        ""
                    }

                }
            }
        }
    }
}