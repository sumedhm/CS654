<?php

	$u = $_POST['username'];
	$p = $_POST['password'];
	
	$mysqli = mysqli_connect("localhost", "root", "root", "attendance");
	
	$query = mysqli_query($mysqli, "SELECT * from instructor where username='".$u."'");
		$row = mysqli_fetch_array($query);
	if($row['password'] == SHA1($p)){
		$tok1 = SHA1($u).SHA1($row['password']);
		setcookie("uid",$u, 0);
		setcookie("tok",$tok1, 0);
		setcookie("name", $row['name'], 0);
		echo 'success';
	} else {
		echo "fail";
	}

?>

