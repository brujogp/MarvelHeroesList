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


    /**
     * Generates an OkHttpClient with our trusted CAs
     * to make calls to a service which requires it.
     *
     * @param context the context to access our file.
     * @return OkHttpClient with our trusted CAs added.
     */

    //TODO En caso de necesitar agregar un certificado de seguridad usar el siguiente código, pasarle el archivo correspondiente y pasarlo en el cliente de la funcion provideRetrofitApiService
    /*
     private fun generateSecureOkHttpClient(context: Context): OkHttpClient {

         // Create a simple builder for our http client
         val httpClientBuilder = OkHttpClient.Builder()
             .readTimeout(60, TimeUnit.SECONDS)
             .connectTimeout(60, TimeUnit.SECONDS)

         // Get the file of our certificate
         // Load CAs from an InputStream
         // (could be from a resource or ByteArrayInputStream or ...)
         val caInput =
             context.resources.openRawResource(R.raw.api_bancoazteca/*dev_api_bancoazteca*/)

         val interceptor = HttpLoggingInterceptor()
         interceptor.level =
             if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

         val cf: CertificateFactory = CertificateFactory.getInstance("X.509")
         val ca: Certificate = caInput.use {
             cf.generateCertificate(it) as Certificate
         }
         println("ca=" + ca.publicKey)

         // Create a KeyStore containing our trusted CAs
         val keyStoreType = KeyStore.getDefaultType()
         val keyStore = KeyStore.getInstance(keyStoreType).apply {
             load(null, null)
             setCertificateEntry("ca", ca)
         }

         // Create a TrustManager that trusts the CAs inputStream our KeyStore
         val trustManagerFactory: TrustManagerFactory =
             TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
         trustManagerFactory.init(keyStore)

         val trustManagers: Array<TrustManager> = trustManagerFactory.trustManagers
         check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
             "Unexpected default trust managers:" + trustManagers.contentToString()
         }
         val trustManager: X509TrustManager = trustManagers[0] as X509TrustManager
         val sslContext = SSLContext.getInstance("SSL").apply {
             init(null, trustManagerFactory.trustManagers, null)
         }

         val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory

         return httpClientBuilder
             .sslSocketFactory(sslSocketFactory, trustManager)
             .addInterceptor(interceptor)
             .hostnameVerifier(NullHostNameVerifier())
             .build()

     }*/

    /*
    class NullHostNameVerifier : HostnameVerifier {
        override fun verify(hostname: String, session: SSLSession?): Boolean {
            Log.i("RestUtilImpl", "Approving certificate for $hostname")
            return if (session != null) {
                hostname == session.peerHost
            } else {
                false
            }
        }
    }
    */
}