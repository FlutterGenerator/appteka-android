package com.tomclaw.appsend.screen.upload.adapter.notice

import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.avito.konveyor.adapter.BaseViewHolder
import com.avito.konveyor.blueprint.ItemView
import com.tomclaw.appsend.R
import com.tomclaw.appsend.util.bind

interface NoticeItemView : ItemView {

    fun setNoticeTypeInfo()

    fun setNoticeTypeWarning()

    fun setNoticeTypeError()

    fun setNoticeText(text: String)

    fun setOnClickListener(listener: (() -> Unit)?)

}

@Suppress("DEPRECATION")
class NoticeItemViewHolder(view: View) : BaseViewHolder(view), NoticeItemView {

    private val context = view.context
    private val resources = view.resources
    private val background: View = view.findViewById(R.id.notice_back)
    private val icon: ImageView = view.findViewById(R.id.notice_icon)
    private val text: TextView = view.findViewById(R.id.notice_text)

    private var clickListener: (() -> Unit)? = null

    init {
        view.setOnClickListener { clickListener?.invoke() }
    }

    override fun setNoticeTypeInfo() {
        setBackgroundColor(R.color.notice_info_back_color)
        icon.setImageResource(R.drawable.ic_info)
        icon.setColorFilter(resources.getColor(R.color.notice_info_color))
        text.setTextColor(resources.getColor(R.color.notice_info_text_color))
    }

    override fun setNoticeTypeWarning() {
        setBackgroundColor(R.color.notice_warning_back_color)
        icon.setImageResource(R.drawable.ic_warning)
        icon.setColorFilter(resources.getColor(R.color.notice_warning_color))
        text.setTextColor(resources.getColor(R.color.notice_warning_text_color))
    }

    override fun setNoticeTypeError() {
        setBackgroundColor(R.color.notice_error_back_color)
        icon.setImageResource(R.drawable.ic_error)
        icon.setColorFilter(resources.getColor(R.color.notice_error_color))
        text.setTextColor(resources.getColor(R.color.notice_error_text_color))
    }

    override fun setNoticeText(text: String) {
        this.text.bind(text)
    }

    override fun setOnClickListener(listener: (() -> Unit)?) {
        this.clickListener = listener
    }

    override fun onUnbind() {
        this.clickListener = null
    }

    private fun setBackgroundColor(colorRes: Int) {
        val backgroundTintList = ColorStateList.valueOf(resources.getColor(colorRes))
        background.backgroundTintList = backgroundTintList
        background.backgroundTintMode = PorterDuff.Mode.SRC_ATOP
    }

}
