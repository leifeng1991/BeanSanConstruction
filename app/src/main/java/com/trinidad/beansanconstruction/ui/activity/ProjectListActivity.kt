package com.trinidad.beansanconstruction.ui.activity

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.moufans.lib_base.base.activity.BaseActivity
import com.moufans.lib_base.ext.convertReqExecute
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.databinding.ActivityProjectListBinding
import com.trinidad.beansanconstruction.ext.appApi
import com.trinidad.beansanconstruction.ui.adapter.ProjectListAdapter
import kotlinx.coroutines.launch

/**
 * 项目列表
 */
class ProjectListActivity : BaseActivity<ActivityProjectListBinding>() {
    //  0:项目 1倒土场
    private val mType by lazy {
        intent.getIntExtra(INTENT_P_TYPE, 0)
    }

    private val mIsChoice by lazy {
        intent.getBooleanExtra(INTENT_P_IS_CHOICE, false)
    }

    private val mProjectListAdapter by lazy {
        ProjectListAdapter(mIsChoice)
    }

    override fun getDataBindingLayoutResId(): Int {
        return R.layout.activity_project_list
    }

    override fun initView() {
        val title = if (1 == mType) {
            if (mIsChoice)
                "倒土场"
            else
                "终点/倒土点"
        } else {
            if (mIsChoice)
                "项目"
            else
                "起点/项目"
        }
        mDataBinding.mAddedTextView.visibility = if (mIsChoice) View.GONE else View.VISIBLE
        setHeaderTitle(title)
        mDataBinding.mAddedTextView.text = if (0 == mType) "新增项目" else "新增倒土点"

        mDataBinding.mProjectRecyclerView.apply {
            setPullRefreshAndLoadingMoreEnabled(false, loadingMoreEnabled = false)
            setLayoutManager(LinearLayoutManager(this@ProjectListActivity))
            setAdapter(mProjectListAdapter)
        }
    }

    override fun initListener() {
        // 搜索
        mDataBinding.mSearchCompanyEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                getProject()
                true
            }
            false
        }
        mProjectListAdapter.addChildClickViewIds(R.id.mEditTextView)
        mProjectListAdapter.setOnItemChildClickListener { _, _, position ->
            val itemBean = mProjectListAdapter.data[position]
            val json = Gson().toJson(itemBean)
            if (mIsChoice) {
                setResult(RESULT_OK, Intent().apply { putExtra(INTENT_DATA, json) })
                finish()
            } else {
                if (0 == mType) {
                    // 创建项目
                    startActivity(CreateProjectActivity.newIntent(this, json))
                } else {
                    // 创建倒土场
                    startActivity(CreatePourPointSoilActivity.newIntent(this, json))
                }
            }

        }
        // 添加
        mDataBinding.mAddedTextView.setOnClickListener {
            if (mIsChoice) {

            } else {
                if (0 == mType) {
                    // 创建项目
                    startActivity(CreateProjectActivity.newIntent(this, ""))
                } else {
                    // 创建倒土场
                    startActivity(CreatePourPointSoilActivity.newIntent(this, ""))
                }
            }

        }
    }

    override fun processingLogic() {

    }

    override fun onResume() {
        super.onResume()
        getProject()
    }

    private fun getProject() {
        val keyWord = mDataBinding.mSearchCompanyEditText.text.toString().trim()
        lifecycleScope.launch {
            convertReqExecute({ if (mType == 0) appApi.getProject(keyWord) else appApi.getPoint(keyWord) }, onSuccess = {
                mProjectListAdapter.setList(it)
            }, baseView = this@ProjectListActivity)
        }
    }

    companion object {
        private const val INTENT_P_TYPE = "type"
        private const val INTENT_P_IS_CHOICE = "isChoice"
        private const val INTENT_DATA = "data"

        /**
         * @param type 0:项目 1倒土场
         * @param isChoice true选择起始点
         */
        @JvmOverloads
        fun newIntent(context: Context, type: Int = 0, isChoice: Boolean = false): Intent {
            return Intent(context, ProjectListActivity::class.java).apply {
                putExtra(INTENT_P_TYPE, type)
                putExtra(INTENT_P_IS_CHOICE, isChoice)
            }
        }

        fun getResult(data: Intent): String {
            return data.getStringExtra(INTENT_DATA) ?: ""
        }
    }
}