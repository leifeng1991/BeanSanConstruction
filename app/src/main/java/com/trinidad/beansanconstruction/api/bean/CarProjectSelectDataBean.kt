package com.trinidad.beansanconstruction.api.bean

class CarProjectSelectDataBean {
    // 总记录数
    var total: String? = null

    // 数据组
    var records: List<RecordsBean> = mutableListOf()

    class RecordsBean {
        // 主键id
        var id: String? = null

        // 项目名称
        var pname: String? = null

        // 费用类型
        var ctname: String? = null

        // 单价
        var unitPrice: String? = null

        // 单位
        var pattern: String? = null

        // 工程量
        var number: String? = null

        // 总价
        var sumPrice: String? = null

        // 创建时间
        var createTime: String? = null
    }
}