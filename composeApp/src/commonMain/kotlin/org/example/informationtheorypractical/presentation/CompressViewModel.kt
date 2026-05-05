package org.example.informationtheorypractical.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.example.informationtheorypractical.domain.usecases.CompressTextUseCase
import org.example.informationtheorypractical.domain.usecases.DecompressTextUseCase

class CompressionViewModel(
    private val compressText: CompressTextUseCase,
    private val decompressText: DecompressTextUseCase,
): ViewModel() {
    private val _state = MutableStateFlow(AppState())
    val state: StateFlow<AppState> = _state.asStateFlow()


    fun onEvent(event: AppEvent) {
        when (event) {
            is AppEvent.InputChanged -> {
                _state.update { it.copy(inputText = event.text) }
            }
            is AppEvent.AlgorithmChanged -> {
                _state.update { it.copy(selectedAlgorithm = event.algorithm) }
            }
            is AppEvent.CompressClicked -> compressData()
            is AppEvent.DecompressClicked -> decompressData()
        }
    }

    private fun compressData() {
        val currentState = _state.value
        if (currentState.inputText.isEmpty()) return

        // Run the compression
        val result = compressText(
            text = currentState.inputText,
            algorithm = currentState.selectedAlgorithm
        )

        // Push the results back to the UI
        _state.update {
            it.copy(
                outputText = result.compressedText,
                originalSize = result.originalSize,
                compressedSize = result.compressedSize,
                compressionRate = result.compressionRate,
                activeResult = result
            )
        }
    }

    private fun decompressData() {
        val currentState = _state.value
        val textToDecompress = currentState.inputText

        if (textToDecompress.isEmpty()) return

        try {
            // Attempt decompression using the selected compressor
            val decodedText = decompressText(
                text = currentState.inputText,
                algorithm = currentState.selectedAlgorithm,
                metadata = currentState.activeResult?.metadata
            )

            // Success! Show the result in the bottom box
            _state.update {
                it.copy(
                    outputText = decodedText
                )
            }
        } catch (_: Exception) {
            // If it crashes (e.g. bad format for the selected algorithm)
            _state.update {
                it.copy(
                    outputText = "Error: Cannot decompress this text. Make sure the data is valid."
                )
            }
        }
    }
}