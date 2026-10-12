# Dokumentasi Aplikasi Student Manager

Kuis 1 – Pemrograman Perangkat Bergerak
Nama: Yeremia Gunawan · NRP: 5053251028

---

## 1. Deskripsi Studi Kasus

**Student Manager** adalah aplikasi Android sederhana untuk mengelola data mahasiswa. Bayangkan
seorang staf administrasi program studi yang setiap hari perlu mencatat mahasiswa baru,
memperbaiki data yang salah ketik, menghapus data yang sudah tidak berlaku, dan mencari data
mahasiswa tertentu dengan cepat. Aplikasi ini menggantikan pencatatan manual tersebut dengan
antarmuka yang ringkas di ponsel.

Setiap mahasiswa memiliki tiga atribut:

| Atribut | Keterangan |
|---|---|
| NIM | Nomor induk mahasiswa, hanya angka, harus unik |
| Nama | Nama lengkap mahasiswa |
| Program Studi | Dipilih dari daftar: Informatika, Sistem Informasi, Teknik Komputer, Desain Komunikasi Visual, Manajemen, Akuntansi |

### Fitur Utama

1. **Daftar Mahasiswa** – menampilkan semua mahasiswa dalam bentuk kartu (avatar, nama, NIM,
   program studi) beserta jumlah mahasiswa.
2. **Tambah Mahasiswa** – tombol FAB (+) membuka form dengan kolom NIM, Nama, dan dropdown Program Studi.
3. **Edit Mahasiswa** – ikon pensil pada kartu membuka form yang sudah terisi data lama.
4. **Hapus Mahasiswa** – ikon tempat sampah menampilkan dialog konfirmasi sebelum data dihapus.
5. **Cari Mahasiswa** – kolom pencarian menyaring daftar secara langsung berdasarkan nama, NIM,
   atau program studi (tidak membedakan huruf besar/kecil).
6. **Splash Screen** – tampilan awal aplikasi selama ±1,5 detik.
7. **Empty State** – pesan "Belum ada data mahasiswa" bila daftar kosong, atau
   "Mahasiswa tidak ditemukan" bila pencarian tidak menghasilkan apa pun.
8. **Menu (⋮)** – *Refresh* (mengosongkan pencarian), *Tentang Aplikasi*, dan *Keluar*.

Tambahan di luar wireframe: validasi input (kolom wajib diisi, NIM hanya angka dan tidak boleh
duplikat) serta *snackbar* sebagai umpan balik setelah tambah/ubah/hapus.

### Teknologi

Kotlin · Jetpack Compose · Material 3 · Android ViewModel (`androidx.lifecycle`).

---

## 2. Alur Navigasi

```
Splash Screen ──► Daftar Mahasiswa (Home)
                     ├──► Tambah Mahasiswa ──(Simpan/Batal/Back)──► Daftar
                     ├──► Edit Mahasiswa   ──(Simpan/Batal/Back)──► Daftar
                     ├──► Dialog Hapus     ──(Hapus/Batal)────────► Daftar
                     └──► Pencarian (filter langsung di layar yang sama)
```

Navigasi dikelola oleh `StudentViewModel.screen` (sealed interface `Screen`: `Splash`, `List`,
`Add`, `Edit(id)`), sehingga tidak memerlukan dependensi tambahan. Tombol *Back* sistem pada
layar form kembali ke daftar.

---

## 3. Rancangan Layar

| Layar | Komponen Compose |
|---|---|
| Splash | `Surface`, `Text`, `CircularProgressIndicator` |
| Daftar | `Scaffold`, `TopAppBar`, `OutlinedTextField` (pencarian), `LazyColumn`, `Card`, `FloatingActionButton` (bulat, biru), `SnackbarHost` |
| Menu ⋮ | `OverlayMenu` (Refresh, Tentang Aplikasi, Keluar) |
| Dialog hapus / tentang | `OverlayDialog` |
| Form tambah/edit | `Scaffold`, `TopAppBar` (tombol kembali), `OutlinedTextField`, dropdown program studi inline, `Button`, `OutlinedButton` (sudut 8 dp) |

Warna tema mengikuti wireframe (biru); *dynamic color* dimatikan agar tampilan konsisten di semua
perangkat.

---

## 4. Struktur Proyek

