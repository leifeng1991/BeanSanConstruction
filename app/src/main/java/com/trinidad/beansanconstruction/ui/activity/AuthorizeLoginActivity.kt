package com.trinidad.beansanconstruction.ui.activity

import android.content.Context
import android.content.Intent
import androidx.lifecycle.lifecycleScope
import com.moufans.lib_base.base.activity.BaseActivity
import com.moufans.lib_base.ext.baseReqExecute
import com.moufans.lib_base.ext.convertReqExecute
import com.moufans.lib_base.ext.setOnClickListener2
import com.moufans.lib_base.utils.ToastUtil
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.constants.AppConstants
import com.trinidad.beansanconstruction.databinding.ActivityAuthorizeLoginBinding
import com.trinidad.beansanconstruction.ext.appApi
import com.trinidad.beansanconstruction.utils.SharedPrefUtil
import kotlinx.coroutines.launch

/**
 * 授权登录
 */
class AuthorizeLoginActivity : BaseActivity<ActivityAuthorizeLoginBinding>() {
    private val key by lazy {
        intent.getStringExtra(INTENT_P_KEY)
    }

    override fun getDataBindingLayoutResId(): Int {
        return R.layout.activity_authorize_login
    }

    override fun initView() {

    }

    override fun initListener() {
        mDataBinding.mAuthorizeLoginTextView.setOnClickListener2 {
            login()
        }
    }

    override fun processingLogic() {
    }

    private fun login() {
        lifecycleScope.launch {
            convertReqExecute({ appApi.postKey(key ?: "") }, onSuccess = {
                ToastUtil.showShort("授权成功")
                finish()
            }, baseView = this@AuthorizeLoginActivity)
        }
    }

    companion object {
        private const val INTENT_P_KEY = "key"

        @JvmOverloads
        fun newIntent(context: Context, key: String): Intent {
            return Intent(context, AuthorizeLoginActivity::class.java).apply {
                putExtra(INTENT_P_KEY, key)
            }
        }
    }
}