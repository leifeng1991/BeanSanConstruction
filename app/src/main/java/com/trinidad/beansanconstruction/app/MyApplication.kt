package com.trinidad.beansanconstruction.app

import com.moufans.lib_base.base.BaseApplication
import com.trinidad.beansanconstruction.utils.SharedPrefUtil
import com.umeng.commonsdk.UMConfigure

class MyApplication : BaseApplication() {
    
    override fun onCreate() {
        super.onCreate()
        instance = this
        SharedPrefUtil.init(this)
        //初始化组件化基础库, 所有友盟业务SDK都必须调用此初始化接口。
        UMConfigure.init(this, "623be2eb3b35c7047a11c6ec", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "")
    }
    
    companion object {
        @JvmStatic lateinit var instance: MyApplication
            private set
    }
}