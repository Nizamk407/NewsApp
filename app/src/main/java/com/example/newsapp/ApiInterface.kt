package com.example.newsapp

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiInterface {

    @GET("everything?q=bitcoin&apiKey=6e05718e201f4db9a4698bfe3e690682")
    fun getNews(): Call<NewsResponse>

    companion object {

        fun create(): ApiInterface {

            val BASE_URL = "https://newsapi.org/v2/"
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(ApiInterface::class.java)

        }

    }
}