package com.trinidad.beansanconstruction.ui.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.moufans.lib_base.base.activity.BaseActivity
import com.moufans.lib_base.base.adapter.BaseFragmentPagerAdapter
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.databinding.ActivityRefuelRecordBinding
import com.trinidad.beansanconstruction.ui.fragment.RefuelRecordFragment

class RefuelRecordActivity : BaseActivity<ActivityRefuelRecordBinding>() {
    private val mFragments = ArrayList<Fragment>().apply {
        add(RefuelRecordFragment.newInstance("2"))
        add(RefuelRecordFragment.newInstance("1"))
    }
    private val mTitles = ArrayList<String>().apply {
        add("当日加油统计")
        add("全部加油记录")
    }

    override fun getDataBindingLayoutResId(): Int {
        return R.layout.activity_refuel_record
    }

    override fun initView() {
        setHeaderTitle("加油记录")
        mDataBinding.mViewPager.adapter = BaseFragmentPagerAdapter(supportFragmentManager, mFragments, mTitles)
        mDataBinding.mCommonTabLayout.setViewPager(mDataBinding.mViewPager)
        mDataBinding.mViewPager.currentItem = 0
    }

    override fun initListener() {
    }

    override fun processingLogic() {
    }


    companion object {

        @JvmOverloads
        fun newIntent(context: Context): Intent {
            return Intent(context, RefuelRecordActivity::class.java)
        }
    }
}