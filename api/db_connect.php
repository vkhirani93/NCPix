<?php
 	define('DB_HOST', '192.168.50.166');
 	define('DB_USER', 'test');
 	define('DB_PASSWORD', '');
 	define('DB_NAME', 'test');
	
	$pdo = new PDO('mysql:host='.DB_HOST.'; dbname='.DB_NAME.'; charset=utf8', DB_USER, DB_PASSWORD);
	$pdo -> setAttribute(PDO::ATTR_EMULATE_PREPARES, false);
	$pdo -> setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
?>
