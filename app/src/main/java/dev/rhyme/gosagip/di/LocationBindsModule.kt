package dev.rhyme.gosagip.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.rhyme.gosagip.data.location.FusedLocationService
import dev.rhyme.gosagip.data.location.LocationService
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface LocationBindsModule {
    @Singleton
    @Binds
    fun bindLocationService(impl: FusedLocationService): LocationService
}