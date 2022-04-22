package com.trinidad.beansanconstruction.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.moufans.lib_base.base.fragment.ViewPageFragment
import com.moufans.lib_base.ext.baseReqExecute
import com.moufans.lib_base.ext.convertReqExecute
import com.moufans.lib_base.ext.setOnClickListener2
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.api.bean.UserDataBean
import com.trinidad.beansanconstruction.constants.AppConstants
import com.trinidad.beansanconstruction.databinding.FragmentMineBinding
import com.trinidad.beansanconstruction.ext.appApi
import com.trinidad.beansanconstruction.ui.activity.ChangePasswordActivity
import com.trinidad.beansanconstruction.ui.activity.EditProfileActivity
import com.trinidad.beansanconstruction.ui.activity.LoginActivity
import com.trinidad.beansanconstruction.ui.activity.SettingActivity
import com.trinidad.beansanconstruction.utils.CommonDialogUtil
import com.trinidad.beansanconstruction.utils.DeviceUtils
import com.trinidad.beansanconstruction.utils.DialogBuilder
import com.trinidad.beansanconstruction.utils.SharedPrefUtil
import kotlinx.coroutines.launch


/**
 * 我的
 */
class MineFragment : ViewPageFragment<FragmentMineBinding>() {
    private var mUserInfo: UserDataBean? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_mine
    }

    override fun onResume() {
        super.onResume()
        getUserInfo()
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        mFragmentDataBinding.mVersionsTextView.text = "V ${DeviceUtils.getVersionName(requireContext())}"
    }

    override fun initListener() {
        mFragmentDataBinding.apply {
            // 设置
            mSettingTextView.setOnClickListener2 {
                if (mUserInfo != null)
                    startActivity(SettingActivity.newIntent(requireContext(), mUserInfo?.phone ?: ""))
            }
            // 编辑资料
            mEditProfileTextView.setOnClickListener2 {
                startActivity(EditProfileActivity.newIntent(requireContext()))
            }
            // 个人资料
            mUserIconImageView.setOnClickListener2 {

            }
            // 密码设置
            mPasswordSettingTextView.setOnClickListener2 {
                startActivity(ChangePasswordActivity.newIntent(requireContext()))
            }
            // 退出登录
            mLogoutRTextView.setOnClickListener2 {
                CommonDialogUtil.commonGeneralDialog(requireActivity(), "退出登录", "确认要退出登录吗？", object : CommonDialogUtil.OnButtonClickListener {
                    override fun onLeftButtonClick(dialog: DialogBuilder) {
                        logout()
                    }

                    override fun onRightOrCenterButtonClick(dialog: DialogBuilder) {
                    }
                }, "确认", "取消")
            }
        }
    }

    override fun processingLogic() {
    }

    override fun refreshOnceData() {
    }

    private fun logout() {
        lifecycleScope.launch {
            baseReqExecute({ appApi.loginOut() }, onSuccess = {
            }, onFailure = { _, status, _ ->
                if (status == -205) {
                    SharedPrefUtil.put(AppConstants.USER_TOKEN, "")
                    SharedPrefUtil.put(AppConstants.USER_CODE, "")
                    startActivity(LoginActivity.newIntent(requireContext()))
                    requireActivity().finish()
                }
            }, baseView = this@MineFragment)
        }
    }

    private fun getUserInfo() {
        lifecycleScope.launch {
            convertReqExecute({ appApi.findAdminInfo() }, onSuccess = {
                mUserInfo = it
                mFragmentDataBinding.mUserNameTextView.text = it.name ?: "--"
                mFragmentDataBinding.mUserIdTextView.text = "ID：${it.id}"
            })
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = MineFragment().apply { arguments = Bundle().apply {} }
    }
}
