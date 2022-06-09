package com.trinidad.beansanconstruction.ui.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.TextUtils
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.moufans.lib_base.base.activity.BaseActivity
import com.moufans.lib_base.ext.convertReqExecute
import com.moufans.lib_base.ext.setOnClickListener2
import com.moufans.lib_base.utils.ImageLoader
import com.moufans.lib_base.utils.PermissionHelper
import com.moufans.lib_base.utils.ToastUtil
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.api.param.RequestParamJsonBean
import com.trinidad.beansanconstruction.databinding.ActivityEditProfileBinding
import com.trinidad.beansanconstruction.ext.appApi
import com.trinidad.beansanconstruction.utils.Glide4Engine
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.internal.entity.CaptureStrategy
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream

/**
 * 编辑资料
 */
class EditProfileActivity : BaseActivity<ActivityEditProfileBinding>() {
    private val mUserName by lazy {
        intent.getStringExtra(INTENT_P_USER_NAME)
    }
    private var mUserImg = ""
    private lateinit var mHelper: PermissionHelper
    private var mImagePath = ""

    override fun getDataBindingLayoutResId(): Int {
        return R.layout.activity_edit_profile
    }

    override fun initView() {
        setHeaderTitle("编辑资料")
        mUserImg = intent.getStringExtra(INTENT_P_USER_IMG) ?: ""
        // 请求权限
        mHelper = PermissionHelper(this)
        mDataBinding.apply {
            mNicknameEditText.setText(mUserName)
            ImageLoader.setImage(mUserImg, mUserIconImageView, R.mipmap.ic_launcher)
        }
    }

    override fun initListener() {
        // 选择照片
        mDataBinding.mUserIconImageView.setOnClickListener2 {
            selectImage()
        }
        // 保存修改
        mDataBinding.mSaveTextView.setOnClickListener2 {
            if (!TextUtils.isEmpty(mImagePath) || mDataBinding.mNicknameEditText.text.toString() != mUserName) {
                if (!TextUtils.isEmpty(mImagePath)) {
                    // 压缩
                    Luban.with(this)
                        .load(mImagePath)
                        .setCompressListener(object : OnCompressListener {
                            override fun onStart() {
                                // 压缩开始前调用，可以在方法内启动 loading UI
                                showLoading()
                            }

                            override fun onSuccess(file: File) {
                                // 压缩成功后调用，返回压缩后的图片文件
                                upload(file.path)
                            }

                            override fun onError(e: Throwable) {
                                // 当压缩过程出现问题时调用
                                upload(mImagePath)
                            }
                        }).launch()

                } else {
                    update()
                }
            } else {
                finish()
            }
        }
    }

    override fun processingLogic() {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE) {
            // 选择的图片
            if (resultCode == RESULT_OK) {
                val images = Matisse.obtainPathResult(data)
                if (images.size > 0) {
                    mImagePath = images[0]
                    ImageLoader.setImage(images[0], mDataBinding.mUserIconImageView, roundingRadius = 50f)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun selectImage() { // 选择图片
        mHelper.requestPermissions("请授予[读写][拍照]权限，否则无法拍照和读取本地图片", object : PermissionHelper.PermissionListener {
            override fun doAfterGrand(vararg permission: String) { // 请求权限成功
                Matisse.from(this@EditProfileActivity)
                    .choose(MimeType.ofImage()) // 选择mime类型
                    .showSingleMediaType(true) // 只显示一种
//                        .theme()// 样式
//                        .countable(true)// 是否显示数量
                    .maxSelectable(1) // 最多可选，去除之前选中的
//                        .maxSelectablePerMediaType()
//                        .addFilter()// 过滤
                    .capture(true) // 可拍照
//                        .originalEnable(true)// 是否显示原图选项
//                        .autoHideToolbarOnSingleTap(true)// 确定当用户点击图片时，是否在预览模式下隐藏顶部和底部工具栏
//                        .maxOriginalSize()// 最大原始大小，单位为MB。只有当{link@originalEnable}设置为true时才有用
                    .captureStrategy(
                        CaptureStrategy(true, "$packageName.common.fileprovider", "image")
                    ) // 保存照片策略
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) // 限制activity方向
//                        .spanCount()// 设置显示图片页面span
//                        .gridExpectedSize()// grid预期大小
                    .thumbnailScale(0.85f) // 缩略图缩放
                    .imageEngine(Glide4Engine()) // 图片引擎
//                        .setOnSelectedListener()// 选中监听
//                        .setOnCheckedListener()// 选中监听
                    .forResult(REQUEST_IMAGE)
            }

            override fun doAfterDenied(vararg permission: String) {}
        }, "android.permission.READ_EXTERNAL_STORAGE", Manifest.permission.CAMERA)
    }

    private fun upload(imagePath: String) {

        val file = File(imagePath)
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        val fileBody: RequestBody = RequestBody.create("image/png".toMediaTypeOrNull(), file)
        builder.addFormDataPart("file", file.name, fileBody)
        lifecycleScope.launch {
            convertReqExecute({ appApi.uploadImg(builder.build()) }, onSuccess = {
                mUserImg = it
                update()
            }, baseView = this@EditProfileActivity)
        }

    }

    private fun update() {
        val body = Gson().toJson(RequestParamJsonBean().apply {
            name = mDataBinding.mNicknameEditText.text.toString()
            picUrl = mUserImg
        }).toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        lifecycleScope.launch {
            convertReqExecute({ appApi.update(body) }, onSuccess = {
                ToastUtil.showShort("保存成功")
                finish()
            })
        }
    }

    /**
     *
     * @param path 图片路径
     * @return
     */
    fun getBitmap(path: String?): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val fis = FileInputStream(path)
            bitmap = BitmapFactory.decodeStream(fis)
        } catch (e: Exception) {
        }
        return bitmap
    }

    /*
     *Bitmap转byte数组
     */
    fun BitmapToBytes(bitmap: Bitmap): ByteArray? {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        return baos.toByteArray()
    }

    companion object {
        private const val REQUEST_IMAGE = 123
        private const val INTENT_P_USER_NAME = "userName"
        private const val INTENT_P_USER_IMG = "userImg"

        @JvmOverloads
        fun newIntent(context: Context, userName: String, userImg: String): Intent {
            return Intent(context, EditProfileActivity::class.java).apply {
                putExtra(INTENT_P_USER_NAME, userName)
                putExtra(INTENT_P_USER_IMG, userImg)
            }
        }
    }
}