
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
		<ul>
		<li><a href='#new-student'>Register new student</a></li> 
		<li><a href='#new-instructor'>Register new instructor</a></li>
		<li><a href='#add-student'>Add new student to course</a></li>
		<li><a href='#add-instructor'>Add new course instructor</a></li>
		</ul>
		
		<div class="panel panel-success" style="margin-top:50px;" id='new-student'>
		  <div class="panel-heading">
			<h3 class="panel-title">Register new student</h3>
		  </div>
		  <div class="panel-body">
		  	<form class="form-horizontal" action="add.php" method="POST" role="form">
		  		<input type='hidden' name='form' value='1'>
		  		  <div class="form-group">
					<label for="roll" class="col-sm-2 control-label">Roll number</label>
					<div class="col-sm-10">
					  <input type="text" class="form-control" id="roll" name='roll' placeholder="Roll number">
					</div>
				  </div>
				  <div class="form-group">
					<label for="name" class="col-sm-2 control-label">Name</label>
					<div class="col-sm-10">
					  <input type="text" class="form-control" id="name" name='name' placeholder="Student's Name">
					</div>
				  </div>
				  <div class="form-group">
					<label for="username" class="col-sm-2 control-label">Admin Username</label>
					<div class="col-sm-10">
					  <input type="text" class="form-control" id="username" name='username' placeholder="Should be entered by admin">
					</div>
				  </div>
				  <div class="form-group">
					<label for="password" class="col-sm-2 control-label">Admin Password</label>
					<div class="col-sm-10">
					  <input type="password" class="form-control" id="password" name='password' placeholder="Admin Password">
					</div>
				  </div>
				  <div class="form-group">
					<div class="col-sm-offset-2 col-sm-10">
					  <button type="submit" class="btn btn-default">Register</button>
					</div>
				  </div>
			</form>
		  </div>
		
       </div>
	
		
		<div class="panel panel-success" style="margin-top:50px;" id='new-instructor'>
		  <div class="panel-heading">
			<h3 class="panel-title">Register new instructor</h3>
		  </div>
		  <div class="panel-body">
		  	<form class="form-horizontal" action="add.php" method="POST" role="form">
		  		<input type='hidden' name='form' value='2'>
				  <div class="form-group">
					<label for="name" class="col-sm-2 control-label">Name</label>
					<div class="col-sm-10">
					  <input type="text" class="form-control" id="name" name='name' placeholder="Instructor's Name">
					</div>
				  </div>
		  		  <div class="form-group">
					<label for="iusername" class="col-sm-2 control-label">Username</label>
					<div class="col-sm-10">
					  <input type="text" class="form-control" id="iusername" name='iusername' placeholder="Create a username">
					</div>
				  </div>
				  <div class="form-group">
					<label for="ipassword" class="col-sm-2 control-label">Password</label>
					<div class="col-sm-10">
					  <input type="password" class="form-control" id="ipassword" name='ipassword' placeholder="Create a password">
					</div>
				  </div>
				  <div class="form-group">
					<label for="username" class="col-sm-2 control-label">Admin Username</label>
					<div class="col-sm-10">
					  <input type="text" class="form-control" id="username" name='username' placeholder="Should be entered by admin">
					</div>
				  </div>
				  <div class="form-group">
					<label for="password" class="col-sm-2 control-label">Admin Password</label>
					<div class="col-sm-10">
					  <input type="password" class="form-control" id="password" name='password' placeholder="Admin Password">
					</div>
				  </div>
				  <div class="form-group">
					<div class="col-sm-offset-2 col-sm-10">
					  <button type="submit" class="btn btn-default">Register</button>
					</div>
				  </div>
			</form>
		  </div>
		
       </div>
	
	<div class="panel panel-success" style="margin-top:50px;" id='add-student'>
		  <div class="panel-heading">
			<h3 class="panel-title">Add student to course</h3>
		  </div>
		  <div class="panel-body">
		  	<form class="form-horizontal" action="add.php" method="POST" role="form">
		  		<input type='hidden' name='form' value='3'>
		  		  <div class="form-group">
					<label for="roll" class="col-sm-2 control-label">Roll number</label>
					<div class="col-sm-10">
					  <input type="text" class="form-control" id="roll" name='roll' placeholder="Roll number">
					</div>
				  </div>
				  <div class="form-group">
					<label for="course" class="col-sm-2 control-label">Course</label>
					<div class="col-sm-10">
					  <input type="text" class="form-control" id="course" name='course' placeholder="Enter course name">
					</div>
				  </div>
				  <div class="form-group">
					<label for="username" class="col-sm-2 control-label">Admin Username</label>
					<div class="col-sm-10">
					  <input type="text" class="form-control" id="username" name='username' placeholder="Should be entered by admin">
					</div>
				  </div>
				  <div class="form-group">
					<label for="password" class="col-sm-2 control-label">Admin Password</label>
					<div class="col-sm-10">
					  <input type="password" class="form-control" id="password" name='password' placeholder="Admin Password">
					</div>
				  </div>
				  <div class="form-group">
					<div class="col-sm-offset-2 col-sm-10">
					  <button type="submit" class="btn btn-default">Add student</button>
					</div>
				  </div>
			</form>
		  </div>
		
       </div>
	
		<div class="panel panel-success" style="margin-top:50px;" id='add-instructor'>
		  <div class="panel-heading">
			<h3 class="panel-title">Add course to instructor</h3>
		  </div>
		  <div class="panel-body">
		  	<form class="form-horizontal" action="add.php" method="POST" role="form">
		  		<input type='hidden' name='form' value='4'>
		  		  <div class="form-group">
					<label for="iusername" class="col-sm-2 control-label">Username</label>
					<div class="col-sm-10">
					  <input type="text" class="form-control" id="iusername" name='iusername' placeholder="Instructor username">
					</div>
				  </div>
				  <div class="form-group">
					<label for="course" class="col-sm-2 control-label">Course</label>
					<div class="col-sm-10">
					  <input type="text" class="form-control" id="course" name='course' placeholder="Enter course name">
					</div>
				  </div>
				  <div class="form-group">
					<label for="username" class="col-sm-2 control-label">Admin Username</label>
					<div class="col-sm-10">
					  <input type="text" class="form-control" id="username" name='username' placeholder="Should be entered by admin">
					</div>
				  </div>
				  <div class="form-group">
					<label for="password" class="col-sm-2 control-label">Admin Password</label>
					<div class="col-sm-10">
					  <input type="password" class="form-control" id="password" name='password' placeholder="Admin Password">
					</div>
				  </div>
				  <div class="form-group">
					<div class="col-sm-offset-2 col-sm-10">
					  <button type="submit" class="btn btn-default">Add course</button>
					</div>
				  </div>
			</form>
		  </div>
		
       </div>
	
	<br><br>
	
	</div>
	
	
	</body>
	
</html>
