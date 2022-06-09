package com.trinidad.beansanconstruction.ui.activity

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.google.gson.Gson
import com.moufans.lib_base.base.activity.BaseActivity
import com.moufans.lib_base.ext.convertReqExecute
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.databinding.ActivityCostTypesBinding
import com.trinidad.beansanconstruction.ext.appApi
import com.trinidad.beansanconstruction.ui.adapter.CostTypesListAdapter
import kotlinx.coroutines.launch

class CostTypesActivity : BaseActivity<ActivityCostTypesBinding>() {
    private val mCostTypesListAdapter by lazy {
        CostTypesListAdapter()
    }

    override fun getDataBindingLayoutResId(): Int {
        return R.layout.activity_cost_types
    }

    override fun initView() {
        setHeaderTitle("费用类型")
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
        // 编辑、选择点击事件
        mCostTypesListAdapter.addChildClickViewIds(R.id.mLeftRTextView, R.id.mRightRTextView)
        mCostTypesListAdapter.setOnItemChildClickListener { _, view, position ->
            if (view.id == R.id.mLeftRTextView) {
                // 跳转到编辑页
                startActivity(CreateCostTypesActivity.newIntent(this, Gson().toJson(mCostTypesListAdapter.data[position])))
            } else if (view.id == R.id.mRightRTextView) {
                // 选择
                setResult(RESULT_OK, Intent().apply { putExtra(INTENT_DATA, Gson().toJson(mCostTypesListAdapter.data[position])) })
                finish()
            }
        }
        // 刷新和加载
        mDataBinding.mRecyclerView.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                getList()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                getList()
            }

        })
        // 新增费用类型
        mDataBinding.mNewFeeTypeTextView.setOnClickListener {
            startActivity(CreateCostTypesActivity.newIntent(this))
        }
    }

    override fun processingLogic() {

    }

    override fun onResume() {
        super.onResume()
        mDataBinding.mRecyclerView.currentPage = 1
        getList()
    }

    private fun getList() {
        val keyWord = mDataBinding.mSearchEditText.text.toString().trim()
        lifecycleScope.launch {
            convertReqExecute({ appApi.costTypeSelect(keyWord, "${mDataBinding.mRecyclerView.currentPage}") }, onSuccess = {
                mDataBinding.mRecyclerView.handlerSuccess(mCostTypesListAdapter, it.records)
            }, onFailure = { _, status, _ ->
                mDataBinding.mRecyclerView.handlerError(mCostTypesListAdapter, status)
            }, baseView = this@CostTypesActivity)
        }
    }

    companion object {
        private const val INTENT_DATA = "data"

        fun newIntent(context: Context): Intent {
            return Intent(context, CostTypesActivity::class.java)
        }

        fun getResult(data: Intent): String {
            return data.getStringExtra(INTENT_DATA) ?: ""
        }
    }
}