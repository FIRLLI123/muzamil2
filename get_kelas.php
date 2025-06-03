<?php 
    require_once('connection.php');

    $jadwal = $_REQUEST['jadwal'];
    $tanggal = $_REQUEST['tanggal'];
    
    // Debug: Tampilkan parameter yang diterima
    error_log("Parameter yang diterima:");
    error_log("jadwal: [" . $jadwal . "]");
    error_log("tanggal: [" . $tanggal . "]");
    
    // Debug: Cek tipe data
    error_log("Tipe data jadwal: " . gettype($jadwal));
    error_log("Tipe data tanggal: " . gettype($tanggal));
    
    // Debug: Cek panjang string
    error_log("Panjang string jadwal: " . strlen($jadwal));
    error_log("Panjang string tanggal: " . strlen($tanggal));
    
    // Debug: Cek karakter ASCII
    error_log("ASCII jadwal: " . implode(',', array_map('ord', str_split($jadwal))));
    error_log("ASCII tanggal: " . implode(',', array_map('ord', str_split($tanggal))));
    
    $query = mysqli_query($conn, 
        "SELECT * FROM m_kelas 
         WHERE jadwal = '$jadwal' 
         AND tanggal = '$tanggal'
         ORDER BY id DESC"
    );
    
    if (!$query) {
        error_log("Error MySQL: " . mysqli_error($conn));
    }
    
    // Debug: Cek jumlah baris yang ditemukan
    $num_rows = mysqli_num_rows($query);
    error_log("Jumlah baris yang ditemukan: " . $num_rows);
    
    $result = array();
    while($row = mysqli_fetch_array($query)){
        // Debug: Tampilkan data yang ditemukan
        error_log("Data ditemukan:");
        error_log("id: " . $row['id']);
        error_log("jadwal: " . $row['jadwal']);
        error_log("tanggal: " . $row['tanggal']);
        
        array_push($result, array(
            'id' => $row['id'],
            'jadwal' => $row['jadwal'],
            'jam_mulai' => $row['jam_mulai'],
            'jam_telat' => $row['jam_telat'],
            'jam_berakhir' => $row['jam_berakhir'],
            'pelajaran' => $row['pelajaran']
        ));
    }
    
    // Debug: Tampilkan hasil akhir
    error_log("Jumlah data dalam array result: " . count($result));
    
    echo json_encode(array(
        'result' => $result
    ));
    
    mysqli_close($conn);
?> 