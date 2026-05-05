package org.example.informationtheorypractical.domain.usecases

import org.example.informationtheorypractical.di.CompressorProvider
import org.example.informationtheorypractical.domain.models.Algorithm
import org.example.informationtheorypractical.domain.models.CompressionResult

class CompressTextUseCase(
    private val provider: CompressorProvider
) {
    operator fun invoke(text: String, algorithm: Algorithm): CompressionResult {
        if (text.isEmpty()) return CompressionResult(
            compressedText = "",
            originalSize = 0,
            compressedSize = 0,
            metadata = null
        )

        val compressor = provider.get(algorithm)

        return compressor.compress(text)
    }
}