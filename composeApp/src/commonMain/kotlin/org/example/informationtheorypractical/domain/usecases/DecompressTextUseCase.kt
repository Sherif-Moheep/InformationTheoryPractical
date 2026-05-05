package org.example.informationtheorypractical.domain.usecases

import org.example.informationtheorypractical.di.CompressorProvider
import org.example.informationtheorypractical.domain.models.Algorithm
import org.example.informationtheorypractical.domain.models.CompressionResult

class DecompressTextUseCase(
    private val provider: CompressorProvider
) {
    operator fun invoke(
        text: String,
        algorithm: Algorithm,
        metadata: Any?
    ): String {
        if (text.isEmpty()) return ""

        val compressor = provider.get(algorithm)
        val targetData = CompressionResult(
            compressedText = text,
            originalSize = 0,
            compressedSize = 0,
            metadata = metadata)

        return compressor.decompress(targetData)
    }
}