package com.example.tripwiseapp.api

import com.example.tripwiseapp.BuildConfig
import com.example.tripwiseapp.helpers.ETripType
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content

suspend fun UpdateSuggestion(originalSuggestion: String, prompt: String): String {
    val generativeModel = GenerativeModel(
        modelName = "gemini-2.0-flash",
        apiKey = BuildConfig.API_KEY
    )
    val prompt = content {
        text(
            "Please improve the travel suggestion based on the following user request:\n\n" +
            "\"$prompt\"\n\n" +
            "Keep the original suggestion mostly unchanged, modify only what is necessary. " +
            "Return only plain text, do not use Markdown formatting.\n\n" +
            "Original suggestion:\n$originalSuggestion"
        )
    }

    val response = generativeModel.generateContent(prompt)

    return response.text ?: "It was not possible to generate a suggestion at the moment."
}