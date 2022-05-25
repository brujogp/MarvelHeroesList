package com.test.marvel.di.modules

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.test.marvel.BuildConfig
import com.test.marvel.data.service.Api
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.*


@InstallIn(SingletonComponent::class)
@Module
class RetrofitModule {
    companion object {
        const val TAG = "RetrofitModule"
    }

    @Provides
    @Singleton
    fun provideRetrofitService(@ApplicationContext appContext: Context): Api {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

        val client = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .followRedirects(true)
            .addInterceptor(interceptor)
            .addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val request = chain.request().newBuilder()

                    val customUrl = HttpUrl.Builder()
                    customUrl.host(chain.request().url.host)
                    customUrl.encodedPath(chain.request().url.encodedPath)
                    customUrl.scheme(chain.request().url.scheme)

                    for (e: String in chain.request().url.queryParameterNames) {
                        val queryValue: List<String?> =
                            chain.request().url.queryParameterValues(e)

                        customUrl.addQueryParameter(e, queryValue[0])
                    }

                    customUrl.addQueryParameter("apikey", Api.API_KEY)
                    customUrl.addQueryParameter("hash", Api.M5D_DIGEST_HASH)
                    customUrl.addQueryParameter("ts", "1")

                    request.url(customUrl.build())

                    return chain.proceed(request.build())
                }
            })
            .build()

        return Retrofit.Builder()
            .baseUrl(Api.BASE_URL)
            .client(client)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
                )
            )
            .build()
            .create(Api::class.java)
    }
}
