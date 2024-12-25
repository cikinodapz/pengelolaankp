package com.example.jumatexpress

data class User(
    val id: String = "",
    val name: String,
    val email: String,
    val password: String
    // tambahkan field lain sesuai kebutuhan
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val user: User,
    val userId: Int//
)

//data class Logbook(
//    val id: Int = "",
//    val tanggal: String,
//    val topik_pekerjaan: String,
//    val deskripsi: String,
//    val id_mahasiswa: Int
//)

data class Logbook(
    val id: Int = 0,  // Ganti "" dengan nilai default yang valid untuk tipe Int
    val tanggal: String,
    val topik_pekerjaan: String,
    val deskripsi: String,
    val id_mahasiswa: Int,
    val image_path: String? = null,
    val imageUrl: String? = null
)

data class ImageUploadResponse(
    val imageUrl: String // URL gambar setelah di-upload
)



data class LogbookRequest(
    val tanggal: String,
    val topik_pekerjaan: String,
    val deskripsi: String
)

data class LogbookResponse(
    val message: String,
    val logbook: Logbook
)
