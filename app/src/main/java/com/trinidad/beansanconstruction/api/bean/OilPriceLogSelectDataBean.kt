package com.trinidad.beansanconstruction.api.bean

class OilPriceLogSelectDataBean {
    var records: List<RecordsBean> = ArrayList()

    // 总记录数
    var total: String? = null

    // 每页显示数据
    var size: String? = null

    // 当前页
    var current: String? = null

    // 总共有多少页
    var pages: String? = null

    inner class RecordsBean {
        // id
        var id: String? = null

        // 油品质
        var quality: String? = null

        // 价格
        var price: String? = null

        // 本次加油金额
        var money: String? = null

        // 加油量
        var number: String? = null

        // 车牌
        var carPlate: String? = null

        // 车主
        var personName: String? = null

        // 车型
        var ctName: String? = null

        // 公司
        var scName: String? = null

        // 车队
        var mName: String? = null

        // 加油人员
        var aname: String? = null

        // 创建时间
        var createTime: String? = null

    }
}