package dev.rhyme.gosagip.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val apiServices: ApiServices
) {
}