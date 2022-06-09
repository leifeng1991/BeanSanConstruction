package com.trinidad.beansanconstruction.api.bean

class DataTableSelDataBean {
    // 项目数量
    var psum: String? = null

    // 车辆数量
    var carSum: String? = null

    // 车队数量
    var msum: String? = null

    // 人员数量
    var usum: String? = null

    // 项目经营情况
    var project: ProjectBean? = null

    // 车辆经营情况
    var carData: CarDataBean? = null

    class ProjectBean {
        // id
        var id: String? = null

        // 项目名称
        var name: String? = null

        // 产值
        var outputValue: String? = null

        // 费用支付
        var expenditure: String? = null

        // 利润
        var profit: String? = null
    }

    class CarDataBean {
        // id
        var id: String? = null

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