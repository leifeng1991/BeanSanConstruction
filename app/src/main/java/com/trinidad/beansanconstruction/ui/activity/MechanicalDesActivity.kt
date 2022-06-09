package com.trinidad.beansanconstruction.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.View
import android.widget.NumberPicker
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.gzuliyujiang.wheelpicker.DatePicker
import com.github.gzuliyujiang.wheelpicker.OptionPicker
import com.github.gzuliyujiang.wheelpicker.contract.OnOptionPickedListener
import com.github.gzuliyujiang.wheelpicker.entity.DateEntity
import com.github.gzuliyujiang.wheelpicker.widget.DateWheelLayout
import com.google.gson.Gson
import com.moufans.lib_base.base.activity.BaseActivity
import com.moufans.lib_base.ext.convertReqExecute
import com.moufans.lib_base.utils.span.AndroidSpan
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.api.bean.CarInfoSelectDataBean
import com.trinidad.beansanconstruction.api.bean.ProjectListDataBean
import com.trinidad.beansanconstruction.api.bean.ProjectVehicleDataTableDataBean
import com.trinidad.beansanconstruction.databinding.ActivityMechanicalDesBinding
import com.trinidad.beansanconstruction.ext.appApi
import com.trinidad.beansanconstruction.ui.adapter.MechanicalDesContentListAdapter
import com.trinidad.beansanconstruction.ui.adapter.MechanicalDesListAdapter
import com.trinidad.beansanconstruction.utils.DateTimeUtil
import kotlinx.coroutines.launch

class MechanicalDesActivity : BaseActivity<ActivityMechanicalDesBinding>() {
    //1项目 2车辆
    private val mType by lazy {
        intent.getIntExtra(INTENT_P_TYPE, 1)
    }

    // 项目id
    private var mId = ""

    private var mYear = ""

    // 标题
    private val mLeftAdapter by lazy {
        MechanicalDesListAdapter(R.layout.item_mechanical_des_list)
    }

    // 内容
    private val mRightAdapter by lazy {
        MechanicalDesContentListAdapter(R.layout.item_mechanical_des_list_1)
    }

    private var mDatePicker: OptionPicker? = null

    override fun getDataBindingLayoutResId(): Int {
        return R.layout.activity_mechanical_des
    }

    override fun initView() {
        mId = intent.getStringExtra(INTENT_P_ID) ?: ""
        mYear = "${intent.getIntExtra(INTENT_P_YEAR, DateTimeUtil.getYear(System.currentTimeMillis()).toInt())}"
        mDataBinding.apply {
            mYearTextView.text = mYear
            mTitleNameTextView.text = if (mType == 1) "项目信息" else "车辆信息"
            mRightTextView3.visibility = if (mType == 1) View.GONE else View.VISIBLE
            mRightTextView4.visibility = if (mType == 1) View.GONE else View.VISIBLE
            mRightTextView5.visibility = if (mType == 1) View.GONE else View.VISIBLE

        }
    }

