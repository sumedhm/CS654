
  <?php
  	$ip = "172.27.30.194";
  	$host = 'localhost';
  	$user = "root";
  	$pass = "root";
  	include("login.php");
  	$home = "http://".$ip."/noproxy/";
  ?>  
  
  <html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>No Proxy Attendance Marking System</title>

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
		<div class="panel panel-success" style="margin-top:50px;">
		  <div class="panel-heading">
			<h3 class="panel-title">View attendance by date</h3>
		  </div>
		  <div class="panel-body">
			  <form class="form-inline" action="view.php" method="POST">
			  
			  	Course
				<select class="form-control" id="course1" name="course1">
				  <?php
				    $mysqli = mysqli_connect($host, $user, $pass, "attendance");
				  	$query = "SELECT DISTINCT(course) AS course from courses order by course";
				  	$result = mysqli_query($mysqli, $query);
				    $course1 = "null";
				    while($row = mysqli_fetch_array($result)){
				  		if($course1=="null") $course1 = $row['course'];
				  		echo "<option>".$row['course']."</option>";
				  	}
				  	
				  ?>
				</select>
			
				&nbsp;&nbsp;&nbsp;&nbsp;
				Date
				
				<select class="form-control" id="date" name="date">
				  <?php
				  	$query = "SELECT DISTINCT(date) from attendance where course='".$course1."'";
			  		$result = mysqli_query($mysqli, $query);
			  		while($row = mysqli_fetch_array($result)){
				  		echo "<option>".$row['date']."</option>";
				  	}
				  ?>
				</select>
				<button type="submit" name="bydate" class="btn btn-info" style='margin-left:40px;'>Go!</button>
			  </form>
		  </div>
		</div>
		
		<div class="panel panel-success" style="margin-top:80px;">
		  <div class="panel-heading">
			<h3 class="panel-title">View attendance by students</h3>
		  </div>
		  <div class="panel-body">
			<form class="form-inline" action="view.php" method="POST">
		  	Course
			<select class="form-control" id="course2" name="course2">
			  <?php
				    $mysqli = mysqli_connect($host, $user, $pass, "attendance");
				  	$query = "SELECT DISTINCT(course) AS course from courses order by course";
				  	$result = mysqli_query($mysqli, $query);
				    $course1 = "null";
				    while($row = mysqli_fetch_array($result)){
				  		if($course1=="null") $course1 = $row['course'];
				  		echo "<option>".$row['course']."</option>";
				  	}
				  	
			  ?>
			</select>
			
			&nbsp;&nbsp;&nbsp;&nbsp;
			Roll number
			<select class="form-control" id="roll" name="roll">
			  <?php
				  	$query = "SELECT DISTINCT(roll) from courses where course='".$course1."'";
			  		$result = mysqli_query($mysqli, $query);
			  		while($row = mysqli_fetch_array($result)){
				  		echo "<option>".$row['roll']."</option>";
				  	}
				?>
			</select>
			
			<button type="submit" name="byroll" class="btn btn-info" style='margin-left:40px;'>Go!</button>
			</form>
		  </div>
		</div>
    </div>

    <script src="js/bootstrap.min.js"></script>
  </body>

</html>
