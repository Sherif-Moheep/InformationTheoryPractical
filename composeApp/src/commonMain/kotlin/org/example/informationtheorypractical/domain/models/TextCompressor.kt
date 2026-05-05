package org.example.informationtheorypractical.domain.models

interface TextCompressor {
    fun compress(text: String): CompressionResult
    fun decompress(data: CompressionResult): String
}