<?php
// Database configuration
$host = 'localhost';
$username = 'root';
$password = '';
$database = 'muzamil';

// Create connection
$conn = new mysqli($host, $username, $password, $database);

// Check connection
if ($conn->connect_error) {
    error_log("Connection failed: " . $conn->connect_error);
    die(json_encode([
        'status' => 'error',
        'message' => 'Database connection failed: ' . $conn->connect_error
    ]));
}

// Set charset to utf8
if (!$conn->set_charset("utf8")) {
    error_log("Error loading character set utf8: " . $conn->error);
}

// Enable error reporting
error_reporting(E_ALL);
ini_set('display_errors', 1);
?> 