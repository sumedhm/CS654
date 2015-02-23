<?php

$mysqli = mysqli_connect("localhost","root","root","attendance");
$adminname = 'admin';
$adminpass = 'adminpass';
$form = $_POST['form'];

if($form=='1'){
	$roll = $_POST['roll'];
	$name = $_POST['name'];
	if($adminname==$_POST['username'] && $adminpass==$_POST['password']){
		$query = "SELECT * from students where roll='".$roll."'";
		$res = mysqli_query($mysqli, $query);
		if(mysqli_num_rows($res)>0){
			echo "Roll number already present.";
			exit;
		} else {
			$query = "INSERT into students values(0,'".$roll."', '".$name."')";
			$res = mysqli_query($mysqli, $query);
			if($res){
				echo "Student registered successfully.";
			} else echo mysqli_error($mysqli);
		}
	} else {
		echo "Wrong admin username or password.";
		exit;
	}

} else if($form=='2'){
	
	$username = $_POST['iusername'];
	$name = $_POST['name'];
	$password = $_POST['ipassword'];
	if($adminname==$_POST['username'] && $adminpass==$_POST['password']){
		$query = "SELECT * from instructor where username='".$username."'";
		$res = mysqli_query($mysqli, $query);
		if(mysqli_num_rows($res)>0){
			echo "Username already present.";
			exit;
		} else {
			$query = "INSERT into instructor values(0,'".$name."', '".$username."', '".SHA1($password)."', '')";
			$res = mysqli_query($mysqli, $query);
			if($res){
				echo "Instructor registered successfully.";
			} else echo mysqli_error($mysqli);
		}
	} else {
		echo "Wrong admin username or password.";
		exit;
	}

} else if($form=='3'){
	
	$roll = $_POST['roll'];
	$course = $_POST['course'];
	if($adminname==$_POST['username'] && $adminpass==$_POST['password']){
		$query = "SELECT * from courses where roll='".$roll."' and course='".$course."'";
		$res = mysqli_query($mysqli, $query);
		if(mysqli_num_rows($res)>0){
			echo "Roll number already registered for this course.";
			exit;
		} else {
			$query = "INSERT into courses values(0,'".$roll."', '".$course."')";
			$res = mysqli_query($mysqli, $query);
			if($res){
				echo "Student registered successfully.";
			} else echo mysqli_error($mysqli);
		}
	} else {
		echo "Wrong admin username or password.";
		exit;
	}

} else if($form=='4'){

	$username = $_POST['iusername'];
	$course = $_POST['course'];
	if($adminname==$_POST['username'] && $adminpass==$_POST['password']){
		$query = "SELECT courses from instructor where username='".$username."'";
		$res = mysqli_query($mysqli, $query);
		$row = mysqli_fetch_array($res);
		$courses = $row['courses'];
		if(strpos($row['courses'], $course)!==false){
			echo "Instructor already has this course.";
			exit;
		} else {
			$query = "UPDATE instructor set courses='".$row['courses']." | ".$course."' where username='".$username."'";
			$res = mysqli_query($mysqli, $query);
			if($res){
				echo "Course added successfully.";
			} else echo mysqli_error($mysqli);
		}
	} else {
		echo "Wrong admin username or password.";
		exit;
	}

} else {
	echo "Fatal Error";
	exit;
}

?>
