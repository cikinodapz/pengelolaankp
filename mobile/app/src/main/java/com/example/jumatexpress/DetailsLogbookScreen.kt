package com.example.jumatexpress

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun DetailsLogbookScreen(
//    navController: NavController,
//    logbookId: Int // ID logbook yang diterima melalui navigasi
//) {
//    val context = LocalContext.current
//    val viewModel: MainViewModel = viewModel()
//
//    // Ambil detail logbook berdasarkan ID
//    val logbookDetails by viewModel.logbookDetails.collectAsState()
//    val uiState by viewModel.uiState.collectAsState(initial = UiState.Loading)
//
//    // State untuk mengontrol dialog konfirmasi
//    var showDialog by remember { mutableStateOf(false) }
//    var updatedLogbook: Logbook? by remember { mutableStateOf(null) }
//
//    // Panggil getLogbookDetails ketika composable pertama kali dimuat
//    LaunchedEffect(logbookId) {
//        viewModel.getLogbookDetails(logbookId, context)
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Detail Logbook") },
//                navigationIcon = {
//                    IconButton(onClick = { navController.navigateUp() }) {
//                        Icon(Icons.Default.ArrowBack, "Kembali")
//                    }
//                },
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = MaterialTheme.colorScheme.primary,
//                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
//                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
//                )
//            )
//        }
//    ) { paddingValues ->
//        Column(
//            modifier = Modifier
//                .padding(paddingValues)
//                .padding(16.dp)
//                .fillMaxSize(),
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            when (uiState) {
//                is UiState.Loading -> {
//                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
//                }
//                is UiState.Successfull -> {
//                    val logbook = logbookDetails
//                    if (logbook != null) {
//                        // Menambahkan TextField untuk mengedit logbook
//                        var editedTanggal by remember { mutableStateOf(logbook.tanggal) }
//                        var editedTopikPekerjaan by remember { mutableStateOf(logbook.topik_pekerjaan) }
//                        var editedDeskripsi by remember { mutableStateOf(logbook.deskripsi) }
//
//                        Text(
//                            text = "Tanggal",
//                            style = MaterialTheme.typography.titleMedium
//                        )
//                        TextField(
//                            value = editedTanggal,
//                            onValueChange = { editedTanggal = it },
//                            label = { Text("Tanggal") },
//                            modifier = Modifier.fillMaxWidth()
//                        )
//
//                        Text(
//                            text = "Topik Pekerjaan",
//                            style = MaterialTheme.typography.titleMedium
//                        )
//                        TextField(
//                            value = editedTopikPekerjaan,
//                            onValueChange = { editedTopikPekerjaan = it },
//                            label = { Text("Topik Pekerjaan") },
//                            modifier = Modifier.fillMaxWidth()
//                        )
//
//                        Text(
//                            text = "Deskripsi",
//                            style = MaterialTheme.typography.titleMedium
//                        )
//                        TextField(
//                            value = editedDeskripsi,
//                            onValueChange = { editedDeskripsi = it },
//                            label = { Text("Deskripsi") },
//                            modifier = Modifier.fillMaxWidth()
//                        )
//
//                        Spacer(modifier = Modifier.height(16.dp))
//
//                        // Tombol untuk memperbarui data
//                        Button(
//                            onClick = {
//                                // Simpan perubahan sementara dan tampilkan dialog konfirmasi
//                                updatedLogbook = Logbook(
//                                    id = logbook.id, // ID logbook yang sama
//                                    tanggal = editedTanggal,
//                                    topik_pekerjaan = editedTopikPekerjaan,
//                                    deskripsi = editedDeskripsi,
//                                    id_mahasiswa = logbook.id_mahasiswa // Tambahkan id_mahasiswa dari logbook asli
//                                )
//                                showDialog = true // Tampilkan dialog konfirmasi
//                            },
//                            modifier = Modifier.fillMaxWidth()
//                        ) {
//                            Text("Update")
//                        }
//                    }
//                }
//                is UiState.Error -> {
//                    val errorMessage = (uiState as UiState.Error).message
//                    Text(
//                        text = errorMessage,
//                        color = Color.Gray,
//                        modifier = Modifier.align(Alignment.CenterHorizontally)
//                    )
//                }
//                else -> {
//                    Text(
//                        text = "Unknown State",
//                        color = Color.Gray,
//                        modifier = Modifier.align(Alignment.CenterHorizontally)
//                    )
//                }
//            }
//        }
//    }
//
//    // Dialog konfirmasi untuk mengupdate logbook
//    if (showDialog) {
//        AlertDialog(
//            onDismissRequest = { showDialog = false },
//            title = { Text("Konfirmasi Update") },
//            text = { Text("Apakah Anda yakin ingin memperbarui logbook ini?") },
//            confirmButton = {
//                TextButton(
//                    onClick = {
//                        updatedLogbook?.let {
//                            viewModel.editLogbook(logbookId, it, context) // Panggil ViewModel untuk update
//                            navController.navigateUp() // Navigasi kembali setelah update
//                        }
//                        showDialog = false // Tutup dialog setelah konfirmasi
//                    },
//                    colors = ButtonDefaults.textButtonColors(
//                        contentColor = Color.White, // Warna teks tombol
//                        containerColor = Color(0xFFFF9800) // Warna oranye untuk latar belakang
//                    )
//                ) {
//                    Text("Ya")
//                }
//            },
//            dismissButton = {
//                TextButton(
//                    onClick = { showDialog = false }, // Tutup dialog jika dibatalkan
//                    colors = ButtonDefaults.textButtonColors(
//                        contentColor = Color.White, // Warna teks tombol
//                        containerColor = Color(0xFFFF9800) // Warna oranye untuk latar belakang
//                    )
//                ) {
//                    Text("Tidak")
//                }
//            }
//        )
//    }
//
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsLogbookScreen(
    navController: NavController,
    logbookId: Int
) {
    val context = LocalContext.current
    val viewModel: MainViewModel = viewModel()

    val logbookDetails by viewModel.logbookDetails.collectAsState()
    val uiState by viewModel.uiState.collectAsState(initial = UiState.Loading)

    var showDialog by remember { mutableStateOf(false) }
    var updatedLogbook: Logbook? by remember { mutableStateOf(null) }

    var showDatePicker by remember { mutableStateOf(false) }
    var editedTanggal by rememberSaveable { mutableStateOf("") }
    val calendar = remember { Calendar.getInstance() }

    LaunchedEffect(logbookId) {
        viewModel.getLogbookDetails(logbookId, context)
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = calendar.timeInMillis
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        calendar.timeInMillis = millis
                        editedTanggal = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Batal")
                }
            }
        ) {
            DatePicker(state = datePickerState, showModeToggle = false)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Logbook") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (uiState) {
                is UiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                is UiState.Successfull -> {
                    val logbook = logbookDetails
                    if (logbook != null) {
                        if (editedTanggal.isEmpty()) {
                            editedTanggal = logbook.tanggal
                            try {
                                // Parsing tanggal yang ada
                                calendar.time = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(editedTanggal) ?: Calendar.getInstance().time
                                // Menambahkan 1 hari ke tanggal yang sudah ada
                                calendar.add(Calendar.DAY_OF_YEAR, 1)  // Menambahkan 1 hari
                            } catch (e: ParseException) {
                                Log.e("DetailsLogbookScreen", "Error parsing date", e)
                                editedTanggal = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().time)
                            }
                        }


//                        Text(text = "Tanggal", style = MaterialTheme.typography.titleMedium)
                        OutlinedTextField(
                            value = editedTanggal,
                            onValueChange = { /* Jangan diubah langsung di sini */ },
                            label = { Text("Tanggal") },
                            modifier = Modifier.fillMaxWidth(),
                            readOnly = true,
                            trailingIcon = {
                                IconButton(onClick = { showDatePicker = true }) {
                                    Icon(Icons.Default.DateRange, "Pilih Tanggal")
                                }
                            }
                        )

//                        Text(text = "Topik Pekerjaan", style = MaterialTheme.typography.titleMedium)
                        var editedTopikPekerjaan by remember { mutableStateOf(logbook.topik_pekerjaan) }
                        TextField(
                            value = editedTopikPekerjaan,
                            onValueChange = { editedTopikPekerjaan = it },
                            label = { Text("Topik Pekerjaan") },
                            modifier = Modifier.fillMaxWidth()
                        )

//                        Text(text = "Deskripsi", style = MaterialTheme.typography.titleMedium)
                        var editedDeskripsi by remember { mutableStateOf(logbook.deskripsi) }
                        TextField(
                            value = editedDeskripsi,
                            onValueChange = { editedDeskripsi = it },
                            label = { Text("Deskripsi") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                updatedLogbook = Logbook(
                                    id = logbook.id,
                                    tanggal = editedTanggal,
                                    topik_pekerjaan = editedTopikPekerjaan,
                                    deskripsi = editedDeskripsi,
                                    id_mahasiswa = logbook.id_mahasiswa
//                                    image_path = logbook.image_path // Menyalin nilai image_path yang ada
                                )
                                showDialog = true
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Update")
                        }

                    }
                }
                is UiState.Error -> {
                    val errorMessage = (uiState as UiState.Error).message
                    Text(text = errorMessage, color = Color.Gray, modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                else -> {
                    Text(text = "Unknown State", color = Color.Gray, modifier = Modifier.align(Alignment.CenterHorizontally))
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Konfirmasi Update") },
            text = { Text("Apakah Anda yakin ingin memperbarui logbook ini?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        updatedLogbook?.let {
                            viewModel.editLogbook(logbookId, it, context)
                            navController.navigateUp()
                        }
                        showDialog = false
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.White,
                        containerColor = Color(0xFFFF9800)
                    )
                ) {
                    Text("Ya")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.White,
                        containerColor = Color(0xFFFF9800)
                    )
                ) {
                    Text("Tidak")
                }
            }
        )
    }
}






