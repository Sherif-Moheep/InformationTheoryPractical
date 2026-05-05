package org.example.informationtheorypractical.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.example.informationtheorypractical.presentation.components.MetricItem
import org.example.informationtheorypractical.domain.models.Algorithm

@Composable
fun CompressionScreen(
    viewModel: CompressionViewModel
) {
    // 2. Collect the state from the ViewModel
    val state by viewModel.state.collectAsState()

    // UI-only state (ViewModel doesn't care if the menu is open)
    var expanded by remember { mutableStateOf(false) }

    // Center the content on large screens (Desktop/Web) and fill on Mobile
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .widthIn(max = 800.dp)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Text Compressor",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            // --- Algorithm Selector ---
            Box {
                OutlinedButton(
                    onClick = { expanded = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Read the selected algorithm from state
                    Text("Algorithm: ${state.selectedAlgorithm.displayName}")
                    Spacer(Modifier.weight(1f))
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    // Iterate over our enum values
                    Algorithm.entries.forEach { algo ->
                        DropdownMenuItem(
                            text = { Text(algo.displayName) },
                            onClick = {
                                // 3. Dispatch the event!
                                viewModel.onEvent(AppEvent.AlgorithmChanged(algo))
                                expanded = false
                            }
                        )
                    }
                }
            }

            // --- Input Section ---
            OutlinedTextField(
                value = state.inputText, // Read from state
                onValueChange = { viewModel.onEvent(AppEvent.InputChanged(it)) }, // Update via event
                label = { Text("Original Text") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                maxLines = 10
            )

            // --- Action Buttons ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { viewModel.onEvent(AppEvent.CompressClicked) }, // Dispatch!
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Compress ⬇️")
                }

                FilledTonalButton(
                    onClick = { viewModel.onEvent(AppEvent.DecompressClicked) }, // Dispatch!
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Decompress ⬆️")
                }
            }

            // --- Metrics Section ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Map the math from our state to the UI
                    MetricItem("Original Size", "${state.originalSize} chars")
                    MetricItem("Compressed", "${state.compressedSize} units")

                    // Format the Double to 2 decimal places so it looks clean (e.g. "1.45")
                    // Note: String.format is JVM specific, for pure KMP commonMain use:
                    val formattedRate = ((state.compressionRate * 100).toInt() / 100.0).toString()
                    MetricItem("Rate", formattedRate)
                }
            }

            // --- Output Section ---
            OutlinedTextField(
                value = state.outputText, // Read from state
                onValueChange = { /* Read only, so we do nothing here */ },
                label = { Text("Result") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                maxLines = 10,
                readOnly = true
            )
        }
    }
}