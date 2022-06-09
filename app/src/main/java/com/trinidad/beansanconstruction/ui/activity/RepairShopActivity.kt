package com.trinidad.beansanconstruction.ui.activity

import android.content.Intent
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.moufans.lib_base.base.activity.BaseActivity
import com.moufans.lib_base.ext.convertReqExecute
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.databinding.ActivityRepairShopBinding
import com.trinidad.beansanconstruction.ext.appApi
import com.trinidad.beansanconstruction.ui.adapter.RepairShopListAdapter
import kotlinx.coroutines.launch

class RepairShopActivity : BaseActivity<ActivityRepairShopBinding>() {
    private val mRepairShopListAdapter by lazy {
        RepairShopListAdapter()
    }

    override fun getDataBindingLayoutResId(): Int {
        return R.layout.activity_repair_shop
    }

    override fun initView() {
        setHeaderTitle("维修店")
        mDataBinding.mRecyclerView.apply {
            setPullRefreshAndLoadingMoreEnabled(true, loadingMoreEnabled = true)
            setLayoutManager(LinearLayoutManager(this@RepairShopActivity))
            setAdapter(mRepairShopListAdapter)
        }
    }

    override fun initListener() {
        // 搜索
        mDataBinding.mSearchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                mDataBinding.mRecyclerView.currentPage = 1
                carInfoSelect()
                true
            }
            false
        }
        mRepairShopListAdapter.addChildClickViewIds(R.id.mChooseRTextView)
        mRepairShopListAdapter.setOnItemChildClickListener { _, _, position ->
            setResult(RESULT_OK, Intent().apply { putExtra(INTENT_DATA, Gson().toJson(mRepairShopListAdapter.data[position])) })
            finish()
        }
        mDataBinding.mRecyclerView.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                carInfoSelect()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                carInfoSelect()
            }

        })
    }

    override fun processingLogic() {
        carInfoSelect()
    }

    private fun carInfoSelect() {
        val keyWord = mDataBinding.mSearchEditText.text.toString().trim()
        lifecycleScope.launch {
            convertReqExecute({ appApi.repairSelect(keyWord, "${mDataBinding.mRecyclerView.currentPage}") }, onSuccess = {
                mRepairShopListAdapter.setList(it.records)
            }, baseView = this@RepairShopActivity)
        }
    }

    companion object {
        private const val INTENT_DATA = "data"

        fun getResult(data: Intent): String {
            return data.getStringExtra(INTENT_DATA) ?: ""
        }
    }
}