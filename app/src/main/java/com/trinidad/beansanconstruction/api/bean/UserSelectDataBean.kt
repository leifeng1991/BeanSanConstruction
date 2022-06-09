package com.trinidad.beansanconstruction.api.bean

class UserSelectDataBean {
    // 总记录数
    var total: String? = null

    // 数据组
    var records: List<RecordsBean> = mutableListOf()

    class RecordsBean {
        // 主键id
        var id: String? = null

        // 人员名称
        var name: String? = null

        // 联系电话
        var phone: String? = null

        // 角色
        var code: String? = null

        // 创建时间
        var createTime: String? = null
        var picUrl: String? = null
    }
}