package com.trinidad.beansanconstruction.api.bean

class TransportSelectDataBean {
    // 总记录数
    var total: String? = null
    // 数据组
    var records: List<RecordsListBean> = ArrayList()

    class RecordsListBean {
        // 主键id
        var id: String? = null
        // 项目名称
        var startPoint: String? = null
        // 项目id
        var startId: String? = null
        // 项目打卡范围
        var startExtent: String? = null
        // 倒土点名称
        var endPoint: String? = null
        // 倒土点id
        var endId: String? = null
        // 倒土点打卡范围
        var endExtent: String? = null
        // 打卡范围
        var extent: String? = null
        // 价钱
        var money: String? = null
        // 0/停用，1/启用
        var deleted: String? = null
    }
}