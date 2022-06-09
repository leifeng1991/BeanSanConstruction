package com.trinidad.beansanconstruction.ui.activity

import android.content.Context
import android.content.Intent
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.moufans.lib_base.base.activity.BaseActivity
import com.moufans.lib_base.ext.convertReqExecute
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.databinding.ActivityProjectManagementBinding
import com.trinidad.beansanconstruction.ext.appApi
import com.trinidad.beansanconstruction.ui.adapter.HomeFeatureListAdapter
import com.trinidad.beansanconstruction.ui.bean.FeatureListBean
import kotlinx.coroutines.launch

/**
 * 项目管理
 */
class ProjectManagementActivity : BaseActivity<ActivityProjectManagementBinding>() {
    private val mHomeFeatureListAdapter by lazy {
        HomeFeatureListAdapter(data = mutableListOf<FeatureListBean>().apply {
            add(FeatureListBean(R.mipmap.ic_project_setting, "项目设置"))
            add(FeatureListBean(R.mipmap.ic_route_setting, "倒土场设置"))
            add(FeatureListBean(R.mipmap.ic_dtc_setting, "路线管理"))
        })
    }

    override fun getDataBindingLayoutResId(): Int {
        return R.layout.activity_project_management
    }

    override fun initView() {
        setHeaderTitle("项目管理")
        mDataBinding.mProjectManagementRecyclerView.apply {
            layoutManager = GridLayoutManager(this@ProjectManagementActivity, 3)
            adapter = mHomeFeatureListAdapter
        }
    }

    override fun initListener() {
        mHomeFeatureListAdapter.setOnItemClickListener { _, _, position ->
            val item = mHomeFeatureListAdapter.data[position]
            val code = when (item.title) {
                "项目设置" -> {
                    "XM_Start"
                }
                "倒土场设置" -> {
                    "XM_End"
                }
                "路线管理" -> {
                    "XM_Route"
                }
                else -> {
                    ""
                }
            }
            lifecycleScope.launch {
                convertReqExecute({ appApi.selectOneCode(code) }, onSuccess = {
                    when (item.title) {
                        "项目设置" -> {
                            startActivity(ProjectListActivity.newIntent(this@ProjectManagementActivity, 0))
                        }
                        "倒土场设置" -> {
                            startActivity(ProjectListActivity.newIntent(this@ProjectManagementActivity, 1))
                        }
                        "路线管理" -> {
                            startActivity(RouteSettingsListActivity.newIntent(this@ProjectManagementActivity))
                        }
                    }
                })
            }


        }
    }

    override fun processingLogic() {
    }

    companion object {

        @JvmOverloads
        fun newIntent(context: Context): Intent {
            return Intent(context, ProjectManagementActivity::class.java)
        }
    }
}