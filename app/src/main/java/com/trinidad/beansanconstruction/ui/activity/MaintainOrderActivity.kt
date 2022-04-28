package com.trinidad.beansanconstruction.ui.activity

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.github.gzuliyujiang.wheelpicker.DatePicker
import com.github.gzuliyujiang.wheelpicker.OptionPicker
import com.github.gzuliyujiang.wheelpicker.entity.DateEntity
import com.github.gzuliyujiang.wheelpicker.impl.UnitDateFormatter
import com.github.gzuliyujiang.wheelpicker.widget.DateWheelLayout
import com.google.gson.Gson
import com.moufans.lib_base.base.activity.BaseActivity
import com.moufans.lib_base.ext.convertReqExecute
import com.moufans.lib_base.utils.ToastUtil
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.api.bean.CarInfoSelectDataBean
import com.trinidad.beansanconstruction.api.param.RepairCostAddRequestParamBean
import com.trinidad.beansanconstruction.databinding.ActivityMaintainOrderBinding
import com.trinidad.beansanconstruction.ext.appApi
import com.trinidad.beansanconstruction.utils.StringUtil
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody


class MaintainOrderActivity : BaseActivity<ActivityMaintainOrderBinding>() {
    private var mDatePicker: DatePicker? = null
    private var mCarPlateOptionPicker: OptionPicker? = null
    private var mRepairId = ""
    private var mCid = ""

    override fun getDataBindingLayoutResId(): Int {
        return R.layout.activity_maintain_order
    }

    override fun initView() {
    }

    override fun initListener() {
        mDataBinding.apply {
            // 选择时间
            mSelectTimeRTextView.setOnClickListener {
                if (mDatePicker != null && mDatePicker!!.isShowing) {
                    return@setOnClickListener
                }
                if (mDatePicker == null) {
                    mDatePicker = DatePicker(this@MaintainOrderActivity)
                    val wheelLayout: DateWheelLayout = mDatePicker!!.wheelLayout
                    wheelLayout.setDateLabel("年", "月", "日")
                    wheelLayout.setRange(DateEntity.target(2000, 1, 1), DateEntity.target(2099, 12, 31), DateEntity.today());
                }
                mDatePicker!!.setOnDatePickedListener { year, month, day ->

                    mSelectTimeRTextView.text = "$year-${if (month < 10) "0$month" else month}-${if (day < 10) "0$day" else day}"
                }
                mDatePicker!!.show()
            }
            val mStartActivityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.data != null) {
                    try {
                        val beanStr = RepairShopActivity.getResult(it.data!!)
                        if (!TextUtils.isEmpty(beanStr)) {
                            val bean = Gson().fromJson(beanStr, CarInfoSelectDataBean.RecordsBean::class.java)
                            mRepairId = bean.id ?: ""
                            mSelectRepairShopRTextView.text = bean.name
                        }
                    } catch (e: Exception) {

                    }

                }
            }
            // 选择维修店铺
            mSelectRepairShopRTextView.setOnClickListener {
                mStartActivityForResult.launch(Intent(this@MaintainOrderActivity, RepairShopActivity::class.java))
            }
            // 选择车牌
            mSelectNumberPlateRTextView.setOnClickListener {
                showPicker()
            }
            // 提交
            mSubmittedRTextView.setOnClickListener {
                submit()
            }
        }
    }

    override fun processingLogic() {
    }

    private fun showPicker() {
        if (mCarPlateOptionPicker == null) {
            lifecycleScope.launch {
                convertReqExecute({ appApi.carInfoSelSelect("") }, onSuccess = {
                    mCarPlateOptionPicker = OptionPicker(this@MaintainOrderActivity).apply {
                        setData(it)
                        setOnOptionPickedListener { position, _ ->
                            // 设置选中id
                            mCid = it[position].id ?: ""
                            // 设置車牌
                            mDataBinding.mSelectNumberPlateRTextView.setText(it[position]?.carPlate ?: "")
                        }
                        show()
                    }
                }, baseView = this@MaintainOrderActivity)
            }
        } else {
            if (!mCarPlateOptionPicker!!.isShowing)
                mCarPlateOptionPicker!!.show()
        }

    }

    private fun submit() {
        mDataBinding.apply {
            val timeStr = mSelectTimeRTextView.text.toString()
            if (TextUtils.isEmpty(timeStr)) {
                ToastUtil.showShort("请选择维修时间")
                return
            }
            val repairShopStr = mSelectRepairShopRTextView.text.toString()
            if (TextUtils.isEmpty(repairShopStr)) {
                ToastUtil.showShort("请选择维修店铺")
                return
            }
            val numberPlateStr = mSelectNumberPlateRTextView.text.toString()
            val inputNumberPlateStr = mInputNumberPlateREditText.text.toString()
            if (TextUtils.isEmpty(numberPlateStr) && TextUtils.isEmpty(inputNumberPlateStr)) {
                ToastUtil.showShort("请选择/输入车牌")
                return
            }
            val price = mClockInScopeEditText.text.toString()
            if (TextUtils.isEmpty(price) && price.toDouble() <= 0) {
                ToastUtil.showShort("请输入价格")
                return
            }
            val mContent = mContentTextView.text.toString()

            lifecycleScope.launch {
                val body = Gson().toJson(RepairCostAddRequestParamBean().apply {
                    // 描述
                    content = mContent
                    // 车辆id
                    cid = mCid
                    // 维修店id
                    repairId = mRepairId
                    // 价钱
                    money = StringUtil.saveTwoDecimal(price)
                    // 维修时间
                    wxDate = timeStr
                }).toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
                convertReqExecute({ appApi.repairCostAdd(body) }, onSuccess = {
                    lifecycleScope.launch {
                        ToastUtil.showShort("保存成功")
                        // 关闭页面
                        finish()
                    }
                }, baseView = this@MaintainOrderActivity)
            }
        }
    }

    companion object {

        fun newIntent(context: Context): Intent {
            return Intent(context, MaintainOrderActivity::class.java)
        }
    }
}