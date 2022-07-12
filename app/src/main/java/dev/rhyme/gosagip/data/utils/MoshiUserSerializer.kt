package dev.rhyme.gosagip.data.utils

import com.fredporciuncula.flow.preferences.NullableSerializer
import com.squareup.moshi.Moshi
import dev.rhyme.gosagip.data.model.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoshiUserSerializer @Inject constructor(private val moshi: Moshi): NullableSerializer<User> {
    override fun deserialize(serialized: String?): User? {
        return if (serialized == null) {
            null
        } else {
            moshi.adapter(User::class.java).fromJson(serialized)
        }
    }

    override fun serialize(value: User?): String? {
        return if (value == null) {
            null
        } else {
            moshi.adapter(User::class.java).toJson(value)
        }
    }
}