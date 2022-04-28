package com.trinidad.beansanconstruction.constants

interface AppURLConstants {
    companion object {
        // 账号密码登录
        const val PHONE_USER_LOGIN = "phone/user/login"

        // 验证码登录
        const val PHONE_USER_LOGIN_CODE = "phone/user/loginCode"

        // 获取验证码
        const val PHONE_USER_GET_VERIFY_CODE = "phone/user/getVerifyCode"

        // 退出登录
        const val PHONE_USER_LOGIN_OUT = "phone/user/loginOut"

        // 查询公司
        const val PHONE_USER_SEL_COMPANY = "phone/user/selCompany"

        // 查询角色列表
        const val PHONE_USER_SEL_IDENTITY = "phone/user/selidentity"

        // 验证码注册
        const val PHONE_USER_LOGON_CODE = "phone/user/logonCode"

        // 获取当前用户信息
        const val PHONE_USER_FIND_ADMIN_INFO = "phone/user/findAdminInfo"

        // 修改密码
        const val PHONE_USER_UP_PASS = "phone/user/upPass"

        // 新增修改项目
        const val PHONE_PROJECT_ADD_OR_UPDATE = "phone/project/addOrUpdate"

        // 查询项目列表
        const val PHONE_TRANSPORT_GET_PROJECT = "phone/transport/getProject"

        // 新增修改倒土点
        const val PHONE_POINT_ADD_OR_UPDATE = "phone/point/addOrUpdate"

        // 查询倒土点列表
        const val PHONE_TRANSPORT_GET_POINT = "phone/transport/getPoint"

        // 新增修改运输线路
        const val PHONE_TRANSPORT_ADD_UPDATE = "phone/transport/addUpdate"

        // 分页查询运输线路
        const val PHONE_TRANSPORT_SELECT = "phone/transport/select"

        // 线路启/停用
        const val PHONE_TRANSPORT_ON_OR_OFF = "phone/transport/onOrOff"

        // 授权登录
        const val PHONE_USER_POST_KEY = "phone/user/postKey"

        // 查询油价列表
        const val PHONE_OIL_PRICE_SELECT = "phone/oilPrice/select"

        // 修改油价
        const val PHONE_OIL_PRICE_UPDATE = "phone/oilPrice/update"

        // 扫码查询当前车辆信息
        const val PHONE_CAR_INFO_SELECT_ONE = "phone/CarInfo/selectOne/{id}"

        // 加油
        const val PHONE_OIL_PRICE_LOG_INSERT = "phone/oilPriceLog/insert"

        // 分页查询加油记录
        const val PHONE_OIL_PRICE_LOG_SELECT = "phone/oilPriceLog/select"

        // 维修店列表
        const val PHONE_REPAIR_SELECT = "phone/repair/select"

        // 车辆下拉数据
        const val PHONE_CAR_INFO_SEL_LIST = "phone/carInfo/selList"

        // 创建维修订单
        const val PHONE_REPAIR_COST_ADD = "phone/repairCost/add"

        // 维修订单列表
        const val PHONE_REPAIR_COST_SELECT = "phone/repairCost/select"

        // 维修订单详情
        const val PHONE_REPAIR_COST_SELECT_ONE = "phone/repairCost/selectOne/{id}"

        // 维修订单审批
        const val PHONE_REPAIR_COST_UPDATE = "phone/repairCost/update"

        // 人员管理列表
        const val PHONE_LOGON_LOG_SELECT = "phone/logonLog/select"

        // 人员管理审批
        const val PHONE_LOGON_LOG_UPDATE = "phone/logonLog/update"
    }
}