package com.trinidad.beansanconstruction.ui.adapter

import android.graphics.Color
import android.widget.TextView
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.moufans.lib_base.base.adapter.BaseAdapter
import com.moufans.lib_base.utils.span.AndroidSpan
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.api.bean.CarInfoSelectDataBean
import com.trinidad.beansanconstruction.databinding.ItemChooseCarListBinding

class ChooseCarListAdapter(layoutResId: Int = R.layout.item_choose_car_list, data: MutableList<CarInfoSelectDataBean.RecordsBean> = mutableListOf()) : BaseAdapter<CarInfoSelectDataBean.RecordsBean, ItemChooseCarListBinding>(layoutResId, data) {

    override fun convert(holder: BaseDataBindingHolder<ItemChooseCarListBinding>, position: Int, item: CarInfoSelectDataBean.RecordsBean) {
        holder.dataBinding?.apply {
            setMyText(mPlateNumberTextView, "车牌：", item.carPlate ?: "--")
            mPlateStateTextView.text = if (item.type == "1") "自有" else "加盟"
            mPlateStateTextView.helper.backgroundColorNormal = Color.parseColor(if (item.type == "1") "#19CC7E" else "#F7B500")
            setMyText(mSerialNumberTextView, "编号：", item.number ?: "--")
            setMyText(mVehicleModelTextView, "车型：", item.ctName ?: "--")
            setMyText(mVehicleOwnerTextView, "车主：", item.person ?: "--")
            setMyText(mPhoneTextView, "电话：", item.phone ?: "--")
            setMyText(mVehicleBrandTextView, "车辆品牌：", item.brand ?: "--")
            setMyText(mVehicleModelsTextView, "车辆型号：", item.carType ?: "--")
            setMyText(mEngineNumberTextView, "发动机编号：", item.motor ?: "--")
            setMyText(mBuyTimeTextView, "购买时间：", item.gmTime ?: "--")
            setMyText(mVehiclePurchasePriceTextView, "车辆购买价格：",  "¥${item.price}")
        }
    }

    private fun setMyText(textView: TextView, title: String, content: String) {
        textView.text = AndroidSpan().drawCommonSpan(title).drawForegroundColor(content, Color.parseColor("#ff333333")).spanText
    }
}