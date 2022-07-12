package dev.rhyme.gosagip.data.utils

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.util.*

class DateTimestampAdapter {
    @FromJson
    @DateTimestamp
    fun fromJson(value: Long): Date = Date(value)

    @ToJson
    fun toJson(@DateTimestamp value: Date): Long = value.time
}