<?php

	include ("serverID.php");
	header('Content-Type: application/json;  charset=UTF-8');

	$con = mysql_connect(DBserver, DBuser, DBpass);

	if (!$con){
		die('Could not connect: ' + mysql_error());
	}

	mysql_select_db(DBname, $con);

	$turno = $_GET['turno'];
	$fecha = $_GET['fecha'];
	$usuario = $_GET['user'];

  $str="DELETE FROM eventos WHERE turno = '" . $turno . "' and fecha = '" . $fecha . "' and usuario = '" . $usuario ."'";
    echo $str;
    mysql_query($str);
    $delChk=mysql_affected_rows();
    echo "\n\nAfected rows " . $delChk;

	mysql_close($con)
?>
