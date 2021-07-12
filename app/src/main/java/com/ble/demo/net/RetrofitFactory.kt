package com.lsg.dyinfo.net

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitFactory {
    private var baseUrl = "http://47.112.235.41:8090/"
    private var client: OkHttpClient? = null
           get(){
                   return field ?: {
                       field =   OkHttpClient.Builder()
                           .readTimeout(3000, TimeUnit.SECONDS)
                           .writeTimeout(3000, TimeUnit.SECONDS)
                           .connectTimeout(3000, TimeUnit.SECONDS)
                           .addInterceptor(initLogInterceptor())
                           .build()
                       field
                   }()
           }

    private   var instance: Retrofit? = null
            get() {
                return  field ?: {
                    field =  Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                    field
                }()
            }
         var netservice:NetService? = null
             get() {
                 return  field ?: {
                      field = instance!!.create(NetService::class.java)
                       field
                 }()
             }
    private fun initLogInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                message -> Log.i("abcd", message) })
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        return interceptor
    }
}