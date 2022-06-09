package com.trinidad.beansanconstruction.api.bean

class ProjectVehicleDataBean {
    // 总记录数
    var total: String? = null

    // 数据组
    var records: List<RecordsBean> = mutableListOf()

    class RecordsBean {
        // 主键id
        var id: String? = null

        // 项目名称
        var name: String? = null

        // 车牌
        var carPlate: String? = null

        // 产值
        var outputValue: String? = null

        // 费用支付
        var expenditure: String? = null

        // 利润
        var profit: String? = null
    }
}