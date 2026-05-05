package org.example.informationtheorypractical.di

import org.example.informationtheorypractical.domain.models.Algorithm
import org.example.informationtheorypractical.domain.models.TextCompressor
import org.example.informationtheorypractical.domain.algorithms.RleCompressor

// The Provider: Maps the enum to the actual algorithm class
class CompressorProvider(
    private val rleCompressor: RleCompressor,
) {
    fun get(algorithm: Algorithm): TextCompressor {
        return when (algorithm) {
            Algorithm.RLE -> rleCompressor
            else -> rleCompressor // fallback for now
        }
    }
}