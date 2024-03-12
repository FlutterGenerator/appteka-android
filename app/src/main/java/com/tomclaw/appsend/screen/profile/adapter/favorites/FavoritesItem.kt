package com.tomclaw.appsend.screen.profile.adapter.favorites

import android.os.Parcelable
import com.avito.konveyor.blueprint.Item
import kotlinx.parcelize.Parcelize

@Parcelize
data class FavoritesItem(
    override val id: Long,
    val count: Int,
) : Item, Parcelable
