package com.trinidad.beansanconstruction.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.moufans.lib_base.base.adapter.BaseAdapter
import com.moufans.lib_base.utils.span.AndroidSpan
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.api.bean.CarInfoSelectDataBean
import com.trinidad.beansanconstruction.api.bean.ProjectVehicleDataBean
import com.trinidad.beansanconstruction.databinding.ItemChooseCarListBinding
import com.trinidad.beansanconstruction.databinding.ItemProjectOperationListBinding

class ProjectOperationListAdapter(private val type: Int = 1, layoutResId: Int = R.layout.item_project_operation_list, data: MutableList<ProjectVehicleDataBean.RecordsBean> = mutableListOf()) : BaseAdapter<ProjectVehicleDataBean.RecordsBean, ItemProjectOperationListBinding>(layoutResId, data) {

    @SuppressLint("SetTextI18n")
    override fun convert(holder: BaseDataBindingHolder<ItemProjectOperationListBinding>, position: Int, item: ProjectVehicleDataBean.RecordsBean) {
        holder.dataBinding?.apply {
            mTopSpace.visibility = if (position == 0) View.VISIBLE else View.GONE
            mProjectNameTextView.text = "${if (type == 1) item.name else "车牌：${item.carPlate}"}"
            // 产值
            mOutputTextView.text = "¥${item.outputValue}"
            // 费用支付
            mPaymentTextView.text = "¥${item.expenditure}"
            // 利润
            mProfitTextView.text = "¥${item.profit}"
            mReplaceTheProjectImageView.visibility = View.GONE
            mBottomTitleTextView.visibility = View.GONE
            mSwitchTextView.text = "详情"
        }
    }

    private fun setMyText(textView: TextView, title: String, content: String) {
        textView.text = AndroidSpan().drawCommonSpan(title).drawForegroundColor(content, Color.parseColor("#ff333333")).spanText
    }
}