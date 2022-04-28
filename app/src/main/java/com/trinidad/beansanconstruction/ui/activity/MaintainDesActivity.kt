package com.trinidad.beansanconstruction.ui.activity

import android.content.Context
import android.content.Intent
import androidx.lifecycle.lifecycleScope
import com.github.gzuliyujiang.wheelpicker.OptionPicker
import com.moufans.lib_base.base.activity.BaseActivity
import com.moufans.lib_base.ext.convertReqExecute
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.api.bean.RepairCostSelectOneDataBean
import com.trinidad.beansanconstruction.databinding.ActivityMaintainDesBinding
import com.trinidad.beansanconstruction.ext.appApi
import kotlinx.coroutines.launch

class MaintainDesActivity : BaseActivity<ActivityMaintainDesBinding>() {
    private val mId by lazy {
        intent.getStringExtra(INTENT_PARAM_ORDER_ID)
    }

    override fun getDataBindingLayoutResId(): Int {
        return R.layout.activity_maintain_des
    }

    override fun initView() {
    }

    override fun initListener() {
    }

    override fun processingLogic() {
        getDesData()
    }

    private fun getDesData() {
        lifecycleScope.launch {
            convertReqExecute({ appApi.repairCostSelectOne(mId ?: "") }, onSuccess = {
                setData(it)
            }, baseView = this@MaintainDesActivity)
        }
    }

    private fun setData(bean: RepairCostSelectOneDataBean) {
        mDataBinding.apply {
            // 店铺名
            mShopNameTextView.text = bean.repairName
            // 负责人
            mPrincipalTextView.text = "负责人：${bean.repairPerson}"
            // 联系电话
            mPhoneTextView.text = bean.repairPhone
            // 维修费用
            mMaintainPriceTextView.text = "¥${bean.money}"
            // 维修日期
            mMaintainDateTextView.text = bean.wxDate
            // 申请时间
            mApplicationDateTextView.text = bean.createTime
            // 申请人
            mProposerTextView.text = bean.aname
            // 审核人
            mAuditorTextView.text = bean.upName
            // 车牌
            mNumberPlateTextView.text = bean.carPlate
            // 维修内容
            mApplyContentTextView.text = bean.content
            // 审核时间
            mAuditTimeTextView.text = bean.upTime
            // 审核状态（0/拒绝，1/审核中，2/通过）
            val deleted = bean.deleted
            // 审核状态
            mAuditStateTextView.text = when (deleted) {
                "0" -> {
                    "已拒绝"
                }
                "1" -> {
                    "审核中"
                }
                "2" -> {
                    "已通过"
                }
                else -> {
                    "--"
                }
            }
        }
    }

    companion object {
        private const val INTENT_PARAM_ORDER_ID = "orderId"

        fun newIntent(context: Context, id: String): Intent {
            return Intent(context, MaintainDesActivity::class.java).apply {
                putExtra(INTENT_PARAM_ORDER_ID, id)
            }
        }
    }
}