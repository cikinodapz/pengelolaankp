package com.example.jumatexpress

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.*
import com.example.jumatexpress.ui.theme.JumatexpressTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.jumatexpress.UiState.Success
import com.example.jumatexpress.screen.list.LogbookScreen
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream


sealed class UiState {
    object Loading : UiState()
    object Successfull : UiState()
    data class Success(val data: Any) : UiState() // Menggunakan tipe data 'Any' atau tipe lain yang spesifik
    data class Error(val message: String) : UiState()
}


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JumatexpressTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            val viewModel: MainViewModel = viewModel() // Menggunakan ViewModel untuk logika login
            LoginScreen(navController = navController, viewModel = viewModel)
        }
        composable("register") {
            val viewModel: MainViewModel = viewModel()
            RegisterScreen(navController = navController, viewModel = viewModel)
        }
        composable("home") {
            HomeScreen(navController = navController)// Halaman Home yang akan ditampilkan setelah login berhasil
        }

        composable("logbook") {
            val viewModel: MainViewModel = viewModel()



            LogbookScreen(
                navController = navController,
                mainViewModel = viewModel, // Pastikan token diteruskan ke LogbookScreen
                onItemClick = { entryId ->
                    // Handle item click, misalnya navigasi ke detail
                    navController.navigate(Screen.LogbookDetail.createRoute(entryId.toString()))
                }
            )
        }

        composable("logbook_entry") {
            val context = LocalContext.current // Mendapatkan Context
            val viewModel: MainViewModel = viewModel() // Mendapatkan ViewModel
            LogbookEntryScreen(
                navController = navController,
                viewModel = viewModel,
                context = context // Menyediakan context
            )
        }

        // Rute untuk halaman detail logbook
        composable(
            route = "logbook_detail/{logbookId}",
            arguments = listOf(navArgument("logbookId") { type = NavType.IntType }) // Gunakan NavType.IntType untuk ID yang bertipe Integer
        ) { backStackEntry ->
            val logbookId = backStackEntry.arguments?.getInt("logbookId") // Ambil ID dalam bentuk Int
            if (logbookId != null) {
                DetailsLogbookScreen(navController = navController, logbookId = logbookId)
            } else {
                // Jika logbookId tidak ada atau null, tampilkan layar kosong atau error
                Text("Logbook ID tidak valid")
            }
        }

    }




        // Menambahkan rute untuk halaman edit logbook

