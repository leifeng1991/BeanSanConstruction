package com.trinidad.beansanconstruction.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import com.google.gson.Gson
import com.google.zxing.Result
import com.king.zxing.CameraScan
import com.king.zxing.DecodeConfig
import com.king.zxing.DecodeFormatManager
import com.king.zxing.DefaultCameraScan
import com.king.zxing.analyze.MultiFormatAnalyzer
import com.moufans.lib_base.base.activity.BaseActivity
import com.moufans.lib_base.utils.LogUtil
import com.moufans.lib_base.utils.PermissionHelper
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.api.bean.ScanBean
import com.trinidad.beansanconstruction.databinding.ActivityRichScanBinding
import java.lang.Exception


/**
 * 扫一扫
 */
class RichScanActivity : BaseActivity<ActivityRichScanBinding>(), CameraScan.OnScanResultCallback {
    private lateinit var mHelper: PermissionHelper
    private var mCameraScan: CameraScan? = null

    override fun getDataBindingLayoutResId(): Int {
        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom)
        return R.layout.activity_rich_scan
    }

    override fun initView() {
        setHeaderTitle("扫一扫")
        // 请求权限
        mHelper = PermissionHelper(this)
        mHelper.requestPermissions("请授予[相机]权限，否则无法扫码", object : PermissionHelper.PermissionListener {
            override fun doAfterGrand(vararg permission: String) {
                LogUtil.e("==========请求权限成功=================")
                // 请求权限成功
                mCameraScan = DefaultCameraScan(this@RichScanActivity, mDataBinding.previewView)
                val decodeConfig = DecodeConfig()
                decodeConfig.setHints(DecodeFormatManager.QR_CODE_HINTS) //如果只有识别二维码的需求，这样设置效率会更高，不设置默认为DecodeFormatManager.DEFAULT_HINTS
                    .setFullAreaScan(false) //设置是否全区域识别，默认false
                    .setAreaRectRatio(0.8f) //设置识别区域比例，默认0.8，设置的比例最终会在预览区域裁剪基于此比例的一个矩形进行扫码识别
                    .setAreaRectVerticalOffset(0).areaRectHorizontalOffset = 0 //设置识别区域水平方向偏移量，默认为0，为0表示居中，可以为负数

                mCameraScan!!.setPlayBeep(true) //设置是否播放音效，默认为false
                    .setVibrate(true) //设置是否震动，默认为false
                    .setNeedAutoZoom(false) //二维码太小时可自动缩放，默认为false
                    .setNeedTouchZoom(true) //支持多指触摸捏合缩放，默认为true
                    .setDarkLightLux(45f) //设置光线足够暗的阈值（单位：lux），需要通过{@link #bindFlashlightView(View)}绑定手电筒才有效
                    .setBrightLightLux(100f) //设置光线足够明亮的阈值（单位：lux），需要通过{@link #bindFlashlightView(View)}绑定手电筒才有效
                    .bindFlashlightView(mDataBinding.ivFlash) //绑定手电筒，绑定后可根据光线传感器，动态显示或隐藏手电筒按钮
                    .setOnScanResultCallback(this@RichScanActivity) //设置扫码结果回调，需要自己处理或者需要连扫时，可设置回调，自己去处理相关逻辑
                    .setAnalyzer(MultiFormatAnalyzer(decodeConfig)) //设置分析器,DecodeConfig可以配置一些解码时的配置信息，如果内置的不满足您的需求，你也可以自定义实现，
                    .setAnalyzeImage(true) //设置是否分析图片，默认为true。如果设置为false，相当于关闭了扫码识别功能
                    .setOnScanResultCallback(this@RichScanActivity)
                    .startCamera() //启动预览（如果是通过继承CaptureActivity或CaptureFragment实现扫码无需调用这句。）

                //设置闪光灯（手电筒）是否开启,需在startCamera之后调用才有效
                mCameraScan!!.enableTorch(true)
            }

            override fun doAfterDenied(vararg permission: String) {
                // 设置失败
                finish()
            }
        }, Manifest.permission.CAMERA)

    }

    override fun initListener() {
    }

    override fun processingLogic() {
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        mHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onScanResultCallback(result: Result): Boolean {
        if (result.text.contains("type")) {
            try {
                val mScanBean = Gson().fromJson(result.text, ScanBean::class.java)
                when (mScanBean.type) {
                    "car" -> {
                        setResult(RESULT_OK, Intent().apply { putExtra(INTENT_DATA, mScanBean.key) })
                        finish()
                    }
                    "pad_login" -> {
                        startActivity(AuthorizeLoginActivity.newIntent(this, mScanBean?.data ?: ""))
                    }
                }
            } catch (e: Exception) {

            }

        }
        return false // 如果支持连扫返回true
    }

    override fun onDestroy() {
        mCameraScan?.release()
        super.onDestroy()
    }

    companion object {
        private const val INTENT_DATA = "data"

        @JvmOverloads
        fun newIntent(context: Context): Intent {
            return Intent(context, RichScanActivity::class.java)
        }

        fun getResult(data: Intent): String {
            return data.getStringExtra(INTENT_DATA) ?: ""
        }
    }


}