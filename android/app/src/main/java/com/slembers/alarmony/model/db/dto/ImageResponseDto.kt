package com.slembers.alarmony.model.db.dto

import com.google.gson.annotations.SerializedName

data class ImageResponseDto (
    @SerializedName("profileImgUrl") val profileImgUrl : String?
)