//package com.example.jumatexpress
//
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.Divider
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBar
//import androidx.compose.material3.TopAppBarDefaults
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.style.LineHeightStyle
//import androidx.compose.ui.text.style.LineHeightStyle.Alignment.*
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun LogbookDetailScreen(
//    entryId: String?,
//    mainViewModel: MainViewModel,
//    navController: NavController
//) {
//    // Konversi entryId ke Int
//    val logbookId = entryId?.toIntOrNull()
//
//    // State untuk detail logbook
//    val logbooks by mainViewModel._logbooks.collectAsState()
//    val uiState by mainViewModel._uiState.collectAsState()
//
//    // Cari logbook spesifik berdasarkan ID
//    val logbookDetail = logbooks.find { it.id == logbookId }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Detail Logbook") },
//                navigationIcon = {
//                    IconButton(onClick = {
//                        // Navigasi kembali
//                        navController.popBackStack()
//                    }) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
//                    }
//                }
//            )
//        }
//    ) { paddingValues ->
//        // Penanganan state loading dan error
//        when (uiState) {
//            is UiState.Loading -> {
//                Box(
//                    modifier = Modifier.fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    CircularProgressIndicator()
//                }
//            }
//            is UiState.Error -> {
//                val errorMessage = (uiState as UiState.Error).message
//                Box(
//                    modifier = Modifier.fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(
//                        text = errorMessage,
//                        color = Color.Red,
//                        textAlign = TextAlign.Center
//                    )
//                }
//            }
//            is UiState.Successfull -> {
//                // Tampilkan detail logbook jika ditemukan
//                logbookDetail?.let { logbook ->
//                    Column(
//                        modifier = Modifier
//                            .padding(paddingValues)
//                            .padding(16.dp)
//                            .fillMaxWidth()
//                    ) {
//                        // Contoh tampilan detail logbook
//                        Text(
//                            text = "Detail Logbook",
//                            style = MaterialTheme.typography.headlineMedium,
//                            modifier = Modifier.padding(bottom = 16.dp)
//                        )
//
//                        // Tampilkan informasi logbook
//                        DetailItem("Judul", logbook.topik_pekerjaan)
//                        DetailItem("Deskripsi", logbook.deskripsi)
//                        DetailItem("Tanggal", logbook.tanggal)
//
//                        // Tambahkan detail lain sesuai kebutuhan
//                    }
//                } ?: Box(
//                    modifier = Modifier.fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(
//                        text = "Logbook tidak ditemukan",
//                        color = Color.Red,
//                        textAlign = TextAlign.Center
//                    )
//                }
//            }
//        }
//    }
//}
//
//
//// Komponen helper untuk menampilkan item detail
//@Composable
//fun DetailItem(label: String, value: String?) {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 8.dp)
//    ) {
//        Text(
//            text = label,
//            style = MaterialTheme.typography.titleMedium,
//            color = Color.Gray
//        )
//        Text(
//            text = value ?: "Tidak tersedia",
//            style = MaterialTheme.typography.bodyLarge
//        )
//        Divider(modifier = Modifier.padding(top = 8.dp))
//    }
//}
