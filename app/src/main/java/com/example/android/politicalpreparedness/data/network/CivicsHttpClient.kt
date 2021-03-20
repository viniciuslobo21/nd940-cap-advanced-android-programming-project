package com.example.android.politicalpreparedness.data.network
import com.example.android.politicalpreparedness.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class CivicsHttpClient : OkHttpClient() {

    companion object {
        private val loggingInterceptor = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        fun getClient(): OkHttpClient {
            return Builder()
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor { chain ->
                        val original = chain.request()
                        val url = original
                                .url
                                .newBuilder()
                                .addQueryParameter("key", BuildConfig.API_KEY)
                                .build()
                        val request = original
                                .newBuilder()
                                .url(url)
                                .build()
                        chain.proceed(request)
                    }
                    .build()
        }
    }
}