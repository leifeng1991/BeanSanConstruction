package com.trinidad.beansanconstruction

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TabHost
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTabHost
import androidx.lifecycle.lifecycleScope
import com.amap.api.location.AMapLocationClient
import com.moufans.lib_base.base.activity.BaseActivity
import com.moufans.lib_base.ext.convertReqExecute
import com.moufans.lib_base.utils.StatusBarUtil
import com.trinidad.beansanconstruction.constants.AppConstants
import com.trinidad.beansanconstruction.databinding.ActivityMainBinding
import com.trinidad.beansanconstruction.event.LoginEvent
import com.trinidad.beansanconstruction.ext.appApi
import com.trinidad.beansanconstruction.ui.activity.LoginActivity
import com.trinidad.beansanconstruction.ui.fragment.HomeFragment
import com.trinidad.beansanconstruction.ui.fragment.MineFragment
import com.trinidad.beansanconstruction.utils.SharedPrefUtil
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : BaseActivity<ActivityMainBinding>() {
    private val mFragmentList = listOf(HomeFragment::class.java, Fragment::class.java, MineFragment::class.java)
    private val mTabImageList = listOf(R.drawable.selector_main_tab_home, R.drawable.selector_main_tab_scan, R.drawable.selector_main_tab_mine)
    private val mTabTextList = listOf("首页", "扫码登录", "我的")
    private var mFragmentTabHost: FragmentTabHost? = null

    override fun getDataBindingLayoutResId(): Int {
        return R.layout.activity_main
    }

    override fun addHeaderView() {

    }

    override fun onResume() {
        super.onResume()
        getUserInfo()
    }

    override fun setStatusBar() {
        StatusBarUtil.setTransparentForImageViewInFragment(this, null)
        StatusBarUtil.setLightMode(this)
    }

    override fun initView() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this)
        AMapLocationClient.updatePrivacyShow(this@MainActivity, true, true)
        AMapLocationClient.updatePrivacyAgree(this@MainActivity, true)
        // 实例化TabHost对象，得到TabHost
        mFragmentTabHost = findViewById(R.id.mMainTabBottomTabHost)
        mFragmentTabHost!!.setup(this, supportFragmentManager, R.id.mMainTabContentFl)
        mFragmentTabHost!!.tabWidget?.dividerDrawable = null
        // 得到fragment的个数
        val count = mFragmentList.size
        for (i in 0 until count) { // 为每一个Tab按钮设置图标、文字和内容
            val tabItemView = getTabItemView(i)
            val tabSpec: TabHost.TabSpec = mFragmentTabHost!!.newTabSpec(mTabTextList[i]).setIndicator(tabItemView)
            // 将Tab按钮添加进Tab选项卡中
            mFragmentTabHost?.addTab(tabSpec, mFragmentList[i], null)
            // 设置Tab点击事件
            tabItemView.id = i
            tabItemView.setOnClickListener { setCurrentIndex(i) } // 此事件会消费，原生的点击事件，所以得自己处理
        }
    }

    override fun initListener() {
    }

    override fun processingLogic() {
    }

    private fun getUserInfo() {
        lifecycleScope.launch {
            convertReqExecute({ appApi.findAdminInfo() }, onSuccess = {
                SharedPrefUtil.put(AppConstants.USER_CODE, it.code)
            })
        }
    }

    /**
     * 给Tab按钮设置图标和文字
     */
    private fun getTabItemView(index: Int): View {
        val view = View.inflate(applicationContext, R.layout.layout_main_tab_item_view, null)
        val imageView = view.findViewById<ImageView>(R.id.iv_tab_icon)
        val textView = view.findViewById<TextView>(R.id.tv_tab_title)
        // 设置值
        imageView.visibility = View.VISIBLE
        textView.visibility = View.VISIBLE
        imageView.setImageResource(mTabImageList[index])
        textView.text = mTabTextList[index]
        return view
    }

    private fun setCurrentIndex(currentIndex: Int) {
        // 已在当前显示
        if (mFragmentTabHost?.currentTab == currentIndex) return
        setCurrentTab(currentIndex)
    }

    fun setCurrentTab(currentIndex: Int) {
        // 设置当前的tab
        if (currentIndex == 1) {
            // 跳到扫码界面
        } else {
            // 切换
            mFragmentTabHost?.currentTab = currentIndex
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun jumpLogin(event: LoginEvent) {
        startActivity(LoginActivity.newIntent(this))
        finish()
    }

    companion object {

        @JvmOverloads
        fun newIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}