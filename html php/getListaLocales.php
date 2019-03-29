<?php

	include ("serverID.php");
	header('Content-Type: application/json;  charset=UTF-8');

	$con = mysql_connect(DBserver, DBuser, DBpass);
	
	if (!$con){
		die('Could not connect: ' + mysql_error());
	}
		
	mysql_select_db(DBname, $con);
	
	$idCategoria = $_GET['cat'];
	
	if ($idCategoria ==null ){
		$result = mysql_query("SELECT DISTINCT Nombre FROM Locales WHERE mostrar = 1") or die(mysql_error());		
	}else{
		$result = mysql_query("SELECT DISTINCT Nombre FROM Locales WHERE mostrar = 1 and idCategoria = '".$idCategoria."'") or die(mysql_error());

	}
		
	while($row = mysql_fetch_assoc($result))
  	{
		$output[]=$row;
  	}

	print(json_encode($output));

	mysql_close($con)
?>