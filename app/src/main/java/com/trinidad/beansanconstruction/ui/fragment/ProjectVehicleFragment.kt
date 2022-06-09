package com.trinidad.beansanconstruction.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.moufans.lib_base.base.fragment.ViewPageFragment
import com.moufans.lib_base.ext.convertReqExecute
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.databinding.FragmentRefuelRecordListBinding
import com.trinidad.beansanconstruction.ext.appApi
import com.trinidad.beansanconstruction.ui.activity.MechanicalDesActivity
import com.trinidad.beansanconstruction.ui.adapter.ProjectOperationListAdapter
import com.trinidad.beansanconstruction.ui.adapter.RefuelRecordListAdapter
import com.trinidad.beansanconstruction.utils.DateTimeUtil
import kotlinx.coroutines.launch


/**
 * 项目/车辆
 */
class ProjectVehicleFragment : ViewPageFragment<FragmentRefuelRecordListBinding>() {
    private var mType = 1
    private var mYearMonth = DateTimeUtil.getYear(System.currentTimeMillis())
    private lateinit var mProjectOperationListAdapter: ProjectOperationListAdapter

    override fun getLayoutId(): Int {
        return R.layout.fragment_refuel_record_list
    }

    override fun initView() {
        mType = arguments?.getInt(INTENT_P_TYPE) ?: 1
        mYearMonth = arguments?.getString(INTENT_P_YEAR_MONTH) ?: DateTimeUtil.getYear(System.currentTimeMillis())
        mProjectOperationListAdapter = ProjectOperationListAdapter(mType)
        mFragmentDataBinding.mRefuelRecordRecyclerView.apply {
            setPullRefreshAndLoadingMoreEnabled(true, loadingMoreEnabled = true)
            setLayoutManager(LinearLayoutManager(requireContext()))
            setAdapter(mProjectOperationListAdapter)
        }
    }

    override fun initListener() {
        mFragmentDataBinding.mRefuelRecordRecyclerView.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                getListData()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                getListData()
            }
        })
        mProjectOperationListAdapter.setOnItemClickListener { adapter, view, position ->
            startActivity(MechanicalDesActivity.newIntent(requireContext(), mProjectOperationListAdapter.data[position].id ?: "", mType, mYearMonth.split("-")[0].toInt()))
        }
    }

    override fun processingLogic() {
        getListData()
    }


    override fun refreshOnceData() {
    }

    /**
     * 获取数据
     */
    private fun getListData() {
        val yms = mYearMonth.split("-")
        var monthStr = ""
        if (yms.size >= 2) {
            val month = yms[1].toInt()
            if (month < 10)
                monthStr = "0$month"
        }
        if (yms.isNotEmpty())
            mYearMonth = "${yms[0]}-$monthStr"
        lifecycleScope.launch {
            convertReqExecute({
                if (mType == 1)
                    appApi.projectTable(mYearMonth ?: "", "${mFragmentDataBinding.mRefuelRecordRecyclerView.currentPage}")
                else
                    appApi.carTable(mYearMonth ?: "", "${mFragmentDataBinding.mRefuelRecordRecyclerView.currentPage}")
            }, onSuccess = {
                mFragmentDataBinding.mRefuelRecordRecyclerView.handlerSuccess(mProjectOperationListAdapter, it.records)
            }, onFailure = { _, status, _ ->
                mFragmentDataBinding.mRefuelRecordRecyclerView.handlerError(mProjectOperationListAdapter, status)
            }, baseView = this@ProjectVehicleFragment)
        }
    }

    companion object {
        private const val INTENT_P_YEAR_MONTH = "yearMonth"
        private const val INTENT_P_TYPE = "type"

        /**
         * @param yearMonth 年份和月份
         * @param type      1项目 2车辆
         */
        fun newInstance(yearMonth: String, type: Int) = ProjectVehicleFragment().apply {
            arguments = Bundle().apply {
                putString(INTENT_P_YEAR_MONTH, yearMonth)
                putInt(INTENT_P_TYPE, type)
            }
        }
    }
}
