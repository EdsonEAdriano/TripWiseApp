package com.example.tripwiseapp.api.interfaces

import com.example.tripwiseapp.api.models.Request
import com.example.tripwiseapp.api.models.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface IGemini {
    @POST("v1beta/models/gemini-pro:generateContent")
    suspend fun generateContent(
        @Header("Authorization") apiKey: String,
        @Body request: Request
    ): Response
}