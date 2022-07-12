package dev.rhyme.gosagip.data.utils

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class BooleanAdapter {
    @FromJson
    @BooleanType
    fun fromJson(value: Int): Boolean = value != 0

    @ToJson
    fun toJson(@BooleanType value: Boolean): Int = if (value) 1 else 0
}