<?php

$logged_in = "false";

if(isset($_COOKIE['uid']) && isset($_COOKIE['tok']) && isset($_COOKIE['name'])){
	$u = $_COOKIE['uid'];
	$tok = $_COOKIE['tok'];
	
	$mysqli = mysqli_connect("localhost", "root", "root", "attendance");
	
	$query = mysqli_query($mysqli, "SELECT * from instructor where username='".$u."'");
	$row = mysqli_fetch_array($query);
	$tok1 = SHA1($u).SHA1($row['password']);
	
	if($tok==$tok1){
		$logged_in = "true";
	}
	
}

?>
