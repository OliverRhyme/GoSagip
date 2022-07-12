package dev.rhyme.gosagip.data.utils

import com.squareup.moshi.*
import dev.rhyme.gosagip.data.model.Event
import dev.rhyme.gosagip.data.model.Rider

class NestedEventAdapter {
    @Suppress("UNCHECKED_CAST")
    @ToJson
    fun toJson(
        writer: JsonWriter,
        event: Event,
        eventDelegate: JsonAdapter<Event?>,
        riderDelegate: JsonAdapter<Rider?>
    ) {
        val rider = event.rider
        val eventMap = eventDelegate.toJsonValue(event) as Map<String, Any?>
        val finalMap = eventMap.toMutableMap().apply {
            remove("rider")
            putAll(riderDelegate.toJsonValue(rider)!! as Map<String, Any>)
        }
        writer.use {
            it.jsonValue(finalMap)
        }
    }

    @FromJson
    fun fromJson(
        reader: JsonReader,
        delegate: JsonAdapter<Map<String, Any?>?>,
        eventDelegate: JsonAdapter<Event?>
    ): Event? {
        val json = delegate.fromJson(reader)!!
        val mutableJson = json.toMutableMap()
        val rider = delegate.fromJsonValue(mutableJson)!!
        mutableJson["rider"] = rider

        return eventDelegate.fromJsonValue(mutableJson)
    }
}