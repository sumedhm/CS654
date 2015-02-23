

$(document).ready(function(){
	$("#login").submit(function(event){
		//alert("function");
		event.preventDefault();
		var values = $(this).serialize();

		$.ajax({
		    url: "setlogin.php",
		    type: "post",
		    data: values,
		    success: function(response){
		    	response = response.trim();
		    	//console.log("("+response+")");
		        if(response=="success"){
		        	alert("Successfully logged in.");
		        	location.reload();
		        } else {
		        	alert("Invalid username or password. Login failed. Please retry.");
		        }
		    },
		    error:function(){
		        alert("Could not connect. Please retry..");
		    }
		});
	});
	
	$(".form-horizontal").submit(function(event){
		//alert("function");
		event.preventDefault();
		var values = $(this).serialize();

		$.ajax({
		    url: "add.php",
		    type: "post",
		    data: values,
		    success: function(response){
		    	response = response.trim();
		    	//console.log("("+response+")");
		        alert(response);
		    },
		    error:function(){
		        alert("Could not connect. Please retry..");
		    }
		});
	});
	
	
	$(".edit1").click(function(){
		var td = $(this);
		var att = $(this).closest('td').siblings('.att').text();
		$.ajax({
		    url: "edit.php",
		    type: "post",
		    data: {course:$('#course').val(), roll:$(this).closest('td').siblings('.roll').text(), date:$('#date').val(), att: att},
		    success: function(response){
		    	response = response.trim();
		    	console.log("("+response+")");
		    	if(response==att){
		    		alert("You are not the instructor for this course. Contact instructor to edit attendance.");
		    	} else {
			    	td.closest('td').siblings('.att').text(response);
			    }
		    },
		    error:function(){
		        alert("Could not connect. Please retry..");
		    }
		});
	});
	
	$(".edit2").click(function(){
		var td = $(this);
		var att = $(this).closest('td').siblings('.att').text();
		$.ajax({
		    url: "edit.php",
		    type: "post",
		    data: {course:$('#course').val(), roll:$(this).closest('td').siblings('.roll').text(), date:$(this).closest('td').siblings('.date').text(), att: att},
		    success: function(response){
		    	response = response.trim();
		    	console.log("("+response+")");
		    	if(response==att){
		    		alert("You are not the instructor for this course. Contact instructor to edit attendance.");
		    	} else {
			    	td.closest('td').siblings('.att').text(response);
			    }
		    },
		    error:function(){
		        alert("Could not connect. Please retry..");
		    }
		});
	});

});
