<?php

	$course = $_POST['course'];
	$roll = $_POST['roll'];
	$date = $_POST['date'];
	$att = $_POST['att'];
	
	$mysqli = mysqli_connect("localhost","root","root","attendance");
	$att_t = ($att=="A")?"P":"A";
	
	$res = mysqli_query($mysqli, "SELECT courses from instructor where username='".$_COOKIE['uid']."'");
	$row = mysqli_fetch_array($res);
	$courses = $row['courses'];
	
	if(strpos($courses, $course)!==false){
		$query = "UPDATE attendance set attendance='".$att_t."' where course='".$course."' and date='".$date."' and roll='".$roll."'";
		$res = mysqli_query($mysqli, $query);
		if(!$res){
			echo $att;
		} else {	
			echo $att_t;
		}
	} else {
		echo $att;
	}
?>
