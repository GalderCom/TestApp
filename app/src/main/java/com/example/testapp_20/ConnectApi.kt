package com.example.testapp_20

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


class ConnectApi {

    private val apiService: BinlistApiService

    init {
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor { chain ->
                chain.proceed(
                    chain.request().newBuilder()
                        .addHeader("Accept-Version", "3")
                        .build()
                )
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(BinlistApiService::class.java)
    }

    suspend fun getBinInfo(bin: String): Response<DataClass.Bin> {
        return apiService.getBinInfo(bin)
    }

    companion object {
        const val BASE_URL = "https://lookup.binlist.net/"
    }

    // Внутренний интерфейс, который использует Retrofit
    private interface BinlistApiService {
        @GET("{bin}")
        suspend fun getBinInfo(
            @Path("bin") bin: String
        ): Response<DataClass.Bin>
    }
}
