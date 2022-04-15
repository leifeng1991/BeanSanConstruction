package com.trinidad.beansanconstruction.ext

import com.moufans.lib_base.request.net.HttpLogInterceptor
import com.moufans.lib_base.request.net.RetrofitFactory
import com.trinidad.beansanconstruction.BuildConfig.BASE_URL
import com.trinidad.beansanconstruction.api.AppApi
import com.trinidad.beansanconstruction.utils.AppHttpAddHeadersInterceptor
import com.trinidad.beansanconstruction.utils.LoginInterceptor

/**
 * 功能描述：扩展
 **/
val RetrofitFactory.Companion.appInstance
    get() = getInstance(BASE_URL, AppHttpAddHeadersInterceptor(), HttpLogInterceptor(), LoginInterceptor())

val appApi by lazy { RetrofitFactory.appInstance.create(AppApi::class.java) }