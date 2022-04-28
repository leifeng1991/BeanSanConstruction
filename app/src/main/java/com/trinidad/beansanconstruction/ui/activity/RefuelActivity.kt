package com.trinidad.beansanconstruction.ui.activity

import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.github.gzuliyujiang.wheelpicker.OptionPicker
import com.google.gson.Gson
import com.moufans.lib_base.base.activity.BaseActivity
import com.moufans.lib_base.ext.convertReqExecute
import com.moufans.lib_base.ext.setOnClickListener2
import com.moufans.lib_base.utils.ToastUtil
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.api.param.RequestParamJsonBean
import com.trinidad.beansanconstruction.api.bean.OilPriceListBean
import com.trinidad.beansanconstruction.api.bean.SelectOneDataBean
import com.trinidad.beansanconstruction.databinding.ActivityRefuelBinding
import com.trinidad.beansanconstruction.ext.appApi
import com.trinidad.beansanconstruction.utils.DateTimeUtil
import com.trinidad.beansanconstruction.utils.StringUtil
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * 加油
 */
class RefuelActivity : BaseActivity<ActivityRefuelBinding>() {
    // 油价
    private var mOilPriceOptionPicker: OptionPicker? = null

    // 用于重置数据时用
    private var mFirstOilPriceBean: OilPriceListBean? = null

    // 当前选中，用于提交数据
    private var mCheckedOilPriceBean: OilPriceListBean? = null

    // 车牌bean
    private var mSelectOneDataBean: SelectOneDataBean? = null

    // true正在请求
    private var isRequesting = false

    override fun getDataBindingLayoutResId(): Int {
        return R.layout.activity_refuel
    }

    override fun initView() {
        setHeaderTitle("加油")
        mDataBinding.mTimeTextView.text = DateTimeUtil.getCurrentTime()
    }

    override fun initListener() {
        val mStartActivityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.data != null) {
                try {
                    val id = RichScanActivity.getResult(it.data!!)
                    if (!TextUtils.isEmpty(id))
                        selectOne(id)
                } catch (e: Exception) {

                }

            }
        }
        // 油品选择
        mDataBinding.mPickViewImageView.setOnClickListener2 {
            selOilPrice()
        }
        // 扫码
        mDataBinding.mRefuelScanImageView.setOnClickListener2 {
            mStartActivityForResult.launch(Intent(this, RichScanActivity::class.java))
        }
        // 加油数量输入监听
        mDataBinding.mRefuelNumTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                calculate()
            }

        })
        // 重置
        mDataBinding.mResetTextView.setOnClickListener2 {

            if (mFirstOilPriceBean != null) {
                mCheckedOilPriceBean = mFirstOilPriceBean
                mDataBinding.mOilsTextView.text = mFirstOilPriceBean?.provideText()
                mDataBinding.mSetPriceTextView.text = StringUtil.saveTwoDecimal(mFirstOilPriceBean?.money ?: "0.0")
                mDataBinding.mRefuelNumTextView.setText("0")
                calculate()
            } else {
                selOilPrice(false)
            }
            mDataBinding.mTodayRefuelTextView.text = "今日已加油：0   升"
            mSelectOneDataBean = null
            mDataBinding.mLicencePlateTextView.text = ""
        }
        // 重置保存
        mDataBinding.mSaveTextView.setOnClickListener2 {
            oilPriceLogInsert()
        }
    }

    override fun processingLogic() {
        selOilPrice(false)
    }

    /**
     * 油品选择
     */
    private fun selOilPrice(isShow: Boolean = true) {
        lifecycleScope.launch {
            convertReqExecute({ appApi.selOilPrice("") }, onSuccess = {
                if (it.isNotEmpty()) {
                    mFirstOilPriceBean = it[0]
                    mCheckedOilPriceBean = mFirstOilPriceBean
                    mDataBinding.mOilsTextView.text = mFirstOilPriceBean?.provideText()
                    mDataBinding.mSetPriceTextView.text = StringUtil.saveTwoDecimal(mFirstOilPriceBean?.money ?: "0.0")
                    calculate()
                }

                mOilPriceOptionPicker = OptionPicker(this@RefuelActivity).apply {
                    setData(it)
                    setOnOptionPickedListener { position, _ ->
                        val bean = it[position]
                        mDataBinding.mOilsTextView.text = bean.provideText()
                        mDataBinding.mSetPriceTextView.text = StringUtil.saveTwoDecimal(bean.money ?: "0.0")
                        calculate()
                    }
                    if (isShow) {
                        show()
                    }
                }
            }, baseView = this@RefuelActivity)
        }
    }

    /**
     * 计算总价
     */
    private fun calculate() {
        var num = mDataBinding.mRefuelNumTextView.text.toString()
        var price = mDataBinding.mSetPriceTextView.text.toString()
        num = if (TextUtils.isEmpty(num)) "0" else num
        price = if (TextUtils.isEmpty(price)) "0" else price
        mDataBinding.mPriceTextView.text = "¥${StringUtil.saveTwoDecimal(StringUtil.mul(price, num))}"
    }

    /**
     * 扫码查询当前车辆信息
     */
    private fun selectOne(id: String) {
        lifecycleScope.launch {
            convertReqExecute({ appApi.selectOne(id) }, onSuccess = {
                mSelectOneDataBean = it
                mDataBinding.mLicencePlateTextView.text = it.carPlate ?: ""
                mDataBinding.mTodayRefuelTextView.text = "今日已加油：${it.number}   升"
            }, baseView = this@RefuelActivity)
        }
    }

    private fun oilPriceLogInsert() {
        if (mCheckedOilPriceBean == null) {
            ToastUtil.showShort("请选择油品")
            return
        }
        if (mSelectOneDataBean == null) {
            ToastUtil.showShort("请扫码获取车牌号")
            return
        }
        val mNum = mDataBinding.mRefuelNumTextView.text.toString()
        if (TextUtils.isEmpty(mNum) || mNum.toDouble() <= 0) {
            ToastUtil.showShort("加油数量输入不合理")
            return
        }

        isRequesting = true
        lifecycleScope.launch {
            val body = Gson().toJson(RequestParamJsonBean().apply {
                // 油品质
                quality = mCheckedOilPriceBean?.quality
                // 油价
                price = mCheckedOilPriceBean?.money
                // 本次加油金额
                money = StringUtil.saveTwoDecimal(StringUtil.mul(mCheckedOilPriceBean?.money ?: "0", mNum))
                // 加油量
                number = mNum
                // 车辆id
                carId = mSelectOneDataBean?.id
            }).toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            convertReqExecute({ appApi.oilPriceLogInsert(body) }, onSuccess = {
                lifecycleScope.launch {
                    ToastUtil.showShort("保存成功")
                    isRequesting = false
                    // 关闭页面
                    finish()
                }
            }, onFailure = { _, _, _ -> isRequesting = false }, baseView = this@RefuelActivity)
        }
    }

    companion object {

        @JvmOverloads
        fun newIntent(context: Context): Intent {
            return Intent(context, RefuelActivity::class.java)
        }
    }
}