package org.example.informationtheorypractical.di

import org.example.informationtheorypractical.presentation.CompressionViewModel
import org.example.informationtheorypractical.domain.algorithms.RleCompressor
import org.example.informationtheorypractical.domain.usecases.CompressTextUseCase
import org.example.informationtheorypractical.domain.usecases.DecompressTextUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    // algorithms
    singleOf(::RleCompressor)

    // CompressProvider
    singleOf(::CompressorProvider)

    // Use cases
    factoryOf(::CompressTextUseCase)
    factoryOf(::DecompressTextUseCase)

    // ViewModel
    viewModelOf(::CompressionViewModel)
}