package com.trinidad.beansanconstruction.ui.fragment

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.moufans.lib_base.base.fragment.ViewPageFragment
import com.moufans.lib_base.ext.convertReqExecute
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.databinding.FragmentRefuelRecordListBinding
import com.trinidad.beansanconstruction.ext.appApi
import com.trinidad.beansanconstruction.ui.adapter.RefuelRecordListAdapter
import kotlinx.coroutines.launch


/**
 * 加油记录
 */
class RefuelRecordFragment : ViewPageFragment<FragmentRefuelRecordListBinding>() {
    private val mType by lazy {
        arguments?.getString(INTENT_P_TYPE)
    }
    private val mRefuelRecordListAdapter by lazy {
        RefuelRecordListAdapter()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_refuel_record_list
    }

    override fun initView() {
        mFragmentDataBinding.mRefuelRecordRecyclerView.apply {
            setPullRefreshAndLoadingMoreEnabled(true, loadingMoreEnabled = true)
            setLayoutManager(LinearLayoutManager(requireContext()))
            setAdapter(mRefuelRecordListAdapter)
        }
    }

    override fun initListener() {
        mFragmentDataBinding.mRefuelRecordRecyclerView.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                oilPriceLogSelect()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                oilPriceLogSelect()
            }
        })
    }

    override fun processingLogic() {
        oilPriceLogSelect()
    }


    override fun refreshOnceData() {
    }

    private fun oilPriceLogSelect() {
        lifecycleScope.launch {
            convertReqExecute({ appApi.oilPriceLogSelect(mType ?: "", "${mFragmentDataBinding.mRefuelRecordRecyclerView.currentPage}") }, onSuccess = {
                mFragmentDataBinding.mRefuelRecordRecyclerView.handlerSuccess(mRefuelRecordListAdapter, it.records)
            }, onFailure = { _, status, _ ->
                mFragmentDataBinding.mRefuelRecordRecyclerView.handlerError(mRefuelRecordListAdapter, status)
            }, baseView = this@RefuelRecordFragment)
        }
    }

    companion object {
        private const val INTENT_P_TYPE = "type"

        @JvmStatic
        fun newInstance(type: String) = RefuelRecordFragment().apply {
            arguments = Bundle().apply {
                putString(INTENT_P_TYPE, type)
            }
        }
    }
}
