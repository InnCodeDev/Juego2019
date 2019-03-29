<?php

	include ("serverID.php");
	header('Content-Type: application/json;  charset=UTF-8');

	$con = mysql_connect(DBserver, DBuser, DBpass);
	
	if (!$con){
		die('Could not connect: ' + mysql_error());
	}
		
	mysql_select_db(DBname, $con);
	
	
	$highest_id = mysql_result(mysql_query("SELECT MAX(id) FROM Locales"), 0);

	print(json_encode($highest_id+1));

	mysql_close($con)
?>