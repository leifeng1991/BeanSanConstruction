package com.trinidad.beansanconstruction.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.github.gzuliyujiang.wheelpicker.DatePicker
import com.github.gzuliyujiang.wheelpicker.entity.DateEntity
import com.github.gzuliyujiang.wheelpicker.widget.DateWheelLayout
import com.google.gson.Gson
import com.moufans.lib_base.base.activity.BaseActivity
import com.moufans.lib_base.ext.baseReqExecute
import com.moufans.lib_base.utils.ToastUtil
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.api.bean.CarInfoSelectDataBean
import com.trinidad.beansanconstruction.api.bean.CostTypeSelectDataBean
import com.trinidad.beansanconstruction.api.bean.ProjectListDataBean
import com.trinidad.beansanconstruction.api.bean.UserSelectDataBean
import com.trinidad.beansanconstruction.api.param.RequestParamJsonBean
import com.trinidad.beansanconstruction.databinding.ActivityCostInputBinding
import com.trinidad.beansanconstruction.ext.appApi
import com.trinidad.beansanconstruction.utils.StringUtil
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class CostInputActivity : BaseActivity<ActivityCostInputBinding>() {
    private var mProjectListDataBean: ProjectListDataBean? = null
    private var mCostTypeSelectDataBean: CostTypeSelectDataBean.RecordsBean? = null
    private var mUserSelectDataBean: UserSelectDataBean.RecordsBean? = null
    private var mCarInfoSelectDataBean: CarInfoSelectDataBean.RecordsBean? = null
    private var mDatePicker: DatePicker? = null
    private var mCostTypesListDataBean: String? = null
    private var isRequesting = false

    override fun getDataBindingLayoutResId(): Int {
        return R.layout.activity_cost_input
    }

    override fun initView() {
        setHeaderTitle("????????????")
        mDataBinding.apply {
            mPermanentStaffTextView.isSelected = true
            mPermanentStaffView.isSelected = true
        }
    }

    @SuppressLint("SetTextI18n")
    override fun initListener() {
        mDataBinding.apply {
            val mStartActivityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.data != null) {
                    try {
                        val jsonString = ProjectListActivity.getResult(it.data!!)
                        mProjectListDataBean = Gson().fromJson(jsonString, ProjectListDataBean::class.java)
                        mDataBinding.mProjectNameTextView.text = mProjectListDataBean!!.name
                    } catch (e: Exception) {

                    }

                }
            }
            // ????????????
            mSelectProjectLayout.setOnClickListener {
                mStartActivityForResult.launch(ProjectListActivity.newIntent(this@CostInputActivity, 0, true))
            }
            // ????????????
            mTaskDateTextView.setOnClickListener {
                if (mDatePicker != null && mDatePicker!!.isShowing) {
                    return@setOnClickListener
                }
                if (mDatePicker == null) {
                    mDatePicker = DatePicker(this@CostInputActivity)
                    val wheelLayout: DateWheelLayout = mDatePicker!!.wheelLayout
                    wheelLayout.setDateLabel("???", "???", "???")
                    wheelLayout.setRange(DateEntity.target(2000, 1, 1), DateEntity.target(2099, 12, 31), DateEntity.today());
                }
                mDatePicker!!.setOnDatePickedListener { year, month, day ->
                    mTaskDateTextView.text = "$year-${if (month < 10) "0$month" else month}-${if (day < 10) "0$day" else day}"
                }
                mDatePicker!!.show()
            }
            val mStartFeeActivityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.data != null) {
                    try {
                        val jsonString = CostTypesActivity.getResult(it.data!!)
                        mCostTypeSelectDataBean = Gson().fromJson(jsonString, CostTypeSelectDataBean.RecordsBean::class.java)
                        mDataBinding.mSprinklerStationShiftFeeTextView.text = mCostTypeSelectDataBean?.name ?: ""
                        mDataBinding.mPriceTextView.text = "??${mCostTypeSelectDataBean?.price ?: "0"}"
                        mDataBinding.mUnitTextView.text = "???/${mCostTypeSelectDataBean?.pattern}"
                        mDataBinding.mCostTotalTextView.text = "??${StringUtil.mul(mCostTypeSelectDataBean?.price ?: "0", mDataBinding.mNumberTextView.text.toString())}"
                    } catch (e: Exception) {

                    }

                }
            }
            // ??????????????????
            mSprinklerStationShiftFeeTextView.setOnClickListener {
                mStartFeeActivityForResult.launch(CostTypesActivity.newIntent(this@CostInputActivity))
            }
            // ??????
            mSubTextView.setOnClickListener {
                if (mDataBinding.mNumberTextView.text.toString() == "1") {
                    ToastUtil.showShort("??????????????????")
                    return@setOnClickListener
                }
                var num = mDataBinding.mNumberTextView.text.toString().toInt()
                num--
                mDataBinding.mNumberTextView.text = "$num"
                mDataBinding.mCostTotalTextView.text = "??${StringUtil.mul(mCostTypeSelectDataBean?.price ?: "0", mDataBinding.mNumberTextView.text.toString())}"
            }
            // ??????
            mAddTextView.setOnClickListener {
                var num = mDataBinding.mNumberTextView.text.toString().toInt()
                num++
                mDataBinding.mNumberTextView.text = "$num"
                mDataBinding.mCostTotalTextView.text = "??${StringUtil.mul(mCostTypeSelectDataBean?.price ?: "0", mDataBinding.mNumberTextView.text.toString())}"
            }
            // ????????????
            mPermanentStaffLayout.setOnClickListener {
                mSelectedNameLayout.visibility = View.VISIBLE
                mInputNameEditText.visibility = View.GONE
                mPermanentStaffTextView.isSelected = true
                mPermanentStaffView.isSelected = true
                mTemporaryWorkerTextView.isSelected = false
                mTemporaryWorkerView.isSelected = false
            }
            // ????????????
            mTemporaryWorkerLayout.setOnClickListener {
                mSelectedNameLayout.visibility = View.GONE
                mInputNameEditText.visibility = View.VISIBLE
                mPermanentStaffTextView.isSelected = false
                mPermanentStaffView.isSelected = false
                mTemporaryWorkerTextView.isSelected = true
                mTemporaryWorkerView.isSelected = true
            }
            val mStartPeopleActivityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.data != null) {
                    try {
                        val jsonString = PeopleActivity.getResult(it.data!!)
                        mUserSelectDataBean = Gson().fromJson(jsonString, UserSelectDataBean.RecordsBean::class.java)
                        mSelectedNameTextView.text = mUserSelectDataBean?.name ?: ""
                    } catch (e: Exception) {

                    }

                }
            }
            // ????????????
            mSelectedNameLayout.setOnClickListener {
                mStartPeopleActivityForResult.launch(PeopleActivity.newIntent(this@CostInputActivity))
            }
            val mStartCarActivityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.data != null) {
                    try {
                        val jsonString = ChooseCarActivity.getResult(it.data!!)
                        mCarInfoSelectDataBean = Gson().fromJson(jsonString, CarInfoSelectDataBean.RecordsBean::class.java)
                        // ??????
                        mPlateNumberTextView.text = mCarInfoSelectDataBean?.carPlate ?: ""
                        // ????????????
                        mCarIDTextView.text = mCarInfoSelectDataBean?.number ?: ""
                    } catch (e: Exception) {

                    }

                }
            }
            // ??????????????????
            mVehicleInformationTextView.setOnClickListener {
                mStartCarActivityForResult.launch(ChooseCarActivity.newIntent(this@CostInputActivity))
            }
            // ????????????
            mSubmitTextView.setOnClickListener {
                submit()
            }
        }
    }

    override fun processingLogic() {
    }

    private fun submit() {
        mDataBinding.apply {
            val mProjectName = mProjectNameTextView.text.toString()
            if (TextUtils.isEmpty(mProjectName)) {
                ToastUtil.showShort("???????????????")
                return
            }
            val mTaskDate = mTaskDateTextView.text.toString()
            if (TextUtils.isEmpty(mTaskDate)) {
                ToastUtil.showShort("???????????????")
                return
            }
            val mSprinklerStationShiftFee = mSprinklerStationShiftFeeTextView.text.toString()
            if (TextUtils.isEmpty(mSprinklerStationShiftFee)) {
                ToastUtil.showShort("?????????????????????")
                return
            }
            if (mDataBinding.mPermanentStaffTextView.isSelected) {
                val mPeopleName = mSelectedNameTextView.text.toString()
                if (TextUtils.isEmpty(mPeopleName)) {
                    ToastUtil.showShort("???????????????")
                    return
                }
            } else {
                if (mCostTypeSelectDataBean?.type == "1") {
                    ToastUtil.showShort("??????????????????????????????????????????????????????????????????")
                    return
                }
                val mPeopleName = mInputNameEditText.text.toString()
                if (TextUtils.isEmpty(mPeopleName)) {
                    ToastUtil.showShort("?????????????????????")
                    return
                }
            }
            if (mDataBinding.mPermanentStaffTextView.isSelected){
                val mPlateNumber = mPlateNumberTextView.text.toString()
                if (TextUtils.isEmpty(mPlateNumber)) {
                    ToastUtil.showShort("?????????????????????")
                    return
                }
            }

        }

        lifecycleScope.launch {
            val body = Gson().toJson(RequestParamJsonBean().apply {
                // ??????id????????????????????????
                id = ""
                // ??????id
                pid = mProjectListDataBean?.id
                // ??????
                period = mDataBinding.mTaskDateTextView.text.toString()
                // ?????????1??????/2?????????
                clas = if (mDataBinding.mClassesRadioGroup.checkedRadioButtonId == R.id.mDayShiftRRadioButton) "1" else (if (mDataBinding.mClassesRadioGroup.checkedRadioButtonId == R.id.mDayShiftRRadioButton) "2" else "")
                // ????????????id
                costTypeId = mCostTypeSelectDataBean?.id
                // ??????
                number = mDataBinding.mNumberTextView.text.toString()
                // ??????
                unitPrice = mCostTypeSelectDataBean?.price
                // ??????
                sumPrice = "${StringUtil.mul(mCostTypeSelectDataBean?.price ?: "0", number ?: "0")}"
                // ??????
                pattern = mCostTypeSelectDataBean?.pattern
                // ??????id???????????????????????????????????????????????????
                aid = mUserSelectDataBean?.id
                // ????????????
                aname = if (mDataBinding.mPermanentStaffTextView.isSelected) mUserSelectDataBean?.name else mDataBinding.mInputNameEditText.text.toString()
                // ??????id
                carId = mCarInfoSelectDataBean?.id
                // ????????????
                content = mDataBinding.mInputWorkEditText.text.toString()

            }).toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            baseReqExecute({ appApi.carProjectAddOrUpdate(body) }, onSuccess = {
                lifecycleScope.launch {
                    if (mCostTypesListDataBean == null)
                        ToastUtil.showShort("????????????")
                    else
                        ToastUtil.showShort("????????????")

                    isRequesting = false
                    // ????????????
                    finish()
                }
            }, onFailure = { _, _, _ ->
                isRequesting = false
            }, baseView = this@CostInputActivity)
        }
    }

    companion object {
        private const val INTENT_P_DATA = "data"

        fun newIntent(context: Context, dataJson: String = ""): Intent {
            return Intent(context, CostInputActivity::class.java).apply {
                putExtra(INTENT_P_DATA, dataJson)
            }
        }
    }
}