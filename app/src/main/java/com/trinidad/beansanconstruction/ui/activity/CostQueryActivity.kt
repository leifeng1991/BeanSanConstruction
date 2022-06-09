package com.trinidad.beansanconstruction.ui.activity

import android.content.Context
import android.content.Intent
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.moufans.lib_base.base.activity.BaseActivity
import com.moufans.lib_base.ext.convertReqExecute
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.databinding.ActivityCostQueryBinding
import com.trinidad.beansanconstruction.ext.appApi
import com.trinidad.beansanconstruction.ui.adapter.CostQueryListAdapter
import kotlinx.coroutines.launch

class CostQueryActivity : BaseActivity<ActivityCostQueryBinding>() {
    private val mCostQueryListAdapter by lazy {
        CostQueryListAdapter()
    }

    override fun getDataBindingLayoutResId(): Int {
        return R.layout.activity_cost_query
    }

    override fun initView() {
        setHeaderTitle("费用查询")
        mDataBinding.mRecyclerView.apply {
            setPullRefreshAndLoadingMoreEnabled(true, loadingMoreEnabled = true)
            loadSize = 10
            setLayoutManager(LinearLayoutManager(this@CostQueryActivity))
            setAdapter(mCostQueryListAdapter)
        }
    }

    override fun initListener() {
        // 搜索
        mDataBinding.mSearchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                mDataBinding.mRecyclerView.currentPage = 1
                getList()
                true
            }
            false
        }
        mCostQueryListAdapter.addChildClickViewIds(R.id.mCheckRTextView)
        mCostQueryListAdapter.setOnItemChildClickListener { _, _, position ->
            startActivity(CostQueryDesActivity.newIntent(this, mCostQueryListAdapter.data[position].id ?: ""))
        }
        mDataBinding.mRecyclerView.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                getList()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                getList()
            }

        })
    }

    override fun processingLogic() {
        getList()
    }

    private fun getList() {
        val keyWord = mDataBinding.mSearchEditText.text.toString().trim()
        lifecycleScope.launch {
            convertReqExecute({ appApi.carProjectSelect(keyWord, "${mDataBinding.mRecyclerView.currentPage}") }, onSuccess = {
                mDataBinding.mRecyclerView.handlerSuccess(mCostQueryListAdapter, it.records)
            }, onFailure = { _, status, _ ->
                mDataBinding.mRecyclerView.handlerError(mCostQueryListAdapter, status)
            }, baseView = this@CostQueryActivity)
        }
    }

    companion object {

        fun newIntent(context: Context): Intent {
            return Intent(context, CostQueryActivity::class.java)
        }
    }
}