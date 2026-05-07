package org.example.informationtheorypractical.di

import org.example.informationtheorypractical.domain.algorithms.HuffmanCompressor
import org.example.informationtheorypractical.domain.algorithms.PatternSubstitutionCompressor
import org.example.informationtheorypractical.domain.models.Algorithm
import org.example.informationtheorypractical.domain.models.TextCompressor
import org.example.informationtheorypractical.domain.algorithms.RleCompressor
import org.example.informationtheorypractical.domain.algorithms.ShannonFanoCompressor
import org.example.informationtheorypractical.domain.algorithms.SimpleRepetitionCompressor

// The Provider: Maps the enum to the actual algorithm class
class CompressorProvider(
    private val rleCompressor: RleCompressor,
    private val huffmanCompressor: HuffmanCompressor,
    private val shannonFanoCompressor: ShannonFanoCompressor,
    private val simpleRepetitionCompressor: SimpleRepetitionCompressor,
    private val patternSubstitutionCompressor: PatternSubstitutionCompressor,
) {
    fun get(algorithm: Algorithm): TextCompressor {
        return when (algorithm) {
            Algorithm.RLE -> rleCompressor
            Algorithm.HUFFMAN -> huffmanCompressor
            Algorithm.SHANNON -> shannonFanoCompressor
            Algorithm.REPETITION -> simpleRepetitionCompressor
            Algorithm.SUBSTITUTION -> patternSubstitutionCompressor
        }
    }
}