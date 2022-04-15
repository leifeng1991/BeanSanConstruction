package com.trinidad.beansanconstruction.utils

import com.moufans.lib_base.request.net.HttpAddHeadersInterceptor
import com.moufans.lib_base.utils.DeviceUtils
import com.moufans.lib_base.utils.InitUtils.getApplication
import com.trinidad.beansanconstruction.constants.AppConstants
import okhttp3.Request

class AppHttpAddHeadersInterceptor : HttpAddHeadersInterceptor() {
    override fun setHeader(request: Request.Builder) {
        super.setHeader(request)
        request.addHeader("Version", DeviceUtils.getVersionName(getApplication()))
        request.addHeader("token", SharedPrefUtil.get(AppConstants.USER_TOKEN, "") ?: "")
    }
}