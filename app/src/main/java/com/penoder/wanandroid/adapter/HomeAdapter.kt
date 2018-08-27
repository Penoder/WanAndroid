package com.penoder.wanandroid.adapter

import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.penoder.mylibrary.adapter.CommonRecycleAdapter
import com.penoder.mylibrary.utils.ImgLoadUtil
import com.penoder.wanandroid.R
import com.penoder.wanandroid.beans.ArticleBean


/**
 * @author Penoder
 * @date 2018/8/21
 */
class HomeAdapter(datas: List<ArticleBean>) : CommonRecycleAdapter<ArticleBean>(datas, R.layout.item_home_recycler) {

    override fun onConvertView(articleBean: ArticleBean?, holder: ViewHolder?, position: Int) {

        val txtViewChapterAuthor: TextView? = holder?.getView(R.id.txtView_chapterAuthor)
        val txtViewChapterTime: TextView? = holder?.getView(R.id.txtView_chapterTime)
        val imgViewChapterPic: ImageView? = holder?.getView(R.id.imgView_chapterPic)
        val txtViewChapterTitle: TextView? = holder?.getView(R.id.txtView_chapterTitle)
        val txtViewChapterDesc: TextView? = holder?.getView(R.id.txtView_chapterDesc)
        val txtViewChapterTag: TextView? = holder?.getView(R.id.txtView_chapterTag)
        val viewHomeDivider: View? = holder?.getView(R.id.view_homeDivider)
        viewHomeDivider?.visibility = if (itemCount < position + 2) View.GONE else View.VISIBLE

        txtViewChapterAuthor?.text = articleBean?.author
        txtViewChapterTime?.text = articleBean?.niceDate
        txtViewChapterTitle?.text = articleBean?.title
        txtViewChapterDesc?.text = articleBean?.desc
        txtViewChapterTag?.text = articleBean?.chapterName

        if (TextUtils.isEmpty(articleBean?.envelopePic)) {
            imgViewChapterPic?.visibility = View.GONE
        } else {
            imgViewChapterPic?.visibility = View.VISIBLE
            ImgLoadUtil.loadImg(articleBean?.envelopePic, imgViewChapterPic)
        }

        holder?.itemView?.setOnClickListener {
            onItemClickListener?.onItemClick(articleBean)
        }
    }

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(articleBean: ArticleBean?)
    }
}