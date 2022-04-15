package com.trinidad.beansanconstruction.ui.activity

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.amap.api.services.core.PoiItem
import com.google.gson.Gson
import com.moufans.lib_base.base.activity.BaseActivity
import com.moufans.lib_base.ext.baseReqExecute
import com.moufans.lib_base.ext.convertReqExecute
import com.moufans.lib_base.ext.setOnClickListener2
import com.moufans.lib_base.utils.ToastUtil
import com.trinidad.beansanconstruction.MainActivity
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.api.RequestParamJsonBean
import com.trinidad.beansanconstruction.api.bean.ProjectListDataBean
import com.trinidad.beansanconstruction.constants.AppConstants
import com.trinidad.beansanconstruction.databinding.ActivityCreateProjectBinding
import com.trinidad.beansanconstruction.ext.appApi
import com.trinidad.beansanconstruction.utils.SharedPrefUtil
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * 创建项目
 */
class CreateProjectActivity : BaseActivity<ActivityCreateProjectBinding>() {
    private var mPoiItem: PoiItem? = null
    private var mId = ""
    private val mProjectListDataBean by lazy {
        if (TextUtils.isEmpty(intent.getStringExtra(INTENT_PROJECT_LIST_DATA_BEAN)))
            null
        else
            Gson().fromJson(intent.getStringExtra(INTENT_PROJECT_LIST_DATA_BEAN), ProjectListDataBean::class.java)
    }
    private var isRequesting = false

    override fun getDataBindingLayoutResId(): Int {
        return R.layout.activity_create_project
    }

    override fun initView() {

        if (mProjectListDataBean != null) {
            setHeaderTitle("编辑项目")
            mId = mProjectListDataBean?.id ?: ""
            mDataBinding.apply {
                mProjectNameEditText.setText(mProjectListDataBean?.name ?: "")
                mProjectAddressEditText.setText(mProjectListDataBean?.siteName ?: "")
                mClockInScopeEditText.setText(mProjectListDataBean?.extent ?: "0")
                mLatitudeTextView.text = mProjectListDataBean?.coordinate?.y ?: "0"
                mLongitudeTextView.text = mProjectListDataBean?.coordinate?.x ?: "0"
            }
        } else {
            setHeaderTitle("创建项目")
        }
    }

    override fun initListener() {
        val mStartActivityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.data != null) {
                try {
                    val jsonString = MapActivity.getResult(it.data!!)
                    mPoiItem = Gson().fromJson(jsonString, PoiItem::class.java)
                    if (mPoiItem != null) {
                        mDataBinding.mLongitudeTextView.text = "${mPoiItem!!.latLonPoint.longitude}"
                        mDataBinding.mLatitudeTextView.text = "${mPoiItem!!.latLonPoint.latitude}"
                    }
                } catch (e: Exception) {

                }

            }
        }
        mDataBinding.apply {
            // 设定定位
            mSetRegistrationRTextView.setOnClickListener {
                var mLatitude = mLatitudeTextView.text.toString()
                var mLongitude = mLongitudeTextView.text.toString()
                if (TextUtils.isEmpty(mLatitude)) {
                    mLatitude = "0"
                }
                if (TextUtils.isEmpty(mLongitude)) {
                    mLongitude = "0"
                }
                mStartActivityForResult.launch(MapActivity.newIntent(this@CreateProjectActivity, mLongitude.toDouble(), mLatitude.toDouble()))
            }
        }
        // 保存
        mDataBinding.mSaveRTextView.setOnClickListener2 {
            addOrUpdate()
        }
    }

    override fun processingLogic() {
    }

    private fun addOrUpdate() {
        if (isRequesting) {
            return
        }
        isRequesting = true
        val projectName = mDataBinding.mProjectNameEditText.text.toString().trim()
        if (TextUtils.isEmpty(projectName)) {
            ToastUtil.showShort("请输入项目名称")
            return
        }
        val projectAddress = mDataBinding.mProjectAddressEditText.text.toString().trim()
        val clockInScope = mDataBinding.mClockInScopeEditText.text.toString().trim()
        if (TextUtils.isEmpty(clockInScope)) {
            ToastUtil.showShort("请输入打卡范围")
            return
        }
        if (mPoiItem == null && mProjectListDataBean == null) {
            ToastUtil.showShort("请设定定位")
            return
        }
        lifecycleScope.launch {
            val body = Gson().toJson(RequestParamJsonBean().apply {
                // 为空时添加，不为空根据id修改
                id = mId
                // 点位名称
                name = projectName
                // 地址
                siteName = projectAddress
                // 起点经纬度
                coordinate = RequestParamJsonBean.Coordinate().apply {
                    // 经度
                    x = if (mProjectListDataBean == null) "${mPoiItem?.latLonPoint?.longitude}" else (if (mPoiItem != null) "${mPoiItem?.latLonPoint?.longitude}" else "${mProjectListDataBean?.coordinate?.x}")
                    // 纬度
                    y = if (mProjectListDataBean == null) "${mPoiItem?.latLonPoint?.latitude}" else (if (mPoiItem != null) "${mPoiItem?.latLonPoint?.latitude}" else "${mProjectListDataBean?.coordinate?.y}")
                }
                extent = clockInScope
            }).toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            baseReqExecute({ appApi.addOrUpdate(body) }, onSuccess = {
                lifecycleScope.launch {
                    if (mProjectListDataBean == null)
                        ToastUtil.showShort("创建成功")
                    else
                        ToastUtil.showShort("修改成功")

                    isRequesting = false
                    // 关闭页面
                    finish()
                }
            }, onFailure = { _, _, _ ->
                isRequesting = false
            }, baseView = this@CreateProjectActivity)
        }
    }

    companion object {
        private const val INTENT_PROJECT_LIST_DATA_BEAN = "ProjectListDataBean"

        @JvmOverloads
        fun newIntent(context: Context, mProjectListDataBeanJson: String): Intent {
            return Intent(context, CreateProjectActivity::class.java).apply {
                putExtra(INTENT_PROJECT_LIST_DATA_BEAN, mProjectListDataBeanJson)
            }
        }
    }
}