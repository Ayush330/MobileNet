package com.example.apivalidator

import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private val moshi = Moshi.Builder()
    .build()



object RetrofitServiceBuilder
{
    private val retrofit by lazy{
        Retrofit.Builder()
            .baseUrl("http://192.168.43.4:5000")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    val api : RetrofitService by lazy {
        retrofit.create(RetrofitService::class.java)
    }
}