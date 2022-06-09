package com.trinidad.beansanconstruction.ui.activity

import android.content.Context
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
import com.trinidad.beansanconstruction.databinding.ActivityChooseCarBinding
import com.trinidad.beansanconstruction.ext.appApi
import com.trinidad.beansanconstruction.ui.adapter.ChooseCarListAdapter
import kotlinx.coroutines.launch

class ChooseCarActivity : BaseActivity<ActivityChooseCarBinding>() {
    private val mChooseCarListAdapter by lazy {
        ChooseCarListAdapter()
    }

    override fun getDataBindingLayoutResId(): Int {
        return R.layout.activity_choose_car
    }

    override fun initView() {
        setHeaderTitle("车辆")
        mDataBinding.mRecyclerView.apply {
            setPullRefreshAndLoadingMoreEnabled(true, loadingMoreEnabled = true)
            loadSize = 10
            setLayoutManager(LinearLayoutManager(this@ChooseCarActivity))
            setAdapter(mChooseCarListAdapter)
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
        mChooseCarListAdapter.addChildClickViewIds(R.id.mChooseRTextView)
        mChooseCarListAdapter.setOnItemChildClickListener { _, _, position ->
            setResult(RESULT_OK, Intent().apply { putExtra(INTENT_DATA, Gson().toJson(mChooseCarListAdapter.data[position])) })
            finish()
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
            convertReqExecute({ appApi.carInfoSelect(keyWord, "${mDataBinding.mRecyclerView.currentPage}") }, onSuccess = {
                mDataBinding.mRecyclerView.handlerSuccess(mChooseCarListAdapter, it.records)
            }, onFailure = { _, status, _ ->
                mDataBinding.mRecyclerView.handlerError(mChooseCarListAdapter, status)
            }, baseView = this@ChooseCarActivity)
        }
    }

    companion object {
        private const val INTENT_DATA = "data"

        fun newIntent(context: Context): Intent {
            return Intent(context, ChooseCarActivity::class.java)
        }

        fun getResult(data: Intent): String {
            return data.getStringExtra(INTENT_DATA) ?: ""
        }
    }


}