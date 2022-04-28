package com.trinidad.beansanconstruction.api.bean

class CarInfoSelectDataBean {
    // 总记录数
    var total: String? = null

    // 数据组
    var records: List<RecordsBean> = mutableListOf()

    class RecordsBean {
        // 主键id
        var id: String? = null

        // 维修店名
        var name: String? = null

        // 联系人
        var person: String? = null

        // 项目打卡范围
        var phone: String? = null

        // 地址
        var address: String? = null
    }
}