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

        // 修改个人信息
        const val PHONE_USER_UP_DATE = "phone/user/update"

        // 图片上传
        const val PHONE_UPLOAD_IMG = "phone/upload/uploadImg"

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
        const val PHONE_CAR_INFO_SELECT_ONE = "phone/carInfo/selectOne/{id}"

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

        // 车辆列表
        const val PHONE_CAR_INFO_SELECT = "phone/carInfo/select"

        // 人员列表
        const val PHONE_USER_SELECT = "phone/user/select"

        // 费用类型列表
        const val PHONE_COST_TYPE_SELECT = "phone/costType/select"

        // 创建/修改费用类型
        const val PHONE_COST_TYPE_ADD_OR_UPDATE = "phone/costType/addOrUpdate"

        // 费用类型单条
        const val PHONE_COST_TYPE_SELECT_ONE = "phone/costType/selectOne/{id}"

        // 创建/修改费用录入
        const val PHONE_CAR_PROJECT_ADD_OR_UPDATE = "phone/carProject/addOrUpdate"

        // 费用列表
        const val PHONE_CAR_PROJECT_SELECT = "phone/carProject/select"

        // 项目管理（机械管理下）
        const val PHONE_DATA_TABLE_SEL_DATA = "phone/dataTable/selData"

        // 费用单条
        const val PHONE_CAR_PROJECT_SELECT_ONE = "phone/carProject/selectOne/{id}"

        // 项目数据分析
        const val PHONE_DATA_TABLE_PROJECT_DATA = "phone/dataTable/projectTable"

        // 车辆数据分析
        const val PHONE_DATA_TABLE_CAR_TABLE = "phone/dataTable/carTable"

        // 项目详情
        const val PHONE_DATA_TABLE_GET_PROJECT_ONE = "phone/dataTable/getProjectList"

        // 车辆详情
        const val PHONE_DATA_TABLE_GET_CAR_ONE = "phone/dataTable/getCarList"

        // 查询是否有能使用该模块
        const val PHONE_MODEL_SELECT_ONE = "phone/model/selectOne/{code}"

        // 版本检查
        const val PHONE_MODEL_SELECT_ONE_NEW_VERSION = "phone/edition/selectOne/{newversion}"
    }
}