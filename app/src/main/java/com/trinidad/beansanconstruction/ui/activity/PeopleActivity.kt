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
import com.trinidad.beansanconstruction.databinding.ActivityPeopleBinding
import com.trinidad.beansanconstruction.ext.appApi
import com.trinidad.beansanconstruction.ui.adapter.PeopleListAdapter
import kotlinx.coroutines.launch

class PeopleActivity : BaseActivity<ActivityPeopleBinding>() {
    private val mPeopleListAdapter by lazy {
        PeopleListAdapter()
    }

    override fun getDataBindingLayoutResId(): Int {
        return R.layout.activity_people
    }

    override fun initView() {
        setHeaderTitle("人员")
        mDataBinding.mRecyclerView.apply {
            setPullRefreshAndLoadingMoreEnabled(true, loadingMoreEnabled = true)
            setLayoutManager(LinearLayoutManager(this@PeopleActivity))
            setAdapter(mPeopleListAdapter)
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
        mPeopleListAdapter.addChildClickViewIds(R.id.mChooseRTextView)
        mPeopleListAdapter.setOnItemChildClickListener { _, _, position ->
            setResult(RESULT_OK, Intent().apply { putExtra(INTENT_DATA, Gson().toJson(mPeopleListAdapter.data[position])) })
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
            convertReqExecute({ appApi.userSelect(keyWord, "${mDataBinding.mRecyclerView.currentPage}") }, onSuccess = {
                mDataBinding.mRecyclerView.handlerSuccess(mPeopleListAdapter, it.records)
            }, onFailure = { _, status, _ ->
                mDataBinding.mRecyclerView.handlerError(mPeopleListAdapter, status)
            }, baseView = this@PeopleActivity)
        }
    }

    companion object {
        private const val INTENT_DATA = "data"

        fun newIntent(context: Context): Intent {
            return Intent(context, PeopleActivity::class.java)
        }

        fun getResult(data: Intent): String {
            return data.getStringExtra(INTENT_DATA) ?: ""
        }
    }
}