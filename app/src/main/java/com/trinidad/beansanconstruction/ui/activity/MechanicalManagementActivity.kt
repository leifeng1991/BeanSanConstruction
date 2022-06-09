package com.trinidad.beansanconstruction.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.moufans.lib_base.base.activity.BaseActivity
import com.moufans.lib_base.ext.convertReqExecute
import com.moufans.lib_base.utils.StatusBarUtil
import com.moufans.lib_base.utils.span.AndroidSpan
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.api.bean.CarInfoSelectDataBean
import com.trinidad.beansanconstruction.api.bean.DataTableSelDataBean
import com.trinidad.beansanconstruction.api.bean.ProjectListDataBean
import com.trinidad.beansanconstruction.databinding.ActivityMechanicalManagementBinding
import com.trinidad.beansanconstruction.ext.appApi
import kotlinx.coroutines.launch

class MechanicalManagementActivity : BaseActivity<ActivityMechanicalManagementBinding>() {
    // 项目id（为空时，默认第一条数据）
    private var mPid = ""

    // 车辆id（为空时，默认第一条数据）
    private var mCarId = ""

    private var mDataTableSelDataBean: DataTableSelDataBean? = null

    override fun getDataBindingLayoutResId(): Int {
        return R.layout.activity_mechanical_management
    }

    override fun setStatusBar() {
        StatusBarUtil.setTranslucent(this)
        StatusBarUtil.setColor(this, Color.parseColor("#ff13a666"), 0)
        StatusBarUtil.setLightMode(this)
    }

    override fun initView() {
        setHeaderLeftImage(R.mipmap.repast_ic_shop_home_back_white)
        mHeaderBarDataBinding?.root?.setBackgroundColor(Color.parseColor("#ff13a666"))
        setHeaderTitle(AndroidSpan().drawForegroundColor("项目管理", Color.WHITE).spanText)
        mDataBinding.mProjectInclude.mSwitchTextView.text = "切换项目"
    }

