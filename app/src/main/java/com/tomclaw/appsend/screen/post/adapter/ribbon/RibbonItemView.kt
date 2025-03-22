package com.tomclaw.appsend.screen.post.adapter.ribbon

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.avito.konveyor.adapter.BaseViewHolder
import com.avito.konveyor.adapter.SimpleRecyclerAdapter
import com.avito.konveyor.blueprint.ItemView
import com.tomclaw.appsend.R

interface RibbonItemView : ItemView {

    fun notifyChanged()

}

class RibbonItemViewHolder(
    view: View,
    private val adapter: SimpleRecyclerAdapter,
) : BaseViewHolder(view), RibbonItemView {

    private val recycler: RecyclerView = view.findViewById(R.id.recycler)

    private val layoutManager: LinearLayoutManager

    init {
        val orientation = RecyclerView.HORIZONTAL
        layoutManager = LinearLayoutManager(view.context, orientation, false)
        adapter.setHasStableIds(true)
        recycler.adapter = adapter
        recycler.layoutManager = layoutManager
        recycler.itemAnimator = DefaultItemAnimator()
        recycler.itemAnimator?.changeDuration = DURATION_MEDIUM
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun notifyChanged() {
        adapter.notifyDataSetChanged()
    }

    override fun onUnbind() {
    }

}

private const val DURATION_MEDIUM = 300L
