package com.trinidad.beansanconstruction.ui.activity

import android.content.Context
import android.content.Intent
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.moufans.lib_base.base.activity.BaseActivity
import com.moufans.lib_base.ext.convertReqExecute
import com.moufans.lib_base.utils.LogUtil
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.databinding.ActivityJoinCompanyBinding
import com.trinidad.beansanconstruction.ext.appApi
import com.trinidad.beansanconstruction.ui.adapter.JoinCompanyAdapter
import kotlinx.coroutines.launch

/**
 * 加入公司
 */
class JoinCompanyActivity : BaseActivity<ActivityJoinCompanyBinding>() {
    private val mJoinCompanyAdapter by lazy {
        JoinCompanyAdapter()
    }

    override fun getDataBindingLayoutResId(): Int {
        return R.layout.activity_join_company
    }

    override fun initView() {
        setHeaderTitle("加入公司")
        mDataBinding.mCompanyRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@JoinCompanyActivity)
            adapter = mJoinCompanyAdapter
        }

    }

    override fun initListener() {
        mDataBinding.mSearchCompanyEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchCompany()
                true
            }
            false
        }

        // 加入公司点击事件
        mJoinCompanyAdapter.addChildClickViewIds(R.id.mApplyJoinTextView)
        mJoinCompanyAdapter.setOnItemChildClickListener { _, view, position ->
            val item = mJoinCompanyAdapter.data[position]
            if (view.id == R.id.mApplyJoinTextView) {
                // 申请加入
                startActivity(SelectRoleActivity.newIntent(this, item.id ?: ""))
            }
        }
    }

    override fun processingLogic() {
        searchCompany()
    }

    private fun searchCompany() {
        val keyWord = mDataBinding.mSearchCompanyEditText.text.toString().trim()
        lifecycleScope.launch {
            convertReqExecute({ appApi.selCompany(keyWord) }, onSuccess = {
                mJoinCompanyAdapter.setList(it)
            }, baseView = this@JoinCompanyActivity)
        }
    }

    companion object {

        @JvmOverloads
        fun newIntent(context: Context): Intent {
            return Intent(context, JoinCompanyActivity::class.java)
        }
    }
}