//        SAYA KOMEN DULU YA UNTUK REVISI
//        composable("edit_logbook/{logbookId}") { backStackEntry ->
//            val logbookId = backStackEntry.arguments?.getString("logbookId")?.toIntOrNull() ?: 0
//            val context = LocalContext.current // Mendapatkan Context
//            val viewModel: MainViewModel = viewModel() // Mendapatkan ViewModel
//            EditLogbookScreen(
//                navController = navController,
//                logbookId = logbookId,
//                viewModel = viewModel,
//                context = context
//            )
//        }



}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, viewModel: MainViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Observasi status UI
    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        // Card Container
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .shadow(8.dp, RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.8f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo
                Image(
                    painter = painterResource(id = R.drawable.logo_app2), // Sesuaikan dengan nama file di drawable
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .size(160.dp)
                        .padding(bottom = 16.dp)
                )

                // Header Section
                Text(
                    text = "Praktify App",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Please login to continue",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Email Input
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary
                    )
                )

                // Password Input
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary
                    )
                )

                // Login Button
                Button(
                    onClick = {
                        viewModel.login(email, password, context)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                    enabled = email.isNotEmpty() && password.isNotEmpty()
                ) {
                    Text("Login", color = MaterialTheme.colorScheme.onPrimary)
                }

                // Register Redirect
                TextButton(onClick = { navController.navigate("register") }) {
                    Text(
                        "Don't have an account? Register here",
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // Error Message
                if (uiState is UiState.Error) {
                    val errorMessage = (uiState as UiState.Error).message
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
        }
    }

    // Navigate on Successful Login
    LaunchedEffect(uiState) {
        if (uiState is UiState.Successfull) {
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        }
    }
}




// Ganti function viewModel() dengan companion object ini
@Composable
fun viewModel(): MainViewModel {
    val factory = remember {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return MainViewModel() as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }

    return androidx.lifecycle.viewmodel.compose.viewModel(factory = factory)
}

@Composable
fun AddUserForm(onSubmit: (User) -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            singleLine = true
        )

        Button(
            onClick = {
                if (name.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
                    // Tambahkan id saat membuat objek User
                    onSubmit(User(
                        id = "", // Bisa diisi dengan UUID atau string kosong
                        name = name,
                        email = email,
                        password = password
                    ))
                    // Clear form after submit
                    name = ""
                    email = ""
                    password = ""
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = name.isNotBlank() && email.isNotBlank() && password.isNotBlank()
        ) {
            Text("Submit")
        }
    }
}

@Composable
fun UserList(users: List<User>) {
    LazyColumn {
        items(users) { user ->
            Text(text = "${user.name} - ${user.email}")
        }
    }
}

class MainViewModel : ViewModel() {
    private val api = ApiClient.instance.create(ApiService::class.java)

    // UI state untuk detail logbook
    private val _logbookDetails = MutableStateFlow<Logbook?>(null) // Menyimpan detail logbook
    val logbookDetails: StateFlow<Logbook?> = _logbookDetails

    // UI state for general users and logbooks
    val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    // UI state for logbooks specifically
    val _logbooks = MutableStateFlow<List<Logbook>>(emptyList())
    val logbooks: StateFlow<List<Logbook>> = _logbooks

    // State to store the current user's ID
    private val _userId = MutableStateFlow<Int?>(null)  // Initial state is null (no user logged in)
    val userId: StateFlow<Int?> = _userId

    init {
        loadUsers()
    }

    // Load users
    fun loadUsers() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = api.getUsers()
                if (response.isSuccessful && response.body() != null) {
                    _uiState.value = Success(response.body()!!)
                } else {
                    _uiState.value = UiState.Error("Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error: ${e.message}")
            }
        }
    }

    // Add user
    fun addUser(user: User) {
        viewModelScope.launch {
            try {
                val response = api.addUser(user)
                if (response.isSuccessful) {
                    loadUsers() // Refresh data after successful addition
                } else {
                    _uiState.value = UiState.Error("Error adding user: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error adding user: ${e.message}")
            }
        }
    }


    fun login(email: String, password: String, context: Context) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val response = api.loginUser(LoginRequest(email, password))

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        // Simpan token dan userId
                        saveToken(context, loginResponse.token)
                        _userId.value = loginResponse.userId  // Menyimpan userId yang diterima dari server

                        // Perbarui UI state menjadi Success
                        _uiState.value = UiState.Successfull
                    } else {
                        _uiState.value = UiState.Error("Login failed: Empty response body")
                    }
                } else {
                    _uiState.value = UiState.Error("Login failed: ${response.message()}")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error: ${e.localizedMessage}")
            }
        }
    }

    private fun saveToken(context: Context, token: String) {
        // Menyimpan token JWT ke SharedPreferences
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("jwt_token", token).apply()
    }

    fun getToken(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("jwt_token", "") ?: ""
    }






