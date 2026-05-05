package org.example.informationtheorypractical.domain.models

enum class Algorithm(val displayName: String) {
    RLE("Run-Length Encoding (RLE)"),
    REPETITION("Simple Repetition"),
    SUBSTITUTION("Pattern Substitution"),
    SHANNON("Shannon-Fano"),
    HUFFMAN("Huffman")
}