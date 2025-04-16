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
## Tampilan Antarmuka Pengguna Aplikasi

<table>
  <tr>
    <td align="center" style="padding: 10px;">
      <strong>Gambar 1.</strong><br/>Tampilan antarmuka pengguna pada Splash Screen aplikasi.<br/>
      <img src="Screenshot User Interface/splash screen.jpg" alt="Tampilan Splash Screen" style="max-width: 100%; height: auto; border: 1px solid #ccc; border-radius: 8px;">
    </td>
    <td align="center" style="padding: 10px;">
      <strong>Gambar 2.</strong><br/>Tampilan antarmuka pengguna pada menu Home screen aplikasi.<br/>
      <img src="Screenshot User Interface/home.jpg" alt="Tampilan Home Screen" style="max-width: 100%; height: auto; border: 1px solid #ccc; border-radius: 8px;">
    </td>
  </tr>
  <tr>
    <td align="center" style="padding: 10px;">
      <strong>Gambar 3.</strong><br/>Tampilan antarmuka pengguna pada menu Identifikasi dengan Kamera.<br/>
      <img src="Screenshot User Interface/identifikasi dengan kamera.jpg" alt="Identifikasi Kamera" style="max-width: 100%; height: auto; border: 1px solid #ccc; border-radius: 8px;">
    </td>
    <td align="center" style="padding: 10px;">
      <strong>Gambar 4.</strong><br/>Tampilan antarmuka pengguna pada menu Identifikasi dari Galeri.<br/>
      <img src="Screenshot User Interface/identifikasi dari galeri.jpg" alt="Identifikasi Galeri" style="max-width: 100%; height: auto; border: 1px solid #ccc; border-radius: 8px;">
    </td>
  </tr>
  <tr>
    <td align="center" style="padding: 10px;">
      <strong>Gambar 5.</strong><br/>Tampilan antarmuka pengguna pada menu Bantuan aplikasi.<br/>
      <img src="Screenshot User Interface/bantuan.jpg" alt="Menu Bantuan" style="max-width: 100%; height: auto; border: 1px solid #ccc; border-radius: 8px;">
    </td>
    <td align="center" style="padding: 10px;">
      <strong>Gambar 6.</strong><br/>Tampilan antarmuka pengguna pada menu Tentang aplikasi.<br/>
      <img src="Screenshot User Interface/tentang.jpg" alt="Menu Tentang" style="max-width: 100%; height: auto; border: 1px solid #ccc; border-radius: 8px;">
    </td>
  </tr>
</table>


source datasets : https://www.kaggle.com/datasets/alinedobrovsky/plant-disease-classification-merged-dataset?rvi=1
