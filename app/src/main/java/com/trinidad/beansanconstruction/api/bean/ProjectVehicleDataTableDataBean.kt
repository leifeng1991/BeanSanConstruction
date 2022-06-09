package com.trinidad.beansanconstruction.api.bean

class ProjectVehicleDataTableDataBean {
    // 项目id
    var id: String? = null

    // 项目名称
    var name: String? = null

    // 项目地址
    var siteName: String? = null

    // 车牌
    var carPlate: String? = null

    // 车辆编号
    var number: String? = null

    // 联系方式
    var phone: String? = null

    // 车主
    var person: String? = null

    // 车型
    var ctName: String? = null

    // 数据组
    var list: List<List<String>> = mutableListOf()

    class ListBean {
        // 管理费
        var glf: String? = null

        // 月产值
        var ycz: String? = null

        // 工资开支
        var gzkz: String? = null

        // 保养维修
        var bywx: String? = null

        // 月油费
        var yyf: String? = null

        // 顶班费
        var dbf: String? = null

        // 燃油比
        var ryb: String? = null

        // 总费用
        var zfy: String? = null

        // 月份
        var yf: String? = null

        // 月利润
        var ylr: String? = null
    }
}