package org.example.informationtheorypractical.domain.algorithms

import org.example.informationtheorypractical.domain.models.CompressionResult
import org.example.informationtheorypractical.domain.models.TextCompressor

class SimpleRepetitionCompressor : TextCompressor {

    // We use a flag to tell the decompressor: "Hey, a compressed run is starting!"
    private val FLAG = '@'

    override fun compress(text: String): CompressionResult {
        if (text.isEmpty()) return CompressionResult("", 0, 0)

        val compressedText = StringBuilder()
        var i = 0

        while (i < text.length) {
            var count = 1
            // Count how many times the current character repeats consecutively
            while (i + count < text.length && text[i] == text[i + count]) {
                count++
            }

            // Only compress if the count is 4 or more (to ensure we actually save space)
            // AAAA (4 chars) -> @A4 (3 chars) -> Space saved!
            if (count > 3) {
                compressedText.append("$FLAG${text[i]}$count")
            } else {
                // If 3 or less, just output the raw characters
                for (j in 0 until count) {
                    compressedText.append(text[i])
                }
            }

            i += count // Skip past the characters we just processed
        }

        val output = compressedText.toString()

        // Remember to calculate in bits (* 8) for consistent UI metrics!
        return CompressionResult(
            compressedText = output,
            originalSize = text.length * 8,
            compressedSize = output.length * 8,
            metadata = null
        )
    }

    override fun decompress(data: CompressionResult): String {
        val text = data.compressedText
        if (text.isEmpty()) return ""

        val decodedString = StringBuilder()
        var i = 0

        while (i < text.length) {
            if (text[i] == FLAG) {
                // We found the flag! Next character is the target, followed by the count
                val charToRepeat = text[i + 1]

                // Read the number (it could be more than 1 digit, e.g., @A12)
                var countStr = ""
                var j = i + 2
                while (j < text.length && text[j].isDigit()) {
                    countStr += text[j]
                    j++
                }

                val count = countStr.toInt()
                for (k in 0 until count) {
                    decodedString.append(charToRepeat)
                }

                i = j // Jump to the end of the number
            } else {
                // Normal character, just append it
                decodedString.append(text[i])
                i++
            }
        }

        return decodedString.toString()
    }
}