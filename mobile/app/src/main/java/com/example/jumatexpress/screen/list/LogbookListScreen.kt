package com.example.jumatexpress.screen.list

import LogbookCard
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.FractionalThreshold
import androidx.wear.compose.material.rememberSwipeableState
import androidx.wear.compose.material.swipeable
import com.example.jumatexpress.Logbook
import com.example.jumatexpress.MainViewModel
import com.example.jumatexpress.R
import com.example.jumatexpress.UiState
import kotlin.math.roundToInt




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogbookScreen(
    mainViewModel: MainViewModel,
    navController: NavController,
    onItemClick: (Int) -> Unit
) {
    val logbooks by mainViewModel.logbooks.collectAsState(emptyList())
    val uiState by mainViewModel.uiState.collectAsState(initial = UiState.Loading)
    val context = LocalContext.current

// Memanggil fungsi logbooks saat composable pertama kali dimuat
    LaunchedEffect(Unit) {
        mainViewModel.logbooks(context) // Panggil fungsi logbooks dengan context
    }

// Variabel untuk menyimpan logbook yang dipilih
    var selectedLogbookId by remember { mutableStateOf<String?>(null) }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                // Ambil deskripsi jika diperlukan (misalnya dari input user)
                val description = "Deskripsi gambar logbook" // Ganti dengan input deskripsi dari UI jika ada

                // Pastikan logbook dipilih
                selectedLogbookId?.let { logbookId ->
                    // Panggil fungsi upload gambar dari ViewModel
                    mainViewModel.uploadLogbookImage(logbookId = logbookId, imageUrl = uri, description = description, context = context)
                } ?: run {
                    // Tampilkan pesan atau lakukan sesuatu jika logbook tidak dipilih
                    Log.e("Logbook", "No logbook selected")
                }
            }
        }
    )


    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFFFA726), // Oranye terang
                                Color(0xFFFF7043)  // Oranye kemerahan
                            )
                        )
                    )
            ) {
                TopAppBar(
                    title = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Judul Logbook KP
                            Text(
                                text = "Logbook KP",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 30.sp
                                ),
                                color = Color.White // Warna teks putih untuk kontras yang baik
                            )
                            Spacer(modifier = Modifier.weight(1f)) // Memberikan jarak di antara judul dan logo

                            // Logo di sudut kanan
                            Image(
                                painter = painterResource(id = R.drawable.logo_app2), // Ganti dengan resource logo yang sesuai
                                contentDescription = "Logo",
                                modifier = Modifier
                                    .size(80.dp) // Ukuran logo
                                    .align(Alignment.CenterVertically) // Pastikan sejajar secara vertikal
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent, // Transparan agar gradasi terlihat
                        titleContentColor = Color.White
                    ),
                    modifier = Modifier.background(Color.Transparent) // Tidak menimpa gradasi
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("logbook_entry")
                },
                shape = MaterialTheme.shapes.large,
                containerColor = MaterialTheme.colorScheme.secondary,
                elevation = FloatingActionButtonDefaults.elevation(8.dp)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Tambah Logbook",
                    tint = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.padding(4.dp)
                )
            }
        },
        bottomBar = {
            NavigationBar(containerColor = Color(0xFFFFA726)) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = false,
                    onClick = { navController.navigate("home") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.List, contentDescription = "Laporan") },
                    label = { Text("Laporan") },
                    selected = false,
                    onClick = { navController.navigate("laporan") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Send, contentDescription = "Pengajuan") },
                    label = { Text("Pengajuan") },
                    selected = false,
                    onClick = { navController.navigate("pengajuan") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.LibraryBooks, contentDescription = "Logbook") },
                    label = { Text("Logbook") },
                    selected = true,
                    onClick = {}
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profil") },
                    label = { Text("Profil") },
                    selected = false,
                    onClick = { navController.navigate("profil") }
                )
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            when (uiState) {
                is UiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                is UiState.Successfull -> {
                    if (logbooks.isEmpty()) {
                        Text(
                            text = "Tidak ada logbook ditemukan",
                            color = Color.Gray,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(logbooks) { logbook ->
                                SwipeableLogbookCard(
                                    logbook = logbook,
                                    onDelete = { logbookId ->
                                        mainViewModel.deleteLogbook(logbookId, context)
                                    },
                                    onEdit = { logbookId ->
                                        navController.navigate("edit_logbook/${logbookId}")
                                    },
                                    onClick = {
                                        navController.navigate("logbook_detail/${logbook.id}")
                                    },
                                    // AKTIFKAN INI JIKA MAKE VERSI 2
                                    onUploadImage = { logbookId, uri ->
                                        // Menyimpan ID logbook yang dipilih
                                        selectedLogbookId = logbookId.toString()
                                        // Pastikan ID logbook yang benar dikirimkan saat gambar di-upload
                                        println("Upload gambar untuk logbook ID: $logbookId dengan URI: $uri")
                                        // Panggil launcher untuk memilih gambar dan upload ke logbook yang sesuai
                                        pickImageLauncher.launch("image/*") // Pastikan memanggil launcher di sini
                                    }
                                )
                            }
                        }
                    }
                }
                is UiState.Error -> {
                    val errorMessage = (uiState as UiState.Error).message
                    Text(
                        text = errorMessage,
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                else -> {
                    Text(
                        text = "Unknown State",
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}






//SEKARANG DAH DIBISA DISWIPE YAGESYA
//DAH BISA DIHAPUS JUGA WOWOWOWOOWOWOWOW

//@OptIn(ExperimentalWearMaterialApi::class)
//@Composable
//fun SwipeableLogbookCard(
//    logbook: Logbook,
//    onDelete: (Int) -> Unit, // Terima ID logbook sebagai parameter
//    onClick: () -> Unit,
//    onEdit: (Int) -> Unit // Menambahkan parameter untuk edit
//) {
//    // Menyimpan state swipeable
//    val swipeableState = rememberSwipeableState(initialValue = 0)
//
//    // Lebar tetap untuk area swipe dan tombol hapus
//    val deleteAreaWidth = 100.dp
//
//    // Konversi lebar area hapus ke pixel
//    val deleteAreaWidthPx = with(LocalDensity.current) { deleteAreaWidth.toPx() }
//
//    // Tentukan anchor points yang lebih pasti
//    val anchors = mapOf(0f to 0, -deleteAreaWidthPx to 1)
//
//    // Threshold untuk swipe
//    val swipeThreshold = 0.3f
//
//    // State untuk menampilkan dialog konfirmasi
//    val showDialog = remember { mutableStateOf(false) }
//    val coroutineScope = rememberCoroutineScope()
//
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(IntrinsicSize.Min)
//            .swipeable(
//                state = swipeableState,
//                anchors = anchors,
//                thresholds = { _, _ -> FractionalThreshold(swipeThreshold) },
//                orientation = Orientation.Horizontal // Swipe horizontal
//            )
//    ) {
//        // Background merah untuk tombol hapus (selalu terlihat di kiri)
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color(0xFFFFCDD2)) // Merah pudar
//                .padding(end = 40.dp), // Berpindah ke kiri
//            contentAlignment = Alignment.CenterEnd // Memposisikan ikon di tengah kiri
//        ) {
//            // Ikon sampah
//            Icon(
//                imageVector = Icons.Filled.Delete,
//                contentDescription = "Hapus",
//                tint = Color.Red,
//                modifier = Modifier
//                    .size(30.dp)
//                    .clickable {
//                        // Tampilkan dialog konfirmasi ketika ikon sampah ditekan
//                        showDialog.value = true
//                    }
//            )
//        }
//
//
//        // Konten utama kartu logbook dengan offset
//        Box(
//            modifier = Modifier
//                .offset {
//                    IntOffset(
//                        x = swipeableState.offset.value.roundToInt(),
//                        y = 0
//                    )
//                }
//                .fillMaxWidth()
//                .background(Color.White)
//                .clickable { onClick() }
//        ) {
//            LogbookCard(
//                logbook = logbook,
//                onClick = onClick,
//                onDelete = onDelete,
//                onEdit = onEdit // Pastikan onEdit dipass ke LogbookCard
//            )
//        }
//
//        // Dialog konfirmasi untuk menghapus
//        if (showDialog.value) {
//            AlertDialog(
//                onDismissRequest = { showDialog.value = false },
//                title = { Text("Konfirmasi Penghapusan") },
//                text = { Text("Apakah Anda yakin ingin menghapus logbook ini?") },
//                confirmButton = {
//                    Button(
//                        onClick = {
//                            onDelete(logbook.id) // Lakukan aksi hapus
//                            showDialog.value = false // Tutup dialog setelah hapus
//                        }
//                    ) {
//                        Text("Ya")
//                    }
//                },
//                dismissButton = {
//                    Button(
//                        onClick = { showDialog.value = false } // Menutup dialog tanpa aksi
//                    ) {
//                        Text("Tidak")
//                    }
//                }
//            )
//        }
//    }
//}



//VERSI 2
//INI YANG ADA DUA FITUR BISA HAPUS DAN SATU LAGI
@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun SwipeableLogbookCard(
    logbook: Logbook,
    onDelete: (Int) -> Unit,
    onClick: () -> Unit,
    onEdit: (Int) -> Unit,
    onUploadImage: (Int, Uri?) -> Unit
) {
    val swipeableState = rememberSwipeableState(initialValue = 0)
    val actionsAreaWidth = 160.dp // Lebar area aksi yang lebih proporsional
    val actionsAreaWidthPx = with(LocalDensity.current) { actionsAreaWidth.toPx() }
    val anchors = mapOf(0f to 0, -actionsAreaWidthPx to 1)
    val swipeThreshold = 0.3f
    val showDialog = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                onUploadImage(logbook.id, uri)
            }
        }
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(swipeThreshold) },
                orientation = Orientation.Horizontal
            )
    ) {
        // Background action area dengan gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFFFECB3), // Light orange
                            Color(0xFFFFCC80)  // Darker orange
                        )
                    )
                ),
            contentAlignment = Alignment.CenterEnd
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Upload image button
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.9f))
                        .clickable { pickImageLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.AddPhotoAlternate,
                        contentDescription = "Upload Gambar",
                        tint = Color(0xFF2196F3),
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Delete button
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.9f))
                        .clickable { showDialog.value = true },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Hapus",
                        tint = Color(0xFFE53935),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // Konten utama kartu dengan animasi
        Box(
            modifier = Modifier
                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                .fillMaxWidth()
                .background(
                    color = Color.White,
//                    shape = RoundedCornerShape(8.dp)
                )
//                .shadow(2.dp, RoundedCornerShape(8.dp))
                .clickable { onClick() }
        ) {
            LogbookCard(
                logbook = logbook,
                onClick = onClick,
                onDelete = onDelete,
                onEdit = onEdit
            )
        }

        // Dialog konfirmasi dengan style yang lebih modern
        if (showDialog.value) {
            AlertDialog(
                onDismissRequest = { showDialog.value = false },
                title = {
                    Text(
                        "Konfirmasi Penghapusan",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        "Apakah Anda yakin ingin menghapus logbook ini?",
                        fontSize = 14.sp
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            onDelete(logbook.id)
                            showDialog.value = false
                        }

                    ) {
                        Text("Hapus", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog.value = false }) {
                        Text("Batal")
                    }
                },
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}






