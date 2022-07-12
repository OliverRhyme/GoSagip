package dev.rhyme.gosagip.di

import android.content.Context
import android.content.SharedPreferences
import com.fredporciuncula.flow.preferences.FlowSharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.rhyme.gosagip.BuildConfig
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object LocalModule {

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(
            "${BuildConfig.APPLICATION_ID}.prefs",
            Context.MODE_PRIVATE
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Singleton
    @Provides
    fun provideFlowSharedPreferences(sharedPreferences: SharedPreferences): FlowSharedPreferences =
        FlowSharedPreferences(sharedPreferences)
}