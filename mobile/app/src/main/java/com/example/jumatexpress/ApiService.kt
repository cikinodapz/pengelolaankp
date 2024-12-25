package com.example.jumatexpress

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

//data class User(val name: String, val email: String, val password: String)

interface ApiService {
    @GET("users")
    suspend fun getUsers(): Response<List<User>>

    @POST("users")
    suspend fun addUser(@Body user: User): Response<User>

    @POST("login") // Replace "login" with your actual login endpoint
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>

//    @GET("logbooks")
//    suspend fun getLogbooks(
//        @Query("id_mahasiswa") idMahasiswa: Int
//    ): Response<List<Logbook>>


    @GET("logbooks")
    suspend fun getLogbooks(
        @Header("Authorization") token: String
    ): Response<List<Logbook>> // Response tipe List<Logbook>

    @POST("logbooks")
    suspend fun addLogbook(
        @Header("Authorization") token: String,
        @Body logbook: Logbook): Response<Logbook>

    @DELETE("logbooks/{id}")
    suspend fun deleteLogbook(
        @Header("Authorization") token: String,
        @Path("id") logbookId: Int // Menyertakan logbookId sebagai path parameter
    ): Response<Unit>


    //TERAKHIR DAH SAMPE BISA HAPUS DENGAN SWIPE YGY
    @PUT("logbooks/{id}")
    suspend fun editLogbook(
        @Header("Authorization") token: String,
        @Path("id") logbookId: Int, // Menyertakan logbookId sebagai path parameter
        @Body logbook: Logbook // Mengirimkan data logbook yang akan diperbarui
    ): Response<Logbook>


    @GET("logbooks/{id}")
    suspend fun getLogbookDetail(
        @Header("Authorization") token: String,
        @Path("id") logbookId: Int // Menggunakan logbookId sebagai path parameter
    ): Response<Logbook> // Response tipe Logbook

    // Fungsi untuk upload gambar
    @Multipart
    @POST("logbooks/{id}/upload")
    suspend fun uploadLogbookImage(
        @Header("Authorization") token: String, // Token otentikasi
        @Path("id") logbookId: Int, // ID logbook
        @Part image: MultipartBody.Part, // Gambar yang di-upload
//        @Part("description") description: RequestBody // Deskripsi atau data lain (opsional)
    ): Response<ImageUploadResponse> // Response yang berisi hasil upload



    //DONE 4 FITUR AWOAKOWAKOWKOAWKOWAOWK untuk ngehapus nya juga udah di tambah dialog
    //SAMPAI BIKIN API LIHAT DETAIL BARU YA V



    //SEBENARNYA UDAH MENCAPAT MINIMAL FUNGSIONAL TAPI KALO BISA GACOR BANGET PAKE FOTO YGY
    //SEKARANG TAMPILANNYA SUDAH JADI LEBIH CANTIK UWUWUWU

    //HUUHUHUHU DONE SAMPAI BISA NAMPILIN POPUP TANGGAL (DATE PICKER DIALOG)

}