```
app/src/main/java/com/example/studentmanager/
├── MainActivity.kt          Titik masuk; membuat ViewModel dan memasang tema
├── StudentViewModel.kt      State aplikasi (daftar, pencarian, layar aktif, pesan) + aksi CRUD
├── data/
│   └── Student.kt           Model Student, opsi prodi, data contoh, filter pencarian, validasi
└── ui/
    ├── StudentApp.kt        Memilih layar berdasarkan state + penanganan tombol Back
    ├── SplashScreen.kt
    ├── StudentListScreen.kt Daftar, pencarian, kartu, empty state, menu, dialog hapus & tentang
    ├── StudentFormScreen.kt Form tambah/edit + validasi + dropdown program studi
    ├── Overlays.kt          OverlayMenu & OverlayDialog (digambar di dalam layar, bukan jendela popup)
    ├── Shapes.kt            Bentuk tombol (sudut 8 dp)
    └── theme/               Color, Type, Theme
app/src/test/.../StudentLogicTest.kt   Unit test logika filter, validasi, dan ViewModel
```

### Keputusan Desain

- **State hoisting.** Composable layar menerima data dan *callback*; semua state bisnis ada di
  `StudentViewModel`, sehingga tetap ada saat layar diputar. State form (isi kolom) memakai
  `rememberSaveable`.
- **Logika murni terpisah.** `filterStudents` dan `validateStudent` berupa fungsi biasa agar mudah diuji.
- **Overlay, bukan popup.** Menu ⋮, dialog hapus/tentang, dan dropdown prodi digambar sebagai
  lapisan di dalam pohon Compose yang sama (bukan `DropdownMenu`/`AlertDialog`, yang membuat
  jendela terpisah). Pada emulator Android 17 yang dipakai, jendela popup tidak pernah menerima
  fokus sehingga menu dan dialog tidak muncul. Tombol Back menutup overlay yang terbuka.
- **Validasi bertahap.** Pesan kesalahan baru muncul setelah pengguna menekan *Simpan*, lalu
  diperbarui saat pengguna mengetik.
- **Penyimpanan.** Data disimpan **di memori**, diawali tiga data contoh seperti pada wireframe.
  Data kembali ke data contoh setelah aplikasi ditutup sepenuhnya. Wireframe tidak
  mensyaratkan penyimpanan permanen; pengembangan lanjutan dapat memakai Room atau DataStore.

---

## 5. Cara Menjalankan

1. Buka folder proyek di **Android Studio**, tunggu *Gradle sync* selesai.
2. Pilih emulator atau perangkat (minSdk 24), lalu tekan **Run ▶**.
3. Menjalankan unit test:

```bash
./gradlew testDebugUnitTest
```

---

## 6. Skenario Pengujian Manual

| # | Langkah | Hasil yang diharapkan |
|---|---|---|
| 1 | Buka aplikasi | Splash ±1,5 detik, lalu daftar dengan 3 mahasiswa, "Jumlah mahasiswa: 3" |
| 2 | Ketik "budi" di pencarian | Hanya Budi Santoso tampil, jumlah menjadi 1 |
| 3 | Ketik "xyz" | Tampil "Mahasiswa tidak ditemukan" |
| 4 | Tekan ✕ pada pencarian | Daftar lengkap kembali |
| 5 | Tekan FAB (+), tekan *Simpan* dengan kolom kosong | Pesan kesalahan muncul pada ketiga kolom |
| 6 | Isi NIM `2301001` (sudah ada) | Pesan "NIM sudah terdaftar" |
| 7 | Isi data valid, pilih prodi, *Simpan* | Kembali ke daftar, mahasiswa baru muncul, snackbar tampil |
| 8 | Tekan ikon pensil, ubah nama, *Simpan* | Data berubah pada daftar |
| 9 | Tekan ikon tempat sampah | Dialog "Hapus Mahasiswa?" muncul |
| 10 | Pilih *Batal* / *Hapus* | Data tetap / data terhapus |
| 11 | Hapus semua mahasiswa | Tampil empty state "Belum ada data mahasiswa" |
| 12 | Menu ⋮ → Refresh / Tentang Aplikasi / Keluar | Pencarian dikosongkan / dialog tentang / aplikasi tertutup |
| 13 | Putar layar saat berada di form | Isian form dan daftar tidak hilang |

---

## 7. Keterbatasan dan Pengembangan Lanjutan

- Data belum tersimpan permanen (lihat bagian 4).
- Teks antarmuka masih ditulis langsung di kode (bahasa Indonesia); dapat dipindahkan ke
  `strings.xml` untuk mendukung multibahasa.
- Menu *Refresh* saat ini hanya mengosongkan pencarian karena belum ada sumber data eksternal.
