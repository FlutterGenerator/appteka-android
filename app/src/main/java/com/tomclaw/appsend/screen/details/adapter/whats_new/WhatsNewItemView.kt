package com.tomclaw.appsend.screen.details.adapter.whats_new

import android.view.View
import android.widget.TextView
import com.avito.konveyor.adapter.BaseViewHolder
import com.avito.konveyor.blueprint.ItemView
import com.tomclaw.appsend.R
import com.tomclaw.appsend.util.bind

interface WhatsNewItemView : ItemView {

    fun setText(value: String)

}

class WhatsNewItemViewHolder(view: View) : BaseViewHolder(view), WhatsNewItemView {

    private val context = view.context
    private val whatsNew: TextView = view.findViewById(R.id.whats_new)

    override fun setText(value: String) {
        whatsNew.bind(value)
    }

}
