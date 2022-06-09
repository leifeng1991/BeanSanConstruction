package com.trinidad.beansanconstruction.api.bean

class NewVersionBean {
    // 版本号
    var newversion: String? = null
    // 安装包下载地址
    var url: String? = null
    // 更新内容
    var content: String? = null
    // 是否强制更新（0/否，1/是）
    var status: String? = null
}