package org.example.informationtheorypractical.presentation

import org.example.informationtheorypractical.domain.models.Algorithm
import org.example.informationtheorypractical.domain.models.CompressionResult

data class AppState(
    val inputText: String = "",
    val outputText: String = "",
    val originalSize: Int = 0,
    val compressedSize: Int = 0,
    val compressionRate: Double = 0.0,
    val selectedAlgorithm: Algorithm = Algorithm.RLE,
    val activeResult: CompressionResult? = null // Holds the metadata needed for decompression
)