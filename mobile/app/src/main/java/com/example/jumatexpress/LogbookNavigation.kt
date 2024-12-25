package com.example.jumatexpress

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jumatexpress.screen.list.LogbookScreen

sealed class Screen(val route: String) {
    object LogbookList : Screen("logbook_list")
    object LogbookEntry : Screen("logbook_entry")
    object LogbookDetail : Screen("logbook_detail/{entryId}") {
        fun createRoute(entryId: String) = "logbook_detail/$entryId"
        const val routeWithArgs = "logbook_detail/{entryId}"
    }
}

//@Composable
//fun LogbookNavigation(
//    navController: NavHostController = rememberNavController(),
//    mainViewModel: MainViewModel, // Menggunakan parameter yang sudah ada
//    onItemClick: (Long) -> Unit // Mendefinisikan tipe yang lebih jelas untuk onItemClick
//) {
//    NavHost(
//        navController = navController,
//        startDestination = Screen.LogbookList.route
//    ) {
//        composable(route = Screen.LogbookList.route) {
//            // Menyediakan mainViewModel untuk LogbookScreenWrapper
//            LogbookScreen(
//                mainViewModel = mainViewModel, // Menyertakan mainViewModel di sini
//                navController = navController,
//                onItemClick = { entryId ->
//                    navController.navigate(Screen.LogbookDetail.createRoute(entryId.toString()))
//                }
//            )
//        }
//
//        // Menambahkan route untuk LogbookEntry
//        composable(route = Screen.LogbookEntry.route) {
//            // Menyediakan mainViewModel untuk LogbookEntryScreen
//            LogbookEntryScreen(
//                viewModel = mainViewModel,
//                navController = navController
//            )
//        }
//
//        composable("logbook_detail/{logbookId}") { backStackEntry ->
//            val logbookId = backStackEntry.arguments?.getString("logbookId")?.toIntOrNull()
//            logbookId?.let {
//                LogbookDetailScreen(logbookId = it) // Panggil screen detail dengan ID logbook
//            }
//        }
//    }
//}

@Composable
fun LogbookNavigation(
    navController: NavHostController = rememberNavController(),
    mainViewModel: MainViewModel, // Menggunakan parameter yang sudah ada
    onItemClick: (Long) -> Unit // Mendefinisikan tipe yang lebih jelas untuk onItemClick
) {
    // Ambil token dari SharedPreferences atau tempat penyimpanan lainnya
    val sharedPreferences = LocalContext.current.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    val token = sharedPreferences.getString("auth_token", "") ?: ""

    NavHost(
        navController = navController,
        startDestination = Screen.LogbookList.route
    ) {
        composable(route = Screen.LogbookList.route) {
            // Menyediakan mainViewModel untuk LogbookScreenWrapper
            LogbookScreen(
                mainViewModel = mainViewModel, // Menyertakan mainViewModel di sini
                navController = navController, // Kirimkan token ke LogbookScreen
                onItemClick = { entryId ->
                    navController.navigate(Screen.LogbookDetail.createRoute(entryId.toString()))
                }
            )
        }
//        // Tambahkan rute lain jika perlu, misalnya untuk detail logbook
//        composable(route = Screen.LogbookDetail.route) { backStackEntry ->
//            val entryId = backStackEntry.arguments?.getString("entryId")?.toLongOrNull() ?: 0L
//            LogbookDetailScreen(entryId = entryId) // Ganti dengan detail screen yang sesuai
//        }
    }
}

