package com.trinidad.beansanconstruction.ui.activity

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.github.gzuliyujiang.wheelpicker.OptionPicker
import com.google.gson.Gson
import com.moufans.lib_base.base.activity.BaseActivity
import com.moufans.lib_base.ext.convertReqExecute
import com.moufans.lib_base.ext.setOnClickListener2
import com.moufans.lib_base.utils.ToastUtil
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.api.RequestParamJsonBean
import com.trinidad.beansanconstruction.api.bean.OilPriceListBean
import com.trinidad.beansanconstruction.databinding.ActivitySetOilPriceBinding
import com.trinidad.beansanconstruction.ext.appApi
import com.trinidad.beansanconstruction.utils.DateTimeUtil
import com.trinidad.beansanconstruction.utils.StringUtil
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.*

class SetOilPriceActivity : BaseActivity<ActivitySetOilPriceBinding>() {
    // 油价
    private var mOilPriceOptionPicker: OptionPicker? = null
    private var mFirstOilPriceBean: OilPriceListBean? = null
    private var mCheckedOilPriceBean: OilPriceListBean? = null
    private var isRequesting = false

    override fun getDataBindingLayoutResId(): Int {
        return R.layout.activity_set_oil_price
    }

    override fun initView() {
        setHeaderTitle("油价设定")
        mDataBinding.mTimeTextView.text = DateTimeUtil.getCurrentTime()
    }

    override fun initListener() {
//        mDataBinding.mTimeTextView.setOnClickListener2 {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                DatePickerDialog(this@SetOilPriceActivity, 0, { _, year, month, dayOfMonth ->
//                    var m = month + 1
//                    val mStr = if (m < 10) "0$m" else m
//                    mDataBinding.mTimeTextView.text = "$year-$mStr-$dayOfMonth"
//                }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).show()
//            }
//        }
        // 选择油品
        mDataBinding.mOilsTextView.setOnClickListener2 {
            selOilPrice()
        }
        // 重置
        mDataBinding.mResetTextView.setOnClickListener2 {
            mDataBinding.mTimeTextView.text = DateTimeUtil.getCurrentTime()
            if (mFirstOilPriceBean != null) {
                mCheckedOilPriceBean = mFirstOilPriceBean
                mDataBinding.mOilsTextView.text = mFirstOilPriceBean?.provideText()
                mDataBinding.mCurrentPriceTextView.text = StringUtil.saveTwoDecimal(mFirstOilPriceBean?.money ?: "0.0")
                mDataBinding.mSetPriceTextView.setText("0")
                mDataBinding.mTimeTextView.text = mFirstOilPriceBean?.createTime
            } else {
                selOilPrice(false)
            }
        }
        // 保存
        mDataBinding.mSaveTextView.setOnClickListener2 {
            updateOilPrice()
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
                    mDataBinding.mOilsTextView.text = mFirstOilPriceBean?.quality ?: ""
                    mDataBinding.mCurrentPriceTextView.text = StringUtil.saveTwoDecimal(mFirstOilPriceBean?.money ?: "0")
                    mDataBinding.mTimeTextView.text = mFirstOilPriceBean?.createTime
                }

                mOilPriceOptionPicker = OptionPicker(this@SetOilPriceActivity).apply {
                    setData(it)
                    setOnOptionPickedListener { position, _ ->
                        mCheckedOilPriceBean = it[position]
                        mDataBinding.mOilsTextView.text = mCheckedOilPriceBean?.provideText()
                        mDataBinding.mCurrentPriceTextView.text = StringUtil.saveTwoDecimal(mCheckedOilPriceBean?.money ?: "0")
                    }
                    if (isShow) {
                        show()
                    }
                }
            }, baseView = this@SetOilPriceActivity)
        }
    }

    private fun updateOilPrice() {
        if (mCheckedOilPriceBean == null) {
            ToastUtil.showShort("请选择油品")
            return
        }
        val price = StringUtil.saveTwoDecimal(mDataBinding.mSetPriceTextView.text.toString())
        if (price.toDouble() <= 0) {
            ToastUtil.showShort("价格设置不合理")
            return
        }
        isRequesting = true
        lifecycleScope.launch {
            val body = Gson().toJson(RequestParamJsonBean().apply {
                // 油品id
                id = mCheckedOilPriceBean?.id
                // 价格
                money = price
            }).toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            convertReqExecute({ appApi.updateOilPrice(body) }, onSuccess = {
                lifecycleScope.launch {
                    ToastUtil.showShort("修改成功")
                    isRequesting = false
                    // 关闭页面
                    finish()
                }
            }, onFailure = { _, _, _ -> isRequesting = false }, baseView = this@SetOilPriceActivity)
        }
    }

    companion object {

        @JvmOverloads
        fun newIntent(context: Context): Intent {
            return Intent(context, SetOilPriceActivity::class.java)
        }
    }

}