package org.example.informationtheorypractical.domain.models

data class CompressionResult(
    val compressedText: String, // The actual compressed string to show on screen
    val originalSize: Int,      // Number of characters before compression
    val compressedSize: Int,    // Number of characters after compression
    val metadata: Any? = null   // Holds the dictionary/tree for Huffman (null for RLE)
) {
    // Calculates the compression rate (Ratio of Original to Compressed)
    val compressionRate: Double
        get() = if (compressedSize == 0) 0.0 else originalSize.toDouble() / compressedSize.toDouble()
}