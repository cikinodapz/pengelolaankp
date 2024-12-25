import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.jumatexpress.Logbook
import com.example.jumatexpress.R
import java.text.SimpleDateFormat
import java.util.Locale


//@Composable
//fun LogbookCard(
//    logbook: Logbook,
//    onDelete: (Int) -> Unit,
//    onClick: () -> Unit,
//    onEdit: (Int) -> Unit
//) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp, vertical = 12.dp)
//            .clickable(onClick = onClick),
//        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
//        shape = RoundedCornerShape(16.dp), // Sudut lebih melengkung untuk tampilan modern
//        colors = CardDefaults.cardColors(
//            containerColor = Color(0xFFFDFDFD) // Latar belakang dengan warna netral
//        )
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(
//                    brush = Brush.verticalGradient(
//                        colors = listOf(
//                            Color(0xFFFFE0B2), // Oranye muda pudar di atas
//                            Color(0xFFFFF3E0)  // Oranye sangat terang di bawah
//                        )
//                    )
//                )
//        ) {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp)
//            ) {
//                // Bagian Ikon Kartu
//                Box(
//                    modifier = Modifier
//                        .size(50.dp)
//                        .background(
//                            color = Color(0xFFFFB74D), // Warna oranye terang untuk ikon
//                            shape = CircleShape
//                        ),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Book,
//                        contentDescription = "Logbook Icon",
//                        tint = Color.White,
//                        modifier = Modifier.size(28.dp)
//                    )
//                }
//
//                Spacer(modifier = Modifier.width(16.dp))
//
//                // Bagian Teks Kartu
//                Column(
//                    modifier = Modifier.weight(1f)
//                ) {
//                    // Tanggal Logbook
//                    Text(
//                        text = logbook.tanggal,
//                        style = MaterialTheme.typography.titleMedium.copy(
//                            color = Color(0xFFEF6C00), // Oranye gelap untuk tanggal
//                            fontWeight = FontWeight.Bold
//                        )
//                    )
//                    Spacer(modifier = Modifier.height(4.dp))
//
//                    // Topik Pekerjaan
//                    Text(
//                        text = logbook.topik_pekerjaan,
//                        style = MaterialTheme.typography.bodyLarge.copy(
//                            color = Color(0xFF6D4C41), // Warna coklat kehijauan untuk teks
//                            fontWeight = FontWeight.Medium,
//                            letterSpacing = 0.5.sp
//                        )
//                    )
//                }
//            }
//        }
//    }
//}


//VERSI 2
@Composable
fun LogbookCard(
    logbook: Logbook,
    onDelete: (Int) -> Unit,
    onClick: () -> Unit,
    onEdit: (Int) -> Unit
) {
    // Mengonversi tanggal ke format yang diinginkan
    val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale("id", "ID")) // Format "1 Januari 2025"
    val formattedDate = try {
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(logbook.tanggal)
        dateFormat.format(date)
    } catch (e: Exception) {
        logbook.tanggal // Jika terjadi kesalahan, tampilkan tanggal asli
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFDFDFD)
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Bagian Gambar Logbook
            if (!logbook.imageUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = logbook.imageUrl,
                    contentDescription = "Logbook Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp) // Ukuran tinggi gambar
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                    contentScale = ContentScale.Crop // Mengatur agar gambar memenuhi area dengan crop
                )
            } else {
                // Placeholder jika `imageUrl` kosong
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp) // Ukuran tinggi placeholder
                        .background(
                            color = Color(0xFFFFB74D), // Warna latar belakang placeholder
                            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = "Placeholder Icon",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp) // Ukuran ikon placeholder
                    )
                }
            }

            // Bagian Teks Kartu
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Tanggal Logbook
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = Color(0xFFEF6C00),
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Topik Pekerjaan
                Text(
                    text = logbook.topik_pekerjaan,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color(0xFF6D4C41),
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.5.sp
                    )
                )
            }
        }
    }
}











