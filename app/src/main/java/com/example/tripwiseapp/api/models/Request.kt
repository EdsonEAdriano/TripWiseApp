package com.example.tripwiseapp.api.models

import com.google.ai.client.generativeai.type.Content

data class
Request(
    val contents: List<Content>
)

data class Content(
    val parts: List<Part>
)

data class Part(
    val text: String
)