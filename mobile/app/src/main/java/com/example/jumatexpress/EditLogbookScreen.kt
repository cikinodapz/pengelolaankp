//package com.example.jumatexpress
//
//import android.content.Context
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material3.Button
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.SnackbarHost
//import androidx.compose.material3.SnackbarHostState
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextButton
//import androidx.compose.material3.TopAppBar
//import androidx.compose.material3.TopAppBarDefaults
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import kotlinx.coroutines.launch
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun EditLogbookScreen(
//    navController: NavController,
//    viewModel: MainViewModel,
//    context: Context,
//    logbookId: Int // ID logbook yang akan diedit
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
//
//    // Ambil data logbook yang akan diedit dari ViewModel
//    val logbook = viewModel.getLogbookById(logbookId)
//
//    // Set initial state for fields (date, activity, description)
//    LaunchedEffect(logbook) {
//        logbook?.let {
//            date = it.tanggal
//            activity = it.topik_pekerjaan
//            description = it.deskripsi
//        }
//    }
//
//    fun handleUpdate() {
//        if (date.isNotEmpty() && activity.isNotEmpty() && description.isNotEmpty()) {
//            val updatedLogbook = Logbook(
//                tanggal = date,
//                topik_pekerjaan = activity,
//                deskripsi = description,
//                id_mahasiswa = mahasiswaId,
//                id = logbookId // ID tetap sama saat update
//            )
//
//            // Mengirimkan objek Logbook dan context yang benar
//            viewModel.editLogbook(logbookId = logbookId, updatedLogbook = updatedLogbook, context = context)
//
//            coroutineScope.launch {
//                snackbarHostState.showSnackbar("Logbook berhasil diperbarui!")
//            }
//        } else {
//            coroutineScope.launch {
//                snackbarHostState.showSnackbar("Tolong isi semua kolom!")
//            }
//        }
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Edit Logbook") },
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
//                    handleUpdate() // Update logbook dengan context
//                    coroutineScope.launch {
//                        snackbarHostState.showSnackbar("Logbook berhasil diperbarui!")
//                        navController.popBackStack() // Kembali ke halaman sebelumnya setelah snackbar selesai
//                    }
//                },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text("Update")
//            }
//
//            TextButton(onClick = { navController.popBackStack() }) {
//                Text("Kembali ke Logbook")
//            }
//        }
//    }
//}
//
//
