package com.trinidad.beansanconstruction.ui.activity

import android.content.Context
import android.content.Intent
import com.moufans.lib_base.base.activity.BaseActivity
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.databinding.ActivitySettingBinding

class SettingActivity : BaseActivity<ActivitySettingBinding>() {
    private val mPhone by lazy {
        intent.getStringExtra(INTENT_P_PHONE)
    }

    override fun getDataBindingLayoutResId(): Int {
        return R.layout.activity_setting
    }

    override fun initView() {
        setHeaderTitle("设置")
        mDataBinding.mChangePhoneTextView.text = mPhone
    }

    override fun initListener() {
    }

    override fun processingLogic() {
    }

    companion object {
        private const val INTENT_P_PHONE = "phone"

        @JvmOverloads
        fun newIntent(context: Context, phone: String): Intent {
            return Intent(context, SettingActivity::class.java).apply {
                putExtra(INTENT_P_PHONE, phone)
            }
        }
    }
}