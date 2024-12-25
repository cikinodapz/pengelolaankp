package com.example.jumatexpress

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale


//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun LogbookEntryScreen(
//    navController: NavController,
//    viewModel: MainViewModel,
//    context: Context // Menambahkan context untuk akses SharedPreferences
//) {
//    var date by remember { mutableStateOf("") }
//    var activity by remember { mutableStateOf("") }
//    var description by remember { mutableStateOf("") }
//
//    // Snackbar control state
//    val snackbarHostState = remember { SnackbarHostState() }
//    val coroutineScope = rememberCoroutineScope()
//
//    // Ambil ID mahasiswa dan token dari SharedPreferences
//    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
//    val mahasiswaId = sharedPreferences.getInt("user_id", -1) // Default -1 jika ID tidak ada
//    val token = sharedPreferences.getString("jwt_token", "") ?: ""
//
//    val context = LocalContext.current
//
//    // State untuk menampilkan dialog konfirmasi
//    var showDialog by remember { mutableStateOf(false) }
//
//    fun handleSubmit(context: Context) { // Menambahkan parameter context
//        if (date.isNotEmpty() && activity.isNotEmpty() && description.isNotEmpty()) {
//            val logbook = Logbook(
//                tanggal = date,
//                topik_pekerjaan = activity,
//                deskripsi = description,
//                id_mahasiswa = mahasiswaId // Gunakan ID mahasiswa dari SharedPreferences
//            )
//            // Mengirim logbook ke ViewModel untuk ditambahkan ke database, bersama dengan context
//            viewModel.addLogbook(logbook, context)
//
//            coroutineScope.launch {
//                snackbarHostState.showSnackbar("Logbook berhasil ditambahkan!")
//            }
//        } else {
//            coroutineScope.launch {
//                snackbarHostState.showSnackbar("Tolong isi semua kolom!")
//            }
//        }
//    }
//
//    // Dialog konfirmasi untuk submit logbook
//    if (showDialog) {
//        AlertDialog(
//            onDismissRequest = { showDialog = false },
//            title = { Text("Konfirmasi Submit") },
//            text = { Text("Apakah Anda yakin ingin mengirim logbook ini?") },
//            confirmButton = {
//                TextButton(
//                    onClick = {
//                        handleSubmit(context) // Lakukan submit setelah konfirmasi
//                        navController.popBackStack() // Kembali ke halaman sebelumnya setelah submit
//                        showDialog = false // Tutup dialog setelah konfirmasi
//                    },
//                    colors = ButtonDefaults.textButtonColors(
//                        contentColor = Color.White,
//                        containerColor = Color(0xFFFF9800) // Oranye
//                    )
//                ) {
//                    Text("Ya")
//                }
//            },
//            dismissButton = {
//                TextButton(
//                    onClick = { showDialog = false }, // Tutup dialog jika dibatalkan
//                    colors = ButtonDefaults.textButtonColors(
//                        contentColor = Color.White,
//                        containerColor = Color(0xFFFF9800) // Oranye
//                    )
//                ) {
//                    Text("Tidak")
//                }
//            }
//        )
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Tambah Logbook") },
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
//        },
//        snackbarHost = { SnackbarHost(snackbarHostState) }
//    ) { paddingValues ->
//        Column(
//            modifier = Modifier
//                .padding(paddingValues)
//                .padding(16.dp)
//                .fillMaxSize(),
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            OutlinedTextField(
//                value = date,
//                onValueChange = { date = it },
//                label = { Text("Tanggal") },
//                placeholder = { Text("YYYY-MM-DD") },
//                modifier = Modifier.fillMaxWidth(),
//                singleLine = true
//            )
//
//            OutlinedTextField(
//                value = activity,
//                onValueChange = { activity = it },
//                label = { Text("Topik Pekerjaan") },
//                placeholder = { Text("Masukkan topik pekerjaan") },
//                modifier = Modifier.fillMaxWidth(),
//                singleLine = true
//            )
//
//            OutlinedTextField(
//                value = description,
//                onValueChange = { description = it },
//                label = { Text("Deskripsi") },
//                placeholder = { Text("Masukkan deskripsi kegiatan") },
//                modifier = Modifier.fillMaxWidth(),
//                minLines = 3
//            )
//
//            Button(
//                onClick = {
//                    // Tampilkan dialog konfirmasi sebelum submit
//                    if (date.isNotEmpty() && activity.isNotEmpty() && description.isNotEmpty()) {
//                        showDialog = true // Tampilkan dialog konfirmasi
//                    } else {
//                        coroutineScope.launch {
//                            snackbarHostState.showSnackbar("Tolong isi semua kolom!")
//                        }
//                    }
//                },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text("Submit")
//            }
//
//            TextButton(onClick = { navController.popBackStack("logbook", false) }) {
//                // Text("Kembali ke Logbook")
//            }
//        }
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogbookEntryScreen(
    navController: NavController,
    viewModel: MainViewModel,
    context: Context
) {
    var date by remember { mutableStateOf("") }
    var activity by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    // State untuk DatePicker
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(Calendar.getInstance()) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val mahasiswaId = sharedPreferences.getInt("user_id", -1)
    val token = sharedPreferences.getString("jwt_token", "") ?: ""

    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    // DatePicker Dialog
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    // Format tanggal ke YYYY-MM-DD
                    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    date = formatter.format(selectedDate.time)
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
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = selectedDate.timeInMillis
            )

            DatePicker(
                state = datePickerState,
                showModeToggle = false,
                title = { Text("") }
            )

            // Update selectedDate when user picks a date
            datePickerState.selectedDateMillis?.let { millis ->
                selectedDate.timeInMillis = millis
            }
        }
    }

    fun handleSubmit(context: Context) {
        if (date.isNotEmpty() && activity.isNotEmpty() && description.isNotEmpty()) {
            val logbook = Logbook(
                tanggal = date,
                topik_pekerjaan = activity,
                deskripsi = description,
                id_mahasiswa = mahasiswaId
                // Menambahkan nilai default untuk image_path
            )
            viewModel.addLogbook(logbook, context)

            coroutineScope.launch {
                snackbarHostState.showSnackbar("Logbook berhasil ditambahkan!")
            }
        } else {
            coroutineScope.launch {
                snackbarHostState.showSnackbar("Tolong isi semua kolom!")
            }
        }
    }


    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Konfirmasi Submit") },
            text = { Text("Apakah Anda yakin ingin mengirim logbook ini?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        handleSubmit(context)
                        navController.popBackStack()
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tambah Logbook") },
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
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = date,
                onValueChange = { },
                label = { Text("Tanggal") },
                placeholder = { Text("YYYY-MM-DD") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.DateRange, "Pilih Tanggal")
                    }
                },
                singleLine = true
            )

            OutlinedTextField(
                value = activity,
                onValueChange = { activity = it },
                label = { Text("Topik Pekerjaan") },
                placeholder = { Text("Masukkan topik pekerjaan") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Deskripsi") },
                placeholder = { Text("Masukkan deskripsi kegiatan") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Button(
                onClick = {
                    if (date.isNotEmpty() && activity.isNotEmpty() && description.isNotEmpty()) {
                        showDialog = true
                    } else {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Tolong isi semua kolom!")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Submit")
            }

            TextButton(onClick = { navController.popBackStack("logbook", false) }) {
                // Text("Kembali ke Logbook")
            }
        }
    }
}



