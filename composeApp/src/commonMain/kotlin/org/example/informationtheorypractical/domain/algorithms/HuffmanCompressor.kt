package org.example.informationtheorypractical.domain.algorithms

import org.example.informationtheorypractical.domain.models.CompressionResult
import org.example.informationtheorypractical.domain.models.TextCompressor

class HuffmanCompressor : TextCompressor {

    override fun compress(text: String): CompressionResult {
        if (text.isEmpty()) return CompressionResult("", 0, 0)

        // Edge case: If the string is just "AAAAA", a tree can't branch.
        if (text.toSet().size == 1) {
            return CompressionResult(
                compressedText = "0".repeat(text.length),
                originalSize = text.length,
                compressedSize = text.length, // 1 bit per char
                metadata = HuffmanNode(text[0], text.length)
            )
        }

        // 1. Build the Frequency Map (Idiomatic Kotlin)
        val frequencies = text.groupingBy { it }.eachCount()

        // 2. Initialize our "Priority Queue" (KMP Safe)
        val nodes = frequencies.map { HuffmanNode(it.key, it.value) }.toMutableList()

        // 3. Build the Huffman Tree
        while (nodes.size > 1) {
            // Sort ascending to bubble the two lowest frequencies to the front
            nodes.sort()

            // Pop the two lowest nodes
            val left = nodes.removeAt(0)
            val right = nodes.removeAt(0)

            // Create a parent node combining their frequencies
            val parentNode = HuffmanNode(
                char = null,
                frequency = left.frequency + right.frequency,
                left = left,
                right = right
            )

            // Push the parent back into the list
            nodes.add(parentNode)
        }

        // The last remaining node is the Root of the entire tree
        val root = nodes.first()

        // 4. Generate the Prefix Codes
        val huffmanCodes = mutableMapOf<Char, String>()
        buildCodes(root, "", huffmanCodes)

        // 5. Encode the original text into a binary string
        val encodedString = StringBuilder()
        for (char in text) {
            encodedString.append(huffmanCodes[char])
        }

        val compressedText = encodedString.toString()
        val originalBits = text.length * 8
        val compressedBits = compressedText.length

        // Return the result, passing the Tree Root as metadata so we can decompress later!
        return CompressionResult(
            compressedText = compressedText,
            originalSize = originalBits,
            compressedSize = compressedBits, // Typically you'd divide by 8 for bytes, but keeping it in bits shows the raw logic well
            metadata = root
        )
    }

    // Recursive helper to traverse the tree and assign '0's (left) and '1's (right)
    private fun buildCodes(node: HuffmanNode?, currentCode: String, codes: MutableMap<Char, String>) {
        if (node == null) return

        // If it's a leaf node (has a character), save the path code we took to get here
        if (node.char != null) {
            codes[node.char] = currentCode
            return
        }

        // Traverse left (add '0') and right (add '1')
        buildCodes(node.left, currentCode + "0", codes)
        buildCodes(node.right, currentCode + "1", codes)
    }

    override fun decompress(data: CompressionResult): String {
        // Grab the tree from memory
        val root = data.metadata as? HuffmanNode
            ?: throw IllegalArgumentException("Cannot decompress: Missing Huffman Tree metadata.")

        if (data.compressedText.isEmpty()) return ""

        val decodedString = StringBuilder()
        var currentNode = root

        // Traverse the tree bit by bit
        for (bit in data.compressedText) {
            currentNode = if (bit == '0') {
                currentNode.left ?: currentNode
            } else {
                currentNode.right ?: currentNode
            }

            // If we hit a leaf node, we found the character! Append it and reset to the root.
            if (currentNode.char != null) {
                decodedString.append(currentNode.char)
                currentNode = root
            }
        }

        return decodedString.toString()
    }
}