//    fun logbooks(context: Context) {
//        val token = getToken(context) // Ambil token yang disimpan
//        if (token != null) {
//            viewModelScope.launch {
//                _uiState.value = UiState.Loading
//                try {
//                    val response = api.getLogbooks("Bearer $token") // Menggunakan token di header
//                    if (response.isSuccessful) {
//                        _logbooks.value = response.body() ?: emptyList()
//                        _uiState.value = UiState.Successfull
//                    } else {
//                        _uiState.value = UiState.Error("Failed: ${response.message()}")
//                    }
//                } catch (e: Exception) {
//                    _uiState.value = UiState.Error("Error: ${e.localizedMessage ?: "Unknown error"}")
//                }
//            }
//        } else {
//            _uiState.value = UiState.Error("Token is missing or invalid. Please login again.")
//        }
//    }
fun logbooks(context: Context) {
    val token = getToken(context)
    if (token != null) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val response = api.getLogbooks("Bearer $token")
                if (response.isSuccessful) {
                    val logbooks = response.body() ?: emptyList()
                    val logbooksWithImageUrl = logbooks.map { logbook ->
                        logbook.copy(
                            imageUrl = logbook.image_path?.let { path ->
                                // Ganti dengan URL server yang sesuai
                                "http://10.0.2.2:3000/$path"  // Pastikan sesuai dengan path server Anda
                            }
                        )
                    }

                    _logbooks.value = logbooksWithImageUrl
                    _uiState.value = UiState.Successfull
                } else {
                    _uiState.value = UiState.Error("Failed: ${response.message()}")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error: ${e.localizedMessage ?: "Unknown error"}")
            }
        }
    } else {
        _uiState.value = UiState.Error("Token is missing or invalid. Please login again.")
    }
}



    //DONE SAMPAI LIHAT LOGBOOK SESUAI DENGAN ID YANG LOGIN YA YANK


    fun addLogbook(logbook: Logbook, context: Context) {
        val token = getToken(context)
        viewModelScope.launch {
            try {
                // Set UI ke state Loading
                _uiState.value = UiState.Loading

                // Panggil API untuk menambahkan logbook
                val response = api.addLogbook("Bearer $token",logbook)

                if (response.isSuccessful) {
                    // Ambil body response
                    val responseBody = response.body()
                    if (responseBody != null) {
                        // Update logbooks dengan data baru
                        _logbooks.value = _logbooks.value + responseBody

                        // Update UI ke state Success
                        _uiState.value = UiState.Successfull
                    } else {
                        // Jika body null, laporkan error
                        _uiState.value = UiState.Error("Respons API kosong, logbook tidak ditemukan.")
                    }
                } else {
                    // Laporkan error jika response API gagal
                    val errorMessage = "Gagal: Kode ${response.code()} - ${response.message()}"
                    _uiState.value = UiState.Error(errorMessage)
                }
            } catch (e: Exception) {
                // Penanganan exception dengan log error yang lebih jelas
                val errorMessage = "Kesalahan: ${e.localizedMessage ?: "Tidak diketahui"}"
                _uiState.value = UiState.Error(errorMessage)
            }
        }
    }

    fun deleteLogbook(logbookId: Int, context: Context) {
        val token = getToken(context) // Ambil token yang disimpan
        if (token != null) {
            viewModelScope.launch {
                _uiState.value = UiState.Loading
                try {
                    // Pastikan endpoint sudah benar dengan menambahkan logbookId di URL
                    val response = api.deleteLogbook("Bearer $token", logbookId) // Menggunakan token di header dan ID logbook di URL
                    if (response.isSuccessful) {
                        // Jika penghapusan berhasil, beri tahu UI dengan state sukses
                        _uiState.value = UiState.Successfull
                        // Setelah berhasil, kita bisa memanggil kembali logbooks untuk memperbarui daftar logbook
                        logbooks(context) // Memanggil kembali logbooks untuk refresh data
                    } else {
                        // Jika gagal, beri tahu UI dengan pesan error
                        _uiState.value = UiState.Error("Failed: ${response.message()}")
                    }
                } catch (e: Exception) {
                    // Tangani error jika ada masalah jaringan atau lainnya
                    _uiState.value = UiState.Error("Error: ${e.localizedMessage ?: "Unknown error"}")
                }
            }
        } else {
            // Jika token tidak ditemukan, beri tahu UI bahwa pengguna perlu login kembali
            _uiState.value = UiState.Error("Token is missing or invalid. Please login again.")
        }
    }


