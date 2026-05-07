package org.example.informationtheorypractical.domain.algorithms

import org.example.informationtheorypractical.domain.models.CompressionResult
import org.example.informationtheorypractical.domain.models.TextCompressor
import kotlin.math.abs

class ShannonFanoCompressor : TextCompressor {

    override fun compress(text: String): CompressionResult {
        if (text.isEmpty()) return CompressionResult("", 0, 0)

        // Edge case: All the same character
        if (text.toSet().size == 1) {
            val singleCodeMap = mapOf("0" to text[0])
            return CompressionResult(
                compressedText = "0".repeat(text.length),
                originalSize = text.length * 8,
                compressedSize = text.length,
                metadata = singleCodeMap
            )
        }

        // 1. Calculate frequencies and sort them in DESCENDING order
        val frequencies = text.groupingBy { it }.eachCount()
        val sortedSymbols = frequencies.toList().sortedByDescending { it.second }

        // 2. Generate the codes recursively
        val codes = mutableMapOf<Char, String>()
        buildCodes(sortedSymbols, "", codes)

        // 3. Encode the text
        val encodedString = StringBuilder()
        for (char in text) {
            encodedString.append(codes[char])
        }

        val compressedText = encodedString.toString()

        // 4. Create a reverse map for decompression (e.g., "110" -> 'B')
        val decodeMap = codes.entries.associateBy({ it.value }) { it.key }

        return CompressionResult(
            compressedText = compressedText,
            originalSize = text.length * 8, // Size in bits
            compressedSize = compressedText.length, // Size in bits
            metadata = decodeMap // Pass the dictionary to the decompressor!
        )
    }

    // Recursive Top-Down Split
    private fun buildCodes(symbols: List<Pair<Char, Int>>, currentCode: String, codes: MutableMap<Char, String>) {
        // Base case: Only one symbol left
        if (symbols.size == 1) {
            codes[symbols[0].first] = currentCode
            return
        }

        // Find the best place to split the list into two halves of equal probability
        val totalFreq = symbols.sumOf { it.second }
        var runningSum = 0
        var bestDiff = Int.MAX_VALUE
        var splitIndex = 0

        for (i in 0 until symbols.size - 1) {
            runningSum += symbols[i].second
            val diff = abs(totalFreq - 2 * runningSum)

            // If the difference is getting smaller, keep going. If it gets bigger, we passed the center.
            if (diff < bestDiff) {
                bestDiff = diff
                splitIndex = i
            } else {
                break
            }
        }

        // Assign '0' to the left half, '1' to the right half, and recurse
        buildCodes(symbols.subList(0, splitIndex + 1), currentCode + "0", codes)
        buildCodes(symbols.subList(splitIndex + 1, symbols.size), currentCode + "1", codes)
    }

    override fun decompress(data: CompressionResult): String {
        @Suppress("UNCHECKED_CAST")
        val decodeMap = data.metadata as? Map<String, Char>
            ?: throw IllegalArgumentException("Missing Shannon-Fano dictionary metadata.")

        if (data.compressedText.isEmpty()) return ""

        val decodedString = StringBuilder()
        val currentBits = StringBuilder()

        // Read the binary string bit by bit. Because these are prefix-free codes,
        // the moment we find a match in the dictionary, we know it's correct.
        for (bit in data.compressedText) {
            currentBits.append(bit)
            val match = decodeMap[currentBits.toString()]

            if (match != null) {
                decodedString.append(match)
                currentBits.clear() // Reset for the next character
            }
        }

        return decodedString.toString()
    }
}