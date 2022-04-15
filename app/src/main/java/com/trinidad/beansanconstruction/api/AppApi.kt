package com.trinidad.beansanconstruction.api

import com.moufans.lib_base.request.BaseResp
import com.trinidad.beansanconstruction.api.bean.*
import com.trinidad.beansanconstruction.constants.AppURLConstants
import com.trinidad.beansanconstruction.constants.RequestParamConstants
import okhttp3.RequestBody
import retrofit2.http.*


interface AppApi {

    /**
     * 获取验证码
     */
    @POST(AppURLConstants.PHONE_USER_GET_VERIFY_CODE)
    suspend fun getVerifyCode(@Body requestBody: RequestBody): BaseResp<Unit>

    /**
     * 账号密码登录
     */
    @POST(AppURLConstants.PHONE_USER_LOGIN)
    suspend fun login(@Body requestBody: RequestBody): BaseResp<LoginDataBean>

    /**
     * 验证码登录
     */
    @POST(AppURLConstants.PHONE_USER_LOGIN_CODE)
    suspend fun loginCode(@Body requestBody: RequestBody): BaseResp<LoginDataBean>

    /**
     * 验证码注册
     */
    @POST(AppURLConstants.PHONE_USER_LOGON_CODE)
    suspend fun logonCode(@Body requestBody: RequestBody): BaseResp<LoginDataBean>

    /**
     * 获取当前用户信息
     */
    @GET(AppURLConstants.PHONE_USER_FIND_ADMIN_INFO)
    suspend fun findAdminInfo(): BaseResp<UserDataBean>

    /**
     * 修改密码
     */
    @POST(AppURLConstants.PHONE_USER_UP_PASS)
    suspend fun upPass(@Body requestBody: RequestBody): BaseResp<Unit>

    /**
     * 退出登录
     */
    @POST(AppURLConstants.PHONE_USER_LOGIN_OUT)
    suspend fun loginOut(): BaseResp<Unit>

    /**
     * 查询公司
     */
    @GET(AppURLConstants.PHONE_USER_SEL_COMPANY)
    suspend fun selCompany(@Query(RequestParamConstants.KEYWORD) keyWord: String): BaseResp<List<CompanyDataBean>>

    /**
     * 查询角色列表
     */
    @GET(AppURLConstants.PHONE_USER_SEL_IDENTITY)
    suspend fun selIdentity(): BaseResp<List<RoleDataBean>>

    /**
     * 新增修改项目
     */
    @POST(AppURLConstants.PHONE_PROJECT_ADD_OR_UPDATE)
    suspend fun addOrUpdate(@Body requestBody: RequestBody): BaseResp<Unit>

    /**
     * 查询项目列表
     */
    @GET(AppURLConstants.PHONE_TRANSPORT_GET_PROJECT)
    suspend fun getProject(@Query(RequestParamConstants.KEYWORD) keyWord: String): BaseResp<List<ProjectListDataBean>>

    /**
     * 新增修改倒土点
     */
    @POST(AppURLConstants.PHONE_POINT_ADD_OR_UPDATE)
    suspend fun addOrUpdatePoint(@Body requestBody: RequestBody): BaseResp<Unit>

    /**
     * 查询项目列表
     */
    @GET(AppURLConstants.PHONE_TRANSPORT_GET_POINT)
    suspend fun getPoint(@Query(RequestParamConstants.KEYWORD) keyWord: String): BaseResp<List<ProjectListDataBean>>

    /**
     * 新增修改运输线路
     */
    @POST(AppURLConstants.PHONE_TRANSPORT_ADD_UPDATE)
    suspend fun addUpdate(@Body requestBody: RequestBody): BaseResp<String>

    /**
     * 分页查询运输线路
     */
    @GET(AppURLConstants.PHONE_TRANSPORT_SELECT)
    suspend fun select(@Query(RequestParamConstants.PAGE_NUM) pageNum: String, @Query(RequestParamConstants.PAGE_SIZE) pageSize: String = "10"): BaseResp<TransportSelectDataBean>

    /**
     * 线路启/停用
     */
    @POST(AppURLConstants.PHONE_TRANSPORT_ON_OR_OFF)
    suspend fun onOrOff(@Body requestBody: RequestBody): BaseResp<Boolean>

    /**
     *授权登录
     */
    @GET(AppURLConstants.PHONE_USER_POST_KEY)
    suspend fun postKey(@Query(RequestParamConstants.KEY) key: String): BaseResp<String>

    /**
     * 查询油价列表
     */
    @GET(AppURLConstants.PHONE_OIL_PRICE_SELECT)
    suspend fun selOilPrice(@Query(RequestParamConstants.KEYWORD) keyWord: String): BaseResp<List<OilPriceListBean>>

    /**
     * 修改油价
     */
    @POST(AppURLConstants.PHONE_OIL_PRICE_UPDATE)
    suspend fun updateOilPrice(@Body requestBody: RequestBody): BaseResp<String>

    /**
     * 扫码查询当前车辆信息
     */
    @GET(AppURLConstants.PHONE_CAR_INFO_SELECT_ONE)
    suspend fun selectOne(@Path("id") id: String): BaseResp<SelectOneDataBean>

    /**
     * 加油
     */
    @POST(AppURLConstants.PHONE_OIL_PRICE_LOG_INSERT)
    suspend fun oilPriceLogInsert(@Body requestBody: RequestBody): BaseResp<Boolean>


    /**
     * 分页查询加油记录
     */
    @GET(AppURLConstants.PHONE_OIL_PRICE_LOG_SELECT)
    suspend fun oilPriceLogSelect(@Query(RequestParamConstants.TYPE) type: String,@Query(RequestParamConstants.PAGE_NUM) pageNum: String, @Query(RequestParamConstants.PAGE_SIZE) pageSize: String = "10"): BaseResp<OilPriceLogSelectDataBean>
}

