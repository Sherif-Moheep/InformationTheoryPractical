package org.example.informationtheorypractical.domain.algorithms

import org.example.informationtheorypractical.domain.models.CompressionResult
import org.example.informationtheorypractical.domain.models.TextCompressor

class PatternSubstitutionCompressor : TextCompressor {

    override fun compress(text: String): CompressionResult {
        if (text.isEmpty()) return CompressionResult("", 0, 0)

        // 1. Initialize the dictionary with standard ASCII characters (0-255)
        val dictionary = (0..255).associateBy({ it.toChar().toString() }, { it }).toMutableMap()
        var dictSize = 256

        var currentPattern = ""
        val resultCodes = mutableListOf<Int>()

        // 2. Read characters and look for repeating patterns
        for (char in text) {
            val combinedPattern = currentPattern + char
            if (dictionary.containsKey(combinedPattern)) {
                // We've seen this pattern before, keep adding to it!
                currentPattern = combinedPattern
            } else {
                // It's a new pattern! Output the code for the old pattern...
                resultCodes.add(dictionary[currentPattern]!!)
                // ...and add the new pattern to our dictionary
                dictionary[combinedPattern] = dictSize++
                // Reset the pattern to just the current character
                currentPattern = char.toString()
            }
        }

        // 3. Don't forget to output the code for the very last pattern
        if (currentPattern.isNotEmpty()) {
            resultCodes.add(dictionary[currentPattern]!!)
        }

        // Join the codes with commas so we can see them in the UI
        val compressedText = resultCodes.joinToString(",")

        // Math: Original is characters * 8 bits.
        // LZW typically uses 12-bit integer codes to store patterns.
        val originalBits = text.length * 8
        val compressedBits = resultCodes.size * 12

        return CompressionResult(
            compressedText = compressedText,
            originalSize = originalBits,
            compressedSize = compressedBits,
            metadata = null // No metadata needed! It rebuilds itself.
        )
    }

    override fun decompress(data: CompressionResult): String {
        val text = data.compressedText
        if (text.isEmpty()) return ""

        val codes = text.split(",").mapNotNull { it.toIntOrNull() }
        if (codes.isEmpty()) return ""

        // 1. Initialize the EXACT same basic dictionary
        val dictionary = (0..255).associateBy({ it }, { it.toChar().toString() }).toMutableMap()
        var dictSize = 256

        // 2. The first code is always a standard character
        var previousString = dictionary[codes[0]] ?: return "Error: Invalid first code"
        val decodedText = StringBuilder(previousString)

        // 3. Loop through the remaining codes to rebuild the text and dictionary simultaneously
        for (i in 1 until codes.size) {
            val currentCode = codes[i]

            val entry = when {
                dictionary.containsKey(currentCode) -> dictionary[currentCode]!!
                currentCode == dictSize -> previousString + previousString[0]
                else -> return "Error: Corrupted LZW data"
            }

            decodedText.append(entry)

            // Add the new pattern to the dictionary just like the compressor did
            dictionary[dictSize++] = previousString + entry[0]
            previousString = entry
        }

        return decodedText.toString()
    }
}