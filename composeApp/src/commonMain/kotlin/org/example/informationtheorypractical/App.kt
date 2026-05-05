package org.example.informationtheorypractical

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import org.example.informationtheorypractical.di.appModule
import org.example.informationtheorypractical.presentation.CompressionScreen
import org.example.informationtheorypractical.presentation.CompressionViewModel
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.koinViewModel
import org.koin.dsl.koinConfiguration

@Composable
@Preview
fun App() {
    MaterialTheme {
        KoinApplication(configuration = koinConfiguration(declaration = { modules(appModule) }), content = {
            val viewModel = koinViewModel<CompressionViewModel>()

            CompressionScreen(viewModel = viewModel)
        })
    }
}