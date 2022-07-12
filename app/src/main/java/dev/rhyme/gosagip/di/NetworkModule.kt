package dev.rhyme.gosagip.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.rhyme.gosagip.data.ApiServices
import dev.rhyme.gosagip.data.model.Ambulance
import dev.rhyme.gosagip.data.model.Rider
import dev.rhyme.gosagip.data.model.User
import dev.rhyme.gosagip.data.model.UserType
import dev.rhyme.gosagip.data.utils.BooleanAdapter
import dev.rhyme.gosagip.data.utils.DateTimestampAdapter
import dev.rhyme.gosagip.data.utils.NestedEventAdapter
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import java.util.*
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(BooleanAdapter())
        .add(DateTimestampAdapter())
        .add(Date::class.java, Rfc3339DateJsonAdapter())
        .add(NestedEventAdapter())
        .add(
            PolymorphicJsonAdapterFactory.of(User::class.java, "type")
                .withSubtype(Rider::class.java, UserType.RIDER.name)
                .withSubtype(Ambulance::class.java, UserType.AMBULANCE.name)
        )
        .build()

    @Provides
    @Singleton
    fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(
                ChuckerInterceptor.Builder(context)
                    .collector(ChuckerCollector(context))
                    .maxContentLength(250000L)
                    .redactHeaders(emptySet())
                    .alwaysReadResponseBody(false)
                    .build()
            )
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(moshi: Moshi, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("http://mactechph.com/gosagip/index.php/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideApiServices(retrofit: Retrofit): ApiServices = retrofit.create()
}