package com.trinidad.beansanconstruction.ui.activity

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.moufans.lib_base.base.activity.BaseActivity
import com.moufans.lib_base.ext.baseReqExecute
import com.moufans.lib_base.ext.convertReqExecute
import com.moufans.lib_base.ext.rawReqExecute
import com.moufans.lib_base.ext.setOnClickListener2
import com.moufans.lib_base.utils.DataStoreHelper
import com.moufans.lib_base.utils.LogUtil
import com.moufans.lib_base.utils.StatusBarUtil
import com.trinidad.beansanconstruction.MainActivity
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.api.RequestParamJsonBean
import com.trinidad.beansanconstruction.constants.AppConstants
import com.trinidad.beansanconstruction.databinding.ActivityLoginBinding
import com.trinidad.beansanconstruction.ext.appApi
import com.trinidad.beansanconstruction.utils.CheckUtils
import com.trinidad.beansanconstruction.utils.SharedPrefUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * 登录页
 */
class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    override fun getDataBindingLayoutResId(): Int {
        return R.layout.activity_login
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
        mDataBinding.apply {
            // 登录方式切换
            mMessageYzmLoginTextView.setOnClickListener2 {
                if ("用短信验证码登录" == mMessageYzmLoginTextView.text.toString()) {
                    mMessageYzmLoginTextView.text = "用密码登录"
                    mInputPasswordLayout.visibility = View.INVISIBLE
                    mForgetPasswordTextView.visibility = View.GONE
                    mInputYzmLayout.visibility = View.VISIBLE
                } else {
                    mMessageYzmLoginTextView.text = "用短信验证码登录"
                    mInputPasswordLayout.visibility = View.VISIBLE
                    mForgetPasswordTextView.visibility = View.VISIBLE
                    mInputYzmLayout.visibility = View.GONE
                }
            }
            // 忘记密码
            mForgetPasswordTextView.setOnClickListener2 {

            }
            // 登录
            mLoginRTextView.setOnClickListener2 {
                login()
            }
            // 获取验证码
            mGetYzmRTextView.setOnClickListener2 {
                getVerificationCode()
            }
            // 注册
            mRegisterTextView.setOnClickListener2 {
                startActivity(JoinCompanyActivity.newIntent(this@LoginActivity))
            }
        }
    }

    override fun processingLogic() {
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
                }, baseView = this@LoginActivity)
            }

        }

    }

    /**
     * 登录
     */
    private fun login() {
        val mPhone = mDataBinding.mInputPhoneEditText.text.toString().trim()
        if (!CheckUtils.checkPhoneNumber(mPhone)) {
            return
        }

        val yzm = mDataBinding.mInputYzmEditText.text.toString().trim()
        val mPassword = mDataBinding.mInputPasswordEditText.text.toString().trim()

        if ("用短信验证码登录" != mDataBinding.mMessageYzmLoginTextView.text.toString()) {
            // 密码登录
            if (!CheckUtils.checkInput(yzm)) {
                return
            }
        } else {
            // 短信验证码登录
            if (!CheckUtils.checkPassWord(mPassword)) {
                return
            }
        }


        val body = Gson().toJson(RequestParamJsonBean().apply {
            username = mPhone
            if ("用短信验证码登录" == mDataBinding.mMessageYzmLoginTextView.text.toString()) {
                password = mPassword
            } else {
                code = yzm
            }

        }).toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        lifecycleScope.launch {
            convertReqExecute({ if ("用短信验证码登录" == mDataBinding.mMessageYzmLoginTextView.text.toString()) appApi.login(body) else appApi.loginCode(body) }, onSuccess = {
                lifecycleScope.launch {
                    // 保存token
                    SharedPrefUtil.put(AppConstants.USER_TOKEN, it.token ?: "")
                    // 跳转到首页
                    startActivity(MainActivity.newIntent(this@LoginActivity))
                    finish()
                }
            }, baseView = this@LoginActivity)
        }

    }

    companion object {

        @JvmOverloads
        fun newIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }
}