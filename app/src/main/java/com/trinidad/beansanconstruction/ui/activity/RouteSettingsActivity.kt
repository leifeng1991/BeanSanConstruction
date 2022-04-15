package com.trinidad.beansanconstruction.ui.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.amap.api.services.core.PoiItem
import com.google.gson.Gson
import com.moufans.lib_base.base.activity.BaseActivity
import com.moufans.lib_base.ext.convertReqExecute
import com.moufans.lib_base.ext.setOnClickListener2
import com.moufans.lib_base.utils.ToastUtil
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.api.RequestParamJsonBean
import com.trinidad.beansanconstruction.api.bean.ProjectListDataBean
import com.trinidad.beansanconstruction.api.bean.TransportSelectDataBean
import com.trinidad.beansanconstruction.databinding.ActivityRouteSettingsBinding
import com.trinidad.beansanconstruction.ext.appApi
import com.trinidad.beansanconstruction.utils.StringUtil
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * 路线设置
 */
class RouteSettingsActivity : BaseActivity<ActivityRouteSettingsBinding>() {
    private var mStartProjectListDataBean: ProjectListDataBean? = null
    private var mEndProjectListDataBean: ProjectListDataBean? = null
    private var mId = ""
    private val mRecordsListBean by lazy {
        if (TextUtils.isEmpty(intent.getStringExtra(INTENT_RECORDS_LIST_BEAN)))
            null
        else
            Gson().fromJson(intent.getStringExtra(INTENT_RECORDS_LIST_BEAN), TransportSelectDataBean.RecordsListBean::class.java)
    }
    private var isRequesting = false

    override fun getDataBindingLayoutResId(): Int {
        return R.layout.activity_route_settings
    }

    override fun initView() {
        setHeaderTitle(if (mRecordsListBean == null) "路线设置" else "编辑路线")
        if (mRecordsListBean != null) {
            mId = mRecordsListBean?.id ?: ""
            mDataBinding.apply {
                mStartRTextView.text = mRecordsListBean?.startPoint ?: ""
                mEndRTextView.text = mRecordsListBean?.endPoint ?: ""
                mPriceTextView.setText(StringUtil.saveTwoDecimal(mRecordsListBean?.money ?: ""))
            }
        }
    }

    override fun initListener() {
        val mStartActivityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.data != null) {
                try {
                    val jsonString = ProjectListActivity.getResult(it.data!!)
                    mStartProjectListDataBean = Gson().fromJson(jsonString, ProjectListDataBean::class.java)
                    mDataBinding.mStartRTextView.text = mStartProjectListDataBean!!.name
                } catch (e: Exception) {

                }

            }
        }
        val mEndActivityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.data != null) {
                try {
                    val jsonString = ProjectListActivity.getResult(it.data!!)
                    mEndProjectListDataBean = Gson().fromJson(jsonString, ProjectListDataBean::class.java)
                    mDataBinding.mEndRTextView.text = mEndProjectListDataBean!!.name
                } catch (e: Exception) {

                }

            }
        }
        mDataBinding.apply {
            // 开始位置
            mStartRTextView.setOnClickListener {
                mStartActivityForResult.launch(ProjectListActivity.newIntent(this@RouteSettingsActivity, 0, true))
            }
            // 结束位置
            mEndRTextView.setOnClickListener {
                mEndActivityForResult.launch(ProjectListActivity.newIntent(this@RouteSettingsActivity, 1, true))
            }
            // 保存
            mSaveTextView.setOnClickListener2 {
                addUpdate()
            }
        }
    }

    override fun processingLogic() {
    }

    private fun addUpdate() {
        if (isRequesting) {
            return
        }
        isRequesting = false
        if (mStartProjectListDataBean == null && mRecordsListBean == null) {
            ToastUtil.showShort("请选择起始点项目")
            return
        }
        if (mEndProjectListDataBean == null && mRecordsListBean == null) {
            ToastUtil.showShort("请选择终点倒土点")
            return
        }
        val price = mDataBinding.mPriceTextView.text.toString()
        if (TextUtils.isEmpty(price)) {
            ToastUtil.showShort("请输入价格")
            return
        }
        lifecycleScope.launch {
            val body = Gson().toJson(RequestParamJsonBean().apply {
                // 为空时添加，不为空根据id修改
                id = mId
                // 项目id（起点）
                startId = if (mRecordsListBean == null) mStartProjectListDataBean!!.id else (if (mStartProjectListDataBean != null) mStartProjectListDataBean!!.id else mRecordsListBean?.startId)
                // 倒土点id（终点）
                endId = if (mRecordsListBean == null) mEndProjectListDataBean!!.id else (if (mEndProjectListDataBean != null) mEndProjectListDataBean!!.id else mRecordsListBean?.endId)
                // 价钱
                money = StringUtil.saveTwoDecimal(price)

            }).toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            convertReqExecute({ appApi.addUpdate(body) }, onSuccess = {
                lifecycleScope.launch {
                    if (mRecordsListBean == null)
                        ToastUtil.showShort("创建成功")
                    else
                        ToastUtil.showShort("修改成功")
                    isRequesting = false
                    // 关闭页面
                    finish()
                }
            }, onFailure = { _, _, _ -> isRequesting = false }, baseView = this@RouteSettingsActivity)
        }
    }

    companion object {
        private const val INTENT_RECORDS_LIST_BEAN = "RecordsListBean"

        @JvmOverloads
        fun newIntent(context: Context, mRecordsListBeanJsonStr: String = ""): Intent {
            return Intent(context, RouteSettingsActivity::class.java).apply {
                putExtra(INTENT_RECORDS_LIST_BEAN, mRecordsListBeanJsonStr)
            }
        }
    }
}