package com.trinidad.beansanconstruction.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.google.gson.Gson
import com.moufans.lib_base.base.activity.BaseActivity
import com.moufans.lib_base.ext.convertReqExecute
import com.moufans.lib_base.utils.ToastUtil
import com.ruffian.library.widget.RTextView
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.api.param.RepairCostAddRequestParamBean
import com.trinidad.beansanconstruction.api.bean.RepairCostSelectDataBean
import com.trinidad.beansanconstruction.api.param.RepairCostUpdateRequestParam
import com.trinidad.beansanconstruction.databinding.ActivityMaintenanceOrderReviewBinding
import com.trinidad.beansanconstruction.ext.appApi
import com.trinidad.beansanconstruction.ui.adapter.MaintenanceOrderReviewListAdapter
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class MaintenanceOrderReviewActivity : BaseActivity<ActivityMaintenanceOrderReviewBinding>() {
    private val mMaintenanceOrderReviewListAdapter by lazy {
        MaintenanceOrderReviewListAdapter()
    }

    override fun getDataBindingLayoutResId(): Int {
        return R.layout.activity_maintenance_order_review
    }

    override fun initView() {
        setHeaderTitle("维修订单审核")
        mDataBinding.mRecyclerView.apply {
            setPullRefreshAndLoadingMoreEnabled(true, loadingMoreEnabled = true)
            setLayoutManager(LinearLayoutManager(this@MaintenanceOrderReviewActivity))
            setAdapter(mMaintenanceOrderReviewListAdapter)
        }
    }

    override fun initListener() {
        // 搜索
        mDataBinding.mSearchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                mDataBinding.mRecyclerView.currentPage = 1
                repairCostSelect()
                true
            }
            false
        }
        // 审核
        mMaintenanceOrderReviewListAdapter.addChildClickViewIds(R.id.mLeftRTextView, R.id.mRightRTextView)
        mMaintenanceOrderReviewListAdapter.setOnItemChildClickListener { _, view, position ->
            if (view is RTextView) {
                dealClickChildItem(view.text.toString(), mMaintenanceOrderReviewListAdapter.data[position])
            }
        }
        // item点击
        mMaintenanceOrderReviewListAdapter.setOnItemClickListener { _, _, position ->
            // 跳转到详情页
            startActivity(MaintainDesActivity.newIntent(this, mMaintenanceOrderReviewListAdapter.data[position].id ?: ""))
        }
        // 刷新和加载
        mDataBinding.mRecyclerView.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                mDataBinding.mRecyclerView.currentPage = 1
                repairCostSelect()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                repairCostSelect()
            }

        })
    }

    override fun processingLogic() {
        repairCostSelect()
    }

    private fun repairCostSelect() {
        val keyWord = mDataBinding.mSearchEditText.text.toString().trim()
        lifecycleScope.launch {
            convertReqExecute({ appApi.repairCostSelect(keyWord, "${mDataBinding.mRecyclerView.currentPage}") }, onSuccess = {
                mDataBinding.mRecyclerView.handlerSuccess(mMaintenanceOrderReviewListAdapter, it.records)
            }, onFailure = { _, status, _ ->
                mDataBinding.mRecyclerView.handlerError(mMaintenanceOrderReviewListAdapter, status)
            }, baseView = this@MaintenanceOrderReviewActivity)
        }
    }

    private fun dealClickChildItem(text: String, bean: RepairCostSelectDataBean.RecordsBean) {
        // 审核状态（0/拒绝，1/审核中，2/通过）
        if (bean.deleted != "1") {
            return
        }
        when (text) {
            "拒绝申请" -> {
                repairCostUpdate(bean, "0")
            }
            "通过审核" -> {
                repairCostUpdate(bean, "2")
            }
        }
    }

    /**
     * 审批
     * @param mDeleted  0/拒绝，2/通过
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun repairCostUpdate(bean: RepairCostSelectDataBean.RecordsBean, mDeleted: String) {
        lifecycleScope.launch {
            val body = Gson().toJson(RepairCostUpdateRequestParam().apply {
                id = bean.id
                deleted = mDeleted
            }).toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            convertReqExecute({ appApi.repairCostUpdate(body) }, onSuccess = {
                bean.deleted = mDeleted
                mMaintenanceOrderReviewListAdapter.notifyDataSetChanged()
                ToastUtil.showShort("提交成功")
            }, baseView = this@MaintenanceOrderReviewActivity)
        }
    }

    companion object {

        fun newIntent(context: Context): Intent {
            return Intent(context, MaintenanceOrderReviewActivity::class.java)
        }
    }
}