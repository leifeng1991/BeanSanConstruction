package com.trinidad.beansanconstruction.ui.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.moufans.lib_base.base.activity.BaseActivity
import com.moufans.lib_base.ext.baseReqExecute
import com.moufans.lib_base.ext.convertReqExecute
import com.moufans.lib_base.utils.DataStoreHelper
import com.moufans.lib_base.utils.LogUtil
import com.moufans.lib_base.utils.StatusBarUtil
import com.moufans.lib_base.utils.ToastUtil
import com.trinidad.beansanconstruction.MainActivity
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.api.RequestParamJsonBean
import com.trinidad.beansanconstruction.constants.AppConstants
import com.trinidad.beansanconstruction.databinding.ActivityRegisterBinding
import com.trinidad.beansanconstruction.ext.appApi
import com.trinidad.beansanconstruction.utils.CheckUtils
import com.trinidad.beansanconstruction.utils.SharedPrefUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * 注册页
 */
class RegisterActivity : BaseActivity<ActivityRegisterBinding>(), TextWatcher {
    // 公司id
    private val mCompanyId by lazy {
        intent.getStringExtra(INTENT_P_COMPANY_ID)
    }

    // 角色id
    private val mRoleId by lazy {
        intent.getStringExtra(INTENT_P_ROLE_ID)
    }

    override fun getDataBindingLayoutResId(): Int {
        return R.layout.activity_register
    }

    override fun addHeaderView() {

    }

    override fun setStatusBar() {
        StatusBarUtil.setTranslucentDiff(this)
        StatusBarUtil.setColor(this, resources.getColor(com.moufans.lib_base.R.color.white), 0)
        StatusBarUtil.setLightMode(this)
    }

    override fun initView() {
    }

    override fun initListener() {
        mDataBinding.mInputPhoneEditText.addTextChangedListener(this)
        mDataBinding.mInputYzmEditText.addTextChangedListener(this)
        mDataBinding.mInputPasswordEditText.addTextChangedListener(this)
        // 获取验证码
        mDataBinding.mGetYzmRTextView.setOnClickListener {
            getVerificationCode()
        }
        // 注册
        mDataBinding.mRegisterImmediatelyRTextView.setOnClickListener {
            register()
        }
        // 同意协议
        mDataBinding.mAgreeCheckedTextView.setOnClickListener {
            mDataBinding.mAgreeCheckedTextView.isSelected = !mDataBinding.mAgreeCheckedTextView.isSelected
            updateButton()
        }
    }

    override fun processingLogic() {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
        updateButton()
    }

    private fun updateButton() {
        val mPhone = mDataBinding.mInputPhoneEditText.text.toString().trim()
        val yzm = mDataBinding.mInputYzmEditText.text.toString().trim()
        val password = mDataBinding.mInputPasswordEditText.text.toString().trim()
        mDataBinding.mRegisterImmediatelyRTextView.isSelected = !TextUtils.isEmpty(mPhone) && !TextUtils.isEmpty(yzm) && !TextUtils.isEmpty(password) && mDataBinding.mAgreeCheckedTextView.isSelected

    }

    /**
     * 获取验证码
     */
    private fun getVerificationCode() {
        val mPhone = mDataBinding.mInputPhoneEditText.text.toString().trim()
        if (CheckUtils.checkPhoneNumber(mPhone)) {
            lifecycleScope.launch {
                val json = Gson().toJson(RequestParamJsonBean().apply {
                    phone = mPhone
                })
                LogUtil.e("================================$json===")
                baseReqExecute({ appApi.getVerifyCode(RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), json)) }, onSuccess = {
                    lifecycleScope.launch {
                        repeat(61) {
                            if (it == 60) {
                                mDataBinding.mGetYzmRTextView.text = "获取验证码"
                            } else {
                                mDataBinding.mGetYzmRTextView.text = "${60 - it} 秒"
                                delay(1000)
                            }

                        }
                    }
                }, baseView = this@RegisterActivity)
            }

        }

    }

    /**
     * 注册
     */
    private fun register() {
        val mPhone = mDataBinding.mInputPhoneEditText.text.toString().trim()
        val yzm = mDataBinding.mInputYzmEditText.text.toString().trim()
        val mPassword = mDataBinding.mInputPasswordEditText.text.toString().trim()
        if (!CheckUtils.checkInput(mPhone, yzm)) {
            return
        }
        if (!CheckUtils.checkPassWord(mPassword)) {
            return
        }
        if (!mDataBinding.mAgreeCheckedTextView.isSelected) {
            ToastUtil.showShort("请选中同意用户协议")
            return
        }

        val body = Gson().toJson(RequestParamJsonBean().apply {
            username = mPhone
            code = yzm
            password = mPassword
            identityCode = mRoleId
            companyId = mCompanyId
        }).toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        lifecycleScope.launch {
            convertReqExecute({ appApi.logonCode(body) }, onSuccess = {
                lifecycleScope.launch {
                    // 保存token
                    SharedPrefUtil.put(AppConstants.USER_TOKEN, it.token ?: "")
                    // 跳转到首页
                    startActivity(MainActivity.newIntent(this@RegisterActivity))
                    // 关闭页面
                    finish()
                }
            }, baseView = this@RegisterActivity)
        }

    }

    companion object {
        private const val INTENT_P_COMPANY_ID = "companyId"
        private const val INTENT_P_ROLE_ID = "roleId"

        /**
         * @param companyId 公司id
         * @param roleId    角色id
         */
        @JvmOverloads
        fun newIntent(context: Context, companyId: String, roleId: String): Intent {
            return Intent(context, RegisterActivity::class.java).apply {
                putExtra(INTENT_P_COMPANY_ID, companyId)
                putExtra(INTENT_P_ROLE_ID, roleId)
            }
        }
    }


}