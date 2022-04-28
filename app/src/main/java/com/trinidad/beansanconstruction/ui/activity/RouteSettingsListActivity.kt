package com.trinidad.beansanconstruction.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.moufans.lib_base.base.activity.BaseActivity
import com.moufans.lib_base.ext.convertReqExecute
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.api.param.RequestParamJsonBean
import com.trinidad.beansanconstruction.api.bean.TransportSelectDataBean
import com.trinidad.beansanconstruction.databinding.ActivityRouteSettingsListBinding
import com.trinidad.beansanconstruction.ext.appApi
import com.trinidad.beansanconstruction.ui.adapter.RouteSettingsListAdapter
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * 路线列表
 */
class RouteSettingsListActivity : BaseActivity<ActivityRouteSettingsListBinding>() {
    private val mRouteSettingsListAdapter by lazy {
        RouteSettingsListAdapter()
    }

    override fun getDataBindingLayoutResId(): Int {
        return R.layout.activity_route_settings_list
    }

    override fun initView() {
        setHeaderTitle("路线设置")
        setHeaderRightText("搜索", Color.parseColor("#666666"), 14f) {

        }
        mDataBinding.mRouteSettingsRecyclerView.apply {
            setPullRefreshAndLoadingMoreEnabled(true, loadingMoreEnabled = true)
            setLayoutManager(LinearLayoutManager(this@RouteSettingsListActivity))
            loadSize = 10
            setAdapter(mRouteSettingsListAdapter)
        }
    }

    override fun initListener() {
        mRouteSettingsListAdapter.addChildClickViewIds(R.id.mEditRTextView, R.id.mHaveClosedRTextView, R.id.mHaveOpenedRTextView)
        mRouteSettingsListAdapter.setOnItemChildClickListener { _, view, position ->
            val itemBean = mRouteSettingsListAdapter.data[position]
            when (view.id) {
                // 编辑
                R.id.mEditRTextView -> {
                    val jsonString = Gson().toJson(itemBean)
                    startActivity(RouteSettingsActivity.newIntent(this, jsonString))
                }
                // 已关闭、已开启
                R.id.mHaveClosedRTextView, R.id.mHaveOpenedRTextView -> {
                    onOrOff(itemBean)
                }
            }
        }
        mDataBinding.mRouteSettingsRecyclerView.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                getRouteSettings()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                getRouteSettings()
            }

        })
        // 新增
        mDataBinding.mAddedTextView.setOnClickListener {
            startActivity(RouteSettingsActivity.newIntent(this))
        }
    }

    override fun processingLogic() {
        getRouteSettings()
    }

    private fun getRouteSettings() {
        lifecycleScope.launch {
            convertReqExecute({ appApi.select("${mDataBinding.mRouteSettingsRecyclerView.currentPage}") }, onSuccess = {
                mDataBinding.mRouteSettingsRecyclerView.handlerSuccess(mRouteSettingsListAdapter, it.records)
            }, onFailure = { _, status, _ ->
                mDataBinding.mRouteSettingsRecyclerView.handlerError(mRouteSettingsListAdapter, status)
            }, baseView = this@RouteSettingsListActivity)
        }
    }

    private fun onOrOff(item: TransportSelectDataBean.RecordsListBean) {
        lifecycleScope.launch {
            val body = Gson().toJson(RequestParamJsonBean().apply {
                id = item.id
                deleted = item.deleted
            }).toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            convertReqExecute({ appApi.onOrOff(body) }, onSuccess = {
                if (it) {
                    if (item.deleted == "0") {
                        item.deleted = "1"
                    } else {
                        item.deleted = "0"
                    }
                    mRouteSettingsListAdapter.notifyDataSetChanged()
                }

            }, baseView = this@RouteSettingsListActivity)
        }
    }

    companion object {

        @JvmOverloads
        fun newIntent(context: Context): Intent {
            return Intent(context, RouteSettingsListActivity::class.java)
        }
    }
}