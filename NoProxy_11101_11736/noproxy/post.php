<?php

$mysqli = mysqli_connect("localhost","root","root","attendance");
$arr = json_decode($HTTP_RAW_POST_DATA);

$code = SHA1("noproxy");
foreach($arr as $obj1) {
	$obj = (array)$obj1;
    $course = $obj['course'];
    $roll = $obj['roll'];
    $date = $obj['date'];
    $present = $obj['present'];
    $rcvd = $obj['code'];
    $query = "SELECT * from courses where course='".$course."' and roll='".$roll."'";
    $res = mysqli_query($mysqli, $query);
    if(mysqli_num_rows($res)==0){
    	echo "OdK";
    	//echo $roll." is not registered for course ".$course;
    	exit;
    }
    
    if(!($rcvd==$code)){
    	//echo "You are not using a verified source. Your IP will be reported.";
    	echo "OK";
    	exit; //Unverified source.
    }
    $query = "SELECT * from attendance where course='".$course."' and date='".$date."'";
    $res = mysqli_query($mysqli, $query);
    if(mysqli_num_rows($res)==0){
    	$query = "SELECT * from courses where course='".$course."'";
    	$res = mysqli_query($mysqli, $query);
    	while($row=mysqli_fetch_array($res)){
    		$query = "INSERT INTO attendance values(0,'".$row['roll']."','".$row['course']."','Not marked','".$date."')";
    		$result = mysqli_query($mysqli, $query);
    	}
    	
    	$query = "UPDATE attendance set attendance='A' where course='".$course."' and attendance<>'P' and date<>'".$date."'";
    	$result = mysqli_query($mysqli, $query);
    	$present = ($present=='1')?'P':'Dubious';
    	$query = "UPDATE attendance set attendance='".$present."' where roll='".$roll."' and course='".$course."' and date='".$date."'";
    	$result = mysqli_query($mysqli, $query);
    	
    } else {
    	$present = ($present=='1')?'P':'Dubious';
    	$query = "UPDATE attendance set attendance='".$present."' where roll='".$roll."' and course='".$course."' and date='".$date."'";
    	$result = mysqli_query($mysqli, $query);    
    } 
    
    //echo "Your attendance(".$roll.") has been marked as ".$present.".";
    
}
    echo "OK";

?>
