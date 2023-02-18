package com.tomclaw.appsend.screen.upload.adapter.select_app

import com.avito.konveyor.blueprint.ItemPresenter
import com.tomclaw.appsend.screen.upload.adapter.ItemListener

class SelectAppItemPresenter(
    private val listener: ItemListener,
) : ItemPresenter<SelectAppItemView, SelectAppItem> {

    override fun bindView(view: SelectAppItemView, item: SelectAppItem, position: Int) {
        view.setOnClickListener { listener.onSelectAppClick() }
    }

}
