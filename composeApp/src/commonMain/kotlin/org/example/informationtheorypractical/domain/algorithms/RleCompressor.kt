package org.example.informationtheorypractical.domain.algorithms

import org.example.informationtheorypractical.domain.models.CompressionResult
import org.example.informationtheorypractical.domain.models.TextCompressor

class RleCompressor : TextCompressor {
    override fun compress(text: String): CompressionResult {
        if (text.isEmpty()) return CompressionResult("", 0, 0)

        val result = StringBuilder()
        var count = 1

        for (i in 1 until text.length) {
            if (text[i] == text[i - 1]) {
                count++
            } else {
                result.append("${text[i - 1]}$count")
                count = 1
            }
        }
        result.append("${text.last()}$count")

        val compressedString = result.toString()

        return CompressionResult(
            compressedText = compressedString,
            originalSize = text.length,
            compressedSize = compressedString.length
        )
    }

    override fun decompress(data: CompressionResult): String {
        val result = StringBuilder()
        var i = 0
        val text = data.compressedText

        while (i < text.length) {
            val char = text[i]
            i++

            var countStr = ""
            while (i < text.length && text[i].isDigit()) {
                countStr += text[i]
                i++
            }

            val count = countStr.toIntOrNull() ?: 1
            repeat(count) { result.append(char) }
        }

        return result.toString()
    }
}