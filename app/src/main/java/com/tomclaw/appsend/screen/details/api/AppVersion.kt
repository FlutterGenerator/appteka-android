package com.tomclaw.appsend.screen.details.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AppVersion(
    @SerializedName("app_id")
    val appId: String,
    @SerializedName("downloads")
    val downloads: Int?,
    @SerializedName("ver_code")
    val verCode: Int,
    @SerializedName("ver_name")
    val verName: String,
    @SerializedName("sdk_version")
    val sdkVersion: Int,
) : Parcelable
