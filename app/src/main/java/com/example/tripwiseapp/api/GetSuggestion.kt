package com.example.tripwiseapp.api

import com.example.tripwiseapp.BuildConfig
import com.example.tripwiseapp.helpers.ETripType
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content

suspend fun getSuggestion(destiny: String, startDate: String, endDate: String, budget: Double, type: String): String {
    val generativeModel = GenerativeModel(
        modelName = "gemini-2.0-flash",
        apiKey = BuildConfig.API_KEY
    )
    val prompt = content {
        text("Put together a travel itinerary with sights and activities for the destiny of $destiny between the days $startDate and $endDate with a budget of $budget to $type. **IMPORTANT** don't generate text with markdown.")
    }

    val response = generativeModel.generateContent(prompt)

    return response.text ?: "It was not possible to generate a suggestion at the moment."
}