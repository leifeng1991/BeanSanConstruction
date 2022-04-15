package com.trinidad.beansanconstruction.ui.activity

import android.content.Context
import android.content.Intent
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.moufans.lib_base.base.activity.BaseActivity
import com.moufans.lib_base.ext.convertReqExecute
import com.moufans.lib_base.utils.StatusBarUtil
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.databinding.ActivitySelectRoleBinding
import com.trinidad.beansanconstruction.ext.appApi
import com.trinidad.beansanconstruction.ui.adapter.RoleListAdapter
import kotlinx.coroutines.launch

/**
 * 选择角色
 */
class SelectRoleActivity : BaseActivity<ActivitySelectRoleBinding>() {
    private val mRoleListAdapter by lazy {
        RoleListAdapter()
    }

    override fun getDataBindingLayoutResId(): Int {
        return R.layout.activity_select_role
    }

    override fun addHeaderView() {

    }

    override fun setStatusBar() {
        StatusBarUtil.setTranslucentDiff(this)
        StatusBarUtil.setColor(this, resources.getColor(com.moufans.lib_base.R.color.white), 0)
        StatusBarUtil.setLightMode(this)
    }

    override fun initView() {
        mDataBinding.mRoleRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@SelectRoleActivity)
            adapter = mRoleListAdapter
        }
    }

    override fun initListener() {
        mRoleListAdapter.setOnItemClickListener { _, _, position ->
            val id = mRoleListAdapter.data[position].code
            mRoleListAdapter.checkedId = id ?: ""
            mRoleListAdapter.notifyDataSetChanged()
        }
        // 下一步
        mDataBinding.mNextStepRTextView.setOnClickListener {
            // 跳转到注册界面
            startActivity(RegisterActivity.newIntent(this, intent.getStringExtra(INTENT_P_COMPANY_ID) ?: "", mRoleListAdapter.checkedId ?: ""))
        }
    }

    override fun processingLogic() {
        lifecycleScope.launch {
            convertReqExecute({ appApi.selIdentity() }, onSuccess = {
                if (it.isNotEmpty())
                    mRoleListAdapter.checkedId = it[0].code
                mRoleListAdapter.setList(it)
            }, baseView = this@SelectRoleActivity)
        }
    }

    companion object {
        private const val INTENT_P_COMPANY_ID = "companyId"

        /**
         * @param companyId 公司id
         */
        @JvmOverloads
        fun newIntent(context: Context, companyId: String): Intent {
            return Intent(context, SelectRoleActivity::class.java).apply {
                putExtra(INTENT_P_COMPANY_ID, companyId)
            }
        }
    }
}