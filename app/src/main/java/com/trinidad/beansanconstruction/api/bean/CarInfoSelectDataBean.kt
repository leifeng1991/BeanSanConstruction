package com.trinidad.beansanconstruction.api.bean

class CarInfoSelectDataBean {
    // 总记录数
    var total: String? = null

    // 数据组
    var records: List<RecordsBean> = mutableListOf()

    class RecordsBean {
        // 主键id
        var id: String? = null

        // 车牌
        var carPlate: String? = null

        // 编号
        var number: String? = null

        // 车型
        var ctName: String? = null

        // 车主
        var person: String? = null

        // 电话
        var phone: String? = null

        // 车辆品牌
        var brand: String? = null

        // 车辆型号
        var carType: String? = null

        // 发动机编号
        var motor: String? = null

        // 购买时间
        var gmTime: String? = null

        // 车辆价格
        var price: String? = null

        // 1自有/2加盟
        var type: String? = null
    }
}