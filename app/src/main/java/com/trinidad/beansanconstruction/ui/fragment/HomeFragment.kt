package com.trinidad.beansanconstruction.ui.fragment

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.moufans.lib_base.base.fragment.ViewPageFragment
import com.moufans.lib_base.utils.StatusBarUtil
import com.moufans.lib_base.utils.ToastUtil
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.constants.AppConstants
import com.trinidad.beansanconstruction.databinding.FragmentNewHomeBinding
import com.trinidad.beansanconstruction.ui.activity.*
import com.trinidad.beansanconstruction.ui.adapter.HomeBannerAdapter
import com.trinidad.beansanconstruction.ui.adapter.HomeFeatureListAdapter
import com.trinidad.beansanconstruction.ui.bean.FeatureListBean
import com.trinidad.beansanconstruction.utils.SharedPrefUtil
import com.youth.banner.indicator.RectangleIndicator


/**
 * 首页
 */
class HomeFragment : ViewPageFragment<FragmentNewHomeBinding>() {
    private val mHomeFeatureListAdapter by lazy {
        HomeFeatureListAdapter(data = mutableListOf<FeatureListBean>().apply {
            add(FeatureListBean(R.mipmap.ic_home_menu_refuel, "加油"))
            add(FeatureListBean(R.mipmap.ic_home_menu_mechanical_management, "机械管理"))
            add(FeatureListBean(R.mipmap.ic_home_menu_vehicle_maintenance, "车辆维修"))
            add(FeatureListBean(R.mipmap.ic_home_menu_pm, "项目管理"))
            add(FeatureListBean(R.mipmap.ic_home_menu_personnel_management, "人员管理"))
            add(FeatureListBean(R.mipmap.ic_home_menu_other, "其他"))
        })
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_new_home
    }

    override fun initView() {
        mFragmentDataBinding.mHeaderBarLayout.setPadding(0, StatusBarUtil.getStatusBarHeight(requireContext()), 0, 0)
        mFragmentDataBinding.mMenuRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = mHomeFeatureListAdapter
        }
        val list = mutableListOf<String>()
        list.add("")
        mFragmentDataBinding.mHomeBanner.setAdapter(HomeBannerAdapter(requireContext(), list)).addBannerLifecycleObserver(this@HomeFragment).indicator = RectangleIndicator(context)
    }

    override fun initListener() {
        // 扫码
        mFragmentDataBinding.mScanCodeImageView.setOnClickListener {
            startActivity(RichScanActivity.newIntent(requireContext()))
        }
        // 功能列表点击事件
        mHomeFeatureListAdapter.setOnItemClickListener { _, _, position ->
            val title = mHomeFeatureListAdapter.data[position].title
//            if (SharedPrefUtil.get(AppConstants.USER_CODE, "") == "YSSJ" && title != "车辆维修") {
//                ToastUtil.showShort("请向管理员开通权限")
//                return@setOnItemClickListener
//            }
            when (title) {
                // 加油
                "加油" -> {
                    startActivity(RefuelManagementActivity.newIntent(requireContext()))
                }
                // 机械管理
                "机械管理" -> {
                    startActivity(MechanicalManagementActivity.newIntent(requireContext()))
                }
                "车辆维修" -> {
                    startActivity(MaintainManagementActivity.newIntent(requireContext()))
                }
                // 项目管理
                "项目管理" -> {
                    startActivity(ProjectManagementActivity.newIntent(requireContext()))
                }
                // 人员管理
                "人员管理" -> {
                    startActivity(PeopleManagementActivity.newIntent(requireContext()))
                }
                // 其他
                "其他" -> {

                }
            }
        }
    }

    override fun processingLogic() {
    }

    override fun refreshOnceData() {
    }

    override fun onStart() {
        super.onStart()
        mFragmentDataBinding.mHomeBanner.start()
    }

    override fun onStop() {
        super.onStop()
        mFragmentDataBinding.mHomeBanner.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mFragmentDataBinding.mHomeBanner.destroy()
    }

    companion object {

        @JvmStatic
        fun newInstance() = HomeFragment().apply { arguments = Bundle().apply {} }
    }
}
