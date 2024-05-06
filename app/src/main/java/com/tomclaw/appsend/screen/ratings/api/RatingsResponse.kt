package com.tomclaw.appsend.screen.ratings.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tomclaw.appsend.screen.details.api.RatingEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class RatingsResponse(
    @SerializedName("entries")
    val entries: List<RatingEntity>,
) : Parcelable
