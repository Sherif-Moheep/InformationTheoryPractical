package org.example.informationtheorypractical.presentation

import org.example.informationtheorypractical.domain.models.Algorithm

sealed interface AppEvent {
    data class InputChanged(val text: String) : AppEvent
    data class AlgorithmChanged(val algorithm: Algorithm) : AppEvent
    object CompressClicked : AppEvent
    object DecompressClicked : AppEvent
}