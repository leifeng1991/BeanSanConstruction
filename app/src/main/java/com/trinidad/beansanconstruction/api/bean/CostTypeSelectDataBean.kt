package com.trinidad.beansanconstruction.api.bean

class CostTypeSelectDataBean {
    // 总记录数
    var total: String? = null

    // 数据组
    var records: List<RecordsBean> = mutableListOf()

    class RecordsBean {
        // 主键id
        var id: String? = null

        // 费用名称
        var name: String? = null

        // 计费单位
        var pattern: String? = null

        // 费用单价
        var price: String? = null

        // 提成金额
        var money: String? = null

        // 提成比例
        var number: String? = null

        // 1/有提成，2/无提成
        var type: String? = null

        // 创建时间
        var createTime: String? = null
    }
}