//    // Fungsi untuk mendapatkan logbook berdasarkan ID
//    fun getLogbookById(id: String?): Logbook? {
//        return _logbooks.value.find { it.id == id }
//    }

    fun getLogbookById(id: String?): Logbook? {
        // Mengonversi id yang bertipe String? menjadi Int jika tidak null
        val logbookId = id?.toIntOrNull()

        // Jika konversi gagal (id null atau tidak bisa dikonversi menjadi Int), kembalikan null
        return if (logbookId != null) {
            _logbooks.value.find { it.id == logbookId }
        } else {
            null
        }
    }

        //AKU KOMENTAR DULU YA SAYANG KUUU
//    fun editLogbook(logbookId: Int, updatedLogbook: Logbook, context: Context) {
//        val token = getToken(context) // Ambil token yang disimpan
//        if (token != null) {
//            viewModelScope.launch {
//                _uiState.value = UiState.Loading
//                try {
//                    // Panggil API untuk memperbarui logbook
//                    val response = api.editLogbook("Bearer $token", logbookId, updatedLogbook)
//
//                    if (response.isSuccessful) {
//                        val responseBody = response.body()
//                        if (responseBody != null) {
//                            // Perbarui daftar logbook di UI
//                            _logbooks.value = _logbooks.value.map { logbook ->
//                                if (logbook.id == logbookId) responseBody else logbook
//                            }
//
//                            // Update state UI ke Success
//                            _uiState.value = UiState.Successfull
//                        } else {
//                            // Jika body null, laporkan error
//                            _uiState.value = UiState.Error("Response body kosong, logbook tidak ditemukan.")
//                        }
//                    } else {
//                        // Laporkan error jika response gagal
//                        val errorMessage = "Gagal: Kode ${response.code()} - ${response.message()}"
//                        _uiState.value = UiState.Error(errorMessage)
//                    }
//                } catch (e: Exception) {
//                    // Penanganan exception dengan log error yang lebih jelas
//                    val errorMessage = "Kesalahan: ${e.localizedMessage ?: "Tidak diketahui"}"
//                    _uiState.value = UiState.Error(errorMessage)
//                }
//            }
//        } else {
//            // Jika token tidak ditemukan, beri tahu UI bahwa pengguna perlu login kembali
//            _uiState.value = UiState.Error("Token is missing or invalid. Please login again.")
//        }
//    }

    fun editLogbook(logbookId: Int, updatedLogbook: Logbook, context: Context) {
        val token = getToken(context) // Ambil token yang disimpan
        if (token != null) {
            viewModelScope.launch {
                _uiState.value = UiState.Loading
                try {
                    // Panggil API untuk memperbarui logbook
                    val response = api.editLogbook("Bearer $token", logbookId, updatedLogbook)

                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            // Perbarui daftar logbook di UI
                            _logbooks.value = _logbooks.value.map { logbook ->
                                if (logbook.id == logbookId) responseBody else logbook
                            }

                            // Update state UI ke Success
                            _uiState.value = UiState.Successfull
                        } else {
                            // Jika body null, laporkan error
                            _uiState.value = UiState.Error("Response body kosong, logbook tidak ditemukan.")
                        }
                    } else {
                        // Laporkan error jika response gagal
                        val errorMessage = "Gagal: Kode ${response.code()} - ${response.message()}"
                        _uiState.value = UiState.Error(errorMessage)
                    }
                } catch (e: Exception) {
                    // Penanganan exception dengan log error yang lebih jelas
                    val errorMessage = "Kesalahan: ${e.localizedMessage ?: "Tidak diketahui"}"
                    _uiState.value = UiState.Error(errorMessage)
                }
            }
        } else {
            // Jika token tidak ditemukan, beri tahu UI bahwa pengguna perlu login kembali
            _uiState.value = UiState.Error("Token is missing or invalid. Please login again.")
        }
    }


    fun getLogbookDetails(logbookId: Int, context: Context) {
        val token = getToken(context) // Ambil token yang disimpan
        if (token != null) {
            viewModelScope.launch {
                _uiState.value = UiState.Loading
                try {
                    // Panggil API untuk mendapatkan detail logbook
                    val response = api.getLogbookDetail("Bearer $token", logbookId)
                    if (response.isSuccessful) {
                        val logbookDetails = response.body()
                        if (logbookDetails != null) {
                            // Simpan data detail logbook ke LiveData
                            _logbookDetails.value = logbookDetails
                            _uiState.value = UiState.Successfull
                        } else {
                            _uiState.value = UiState.Error("Detail logbook tidak ditemukan.")
                        }
                    } else {
                        // Laporkan error jika response gagal
                        _uiState.value = UiState.Error("Gagal memuat detail logbook: ${response.message()}")
                    }
                } catch (e: Exception) {
                    // Penanganan exception dengan log error yang lebih jelas
                    _uiState.value = UiState.Error("Kesalahan: ${e.localizedMessage ?: "Tidak diketahui"}")
                }
            }
        } else {
            // Jika token tidak ditemukan, beri tahu UI bahwa pengguna perlu login kembali
            _uiState.value = UiState.Error("Token is missing or invalid. Please login again.")
        }
    }

    fun uploadLogbookImage(logbookId: String, imageUrl: Uri, description: String, context: Context) {
        val token = getToken(context) // Ambil token yang disimpan
        if (token != null) {
            viewModelScope.launch {
                _uiState.value = UiState.Loading
                try {
                    // Mengonversi Uri menjadi File
                    // Membuat nama file yang unik menggunakan timestamp atau random string
                    val uniqueFileName = "logbook_image_${System.currentTimeMillis()}.jpg" // Gunakan timestamp untuk nama unik
                    val file = File(context.cacheDir, uniqueFileName)

                    val inputStream = context.contentResolver.openInputStream(imageUrl)
                    val outputStream = FileOutputStream(file)
                    inputStream?.copyTo(outputStream)

                    // Membuat request body untuk gambar
                    val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    val body = MultipartBody.Part.createFormData("image", file.name, requestBody)

                    // Panggil API untuk upload gambar
                    val response = api.uploadLogbookImage("Bearer $token", logbookId.toInt(), body)

                    if (response.isSuccessful) {
                        _uiState.value = UiState.Successfull
                        // Jika berhasil, Anda bisa mengupdate UI atau data lainnya
                    } else {
                        _uiState.value = UiState.Error("Upload gagal: ${response.message()}")
                    }
                } catch (e: Exception) {
                    _uiState.value = UiState.Error("Error: ${e.localizedMessage}")
                }
            }
        } else {
            _uiState.value = UiState.Error("Token is missing or invalid. Please login again.")
        }
    }





}


