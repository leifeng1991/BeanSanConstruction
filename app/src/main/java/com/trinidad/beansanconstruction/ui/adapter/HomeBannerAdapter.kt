package com.trinidad.beansanconstruction.ui.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.moufans.lib_base.utils.ImageLoader
import com.trinidad.beansanconstruction.R
import com.youth.banner.adapter.BannerAdapter

/**
 * 功能描述：首页banner
 */
class HomeBannerAdapter(private val mContext: Context, list: List<String> = mutableListOf()) : BannerAdapter<String, HomeBannerAdapter.BannerViewHolder>(list) {
    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val imageView = ImageView(parent.context)
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        imageView.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        return BannerViewHolder(imageView)
    }

    override fun onBindView(holder: BannerViewHolder, data: String, position: Int, size: Int) {
//        ImageLoader.setImage(R.mipmap.icb,, R.mipmap.ic_launcher,10f)
        holder.imageView.setImageResource(R.mipmap.ic_banner)
//        ImageLoader.setImage("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.jj20.com%2Fup%2Fallimg%2F1113%2F061H0102U6%2F20061G02U6-12-1200.jpg&refer=http%3A%2F%2Fimg.jj20.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1649994726&t=ec849424a5f5968ddc8a45b0c02b7f2c",holder.imageView, R.mipmap.ic_launcher,10f)
    }

    class BannerViewHolder(var imageView: ImageView) : RecyclerView.ViewHolder(imageView)
}