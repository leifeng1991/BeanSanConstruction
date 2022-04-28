package com.trinidad.beansanconstruction.ui.activity

import android.content.Context
import android.content.Intent
import com.moufans.lib_base.base.activity.BaseActivity
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.databinding.ActivityMechanicalManagementBinding

class MechanicalManagementActivity : BaseActivity<ActivityMechanicalManagementBinding>() {

    override fun getDataBindingLayoutResId(): Int {
        return R.layout.activity_mechanical_management
    }

    override fun initView() {
    }

    override fun initListener() {
    }

    override fun processingLogic() {
    }

    companion object {

        fun newIntent(context: Context): Intent {
            return Intent(context, MechanicalManagementActivity::class.java)
        }
    }
}