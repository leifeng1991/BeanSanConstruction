package com.trinidad.beansanconstruction.api.bean

class LogonLogSelectDataBean {
    var records: List<RecordsBean> = mutableListOf()

    class RecordsBean {
        // 用户id
        var id: String? = null

        // 姓名
        var name: String? = null

        // 电话
        var phone: String? = null

        // 审核状态（0/拒绝，1/审核中，2/通过）
        var deleted: String? = null

        // 申请时间
        var createTime: String? = null

        // 角色
        var codename: String? = null
        var picUrl: String? = null
    }

}