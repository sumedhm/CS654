CS654A Term Project References:
Group: Anjani Kumar (11101)
	Sumedh Masulkar (11736)

For Android application-

	First of all modify the url variable in JSGDActivity.java,MainActivity.java and DataSend.java
	to the server's ip.

	1.Install the NoProxy.apk on Android device.
	2.connect the device with Secugen Hamster Plus device
	3.enter pin 1234
	4.register button lets you register students in course
	5.start button starts the attendance.
	6.send button sends data to server.
	7.In the register and Attendance activity, before clicking the buttons, 
	  enter your roll number and put your finger on device.
	8. click the Mark/Register Button.

	 
	References:
	[1] FingerPrint Matching is handled by “Secugen FDx SDK
		  Pro for Android.”:
	We are using the sdk provided by Secugen Corporation
	We have modified there standard code(JSGDActivity.java) for matching fingerprints.
	The Android sdk can be downloaded from their site:
	http://www.secugen.com/download/sdkrequest.html

	[2] Sending Data to Server: 
	To send data to server, we have used the methods suggested by this site:
	http://hmkcode.com/android-send-json-data-to-server/

	[3] DataBase Handling in Android:
	For handling Sqlite database in android, we have referred to this site:
	http://mrbool.com/how-to-insert-data-into-a-sqlite-database-in-android/28895

	[4] CSS of Server: Bootstrap - http://getbootstrap.com/getting-started/ 

For main server:

Install Apache/PHP5/MySQL.

Put the folder "noproxy" in /var/www/html/.

Edit IP of server in register.php, index.php, contact.php, view.php
Let the $host variable be "localhost" if database is on mysql server
Start apache.


