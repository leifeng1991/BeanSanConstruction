package com.trinidad.beansanconstruction.ui.activity

import android.content.Context
import android.content.Intent
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.moufans.lib_base.base.activity.BaseActivity
import com.moufans.lib_base.ext.baseReqExecute
import com.moufans.lib_base.ext.setOnClickListener2
import com.moufans.lib_base.utils.ToastUtil
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.api.param.RequestParamJsonBean
import com.trinidad.beansanconstruction.databinding.ActivityChangePasswordBinding
import com.trinidad.beansanconstruction.ext.appApi
import com.trinidad.beansanconstruction.utils.CheckUtils
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * 修改密码
 */
class ChangePasswordActivity : BaseActivity<ActivityChangePasswordBinding>() {

    override fun getDataBindingLayoutResId(): Int {
        return R.layout.activity_change_password
    }

    override fun initView() {
        setHeaderTitle("修改密码")
    }

    override fun initListener() {
        mDataBinding.mSubmitTextView.setOnClickListener2 {
            changePassword()
        }
    }

    override fun processingLogic() {
    }

    private fun changePassword() {
        val mFirstPassword = mDataBinding.mFirstPasswordEditText.text.toString()
        val mSecondPassword = mDataBinding.mSecondPasswordEditText.text.toString()
        if (!CheckUtils.checkPassWord(mFirstPassword) || !CheckUtils.checkPassWord(mSecondPassword)) {
            return
        }

        if (mFirstPassword != mSecondPassword) {
            ToastUtil.showShort("两次密码输入不一致")
            return
        }

        val body = Gson().toJson(RequestParamJsonBean().apply {
            password = mFirstPassword
        }).toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        lifecycleScope.launch {
            baseReqExecute({ appApi.upPass(body) }, onSuccess = {
            }, onFailure = { _, status, _ ->
                if (status == -205) {
                    ToastUtil.showShort("密码修改成功")
                    finish()
                }
            }, baseView = this@ChangePasswordActivity)
        }
    }

    companion object {

        @JvmOverloads
        fun newIntent(context: Context): Intent {
            return Intent(context, ChangePasswordActivity::class.java)
        }
    }
}