//KEDUA KODINGAN INI DALAM BLOK MAINVIEWMODEL TADI YA
//private fun saveToken(token: String) {
//    // Simpan token JWT ke SharedPreferences atau secure storage
//}
//
//fun saveToken(context: Context, token: String) {
//    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
//    sharedPreferences.edit().putString("jwt_token", token).apply()
//}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    JumatexpressTheme {
        // Gunakan Modifier.padding untuk mendefinisikan padding yang diinginkan
        MainScreen(modifier = Modifier.padding(16.dp))
    }
}

//@Composable
//fun LogbookScreenWrapper(
//    navController: NavController,
//    mainViewModel: MainViewModel,
//    onItemClick: (Int) -> Unit
//) {
//    val idMahasiswa = 20 // Ambil ID mahasiswa dari shared preferences atau argument lainnya
//
//    LaunchedEffect(Unit) {
//        mainViewModel.logbooks(idMahasiswa)
//    }
//
//    LogbookScreen(
//        mainViewModel,
//        navController = navController,  // Meneruskan navController ke LogbookScreen
//        onItemClick = onItemClick
//    )
//}


//DONE SAMPAI TAMBAH LOGBOOK DLU YA GES YA MESKI MASI BERANTAKAN TAPI ADA KEMAJUAN ALHAMDULILLAH
