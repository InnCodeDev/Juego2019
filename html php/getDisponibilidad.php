<?php
	include ("serverID.php");
	header('Content-Type: application/json;  charset=UTF-8');

	$con = mysql_connect(DBserver, DBuser, DBpass);
	
	if (!$con){
		die('Could not connect: ' + mysql_error());
	}

	mysql_select_db(DBname, $con);

	$usuario = $_GET['user'];

	$result = mysql_query("SELECT * FROM Disponibilidad WHERE usuario = '" . $usuario . "' ORDER BY TURNO") or die(mysql_error());

	while($row = mysql_fetch_assoc($result))
	{
		$output[]=$row;
  	}

	mysql_close($con);

	print(json_encode($output));

?>
