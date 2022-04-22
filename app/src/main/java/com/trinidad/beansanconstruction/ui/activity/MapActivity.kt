package com.trinidad.beansanconstruction.ui.activity

import android.Manifest
import android.annotation.TargetApi
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.inputmethod.EditorInfo
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode
import com.amap.api.location.AMapLocationClientOption.AMapLocationProtocol
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.AMap
import com.amap.api.maps.AMapOptions
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapView
import com.amap.api.maps.model.*
import com.amap.api.services.core.AMapException
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.core.PoiItem
import com.amap.api.services.poisearch.PoiResult
import com.amap.api.services.poisearch.PoiSearch
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.gson.Gson
import com.moufans.lib_base.base.activity.BaseActivity
import com.moufans.lib_base.utils.LogUtil
import com.moufans.lib_base.utils.StatusBarUtil
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.databinding.ActivityMapBinding
import com.trinidad.beansanconstruction.ui.adapter.LocationAdapter
import java.util.*


open class MapActivity : BaseActivity<ActivityMapBinding>(), AMap.OnCameraChangeListener, AMap.OnMapLoadedListener, PoiSearch.OnPoiSearchListener {
    private val mLongitude by lazy {
        intent.getDoubleExtra(INTENT_LONGITUDE, 0.0)
    }
    private val mLatitude by lazy {
        intent.getDoubleExtra(INTENT_LATITUDE, 0.0)
    }
    private var aMap: AMap? = null
    private var mLocationClient: AMapLocationClient? = null
    private var searchLatlonPoint: LatLonPoint? = null
    private var checkinpoint: LatLng? = null
    private lateinit var mBottomSheetBehavior: BottomSheetBehavior<RelativeLayout>
    private var cityCode = ""
    private val mLocationAdapter by lazy {
        LocationAdapter()
    }
    private var locationMarker: Marker? = null
    private var mlocation: LatLng? = null

    // Poi查询条件类
    private var query: PoiSearch.Query? = null

    // POI搜索
    private var poiSearch: PoiSearch? = null
    private var isKeywordSearch = false
    private var mMapView: MapView? = null

    //是否需要检测后台定位权限，设置为true时，如果用户没有给予后台定位权限会弹窗提示
    private val needCheckBackLocation = false

    //如果设置了target > 28，需要增加这个权限，否则不会弹出"始终允许"这个选择框
    private val BACKGROUND_LOCATION_PERMISSION = "android.permission.ACCESS_BACKGROUND_LOCATION"

