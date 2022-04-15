package com.trinidad.beansanconstruction.api.bean

import com.github.gzuliyujiang.wheelview.contract.TextProvider

class OilPriceListBean : TextProvider {
    // 项目id
    var id: String? = null

    // 油品质
    var quality: String? = null

    // 价格
    var money: String? = null

    // 时间
    var createTime: String? = null
    override fun provideText(): String {
        return quality ?: ""
    }
}