package com.trinidad.beansanconstruction.ui.activity

import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.LinearLayoutManager
import com.moufans.lib_base.base.activity.BaseActivity
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.databinding.ActivityChooseCarBinding
import com.trinidad.beansanconstruction.ui.adapter.ChooseCarListAdapter

class ChooseCarActivity : BaseActivity<ActivityChooseCarBinding>() {
    private val mChooseCarListAdapter by lazy {
        ChooseCarListAdapter()
    }

    override fun getDataBindingLayoutResId(): Int {
        return R.layout.activity_choose_car
    }

    override fun initView() {
        mDataBinding.mRecyclerView.apply {
            setPullRefreshAndLoadingMoreEnabled(true, loadingMoreEnabled = true)
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

    }


}