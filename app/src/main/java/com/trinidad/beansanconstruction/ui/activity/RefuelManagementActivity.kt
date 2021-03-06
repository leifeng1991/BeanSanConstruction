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
 * 加油管理
 */
class RefuelManagementActivity : BaseActivity<ActivityProjectManagementBinding>() {
    private val mHomeFeatureListAdapter by lazy {
        HomeFeatureListAdapter(data = mutableListOf<FeatureListBean>().apply {
            add(FeatureListBean(R.mipmap.ic_refuel, "加油"))
            add(FeatureListBean(R.mipmap.ic_set_the_price_of_oil, "设置油价"))
            add(FeatureListBean(R.mipmap.ic_refuel_record, "加油记录"))
        })
    }

    override fun getDataBindingLayoutResId(): Int {
        return R.layout.activity_project_management
    }

    override fun initView() {
        setHeaderTitle("加油管理")
        mDataBinding.mProjectManagementRecyclerView.apply {
            layoutManager = GridLayoutManager(this@RefuelManagementActivity, 3)
            adapter = mHomeFeatureListAdapter
        }
    }

    override fun initListener() {
        mHomeFeatureListAdapter.setOnItemClickListener { _, _, position ->
            val item = mHomeFeatureListAdapter.data[position]
            val code = when (item.title) {
                "加油" -> {
                    "JY_Add"
                }
                "设置油价" -> {
                    "JY_Price"
                }
                "加油记录" -> {
                    "JY_Log"
                }
                else -> {
                    ""
                }
            }
            lifecycleScope.launch {
                convertReqExecute({ appApi.selectOneCode(code) }, onSuccess = {
                    when (item.title) {
                        "加油" -> {
                            startActivity(RefuelActivity.newIntent(this@RefuelManagementActivity))
                        }
                        "设置油价" -> {
                            startActivity(SetOilPriceActivity.newIntent(this@RefuelManagementActivity))
                        }
                        "加油记录" -> {
                            startActivity(RefuelRecordActivity.newIntent(this@RefuelManagementActivity))
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
            return Intent(context, RefuelManagementActivity::class.java)
        }
    }
}