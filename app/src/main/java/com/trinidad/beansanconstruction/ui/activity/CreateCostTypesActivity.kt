package com.trinidad.beansanconstruction.ui.activity

import android.text.TextUtils
import com.moufans.lib_base.base.activity.BaseActivity
import com.moufans.lib_base.utils.ToastUtil
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.databinding.ActivityCreateCostTypesBinding

class CreateCostTypesActivity : BaseActivity<ActivityCreateCostTypesBinding>() {

    override fun getDataBindingLayoutResId(): Int {
        return R.layout.activity_create_cost_types
    }

    override fun initView() {
    }

    override fun initListener() {
        // 保存
        mDataBinding.mSaveTextView.setOnClickListener {
            save()
        }
        mDataBinding.mRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            mDataBinding.mUnitTextView.text = when (checkedId) {
                R.id.mRRadioButton_0 -> {
                    mDataBinding.mInputUnitEditText.text.toString()
                }
                R.id.mRRadioButton_1 -> {
                    "每车"
                }
                R.id.mRRadioButton_2 -> {
                    "每次"
                }
                R.id.mRRadioButton_3 -> {
                    "每小时"
                }
                R.id.mRRadioButton_4 -> {
                    "每平方"
                }
                R.id.mRRadioButton_5 -> {
                    "每立方"
                }
                R.id.mRRadioButton_6 -> {
                    "每分钟"
                }
                else -> {
                    ""
                }
            }
        }
    }

    override fun processingLogic() {
    }

    private fun save() {
        mDataBinding.apply {
            val nameCostsStr = mNameCostsRTextView.text.toString()
            if (TextUtils.isEmpty(nameCostsStr)) {
                ToastUtil.showShort("请输入费用名称")
                return
            }
            val unitStr = mDataBinding.mUnitTextView.text.toString()
            if (TextUtils.isEmpty(unitStr)) {
                ToastUtil.showShort("请选择计费单位")
                return
            }
            val priceStr = mPriceTextView.text.toString()
            if (TextUtils.isEmpty(priceStr)) {
                ToastUtil.showShort("请输入费用单价")
                return
            }

        }
    }
}