    /**
     * 需要进行检测的权限数组
     */
    private var needPermissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.READ_PHONE_STATE
    )

    private val PERMISSON_REQUESTCODE = 0

    /**
     * 判断是否需要检测，防止不停的弹框
     */
    private var isNeedCheck = true

    /**
     * 定位监听
     */
    private var locationListener = AMapLocationListener { aMapLocation ->
        if (null != aMapLocation) {
            mlocation = LatLng(aMapLocation.latitude, aMapLocation.longitude)
            //然后可以移动到定位点,使用animateCamera就有动画效果
            searchLatlonPoint = LatLonPoint(aMapLocation.latitude, aMapLocation.longitude)
            if (mLatitude > 0 && mLongitude > 0) {
                aMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(mLatitude, mLongitude), 18f))
            } else {
                aMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(mlocation, 18f))
            }
            checkinpoint = mlocation
            cityCode = aMapLocation.cityCode
            LogUtil.e("location", "定位成功")
        } else {
            LogUtil.e("location", "定位失败，loc is null")
        }
    }

    override fun getDataBindingLayoutResId(): Int {
        return R.layout.activity_map
    }

    override fun setStatusBar() {
        StatusBarUtil.setTransparentForImageViewInFragment(this, null)
        StatusBarUtil.setLightMode(this)
    }

    override fun addHeaderView() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT > 28 && applicationContext.applicationInfo.targetSdkVersion > 28) {
            needPermissions = arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                BACKGROUND_LOCATION_PERMISSION
            )
        }
        mMapView = findViewById(R.id.mMapView)
        aMap = mMapView?.map
        aMap!!.setOnCameraChangeListener(this)
        aMap!!.setOnMapLoadedListener(this)
        aMap!!.uiSettings.isZoomControlsEnabled = false
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView?.onCreate(savedInstanceState)

    }


    override fun initView() {
        mBottomSheetBehavior = BottomSheetBehavior.from(mDataBinding.mContentConstraintLayout)
        // 默认状态为展开
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        mDataBinding.mAddressRecyclerView.apply {
            mDataBinding.mAddressRecyclerView.loadSize = 10
            setPullRefreshAndLoadingMoreEnabled(false, loadingMoreEnabled = true)
            setLayoutManager(LinearLayoutManager(this@MapActivity))
            setAdapter(mLocationAdapter)
        }


    }

    override fun initListener() {
        // 返回
        mDataBinding.mMapBackImageView.setOnClickListener {
            finish()
        }
        mDataBinding.mMapGroupImageView.setOnClickListener {
            //然后可以移动到定位点,使用animateCamera就有动画效果
            aMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(checkinpoint, 18f))
        }
        var isSatellite = true
        // 2D和卫星地图切换
        mDataBinding.mSatelliteImageView.setOnClickListener {
            if (isSatellite) {
                // 2D地图
                mDataBinding.mSatelliteImageView.setImageResource(R.mipmap.ic_map_2d)
                aMap?.mapType = AMap.MAP_TYPE_SATELLITE
            } else {
                // 卫星地图
                mDataBinding.mSatelliteImageView.setImageResource(R.mipmap.ic_map_satellite)
                aMap?.mapType = AMap.MAP_TYPE_NORMAL
            }
            isSatellite = !isSatellite
        }
        // 回到當前位置
        mDataBinding.mLocationImageView.setOnClickListener {
            if (mlocation != null && locationMarker != null) {
                //然后可以移动到定位点,使用animateCamera就有动画效果
                aMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(mlocation, 18f))
            }
        }
        // 搜索
        mDataBinding.mSearchAddressEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                mDataBinding.mAddressRecyclerView.currentPage = 1
                searchKeyword(mDataBinding.mSearchAddressEditText.text.toString().trim())
                true
            }
            false
        }
        mDataBinding.mSearchAddressEditText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                mBottomSheetBehavior.state = STATE_EXPANDED
            }
        }
        mDataBinding.mAddressRecyclerView.setOnLoadMoreListener {
            if (isKeywordSearch) {
                searchKeyword(mDataBinding.mSearchAddressEditText.text.toString().trim())
            } else {
                searchNearBy()
            }
        }
        mLocationAdapter.setOnItemClickListener { _, _, position ->
            val itemBean = mLocationAdapter.data[position]
            val jsonString = Gson().toJson(itemBean)
            setResult(RESULT_OK, Intent().apply { putExtra(INTENT_DATA, jsonString) })
            finish()
        }
    }

    override fun processingLogic() {
    }

    /**
     *
     * @param permissions
     * @since 2.5.0
     */
    private fun checkPermissions(vararg permissions: String) {
        try {
            if (Build.VERSION.SDK_INT >= 23 && applicationInfo.targetSdkVersion >= 23) {
                val needRequestPermissonList = findDeniedPermissions(permissions as Array<String>)
                if (null != needRequestPermissonList && needRequestPermissonList.isNotEmpty()) {
                    val array = needRequestPermissonList.toTypedArray()
                    val method = javaClass.getMethod("requestPermissions", *arrayOf<Class<*>?>(Array<String>::class.java, Int::class.javaPrimitiveType))
                    method.invoke(this, array, PERMISSON_REQUESTCODE)
                } else {
                    setupLocationStyle()
                    initLocation()
                    //开始定位
                    startLocation()
                }
            }
        } catch (e: Throwable) {
        }
    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     * @since 2.5.0
     */
    private fun findDeniedPermissions(permissions: Array<String>): List<String> {
        val needRequestPermissonList: MutableList<String> = ArrayList()
        if (Build.VERSION.SDK_INT >= 23 && applicationInfo.targetSdkVersion >= 23) {
            try {
                for (perm in permissions) {
                    val checkSelfMethod = javaClass.getMethod("checkSelfPermission", String::class.java)
                    val shouldShowRequestPermissionRationaleMethod = javaClass.getMethod("shouldShowRequestPermissionRationale",
                        String::class.java)
                    if (checkSelfMethod.invoke(this, perm) as Int != PackageManager.PERMISSION_GRANTED
                        || shouldShowRequestPermissionRationaleMethod.invoke(this, perm) as Boolean) {
                        if (!needCheckBackLocation && BACKGROUND_LOCATION_PERMISSION == perm) {
                            continue
                        }
                        needRequestPermissonList.add(perm)
                    }
                }
            } catch (e: Throwable) {
            }
        }
        return needRequestPermissonList
    }

    /**
     * 检测是否所有的权限都已经授权
     * @param grantResults
     * @return
     * @since 2.5.0
     */
    private fun verifyPermissions(grantResults: IntArray): Boolean {
        for (result in grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    @TargetApi(23) override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, paramArrayOfInt: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, paramArrayOfInt)
        if (requestCode == PERMISSON_REQUESTCODE) {
            if (!verifyPermissions(paramArrayOfInt)) {
                showMissingPermissionDialog()
                isNeedCheck = false
            } else {
                setupLocationStyle()
                initLocation()
                //开始定位
                startLocation()
            }
        }
    }

    /**
     * 显示提示信息
     *
     * @since 2.5.0
     */
    private fun showMissingPermissionDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("提示")
        builder.setMessage("当前应用缺少必要权限。\\n\\n请点击\\\"设置\\\"-\\\"权限\\\"-打开所需权限。")

        // 拒绝, 退出应用
        builder.setNegativeButton("取消", DialogInterface.OnClickListener { dialog, which -> finish() })
        builder.setPositiveButton("设置", DialogInterface.OnClickListener { dialog, which -> startAppSettings() })
        builder.setCancelable(false)
        builder.show()
    }

    /**
     * 启动应用的设置
     *
     * @since 2.5.0
     */
    private fun startAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:$packageName")
        startActivity(intent)
    }

