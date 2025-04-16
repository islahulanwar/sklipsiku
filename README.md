<p align="center"><img src="logo.png" width="250" height="250" align="center"></p>


# SebiaKu
<p align="justify"> Dibuat untuk mengidentifikasi penyakit tanaman pada daun cabai. Identifikasi dilakukan melalui pengambilan citra daun menggunakan metode <i>Convolutional Neural Network</i> (CNN). Program di buat dengan mmenggunakan bahasa <i>phyton</i> dan <b>Google <i>Colaboratory</i></b> sebagai infrastruktur yang digunakan.</p> 
<p align="justify">Proses pelatihan, validasi, dan pengujian dapat dilakukan pada Google <i>Colaboratory</i> dengan cara memasukkan <i>Dataset</i> berupa gambar yang akan diproses pada pemodelan CNN. Pemodelan CNN pada saat ini hanya menggunakan arsitektur <b><i>MobileNet v3</i></b>. Setelah dilakukan proses pemodelan akan menghasilkan data latih berupa file dengan ekstensi <i><b>Tensorflow Lite</b></i> (*.tftlite). Selanjutnya file data latih dimasukan kedalam <b>Android Studio</b> untuk pembuatan aplikasi android. Pembuatan Aplikasi android bertujuan agar memudahkan kepada petani, peneliti dan lainnya dalam melakukan identifikasi awal penyakit yang terdapat pada tanaman cabai. </p>

## Penggunaan Aplikasi
1. Buka Aplikasi <b>Plant Diseases</b>
2. pilih menu yang digunakan, "<b>IDENTIFIKASI DENGAN KAMERA</b> atau <b>IDENTIFIKASI DARI GALERI</b>
3. <b>IDENTIFIKASI DENGAN KAMERA</b> penggunaannya dengan cara mengambil gambar secara langsung pada obyek yang akan diidentifikasi, lalu tekan tombol <b>DETEKSI</b> untuk mengidentifikasi
4. <b>IDENTIFIKASI DARI GALERI</b> pengggunaanya dengan cara menekan tombol <b>PILIH  GAMBAR</b> lalu pilih foto yang terdapat pada galeri kemudian lakukan croping terhadap objek yang mau identifikasi
5. Hasil dari kedua Identifikasi akan muncul dengan nilai probabilitas, serta waktu yang dibutuhkan untuk identifikasi ditampilkan dalam satuan ms

## Screenshot User Interface Aplikasi
<img src="Screenshot User Interface/splash screen.jpg" alt="Tampilan Splash Screen" style="max-width: 100%; height: auto; border: 1px solid #ccc; border-radius: 8px;">

**Gambar 1.** Tampilan antarmuka pengguna pada halaman utama aplikasi.

source datasets : https://www.kaggle.com/datasets/alinedobrovsky/plant-disease-classification-merged-dataset?rvi=1
