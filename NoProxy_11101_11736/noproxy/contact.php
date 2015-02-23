
  <?php
  	$ip = "172.27.30.194";
  	$user = "root";
  	$pass = "root";
  	$home = "http://".$ip."/noproxy/";
  	include("login.php");
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
		<h2>Contact Information</h2>
		<br><br>
		<h4><span class="glyphicon glyphicon-user" aria-hidden="true"></span> Anjani Kumar</h4>
		<span class="glyphicon glyphicon-envelope" aria-hidden="true"></span> anjani@iitk.ac.in,
		<span class="glyphicon glyphicon-phone-alt" aria-hidden="true"></span> 9425890429, <br>
		<span class="glyphicon glyphicon-home" aria-hidden="true"></span> F-207, Hall-9, IIT Kanpur. <br>
		<br><br>
		<h4><span class="glyphicon glyphicon-user" aria-hidden="true"></span> Sumedh Masulkar</h4>
		<span class="glyphicon glyphicon-envelope" aria-hidden="true"></span> sumedh@iitk.ac.in,
		<span class="glyphicon glyphicon-phone-alt" aria-hidden="true"></span> 8005463472, <br>
		<span class="glyphicon glyphicon-home" aria-hidden="true"></span> F-205, Hall-9, IIT Kanpur. <br>
		

	</div>
	
	</body>
	
	</html>
