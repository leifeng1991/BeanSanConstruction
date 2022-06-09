package com.trinidad.beansanconstruction.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.moufans.lib_base.base.activity.BaseActivity
import com.moufans.lib_base.ext.baseReqExecute
import com.moufans.lib_base.utils.LogUtil
import com.moufans.lib_base.utils.ToastUtil
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.api.bean.CostTypeSelectDataBean
import com.trinidad.beansanconstruction.api.param.RequestParamJsonBean
import com.trinidad.beansanconstruction.databinding.ActivityCreateCostTypesBinding
import com.trinidad.beansanconstruction.ext.appApi
import com.trinidad.beansanconstruction.utils.StringUtil
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class CreateCostTypesActivity : BaseActivity<ActivityCreateCostTypesBinding>() {
    private var mCostTypesListDataBean: CostTypeSelectDataBean.RecordsBean? = null
    override fun getDataBindingLayoutResId(): Int {
        return R.layout.activity_create_cost_types
    }

    override fun initView() {
        setHeaderTitle("创建费用类型")
        mCostTypesListDataBean = if (TextUtils.isEmpty(intent.getStringExtra(INTENT_P_DATA)))
            null
        else
            Gson().fromJson(intent.getStringExtra(INTENT_P_DATA), CostTypeSelectDataBean.RecordsBean::class.java)

        if (mCostTypesListDataBean != null) {
            mDataBinding.apply {
                // 项目名称
                mNameCostsRTextView.setText(mCostTypesListDataBean!!.name ?: "")
                when (mCostTypesListDataBean!!.pattern) {
                    "每车" -> {
                        mRRadioButton1.isSelected = true
                    }
                    "每次" -> {
                        mRRadioButton2.isSelected = true
                    }
                    "每小时" -> {
                        mRRadioButton3.isSelected = true
                    }
                    "每平方" -> {
                        mRRadioButton4.isSelected = true
                    }
                    "每立方" -> {
                        mRRadioButton5.isSelected = true
                    }
                    "每分钟" -> {
                        mRRadioButton6.isSelected = true
                    }
                    else -> {
                        mRRadioButton0.isSelected = true
                        mInputUnitEditText.setTextColor(Color.WHITE)
                    }
                }
                mInputUnitEditText.setText(mCostTypesListDataBean!!.pattern ?: "")
                mUnitTextView.text = mCostTypesListDataBean!!.pattern ?: ""
                // 费用单价
                mPriceTextView.setText(mCostTypesListDataBean!!.price ?: "0")
                // 提成方式
                mCutWayRadioGroup.check(if (mCostTypesListDataBean!!.type == "2") R.id.mRRadioButton_8 else R.id.mRRadioButton_7)
                // 单价
                mUnitPriceTextView.text = mCostTypesListDataBean!!.price
                // 提成金额
                mCommissionAmountTextView.text = mCostTypesListDataBean!!.money
                // 比例
                mRatioTextView.text = if (mCostTypesListDataBean!!.number?.contains(".") == true) mCostTypesListDataBean!!.number!!.split(".")[0] else mCostTypesListDataBean!!.number
            }
        }
    }

    override fun initListener() {
        // 保存
        mDataBinding.mSaveTextView.setOnClickListener {
            save()
        }
        mDataBinding.apply {
            mRRadioButton0.setOnClickListener {
                dealChecked(0)
            }
            mRRadioButton1.setOnClickListener {
                dealChecked(1)
            }
            mRRadioButton2.setOnClickListener {
                dealChecked(2)
            }
            mRRadioButton3.setOnClickListener {
                dealChecked(3)
            }
            mRRadioButton4.setOnClickListener {
                dealChecked(4)
            }
            mRRadioButton5.setOnClickListener {
                dealChecked(5)
            }
            mRRadioButton6.setOnClickListener {
                dealChecked(6)
            }
        }
        // 监听单位输入
        mDataBinding.mInputUnitEditText.setOnFocusChangeListener { v, hasFocus ->
            LogUtil.e("==========hasFocus========$hasFocus")
            if (hasFocus) {
                dealChecked(0)
            }
        }
        mDataBinding.mInputUnitEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (mDataBinding.mRRadioButton0.isSelected) {
                    mDataBinding.mUnitTextView.text = s.toString()
                }
            }

        })
        // 监听价格输入
        mDataBinding.mPriceTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val number = if (TextUtils.isEmpty(s.toString())) "0" else s.toString()
                mDataBinding.mUnitPriceTextView.text = StringUtil.saveTwoDecimal(number)
                calculate()
            }

        })
        mDataBinding.mCutWayRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.mRRadioButton_8) {
                mDataBinding.mRatioTextView.text = "0"
                calculate()
            }
        }
        // 减
        mDataBinding.mSubTextView.setOnClickListener {
            mDataBinding.mCutWayRadioGroup.check(R.id.mRRadioButton_7)
            var ratio = mDataBinding.mRatioTextView.text.toString().toInt()
            if (ratio == 0) {
                ToastUtil.showShort("已经到达最小值")
                return@setOnClickListener
            }
            ratio--
            mDataBinding.mRatioTextView.text = "$ratio"
            calculate()
        }
        // 加
        mDataBinding.mAddTextView.setOnClickListener {
            mDataBinding.mCutWayRadioGroup.check(R.id.mRRadioButton_7)
            var ratio = mDataBinding.mRatioTextView.text.toString().toInt()
            if (ratio == 100) {
                ToastUtil.showShort("已经到达最大值")
                return@setOnClickListener
            }
            ratio++
            mDataBinding.mRatioTextView.text = "$ratio"
            calculate()
        }
    }

    override fun processingLogic() {
    }

    private fun dealChecked(type: Int) {
        mDataBinding.apply {
            if (type != 0) {
                mDataBinding.mInputUnitEditText.clearFocus()
            }
            mRRadioButton0.isSelected = type == 0
            mInputUnitEditText.setTextColor(Color.parseColor(if (type == 0) "#ffffff" else "#19CC7E"))
            mRRadioButton1.isSelected = type == 1
            mRRadioButton2.isSelected = type == 2
            mRRadioButton3.isSelected = type == 3
            mRRadioButton4.isSelected = type == 4
            mRRadioButton5.isSelected = type == 5
            mRRadioButton6.isSelected = type == 6

            mDataBinding.mUnitTextView.text = when (type) {
                0 -> {
                    mDataBinding.mInputUnitEditText.text.toString()
                }
                1 -> {
                    "每车"
                }
                2 -> {
                    "每次"
                }
                3 -> {
                    "每小时"
                }
                4 -> {
                    "每平方"
                }
                5 -> {
                    "每立方"
                }
                6 -> {
                    "每分钟"
                }
                else -> {
                    ""
                }
            }
        }
    }

    private fun calculate() {
        // 单价
        var unitPrice = mDataBinding.mUnitPriceTextView.text.toString()
        unitPrice = if (TextUtils.isEmpty(unitPrice)) "0" else unitPrice
        // 比例
        var ratio = mDataBinding.mRatioTextView.text.toString()
        ratio = if (TextUtils.isEmpty(ratio)) "0" else ratio

        mDataBinding.mCommissionAmountTextView.text = StringUtil.saveTwoDecimal(StringUtil.div("${StringUtil.mul(unitPrice, ratio)}", "100", 2))

    }

    private var isRequesting = false

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
            if (priceStr.toDouble() <= 0) {
                ToastUtil.showShort("请输入正确的费用单价")
                return
            }

            if (isRequesting) {
                return
            }

            isRequesting = true

            lifecycleScope.launch {
                val body = Gson().toJson(RequestParamJsonBean().apply {
                    id = mCostTypesListDataBean?.id
                    // 费用名称
                    name = nameCostsStr
                    // 计费单位
                    pattern = unitStr
                    // 费用单价
                    price = StringUtil.saveTwoDecimal(priceStr)
                    // 提成金额
                    money = StringUtil.saveTwoDecimal(mDataBinding.mCommissionAmountTextView.text.toString())
                    // 提成比例
                    number = mDataBinding.mRatioTextView.text.toString()
                    // 1/有提成，2/无提成
                    type = if (mDataBinding.mCutWayRadioGroup.checkedRadioButtonId == R.id.mRRadioButton_7) "1" else "2"
                }).toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
                baseReqExecute({ appApi.costTypeAddOrUpdate(body) }, onSuccess = {
                    lifecycleScope.launch {
                        if (mCostTypesListDataBean == null)
                            ToastUtil.showShort("创建成功")
                        else
                            ToastUtil.showShort("修改成功")

                        isRequesting = false
                        // 关闭页面
                        finish()
                    }
                }, onFailure = { _, _, _ ->
                    isRequesting = false
                }, baseView = this@CreateCostTypesActivity)
            }
        }
    }

    companion object {
        private const val INTENT_P_DATA = "data"

        fun newIntent(context: Context, dataJson: String = ""): Intent {
            return Intent(context, CreateCostTypesActivity::class.java).apply {
                putExtra(INTENT_P_DATA, dataJson)
            }
        }
    }
}