    override fun initListener() {
        val mStartActivityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.data != null) {
                try {
                    mId = if (mType == 1) {
                        val jsonString = ProjectListActivity.getResult(it.data!!)
                        val mStartProjectListDataBean = Gson().fromJson(jsonString, ProjectListDataBean::class.java)
                        mStartProjectListDataBean.id ?: ""
                    } else {
                        val jsonString = ChooseCarActivity.getResult(it.data!!)
                        val mStartProjectListDataBean = Gson().fromJson(jsonString, CarInfoSelectDataBean.RecordsBean::class.java)
                        mStartProjectListDataBean.id ?: ""
                    }

                    getListData()
                } catch (e: Exception) {

                }

            }
        }
        setHeaderRightText(if (mType == 1) "更换项目          " else "更换车辆          ", onClickListener = {
            if (mType == 1)
                mStartActivityForResult.launch(ProjectListActivity.newIntent(this, 0, true))
            else
                mStartActivityForResult.launch(ChooseCarActivity.newIntent(this))
        })
        setHeaderRightRightImage(R.mipmap.ic_two_line, onClickListener = {
            if (mType == 1)
                mStartActivityForResult.launch(ProjectListActivity.newIntent(this, 0, true))
            else
                mStartActivityForResult.launch(ChooseCarActivity.newIntent(this))
        })
        mDataBinding.mSwitchYearLayout.setOnClickListener {
            showDatePicke()
        }
    }

    override fun processingLogic() {
        getListData()
    }

    /**
     * 获取数据
     */
    private fun getListData() {
        lifecycleScope.launch {
            convertReqExecute({
                if (mType == 1)
                    appApi.getProjectOne(mId ?: "", mYear)
                else
                    appApi.getCarOne(mId ?: "", mYear)
            }, onSuccess = {
                setData(it)
            }, baseView = this@MechanicalDesActivity)
        }
    }

    private fun setData(bean: ProjectVehicleDataTableDataBean) {

        mDataBinding.apply {

            setMyText(mRightTextView1, if (mType == 1) "名称：" else "车牌：", if (mType == 1) bean.name ?: "" else bean.carPlate ?: "")
            setMyText(mRightTextView2, if (mType == 1) "地址：" else "车型：", if (mType == 1) bean.siteName ?: "" else bean.ctName ?: "")
            setMyText(mRightTextView3, "车主：", bean.person ?: "")
            setMyText(mRightTextView4, "编号：", bean.number ?: "")
            setMyText(mRightTextView5, "电话：", bean.phone ?: "")

            mLeftRecyclerView.layoutManager = LinearLayoutManager(this@MechanicalDesActivity)
            mLeftRecyclerView.adapter = mLeftAdapter

            if (bean.list.isNotEmpty()){
                mRecyclerView.layoutManager = GridLayoutManager(this@MechanicalDesActivity, bean.list[0].size, GridLayoutManager.HORIZONTAL, false)
                mRightAdapter.count = bean.list[0].size
                mRecyclerView.adapter = mRightAdapter
            }

        }
        if (bean.list.isNotEmpty())
            mLeftAdapter.setList(bean.list[0])
        val contentList = mutableListOf<String>()
        for (item in bean.list) {
            if (bean.list.indexOf(item) != 0) {
                contentList.addAll(item)
            }
        }
        mRightAdapter.setList(contentList)
    }

    private fun setMyText(textView: TextView, title: String, content: String) {
        textView.text = AndroidSpan().drawCommonSpan(title).drawForegroundColor(content, Color.parseColor("#ff333333")).spanText
    }

    private fun showDatePicke() {
        if (mDatePicker != null && mDatePicker!!.isShowing) {
            return
        }
        if (mDatePicker == null) {
            mDatePicker = OptionPicker(this@MechanicalDesActivity)
            mDatePicker!!.setData(mutableListOf<String>().apply {
                add(DateTimeUtil.getYear(System.currentTimeMillis()))
                for (i in 1..50) {
                    add("${DateTimeUtil.getYear(System.currentTimeMillis()).toInt() - i}")
                }

                mDatePicker!!.setOnOptionPickedListener { position, item ->
                    if (mYear != this[position]) {
                        mYear = this[position]
                        mDataBinding.mYearTextView.text = this[position]
                        getListData()
                    }
                }
            })

        }
        mDatePicker!!.show()
    }

    companion object {
        private const val INTENT_P_ID = "id"
        private const val INTENT_P_TYPE = "type"
        private const val INTENT_P_YEAR = "year"

        /**
         * @param id   项目id
         * @param type 1项目 2车辆
         */
        fun newIntent(context: Context, id: String, type: Int, year: Int = DateTimeUtil.getYear(System.currentTimeMillis()).toInt()): Intent {
            return Intent(context, MechanicalDesActivity::class.java).apply {
                putExtra(INTENT_P_ID, id)
                putExtra(INTENT_P_TYPE, type)
                putExtra(INTENT_P_YEAR, year)
            }
        }

    }

}