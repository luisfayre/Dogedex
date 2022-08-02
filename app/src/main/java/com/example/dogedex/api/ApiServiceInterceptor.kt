package com.example.dogedex.api

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import java.lang.RuntimeException

object ApiServiceInterceptor : Interceptor {

    const val NEEDS_AUTH_HEADER_KEY = "needs_authentication"

    private var sessionToken: String? = null

    fun setSessionToken(sessionToken: String) {
        this.sessionToken = sessionToken
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()
        if (request.headers(NEEDS_AUTH_HEADER_KEY) != null) {
            if (sessionToken == null) {
                throw  RuntimeException("Need to be authenticated to perfon")
            } else {
                requestBuilder.addHeader("AUTH-TOKEN", sessionToken!!)
            }
        }
        return chain.proceed(requestBuilder.build())
    }
}