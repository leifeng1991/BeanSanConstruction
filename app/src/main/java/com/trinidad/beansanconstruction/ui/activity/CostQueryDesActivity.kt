package com.trinidad.beansanconstruction.ui.activity

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.moufans.lib_base.base.activity.BaseActivity
import com.moufans.lib_base.ext.convertReqExecute
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.api.bean.CarProjectSelectOneDataBean
import com.trinidad.beansanconstruction.databinding.ActivityCostQueryDesBinding
import com.trinidad.beansanconstruction.ext.appApi
import kotlinx.coroutines.launch

class CostQueryDesActivity : BaseActivity<ActivityCostQueryDesBinding>() {
    private val mId by lazy {
        intent.getStringExtra(INTENT_PARAM_ID)
    }

    override fun getDataBindingLayoutResId(): Int {
        return R.layout.activity_cost_query_des
    }

    override fun initView() {
        setHeaderTitle("详情")
    }

    override fun initListener() {
    }

    override fun processingLogic() {
        getDesData()
    }

    private fun getDesData() {
        lifecycleScope.launch {
            convertReqExecute({ appApi.carProjectSelectOne(mId ?: "") }, onSuccess = {
                setData(it)
            }, baseView = this@CostQueryDesActivity)
        }
    }

    private fun setData(bean: CarProjectSelectOneDataBean) {
        mDataBinding.apply {
            // 项目名称
            mProjectNameTextView.text = bean.pname
            // 任务日期
            mTaskDateTextView.text = bean.period
            // 班次（1/白班，2/夜班）
            mDayShiftRRadioButton.isChecked = bean.clas == "1"
            mNightShiftRRadioButton.isChecked = bean.clas != "2"
            mDayShiftRRadioButton.isClickable = false
            mNightShiftRRadioButton.isClickable = false
            // 费用类型名称
            mSprinklerStationShiftFeeTextView.text = bean.ctname
            // 数量
            mNumberTextView.text = bean.number
            // 费用单价
            mPriceTextView.text = "¥${bean.unitPrice}"
            // 单位
            mUnitTextView.text = "元/${bean.pattern}"
            // 费用小计
            mCostTotalTextView.text = "¥${bean.sumPrice}"
            mPermanentStaffTextView.isSelected = !TextUtils.isEmpty(bean.aid)
            mPermanentStaffView.isSelected = !TextUtils.isEmpty(bean.aid)
            mTemporaryWorkerTextView.isSelected = TextUtils.isEmpty(bean.aid)
            mTemporaryWorkerView.isSelected = TextUtils.isEmpty(bean.aid)
            mSelectedNameLayout.visibility = if (!TextUtils.isEmpty(bean.aid)) View.VISIBLE else View.GONE
            mInputNameEditText.visibility = if (TextUtils.isEmpty(bean.aid)) View.VISIBLE else View.GONE
            mSelectedNameTextView.text = bean.aname
            mInputNameEditText.text = bean.aname
            // 车牌
            mPlateNumberTextView.text = if (TextUtils.isEmpty(bean.carPlate)) "无" else bean.carPlate
            // 车辆编号
            mCarIDTextView.text = if (TextUtils.isEmpty(bean.carNumber)) "无" else bean.carNumber
            mInputWorkEditText.text = bean.content

        }
    }

    companion object {
        private const val INTENT_PARAM_ID = "id"

        fun newIntent(context: Context, id: String): Intent {
            return Intent(context, CostQueryDesActivity::class.java).apply {
                putExtra(INTENT_PARAM_ID, id)
            }
        }
    }
}