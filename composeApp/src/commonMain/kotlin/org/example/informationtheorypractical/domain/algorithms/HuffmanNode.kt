package org.example.informationtheorypractical.domain.algorithms

// This represents a single node in the Huffman Tree
data class HuffmanNode(
    val char: Char?,          // Null for internal branches, holds the actual character for leaf nodes
    val frequency: Int,       // How many times the character appears
    val left: HuffmanNode? = null,
    val right: HuffmanNode? = null
) : Comparable<HuffmanNode> {

    // We must compare nodes by frequency so we can easily find the lowest counts
    override fun compareTo(other: HuffmanNode): Int {
        return this.frequency.compareTo(other.frequency)
    }
}
