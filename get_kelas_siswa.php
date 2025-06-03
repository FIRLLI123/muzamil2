<?php
require_once('connection.php');

// Set header to return JSON
header('Content-Type: application/json');

// Database connection
$host = "localhost";
$username = "root";
$password = "";
$database = "db_sekolah"; // sesuaikan dengan nama database Anda

// Create connection
$conn = new mysqli($host, $username, $password, $database);

// Check connection
if ($conn->connect_error) {
    die(json_encode([
        'status' => 'error',
        'message' => 'Connection failed: ' . $conn->connect_error
    ]));
}

// Get id_siswa from POST request
$id_siswa = isset($_POST['id_siswa']) ? $_POST['id_siswa'] : '';

if (empty($id_siswa)) {
    echo json_encode([
        'status' => 'error',
        'message' => 'ID Siswa tidak boleh kosong'
    ]);
    exit;
}

// Prepare and execute query
$query = "SELECT * FROM m_user 
          JOIN m_siswa ON m_user.id_siswa = m_siswa.id_siswa 
          WHERE m_user.id_siswa = ?";

$stmt = $conn->prepare($query);
$stmt->bind_param("s", $id_siswa);
$stmt->execute();
$result = $stmt->get_result();

// Check if data exists
if ($result->num_rows > 0) {
    $data = $result->fetch_assoc();
    
    // Return success response with data
    echo json_encode([
        'status' => 'success',
        'result' => [$data]
    ]);
} else {
    // Return error if no data found
    echo json_encode([
        'status' => 'error',
        'message' => 'Data tidak ditemukan'
    ]);
}

// Close connection
$stmt->close();
$conn->close();
?> 