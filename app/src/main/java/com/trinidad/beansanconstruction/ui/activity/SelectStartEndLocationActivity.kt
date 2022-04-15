package com.trinidad.beansanconstruction.ui.activity

import androidx.recyclerview.widget.LinearLayoutManager
import com.moufans.lib_base.base.activity.BaseActivity
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.databinding.ActivitySelectStartEndLocationBinding
import com.trinidad.beansanconstruction.ui.adapter.SelectStarEndLocationListAdapter

class SelectStartEndLocationActivity : BaseActivity<ActivitySelectStartEndLocationBinding>() {
    private val mSelectStarEndLocationListAdapter by lazy {
        SelectStarEndLocationListAdapter()
    }

    override fun getDataBindingLayoutResId(): Int {
        return R.layout.activity_select_start_end_location
    }

    override fun initView() {
        mDataBinding.mMyRecyclerView.apply {
            setPullRefreshAndLoadingMoreEnabled(false, loadingMoreEnabled = false)
            setLayoutManager(LinearLayoutManager(this@SelectStartEndLocationActivity))
            setAdapter(mSelectStarEndLocationListAdapter)
        }
    }

    override fun initListener() {
        mSelectStarEndLocationListAdapter.addChildClickViewIds(R.id.mSelectRTextView)
        mSelectStarEndLocationListAdapter.setOnItemChildClickListener { _, view, position ->
            val item = mSelectStarEndLocationListAdapter.data[position]
            when (view.id) {
                // 选择
                R.id.mSelectRTextView -> {

                }
            }
        }
    }

    override fun processingLogic() {
    }
}