package com.trinidad.beansanconstruction.api.bean

class RepairCostSelectDataBean {
    // 总记录数
    var total: String? = null

    // 数据组
    var records: List<RecordsBean> = mutableListOf()

    class RecordsBean {
        // 主键id
        var id: String? = null

        // 维修店名
        var repairName: String? = null

        // 维修日期
        var wxDate: String? = null

        // 申请时间
        var createTime: String? = null

        // 申请人
        var aname: String? = null

        // 车牌/编号
        var carPlate: String? = null

        // 内容
        var content: String? = null

        // 金额
        var money: String? = null

        // 审核状态（0/拒绝，1/审核中，2/通过）
        var deleted: String? = null
    }
}