package com.trinidad.beansanconstruction.ui.fragment

import android.os.Bundle
import com.moufans.lib_base.base.adapter.BaseFragmentPagerAdapter
import com.moufans.lib_base.base.adapter.BaseFragmentStatePagerAdapter
import com.moufans.lib_base.base.fragment.ViewPageFragment
import com.moufans.lib_base.utils.LogUtil
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.databinding.FragmentMonthTabBinding
import com.trinidad.beansanconstruction.utils.DateTimeUtil


/**
 * 月份
 */
class MonthTabFragment : ViewPageFragment<FragmentMonthTabBinding>() {
    private var mType = 1
    private var mYear = DateTimeUtil.getYear(System.currentTimeMillis()).toInt()
    private val mFragments = ArrayList<ProjectVehicleFragment>()
    private val mTitles = arrayListOf<String>()
    private val mCurrentYear = DateTimeUtil.getYear(System.currentTimeMillis()).toInt()
    private val mCurrentMonth = DateTimeUtil.getMonth(System.currentTimeMillis()).toInt()
    private var mSelectedYear = mCurrentYear
    private var mSelectedMonth = mCurrentMonth

    override fun getLayoutId(): Int {
        return R.layout.fragment_month_tab
    }

    override fun initView() {
        mType = arguments?.getInt(INTENT_P_TYPE) ?: 1
        mYear = arguments?.getInt(INTENT_P_YEAR) ?: DateTimeUtil.getYear(System.currentTimeMillis()).toInt()
        mSelectedYear = mYear
        val monthTotal = if (mCurrentYear == mSelectedYear) mSelectedMonth else 12
        LogUtil.e("=============monthTotal=============$monthTotal")
        for (i in 0 until monthTotal) {
            if (i == 0 && mCurrentYear == mSelectedYear) {
                mTitles.add("本月")
                mFragments.add(ProjectVehicleFragment.newInstance("$mSelectedYear-$mSelectedMonth", mType))
            } else {
                val month = monthTotal - i
                mTitles.add("${month}月")
                mFragments.add(ProjectVehicleFragment.newInstance("$mSelectedYear-${month}", mType))
            }
        }

        val adapter = BaseFragmentStatePagerAdapter(childFragmentManager, mTitles, mFragments)
        mFragmentDataBinding.mViewPager.adapter = adapter
        mFragmentDataBinding.mSlidingTabLayout.setViewPager(mFragmentDataBinding.mViewPager)
    }

    override fun initListener() {

    }

    override fun processingLogic() {
    }


    override fun refreshOnceData() {
    }


    companion object {
        private const val INTENT_P_TYPE = "type"
        private const val INTENT_P_YEAR = "year"

        @JvmStatic
        fun newInstance(type: Int, year: Int) = MonthTabFragment().apply {
            arguments = Bundle().apply {
                putInt(INTENT_P_TYPE, type)
                putInt(INTENT_P_YEAR, year)
            }
        }
    }
}
