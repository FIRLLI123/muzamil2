<?php
require_once('connection.php');

// Set header to return JSON
header('Content-Type: application/json');

// Get parameters from POST request
$kelas = $_REQUEST['kelas'];
$tanggal = $_REQUEST['tanggal'];

// Debug: Log received parameters
error_log("Received kelas: " . $kelas);
error_log("Received tanggal: " . $tanggal);

// Validate input
if (empty($kelas) || empty($tanggal)) {
    error_log("Error: Empty parameters");
    echo json_encode([
        'status' => 'error',
        'message' => 'Parameter kelas dan tanggal tidak boleh kosong'
    ]);
    exit;
}

// Convert date format from YYYY/MM/DD to YYYY-MM-DD
$tanggal = str_replace('/', '-', $tanggal);

// Validate date format
if (!preg_match('/^\d{4}-\d{2}-\d{2}$/', $tanggal)) {
    error_log("Error: Invalid date format: " . $tanggal);
    echo json_encode([
        'status' => 'error',
        'message' => 'Format tanggal tidak valid'
    ]);
    exit;
}

// Prepare and execute query
$query = mysqli_query($conn, 
    "SELECT namaguru, pelajaran, jam_mulai from m_kelas where kelas ='$kelas' and tanggal ='$tanggal'"
);

$result = array();
while($row = mysqli_fetch_array($query)){
    array_push($result,array(
        'namaguru' => $row['namaguru'],
        'pelajaran' => $row['pelajaran'],
        'jam_mulai' => $row['jam_mulai']
    ));
}

// Debug: Log the final data array
error_log("Final data array: " . json_encode($result));

// Return success response with data
echo json_encode(array(
    'status' => 'success',
    'result' => $result
));

// Close connection
mysqli_close($conn);
?> 