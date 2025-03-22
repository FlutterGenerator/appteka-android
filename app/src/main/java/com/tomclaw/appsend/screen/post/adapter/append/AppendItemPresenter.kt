package com.tomclaw.appsend.screen.post.adapter.append

import com.avito.konveyor.blueprint.ItemPresenter
import com.tomclaw.appsend.screen.post.adapter.ItemListener

class AppendItemPresenter(
    private val listener: ItemListener,
) : ItemPresenter<AppendItemView, AppendItem> {

    override fun bindView(view: AppendItemView, item: AppendItem, position: Int) {
        with(view) {
            setOnClickListener { listener.onScreenAppendClick() }
        }
    }

}
