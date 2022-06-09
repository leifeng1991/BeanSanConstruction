package com.trinidad.beansanconstruction.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.moufans.lib_base.base.activity.BaseActivity
import com.moufans.lib_base.ext.convertReqExecute
import com.moufans.lib_base.utils.ToastUtil
import com.ruffian.library.widget.RTextView
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.api.bean.LogonLogSelectDataBean
import com.trinidad.beansanconstruction.api.bean.RepairCostSelectDataBean
import com.trinidad.beansanconstruction.api.param.RepairCostUpdateRequestParam
import com.trinidad.beansanconstruction.databinding.ActivityPeopleManagementBinding
import com.trinidad.beansanconstruction.ext.appApi
import com.trinidad.beansanconstruction.ui.adapter.PeopleManagementListAdapter
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class PeopleManagementActivity : BaseActivity<ActivityPeopleManagementBinding>() {
    private val mPeopleManagementListAdapter by lazy {
        PeopleManagementListAdapter()
    }

    override fun getDataBindingLayoutResId(): Int {
        return R.layout.activity_people_management
    }

    override fun initView() {
        setHeaderTitle("人员管理")
        mDataBinding.mRecyclerView.apply {
            setPullRefreshAndLoadingMoreEnabled(true, loadingMoreEnabled = true)
            setLayoutManager(LinearLayoutManager(this@PeopleManagementActivity))
            setAdapter(mPeopleManagementListAdapter)
        }
    }

    override fun initListener() {
        // 搜索
        mDataBinding.mSearchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                mDataBinding.mRecyclerView.currentPage = 1
                getDataList()
                true
            }
            false
        }
        // 审核点击事件
        mPeopleManagementListAdapter.addChildClickViewIds(R.id.mLeftRTextView, R.id.mRightRTextView)
        mPeopleManagementListAdapter.setOnItemChildClickListener { _, view, position ->
            if (view is RTextView) {
                dealClickChildItem(view.text.toString(), mPeopleManagementListAdapter.data[position])
            }
        }
        // 刷新和加载
        mDataBinding.mRecyclerView.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                mDataBinding.mRecyclerView.currentPage = 1
                getDataList()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                getDataList()
            }

        })
    }

    override fun processingLogic() {
        getDataList()
    }

    private fun getDataList() {
        val keyWord = mDataBinding.mSearchEditText.text.toString().trim()
        lifecycleScope.launch {
            convertReqExecute({ appApi.logonLogSelect(keyWord, "${mDataBinding.mRecyclerView.currentPage}") }, onSuccess = {
                mDataBinding.mRecyclerView.handlerSuccess(mPeopleManagementListAdapter, it.records)
            }, onFailure = { _, status, _ ->
                mDataBinding.mRecyclerView.handlerError(mPeopleManagementListAdapter, status)
            }, baseView = this@PeopleManagementActivity)
        }
    }

    private fun dealClickChildItem(text: String, bean: LogonLogSelectDataBean.RecordsBean) {
        // 审核状态（0/审核中，1/拒绝，2/通过）
        if (bean.deleted != "0") {
            return
        }
        when (text) {
            "拒绝申请" -> {
                logonLogUpdate(bean, "1")
            }
            "通过审核" -> {
                logonLogUpdate(bean, "2")
            }

        }
    }

    /**
     * 审批
     * @param mDeleted  0/拒绝，2/通过
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun logonLogUpdate(bean: LogonLogSelectDataBean.RecordsBean, mDeleted: String) {
        lifecycleScope.launch {
            val body = Gson().toJson(RepairCostUpdateRequestParam().apply {
                id = bean.id
                deleted = mDeleted
            }).toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            convertReqExecute({ appApi.logonLogUpdate(body) }, onSuccess = {
                bean.deleted = mDeleted
                mPeopleManagementListAdapter.notifyDataSetChanged()
                ToastUtil.showShort("提交成功")
            }, baseView = this@PeopleManagementActivity)
        }
    }

    companion object {

        fun newIntent(context: Context): Intent {
            return Intent(context, PeopleManagementActivity::class.java)
        }
    }
}