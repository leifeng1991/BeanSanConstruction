package com.trinidad.beansanconstruction.ui.activity

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.GridLayoutManager
import com.moufans.lib_base.base.activity.BaseActivity
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.databinding.ActivityMaintainManagementBinding
import com.trinidad.beansanconstruction.ui.adapter.HomeFeatureListAdapter
import com.trinidad.beansanconstruction.ui.bean.FeatureListBean

class MaintainManagementActivity : BaseActivity<ActivityMaintainManagementBinding>() {
    private val mHomeFeatureListAdapter by lazy {
        HomeFeatureListAdapter(data = mutableListOf<FeatureListBean>().apply {
            add(FeatureListBean(R.mipmap.ic_maintenance_request, "维修申请"))
            add(FeatureListBean(R.mipmap.ic_maintenance_audit, "维修审核"))
            add(FeatureListBean(R.mipmap.ic_maintenance_other, "其他"))
        })
    }

    override fun getDataBindingLayoutResId(): Int {
        return R.layout.activity_maintain_management
    }

    override fun initView() {
        setHeaderTitle("维修管理")
        mDataBinding.mProjectManagementRecyclerView.apply {
            layoutManager = GridLayoutManager(this@MaintainManagementActivity, 3)
            adapter = mHomeFeatureListAdapter
        }
    }

    override fun initListener() {
        mHomeFeatureListAdapter.setOnItemClickListener { _, _, position ->
            val item = mHomeFeatureListAdapter.data[position]
            when (item.title) {
                "维修申请" -> {
                    startActivity(MaintainOrderActivity.newIntent(this))
                }
                "维修审核" -> {
                    startActivity(MaintenanceOrderReviewActivity.newIntent(this))
                }
                "其他" -> {
                }
            }

        }
    }

    override fun processingLogic() {
    }

    companion object {

        fun newIntent(context: Context): Intent {
            return Intent(context, MaintainManagementActivity::class.java)
        }
    }
}