//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        mHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults)
//    }

    /**
     * 设置自定义定位蓝点
     */
    private fun setupLocationStyle() {
        // 设置默认定位按钮是否显示
        aMap!!.uiSettings.isMyLocationButtonEnabled = false
        aMap!!.uiSettings.logoPosition = AMapOptions.LOGO_POSITION_BOTTOM_LEFT
        aMap!!.uiSettings.setLogoBottomMargin(-200)
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap!!.isMyLocationEnabled = true
        // 自定义系统定位蓝点
        val myLocationStyle = MyLocationStyle()
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW) //只定位一次。
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.shop_gps_point))
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(Color.TRANSPARENT)
        myLocationStyle.strokeColor(Color.parseColor("#4A90E2"))
        myLocationStyle.strokeWidth(resources.getDimension(R.dimen.com_dp2))
        // 将自定义的 myLocationStyle 对象添加到地图上
        aMap!!.myLocationStyle = myLocationStyle
    }

    /**
     * 初始化定位，设置回调监听
     */
    private fun initLocation() {
        //初始化client
        mLocationClient = AMapLocationClient(this.applicationContext)
        // 设置定位监听
        mLocationClient!!.setLocationListener(locationListener)
    }

    /**
     * 开始定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private fun startLocation() {
        //设置定位参数
        mLocationClient!!.setLocationOption(getOption())
        // 启动定位
        mLocationClient!!.startLocation()
    }

    /**
     * 停止定位
     */
    open fun deactivate() {
        mLocationClient?.stopLocation()
        mLocationClient?.onDestroy()
        mLocationClient = null
    }

    /**
     * 设置定位参数
     *
     * @return 定位参数类
     */
    private fun getOption(): AMapLocationClientOption? {
        val mOption = AMapLocationClientOption()
        mOption.locationMode = AMapLocationMode.Hight_Accuracy //可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式

        mOption.isGpsFirst = false //可选，设置是否gps优先，只在高精度模式下有效。默认关闭

        mOption.httpTimeOut = 30000 //可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效

        mOption.interval = 2000 //可选，设置定位间隔。默认为2秒

        mOption.isNeedAddress = true //可选，设置是否返回逆地理地址信息。默认是true

        mOption.isOnceLocation = true //可选，设置是否单次定位。默认是false

        mOption.isOnceLocationLatest = true //可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用

        AMapLocationClientOption.setLocationProtocol(AMapLocationProtocol.HTTP) //可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP

        mOption.isSensorEnable = false //可选，设置是否使用传感器。默认是false

        mOption.isWifiScan = true //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差

        mOption.isLocationCacheEnable = true //可选，设置是否使用缓存定位，默认为true

        mOption.geoLanguage = AMapLocationClientOption.GeoLanguage.DEFAULT //可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）


        return mOption
    }

    /**
     * 添加选点marker
     */
    private fun addMarkerInScreenCenter() {
        val latLng = aMap!!.cameraPosition.target
        val screenPosition = aMap!!.projection.toScreenLocation(latLng)
        locationMarker = aMap!!.addMarker(MarkerOptions()
            .anchor(0.5f, 0.5f))
        locationMarker!!.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.shop_repat_store_location))
        //设置Marker在屏幕上,不跟随地图移动
        locationMarker!!.setPositionByPixels(screenPosition.x, screenPosition.y)
    }

    override fun onDestroy() {
        super.onDestroy()
        mLocationClient?.onDestroy()
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView?.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView?.onResume()
        if (Build.VERSION.SDK_INT >= 23 && applicationInfo.targetSdkVersion >= 23) {
            if (isNeedCheck) {
                checkPermissions(*needPermissions)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView?.onPause()
        deactivate()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView?.onSaveInstanceState(outState)
    }

    override fun onCameraChange(p0: CameraPosition) {
        LogUtil.e("===================onCameraChange======================")

    }

    override fun onCameraChangeFinish(cameraPosition: CameraPosition) {
        LogUtil.e("===================onCameraChangeFinish======================")
        checkinpoint = cameraPosition.target
        searchLatlonPoint?.latitude = checkinpoint?.latitude ?: 0.0
        searchLatlonPoint?.longitude = checkinpoint?.longitude ?: 0.0
        mDataBinding.mAddressRecyclerView.currentPage = 1
        searchNearBy()
    }

    override fun onMapLoaded() {
        LogUtil.e("===================onMapLoaded======================")
        addMarkerInScreenCenter()
    }

    /**
     * 周边搜索
     */
    private fun searchNearBy() {
        showLoading()
        isKeywordSearch = false
        // 第一个参数表示搜索字符串，第二个参数表示POI搜索类型二选其一
        // 第三个参数表示POI搜索区域的编码，必设

        if (query == null) {
            query = PoiSearch.Query("", "", "")
            query!!.pageSize = mDataBinding.mAddressRecyclerView.loadSize
        }

        query!!.pageNum = mDataBinding.mAddressRecyclerView.currentPage

        try {
            if (poiSearch == null) {
                poiSearch = PoiSearch(this, query)
                poiSearch!!.setOnPoiSearchListener(this)
            }
            poiSearch!!.bound = PoiSearch.SearchBound(searchLatlonPoint, 2000, true)
            poiSearch!!.searchPOIAsyn()
        } catch (e: AMapException) {
            e.printStackTrace();
        }

    }

    /**
     * @param keyword 关键字搜索
     */
    private fun searchKeyword(keyword: String?) {
        showLoading()
        isKeywordSearch = true
        val query = PoiSearch.Query(keyword, "", cityCode)
        query.pageSize = mDataBinding.mAddressRecyclerView.loadSize// 设置每页最多返回多少条poiitem
        query.pageNum = mDataBinding.mAddressRecyclerView.currentPage//设置查询页码
        try {
            val poiSearch = PoiSearch(this, query)
            poiSearch.setOnPoiSearchListener(this)
            //开始搜索
            poiSearch.searchPOIAsyn()
        } catch (e: AMapException) {
            e.printStackTrace();
        }

    }

    override fun onPoiSearched(poiResult: PoiResult, p1: Int) {
        hideLoading()
        query = null
        poiSearch = null
        if (mDataBinding.mAddressRecyclerView.currentPage == 1) {
            poiResult.pois.add(PoiItem("", searchLatlonPoint, "", ""))
        }
        LogUtil.e("===================onPoiSearched===================${poiResult.pois.toArray().contentToString()}===")
        mDataBinding.mAddressRecyclerView.handlerSuccess(mLocationAdapter, poiResult.pois ?: mutableListOf())
//        mDataBinding.mContentTextView.text = poiResult.pois.toArray().contentToString()
    }

    override fun onPoiItemSearched(p0: PoiItem?, p1: Int) {
        LogUtil.e("===================onPoiItemSearched======================")
    }


    companion object {
        private const val INTENT_DATA = "data"
        private const val INTENT_LONGITUDE = "longitude"
        private const val INTENT_LATITUDE = "latitude"

        @JvmOverloads
        fun newIntent(context: Context, longitude: Double = 0.0, latitude: Double = 0.0): Intent {
            return Intent(context, MapActivity::class.java).apply {
                putExtra(INTENT_LONGITUDE, longitude)
                putExtra(INTENT_LATITUDE, latitude)
            }
        }

        fun getResult(data: Intent): String {
            return data.getStringExtra(INTENT_DATA) ?: ""
        }
    }


}