package com.trinidad.beansanconstruction.ui.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.github.gzuliyujiang.wheelpicker.OptionPicker
import com.moufans.lib_base.base.activity.BaseActivity
import com.moufans.lib_base.base.adapter.BaseFragmentStatePagerAdapter
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.databinding.ActivityDataAnalysisBinding
import com.trinidad.beansanconstruction.ui.fragment.MonthTabFragment
import com.trinidad.beansanconstruction.utils.DateTimeUtil

class DataAnalysisActivity : BaseActivity<ActivityDataAnalysisBinding>() {
    private val mIndex by lazy {
        intent.getIntExtra(INTENT_P_INDEX, 0)
    }
    private val mFragments = ArrayList<Fragment>().apply {
        add(MonthTabFragment.newInstance(1, DateTimeUtil.getYear(System.currentTimeMillis()).toInt()))
        add(MonthTabFragment.newInstance(2, DateTimeUtil.getYear(System.currentTimeMillis()).toInt()))
    }
    private val mTitles = ArrayList<String>().apply {
        add("项目")
        add("车辆")
    }

    private var mDatePicker: OptionPicker? = null

    override fun getDataBindingLayoutResId(): Int {
        return R.layout.activity_data_analysis
    }

    override fun addHeaderView() {

    }

    override fun initView() {
        mDataBinding.mYearTextView.text = DateTimeUtil.getYear(System.currentTimeMillis())
        mDataBinding.mViewPager.adapter = BaseFragmentStatePagerAdapter(supportFragmentManager, mTitles, mFragments)
        mDataBinding.mCommonTabLayout.setViewPager(mDataBinding.mViewPager)
        mDataBinding.mViewPager.currentItem = mIndex
    }

    override fun initListener() {
        mDataBinding.mLeftIv.setOnClickListener {
            finish()
        }
        mDataBinding.mSwitchYearLayout.setOnClickListener {
            showDatePicker()
        }
    }

    override fun processingLogic() {
    }

    private fun showDatePicker() {
        if (mDatePicker != null && mDatePicker!!.isShowing) {
            return
        }
        if (mDatePicker == null) {
            mDatePicker = OptionPicker(this@DataAnalysisActivity)
            mDatePicker!!.setData(mutableListOf<String>().apply {
                add(DateTimeUtil.getYear(System.currentTimeMillis()))
                for (i in 1..50) {
                    add("${DateTimeUtil.getYear(System.currentTimeMillis()).toInt() - i}")
                }

                mDatePicker!!.setOnOptionPickedListener { position, _ ->
                    val index = mDataBinding.mViewPager.currentItem
                    mFragments.clear()
                    mDataBinding.mYearTextView.text = "${this[position]}"
                    mFragments.add(MonthTabFragment.newInstance(1, this[position].toInt()))
                    mFragments.add(MonthTabFragment.newInstance(2, this[position].toInt()))
                    mDataBinding.mViewPager.adapter = BaseFragmentStatePagerAdapter(supportFragmentManager, mTitles, mFragments)
                    mDataBinding.mCommonTabLayout.setViewPager(mDataBinding.mViewPager)
                    mDataBinding.mViewPager.currentItem = index
                    mDataBinding.mCommonTabLayout.currentTab = index

                }
            })

        }
        mDatePicker!!.show()
    }


    companion object {
        private const val INTENT_P_INDEX = "index"

        fun newIntent(context: Context, index: Int = 0): Intent {
            return Intent(context, DataAnalysisActivity::class.java).apply {
                putExtra(INTENT_P_INDEX, index)
            }
        }
    }
}