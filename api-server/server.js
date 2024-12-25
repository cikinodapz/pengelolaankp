const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');
const { User } = require('./models');
const { Logbook } = require('./models');
const jwt = require('jsonwebtoken');
const multer = require('multer');
const path = require('path');




const app = express();
app.use(bodyParser.json());
app.use(cors());
app.use('/uploads', express.static(path.join(__dirname, 'uploads')));

// Konfigurasi multer
const storage = multer.diskStorage({
    destination: (req, file, cb) => {
        cb(null, 'uploads/'); // Folder tempat menyimpan file
    },
    filename: (req, file, cb) => {
        cb(null, `${Date.now()}-${file.originalname}`);
    }
});

const upload = multer({
    storage: storage,
    limits: { fileSize: 5 * 1024 * 1024 }, // Maksimal 5MB
    fileFilter: (req, file, cb) => {
        const fileTypes = /jpeg|jpg|png/;
        const extName = fileTypes.test(path.extname(file.originalname).toLowerCase());
        const mimeType = fileTypes.test(file.mimetype);

        if (extName && mimeType) {
            cb(null, true);
        } else {
            cb(new Error('Hanya file gambar yang diperbolehkan!'));
        }
    }
});

// Tambah data user
app.post('/users', async (req, res) => {
    try {
        const { name, email, password } = req.body;
        const user = await User.create({ name, email, password });
        res.status(201).json(user);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

// Ambil semua user
app.get('/users', async (req, res) => {
    try {
        const users = await User.findAll();
        res.json(users);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

// // Login user
// app.post('/login', async (req, res) => {
//     try {
//         const { email, password } = req.body;

//         // Cari user berdasarkan email
//         const user = await User.findOne({ where: { email } });
//         if (!user) {
//             return res.status(404).json({ error: 'User not found' });
//         }

//         // Bandingkan password secara langsung
//         if (user.password !== password) {
//             return res.status(401).json({ error: 'Invalid password' });
//         }

//         // Tambahkan log setelah user ditemukan dan password cocok
//         console.log('User ID:', user.id);

//         // Login berhasil, kirimkan userId dan token
//         res.status(200).json({
//             message: 'Login successful',
//             token: 'some-token',
//             userId: user.id,  // Pastikan ID mahasiswa dikirimkan
//             user: user
//         });
//     } catch (err) {
//         res.status(500).json({ error: err.message });
//     }
// });



//INI YANG BENAR API NYA YAAA
app.post('/login', async (req, res) => {
    try {
        const { email, password } = req.body;

        // Cari user berdasarkan email
        const user = await User.findOne({ where: { email } });
        if (!user) {
            return res.status(404).json({ error: 'User not found' });
        }

        // Bandingkan password secara langsung
        if (user.password !== password) {
            return res.status(401).json({ error: 'Invalid password' });
        }

        // Tambahkan log setelah user ditemukan dan password cocok
        console.log('User ID:', user.id);

        // Buat JWT token, misalnya dengan durasi 1 jam
        const token = jwt.sign({ userId: user.id }, 'secret-key', { expiresIn: '1h' });

        // Login berhasil, kirimkan userId dan token
        res.status(200).json({
            message: 'Login successful',
            token: token,
            userId: user.id,  // Pastikan ID mahasiswa dikirimkan
            user: user
        });
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});



// const jwt = require('jsonwebtoken');

//INI JUGA BENAR MIDDLEWARE NYA
// Middleware untuk memverifikasi token
const authenticateToken = (req, res, next) => {
    const authHeader = req.headers['authorization'];
    const token = authHeader && authHeader.split(' ')[1]; // Format "Bearer <token>"

    if (!token) {
        return res.status(401).json({ error: 'Access token is missing' });
    }

    jwt.verify(token, 'secret-key', (err, user) => {
        if (err) {
            return res.status(403).json({ error: 'Invalid or expired token' });
        }

        req.user = user; // Simpan payload token di objek req untuk digunakan di endpoint berikutnya
        next();
    });
};

// Endpoint untuk upload gambar
app.post('/logbooks/:id/upload', authenticateToken, upload.single('image'), async (req, res) => {
    try {
        // Ambil userId dari token
        const userId = req.user.userId;

        // Ambil ID logbook dari parameter route
        const logbookId = req.params.id;

        // Cari logbook yang sesuai
        const logbook = await Logbook.findOne({
            where: { 
                id: logbookId,
                id_mahasiswa: userId
            }
        });

        // Jika logbook tidak ditemukan
        if (!logbook) {
            return res.status(404).json({ error: 'Logbook tidak ditemukan atau Anda tidak memiliki izin' });
        }

        // Simpan path gambar ke database
        logbook.image_path = req.file.path;
        await logbook.save();

        const logbooks = await Logbook.findAll({
            where: { id_mahasiswa : userId},
            order: [['tanggal', 'DESC']], // Urutkan berdasarkan tanggal
        });

        const logbooksWithImage = logbooks.map(logbook => {
            return {
                ...logbook.toJSON(),
                imageUrl: logbook.image_path ? `${req.protocol}://${req.get('host')}/${logbook.image_path}` : null
            };
        });

        // Kirim response sukses
        res.status(200).json({
            message: 'Gambar berhasil diupload',
            imagePath: req.file.path,
            logbooksWithImage
        });

    } catch (err) {
        console.error('Error saat upload gambar:', err);
        res.status(500).json({ error: 'Terjadi kesalahan server, coba lagi nanti.' });
    }
});


// // Endpoint untuk mendapatkan logbook berdasarkan id_mahasiswa
// app.get('/logbooks', async (req, res) => {
//     try {
//         const { id_mahasiswa } = req.query; // Ambil ID Mahasiswaid_mahasiswa dari query params

//         // Validasi jika ID tidak ada
//         if (!id_mahasiswa) {
//             return res.status(400).json({ error: 'ID Mahasiswaid_mahasiswa is required' });
//         }

//         // Ambil data logbook berdasarkan id_mahasiswa
//         const logbooks = await Logbook.findAll({
//             where: { id_mahasiswa },
//             order: [['tanggal', 'DESC']], // Urutkan berdasarkan tanggal
//         });

//         console.log('User ID Logbook:', id_mahasiswa);

//         res.status(200).json(logbooks);
//     } catch (err) {
//         res.status(500).json({ error: err.message });
//     }
// });

//INI YANG BENAR APINYA YAAAAA!!!1
// app.get('/logbooks', authenticateToken, async (req, res) => {
//     try {
//         const id_mahasiswa = req.user.userId; // Ambil userId dari token

//         // Ambil data logbook berdasarkan id_mahasiswa
//         const logbooks = await Logbook.findAll({
//             where: { id_mahasiswa },
//             order: [['tanggal', 'DESC']], // Urutkan berdasarkan tanggal
//         });

//         console.log('User ID Logbook:', id_mahasiswa);

//         res.status(200).json(logbooks);
//     } catch (err) {
//         res.status(500).json({ error: err.message });
//     }
// });


app.get('/logbooks', authenticateToken, async (req, res) => {
    try {
        const id_mahasiswa = req.user.userId; // Ambil userId dari token

        // Ambil data logbook berdasarkan id_mahasiswa
        const logbooks = await Logbook.findAll({
            where: { id_mahasiswa },
            order: [['tanggal', 'DESC']], // Urutkan berdasarkan tanggal
        });

        // Menambahkan URL gambar pada data logbook
        const logbooksWithImage = logbooks.map(logbook => {
            return {
                ...logbook.toJSON(),
                imageUrl: logbook.image_path ? `${req.protocol}://${req.get('host')}/${logbook.image_path}` : null
            };
        });

        console.log('User ID Logbook:', id_mahasiswa);

        res.status(200).json(logbooksWithImage);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});





// app.get('/logbooks', async (req, res) => {
//     const userId = req.user.id; // Ambil userId dari sesi login

//     try {
//         const logbooks = await Logbook.findAll({
//             where: {
//                 id_mahasiswa: userId
//             }
//         });
//         res.json(logbooks);
//     } catch (error) {
//         console.error(error);
//         res.status(500).send('Error fetching logbooks');
//     }
// });


// Endpoint untuk menambahkan logbook
// Endpoint untuk menambahkan logbook
app.post('/logbooks', authenticateToken, async (req, res) => {
    try {
        // Ambil userId dari token (yang ada pada req.user setelah authenticateToken dijalankan)
        const userId = req.user.userId; 

        // Validasi jika id_mahasiswa tidak ada
        if (!userId) {
            return res.status(400).json({ error: 'User ID tidak ditemukan dalam token!' });
        }

        // Ambil data dari body (hanya tanggal, topik_pekerjaan, dan deskripsi)
        const { tanggal, topik_pekerjaan, deskripsi } = req.body;

        // Validasi jika ada field kosong
        if (!tanggal || !topik_pekerjaan || !deskripsi) {
            return res.status(400).json({ error: 'Tanggal, topik, dan deskripsi harus diisi!' });
        }

        // Debugging: Log data yang diterima untuk memastikan nilai yang dikirimkan benar
        console.log('Data diterima:', { userId, tanggal, topik_pekerjaan, deskripsi });

        // Tambahkan data logbook ke database
        const newLogbook = await Logbook.create({
            id_mahasiswa: userId,  // id_mahasiswa diambil dari token
            tanggal,
            topik_pekerjaan,
            deskripsi,
        });

        // Jika berhasil, kirim response sukses
        res.status(201).json({
            message: 'Logbook berhasil ditambahkan',
            logbook: newLogbook,
        });
    } catch (err) {
        // Log error untuk debugging
        console.error('Error saat menyimpan logbook:', err);

        // Menambahkan penanganan error yang lebih spesifik jika gagal di database
        if (err.name === 'SequelizeValidationError') {
            return res.status(400).json({ error: 'Validasi gagal: ' + err.errors.map(e => e.message).join(', ') });
        }

        // Response error generik jika ada masalah lain
        res.status(500).json({ error: 'Terjadi kesalahan server, coba lagi nanti.' });
    }
});

//DONE SAMPAI MENAMBAHKAN LOGBOOK BARU ABANGKU


//DISINI BISA MAKE METHOD DELETE ATAUPUN MAKE POST YA GES YA
//npx nodemon server.js to run the app
app.delete('/logbooks/:id', authenticateToken, async (req, res) => {
    try {
        // Ambil userId dari token
        const userId = req.user.userId;
        
        // Ambil ID logbook dari parameter route
        const logbookId = req.params.id;

        // Cari logbook yang akan dihapus
        const logbook = await Logbook.findOne({
            where: { 
                id: logbookId, 
                id_mahasiswa: userId // Pastikan hanya pemilik logbook yang bisa menghapus
            }
        });

        // Jika logbook tidak ditemukan
        if (!logbook) {
            return res.status(404).json({ error: 'Logbook tidak ditemukan atau Anda tidak memiliki izin menghapus' });
        }

        // Hapus logbook
        await logbook.destroy();

        // Kirim response sukses
        res.status(200).json({ 
            message: 'Logbook berhasil dihapus',
            deletedLogbookId: logbookId 
        });

    } catch (err) {
        // Log error untuk debugging
        console.error('Error saat menghapus logbook:', err);

        // Tangani error spesifik jika ada
        if (err.name === 'SequelizeValidationError') {
            return res.status(400).json({ error: 'Validasi gagal: ' + err.errors.map(e => e.message).join(', ') });
        }

        // Response error generik
        res.status(500).json({ error: 'Terjadi kesalahan server, coba lagi nanti.' });
    }
});


app.put('/logbooks/:id', authenticateToken, async (req, res) => {
    try {
        // Ambil userId dari token
        const userId = req.user.userId;

        // Ambil ID logbook dari parameter route
        const logbookId = req.params.id;

        // Ambil data yang ingin diperbarui dari body
        const { tanggal, topik_pekerjaan, deskripsi } = req.body;

        // Validasi jika semua field kosong
        if (!tanggal && !topik_pekerjaan && !deskripsi) {
            return res.status(400).json({ error: 'Harus ada setidaknya satu field yang diperbarui!' });
        }

        // Cari logbook berdasarkan id dan pastikan dimiliki oleh user
        const logbook = await Logbook.findOne({
            where: { 
                id: logbookId,
                id_mahasiswa: userId // Pastikan hanya pemilik logbook yang dapat mengedit
            }
        });

        // Jika logbook tidak ditemukan
        if (!logbook) {
            return res.status(404).json({ error: 'Logbook tidak ditemukan atau Anda tidak memiliki izin mengedit' });
        }

        // Perbarui logbook dengan data baru
        if (tanggal) logbook.tanggal = tanggal;
        if (topik_pekerjaan) logbook.topik_pekerjaan = topik_pekerjaan;
        if (deskripsi) logbook.deskripsi = deskripsi;

        // Simpan perubahan ke database
        await logbook.save();

        // Kirim response sukses
        res.status(200).json({ 
            message: 'Logbook berhasil diperbarui',
            updatedLogbook: logbook 
        });

    } catch (err) {
        // Log error untuk debugging
        console.error('Error saat memperbarui logbook:', err);

        // Tangani error spesifik jika ada
        if (err.name === 'SequelizeValidationError') {
            return res.status(400).json({ error: 'Validasi gagal: ' + err.errors.map(e => e.message).join(', ') });
        }

        // Response error generik
        res.status(500).json({ error: 'Terjadi kesalahan server, coba lagi nanti.' });
    }
});


app.get('/logbooks/:id', authenticateToken, async (req, res) => {
    try {
        // Ambil userId dari token
        const userId = req.user.userId;
        
        // Ambil ID logbook dari parameter route
        const logbookId = req.params.id;

        // Cari logbook berdasarkan ID dan pastikan milik user yang bersangkutan
        const logbook = await Logbook.findOne({
            where: { 
                id: logbookId, 
                id_mahasiswa: userId // Pastikan hanya pemilik logbook yang bisa melihat
            }
        });

        // Jika logbook tidak ditemukan
        if (!logbook) {
            return res.status(404).json({ error: 'Logbook tidak ditemukan atau Anda tidak memiliki izin untuk melihatnya' });
        }

        // Kirim response dengan detail logbook
        res.status(200).json(logbook);

    } catch (err) {
        // Log error untuk debugging
        console.error('Error saat menampilkan detail logbook:', err);

        // Response error generik
        res.status(500).json({ error: 'Terjadi kesalahan server, coba lagi nanti.' });
    }
});


app.listen(3000, () => {
    console.log('Server is running on http://localhost:3000');
});


//TADI DAH SAMPE BISA TES API NYA YA DAH BENAR TAPI SAYA KOMENT DLU BIAR KEREN
//DAH BISA SWIPE DAN HAPUS YGY
//DAH BISA EDITTT GILAK WOEEEYYY TAPI BLM BISA SEKEDAR BUAT NAMPILIN NYA AJA SI V dan juga udah keren buat hapus nya v
//Sampai tampilan alay udah
//SAMPAI BIKIN E PI EI yang untuk nampilin detail dah bisa TINGGAL PENERAPAN PADA ANDRO NYA LAGI GES V


//DONEEEE ABANKUU UDAH KEREN DAN BISA DIEDIT PAS NAMPILIN
//NEXT STEP TU ADD IMAGE IN CARD

//npx nodemon server.js
