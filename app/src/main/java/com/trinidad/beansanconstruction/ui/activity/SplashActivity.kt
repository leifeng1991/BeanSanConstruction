package com.trinidad.beansanconstruction.ui.activity

import android.annotation.SuppressLint
import android.text.TextUtils
import androidx.lifecycle.lifecycleScope
import com.moufans.lib_base.base.activity.BaseActivity
import com.moufans.lib_base.utils.StatusBarUtil
import com.trinidad.beansanconstruction.MainActivity
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.constants.AppConstants
import com.trinidad.beansanconstruction.databinding.ActivitySplashBinding
import com.trinidad.beansanconstruction.utils.DeviceUtils
import com.trinidad.beansanconstruction.utils.SharedPrefUtil
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 启动页
 */
class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    private var mJob: Job? = null

    override fun getDataBindingLayoutResId(): Int {
        return R.layout.activity_splash
    }

    override fun addHeaderView() {

    }

    override fun setStatusBar() {
        StatusBarUtil.setTransparentForImageViewInFragment(this, null)
        StatusBarUtil.setDarkMode(this)
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        mDataBinding.mSplashVersionTextView.text = "Version ${DeviceUtils.getVersionName(this)}"
        mJob = lifecycleScope.launch {
            repeat(REPEAT_TIMES) {
                delay(1000)
                if (it == REPEAT_TIMES - 1) {
                    if (TextUtils.isEmpty(SharedPrefUtil.get(AppConstants.USER_TOKEN, ""))) {
                        // 没登录,跳转到登录页
                        startActivity(LoginActivity.newIntent(this@SplashActivity))
                    } else {
                        // 登录，直接跳转到首页
                        startActivity(MainActivity.newIntent(this@SplashActivity))
                    }

                    finish()
                }
            }

        }
    }

    override fun initListener() {
    }

    override fun processingLogic() {
    }

    companion object {
        private const val REPEAT_TIMES = 3
    }
}