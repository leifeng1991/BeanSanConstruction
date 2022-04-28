package com.trinidad.beansanconstruction.ui.activity

import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.moufans.lib_base.base.activity.BaseActivity
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.databinding.ActivityCostTypesBinding
import com.trinidad.beansanconstruction.ui.adapter.CostTypesListAdapter

class CostTypesActivity : BaseActivity<ActivityCostTypesBinding>() {
    private val mCostTypesListAdapter by lazy {
        CostTypesListAdapter()
    }

    override fun getDataBindingLayoutResId(): Int {
        return R.layout.activity_cost_types
    }

    override fun initView() {
        mDataBinding.mRecyclerView.apply {
            setPullRefreshAndLoadingMoreEnabled(true, loadingMoreEnabled = true)
            setLayoutManager(LinearLayoutManager(this@CostTypesActivity))
            setAdapter(mCostTypesListAdapter)
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
        mCostTypesListAdapter.addChildClickViewIds(R.id.mLeftRTextView, R.id.mRightRTextView)
        mCostTypesListAdapter.setOnItemChildClickListener { adapter, view, position -> }
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

    }
}