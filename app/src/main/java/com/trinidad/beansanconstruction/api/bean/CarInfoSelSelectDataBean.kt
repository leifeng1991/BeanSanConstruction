package com.trinidad.beansanconstruction.api.bean

import com.github.gzuliyujiang.wheelview.contract.TextProvider

class CarInfoSelSelectDataBean : TextProvider {
    // 主键id
    var id: String? = null

    // 车牌编号
    var carPlate: String? = null
    override fun provideText(): String {
        return carPlate ?: "--"
    }


}