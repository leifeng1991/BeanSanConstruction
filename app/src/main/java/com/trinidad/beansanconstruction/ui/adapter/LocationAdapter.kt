package com.trinidad.beansanconstruction.ui.adapter

import android.graphics.Color
import android.view.View
import com.amap.api.services.core.PoiItem
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.moufans.lib_base.base.adapter.BaseAdapter
import com.trinidad.beansanconstruction.R
import com.trinidad.beansanconstruction.databinding.ItemLocationBinding
import com.trinidad.beansanconstruction.utils.StringUtil

class LocationAdapter(private val mLongitude: Double, private val mLatitude: Double, layoutResId: Int = R.layout.item_location, data: MutableList<PoiItem> = mutableListOf()) : BaseAdapter<PoiItem, ItemLocationBinding>(layoutResId, data) {

    var checkedIndex = -10

    override fun convert(holder: BaseDataBindingHolder<ItemLocationBinding>, position: Int, item: PoiItem) {
        holder.dataBinding?.apply {
            if (item.latLonPoint.longitude == mLongitude && item.latLonPoint.latitude == mLatitude && checkedIndex == position) {
                mMapCheckedImageView.visibility = View.VISIBLE
                mRootLayout.setBackgroundColor(Color.parseColor("#2619CC7E"))
            } else {
                mMapCheckedImageView.visibility = View.GONE
                mRootLayout.setBackgroundColor(Color.parseColor("#FFFFFFFF"))
            }

            mAddressNameTextView.text = String.format("%s（%s%s）", item.snippet, item.adName, item.businessArea)
            val distance = if (item.distance < 1000) "${item.distance}m" else "${StringUtil.saveTwoDecimal(StringUtil.div("${item.distance}", "1000", 2))}km"
            val distanceText = if (item.distance == -1) "距离太远" else "${distance}内"
            mDistanceTextView.text = "$distanceText | ${item.title}"

        }
    }
}