    override fun initListener() {
        mDataBinding.apply {
            // 费用类型
            mCostTypesLayout.setOnClickListener {
                lifecycleScope.launch {
                    convertReqExecute({ appApi.selectOneCode("JX_FYLX") }, onSuccess = {
                        startActivity(CostTypesActivity.newIntent(this@MechanicalManagementActivity))
                    })
                }

            }
            // 费用录入
            mCostEntryLayout.setOnClickListener {
                lifecycleScope.launch {
                    convertReqExecute({ appApi.selectOneCode("JX_FYLR") }, onSuccess = {
                        startActivity(CostInputActivity.newIntent(this@MechanicalManagementActivity))
                    })
                }

            }
            // 数据分析
            mDataAnalystLayout.setOnClickListener {
                lifecycleScope.launch {
                    convertReqExecute({ appApi.selectOneCode("JX_SJFX") }, onSuccess = {
                        startActivity(DataAnalysisActivity.newIntent(this@MechanicalManagementActivity))
                    })
                }

            }
            // 查询
            mQueryLayout.setOnClickListener {
                lifecycleScope.launch {
                    convertReqExecute({ appApi.selectOneCode("JX_CX") }, onSuccess = {
                        startActivity(CostQueryActivity.newIntent(this@MechanicalManagementActivity))
                    })
                }
            }
            // 项目数量
            mProjectOneLayout.setOnClickListener {
                startActivity(ProjectListActivity.newIntent(this@MechanicalManagementActivity, 0))
            }
            // 车辆数量
            mProjectTwoLayout.setOnClickListener {
                startActivity(ChooseCarActivity.newIntent(this@MechanicalManagementActivity))
            }
            // 车队数量
            mProjectThreeLayout.setOnClickListener { }
            // 员工数量
            mProjectFourLayout.setOnClickListener {
                startActivity(PeopleActivity.newIntent(this@MechanicalManagementActivity))
            }
            // 更多项目
            mMoreProjectTextView.setOnClickListener {
                startActivity(DataAnalysisActivity.newIntent(this@MechanicalManagementActivity))
            }
            mProjectOperationMoreImageView.setOnClickListener {
                startActivity(DataAnalysisActivity.newIntent(this@MechanicalManagementActivity))
            }
            val mStartActivityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.data != null) {
                    try {
                        val jsonString = ProjectListActivity.getResult(it.data!!)
                        val mStartProjectListDataBean = Gson().fromJson(jsonString, ProjectListDataBean::class.java)
                        mPid = mStartProjectListDataBean.id ?: ""
                        dataTableSelData()
                    } catch (e: Exception) {

                    }

                }
            }
            // 切换项目
            mProjectInclude.mSwitchTextView.setOnClickListener {
                mStartActivityForResult.launch(ProjectListActivity.newIntent(this@MechanicalManagementActivity, 0, true))
            }
            mProjectInclude.mDesLayout.setOnClickListener {
                startActivity(MechanicalDesActivity.newIntent(this@MechanicalManagementActivity, mDataTableSelDataBean?.project?.id ?: "", 1))
            }
            // 更多车辆
            mMoreCarTextView.setOnClickListener {
                startActivity(DataAnalysisActivity.newIntent(this@MechanicalManagementActivity, 1))
            }
            mMoreCarImageView.setOnClickListener {
                startActivity(DataAnalysisActivity.newIntent(this@MechanicalManagementActivity, 1))
            }
            val mSwitchActivityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.data != null) {
                    try {
                        val jsonString = ChooseCarActivity.getResult(it.data!!)
                        val mStartProjectListDataBean = Gson().fromJson(jsonString, CarInfoSelectDataBean.RecordsBean::class.java)
                        mCarId = mStartProjectListDataBean.id ?: ""
                        dataTableSelData()
                    } catch (e: Exception) {

                    }

                }
            }
            // 切换车辆
            mCarInclude.mSwitchTextView.setOnClickListener {
                mSwitchActivityForResult.launch(ChooseCarActivity.newIntent(this@MechanicalManagementActivity))
            }
            mCarInclude.mDesLayout.setOnClickListener {
                startActivity(MechanicalDesActivity.newIntent(this@MechanicalManagementActivity, mDataTableSelDataBean?.carData?.id ?: "", 2))
            }
        }
    }

    override fun processingLogic() {
        dataTableSelData()
    }

    /**
     * 获取数据
     */
    private fun dataTableSelData() {
        lifecycleScope.launch {
            convertReqExecute({ appApi.dataTableSelData(mPid, mCarId) }, onSuccess = {
                setData(it)
            }, baseView = this@MechanicalManagementActivity)
        }
    }

    /**
     * 设置数据
     */
    private fun setData(bean: DataTableSelDataBean) {
        mDataTableSelDataBean = bean
        mDataBinding.apply {
            // 项目数量
            mNumberOfProjectsTextView.text = bean.psum
            // 车辆数量
            mNumberOfVehiclesTextView.text = bean.carSum
            // 车队数量
            mNumberOfTeamTextView.text = bean.msum
            // 员工数量
            mNumberOfEmployeesTextView.text = bean.usum

            mProjectInclude.apply {
                // 项目名称
                mProjectNameTextView.text = bean.project?.name ?: ""
                // 产值
                mOutputTextView.text = "¥${bean.project?.outputValue ?: ""}"
                // 费用支付
                mPaymentTextView.text = "¥${bean.project?.expenditure ?: ""}"
                // 利润
                mProfitTextView.text = "¥${bean.project?.profit ?: ""}"
            }

            mCarInclude.apply {
                // 项目名称
                mProjectNameTextView.text = "车牌：${bean.carData?.carPlate}"
                // 产值
                mOutputTextView.text = "¥${bean.carData?.outputValue ?: ""}"
                // 费用支付
                mPaymentTextView.text = "¥${bean.carData?.expenditure ?: ""}"
                // 利润
                mProfitTextView.text = "¥${bean.carData?.profit ?: ""}"
            }
        }
    }

    companion object {

        fun newIntent(context: Context): Intent {
            return Intent(context, MechanicalManagementActivity::class.java)
        }
    }
}