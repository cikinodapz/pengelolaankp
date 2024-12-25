package com.example.jumatexpress

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.jumatexpress.screen.list.LogbookScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val steps = listOf(
        "1. Pembentukan Kelompok",
        "2. Penyusunan Proposal",
        "3. Pengajuan Proposal",
        "4. Penerbitan Surat Permohonan KP",
        "5. Pengiriman Surat ke Instansi",
        "6. Penyerahan Surat Balasan ke Instansi",
        "7. Penerbitan Surat Pengantar KP",
        "8. Pendaftaran Selesai"
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
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Selamat Datang!",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 32.sp
                                ),
                                color = Color.White // Warna teks putih untuk kontras dengan gradasi
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent, // Transparan agar gradasi terlihat
                        titleContentColor = Color.White
                    ),
                    modifier = Modifier.background(Color.Transparent) // Menghindari tumpang tindih gradasi
                )
            }
        },
        bottomBar = {
            NavigationBar(containerColor = Color(0xFFFFA726)) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = true,
                    onClick = {}
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.List, contentDescription = "Laporan") },
                    label = { Text("Laporan") },
                    selected = false,
                    onClick = {}
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Send, contentDescription = "Pengajuan") },
                    label = { Text("Pengajuan") },
                    selected = false,
                    onClick = {}
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.LibraryBooks, contentDescription = "Logbook") },
                    label = { Text("Logbook") },
                    selected = false,
                    onClick = {
                        navController.navigate("logbook")
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profil") },
                    label = { Text("Profil") },
                    selected = false,
                    onClick = {}
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Background image as watermark
            Image(
                painter = painterResource(id = R.drawable.logo_app2), // Ganti dengan resource logo yang sesuai
                contentDescription = "Logo Watermark",
                modifier = Modifier
                    .fillMaxSize() // Mengisi seluruh ukuran Box
                    .graphicsLayer(alpha = 0.3f), // Menambahkan transparansi
                contentScale = ContentScale.Fit // Sesuaikan ukuran gambar
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Tata Cara Pendaftaran Kerja Praktek",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                steps.forEachIndexed { index, step ->
                    Column {
                        Text(
                            text = step,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 16.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        val additionalText = when (index) {
                            0 -> "Mahasiswa membentuk kelompok sesuai dengan ketentuan yang berlaku."
                            1 -> "Setiap kelompok menyusun proposal yang berisi profil instansi dan rencana kegiatan."
                            2 -> "Proposal diajukan kepada departemen terkait untuk mendapatkan persetujuan."
                            3 -> "Setelah disetujui, surat permohonan KP diterbitkan untuk instansi terkait."
                            4 -> "Surat permohonan KP dikirim ke instansi yang bersangkutan."
                            5 -> "Surat balasan dikembalikan ke departemen untuk proses lebih lanjut."
                            6 -> "Departemen menerbitkan surat pengantar KP untuk instansi."
                            7 -> "Setelah diterima oleh instansi, pendaftaran KP selesai tercatat."
                            else -> null
                        }

                        additionalText?.let {
                            Text(
                                text = it,
                                modifier = Modifier.fillMaxWidth(),
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                                color = Color.Black,
                                textAlign = TextAlign.Justify
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}








@Composable
fun MainScreen() {
    val navController = rememberNavController()

    // Ambil token dari SharedPreferences atau tempat penyimpanan lainnya
    val sharedPreferences = LocalContext.current.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    val token = sharedPreferences.getString("auth_token", "") ?: ""

    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("logbook") {
            val mainViewModel: MainViewModel = viewModel() // Ambil ViewModel di sini
            LogbookScreen(
                mainViewModel = mainViewModel,
                navController = navController, // Kirim token ke LogbookScreen
                onItemClick = { entryId ->
                    // Navigasi ke detail
                    navController.navigate(Screen.LogbookDetail.createRoute(entryId.toString()))
                }
            )
        }
    }
}



