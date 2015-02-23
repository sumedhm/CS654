
  <?php
  	$ip = "172.27.30.194";
	$host = "localhost";
  	$user = "root";
  	$pass = "root";
  	include("login.php");
  	$home = "http://".$ip."/noproxy/";
  ?> 

<html>
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>No Proxy Attendance Marking System</title>
	<style type='text/css'>
		.edit2, .edit1{
			font-size: small;
			cursor: pointer;
			text-decoration: underline;
			color: blue;
			margin-left: 20px;
		}
	</style>
    <!-- Bootstrap -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
	<script src="<?=$home?>jquery.min.js"></script>
	<script src="<?=$home?>login.js"></script>
    
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>
  
  
  <body>
  <div>
  	<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="<?=$home?>">No Proxy - IIT Kanpur</a>
        </div>
        <div id="navbar" class="collapse navbar-collapse">
          <ul class="nav navbar-nav">
            <li class="active"><a href="<?=$home?>">Home</a></li>
            <li><a href="<?=$home?>register.php">Register</a></li>
            <li><a href="<?=$home?>contact.php">Contact</a></li>
          </ul>
          <?php 
          if(isset($_COOKIE['name']) && $logged_in=="true"){
		   echo "<div style='float:right;color:white;margin-top:20px;'>Logged in as ".$_COOKIE['name']." | <a href='".$home."logout.php'>Logout</a></div>";
          
		  } else{
		  	echo '
		      <form class="navbar-form navbar-right" role="form" id="login">
		        <div class="form-group">
		          <input type="text" placeholder="Email" name="username" class="form-control">
		        </div>
		        <div class="form-group">
		          <input type="password" placeholder="Password" name="password" class="form-control">
		        </div>
		        <button type="submit" class="btn btn-success">Sign in</button>
		      </form>
		      ';
		  }
          ?>
        </div><!--/.navbar-collapse -->
    </nav>
    </div>
    <div class="container theme-showcase" style="margin-top:90px;">
	
  
  <table class="table table-striped" style="margin:50px;">
  
  	<?php
  	
  	$mysqli = mysqli_connect($host, $user, $pass, "attendance");
  	$stat = "";
			
  		if(isset($_POST["bydate"])){
  			
  			$course = $_POST["course1"];
  			$date = $_POST["date"];
  			echo "<input type='hidden' id='course' value='".$course."'><input type='hidden' id='date' value='".$date."'>";
  			echo "
  				<caption>Attendance of course ".$course." on ".$date."</caption>
  				<thead>
  				<tr>
  					<th>S.No.</th>
  					<th>Roll no.</th>
  					<th>Student Name</th>
  					<th>Attendance</th>
  				</tr>
  				</thead>
  				<tbody>
  			";			
  			$query = "SELECT attendance.attendance as attendance, students.roll as roll, students.name as name FROM students 
  			INNER JOIN attendance ON attendance.roll=students.roll where course='".$course."' and date='".$date."' order by roll";
			$result = mysqli_query($mysqli, $query);
			$i = 1;
			while($row = mysqli_fetch_array($result)){
				echo "<tr>";
				echo "<td>".$i++.".</td>";
				echo "<td class='roll'>".$row['roll']."</td>";
				echo "<td>".$row['name']."</td>";
				echo "<td class='att'>".$row['attendance']."</td>";
				if($logged_in=="true"){
					echo "<td><span class='edit1'>Toggle</span></td>";
				}
				echo "</tr>";
			}
			
			echo "
			
  				</tbody>
			";
			
			$query = "SELECT roll from attendance where course='".$course."' and date='".$date."'";
			$res = mysqli_query($mysqli, $query);
			$c1 = mysqli_num_rows($res);
			$query .= " and attendance='P'";
  			$res = mysqli_query($mysqli, $query);
			$c2 = mysqli_num_rows($res);
			
			$stat = "Total students in the course = ".$c1.", present on ".$date." = ".$c2.".";
			
  		} else {
  		
  			$course = $_POST["course2"];
  			$roll = $_POST["roll"];
  			echo "<input type='hidden' id='course' value='".$course."'>";
  			$res  = mysqli_query($mysqli, "SELECT name from students where roll='".$roll."'");
  			$row = mysqli_fetch_array($res);
  			$name = $row['name'];
  			echo "
  				<caption>Attendance of course ".$course." for ".$roll."(".$name.")</caption>
  				<thead>
  				<tr>
  					<th>S.No.</th>
  					<th>Student Roll No.</th>
  					<th>Student Name</th>
  					<th>Date</th>
  					<th>Attendance</th>
  				</tr>
  				</thead>
  				<tbody>
  			";			
  			$query = "SELECT * from attendance where course='".$course."' and roll='".$roll."' order by roll";
			$result = mysqli_query($mysqli, $query);
			$c1 = mysqli_num_rows($result);
			$i = 1;
			
			while($row = mysqli_fetch_array($result)){
				echo "<tr>";
				echo "<td>".$i++.".</td>";
				echo "<td class='roll'>".$roll."</td>";
				echo "<td>".$name."</td>";
				echo "<td class='date'>".$row['date']."</td>";
				echo "<td class='att'>".$row['attendance']."</td>";
				if($logged_in=="true"){
					echo "<td><span class='edit2'>Toggle</span></td>";
				}
				echo "</tr>";
			}
			$query = "SELECT * from attendance where course='".$course."' and roll='".$roll."' and attendance='P'";
			$res = mysqli_query($mysqli, $query);
			$c2 = mysqli_num_rows($res);
			$stat = "Total classes - ".$c1.", Classes attended by ".$roll." - ".$c2.".";
			echo "
			
  				</tbody>
			";
  						
  		
  		}
  		
  	?>
  	
  	
  	</table>
  	
  	<?=$stat?>
  </div>
    <!--<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>-->
    <script src="js/bootstrap.min.js"></script>
  </body>


</html>


