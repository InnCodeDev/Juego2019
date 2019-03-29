<?php
	include ("serverID.php");
	header('Content-Type: application/json;  charset=UTF-8');

	$con = mysql_connect(DBserver, DBuser, DBpass);

	if (!$con){
		die('Could not connect: ' + mysql_error());
	}

	mysql_select_db(DBname, $con);

	$fechaEv = $_GET['fecha'];

	if (strcmp($fechaEv, "") != 0){
	    $StringSQL = "SELECT turno, DATE_FORMAT(fecha, '%d-%m-%Y') As fecha, COUNT(*) As Cantidad
					    FROM eventos
					    WHERE DATE_FORMAT(fecha, '%d-%m-%Y')  >= " . $fechaEv . 
					    " GROUP BY turno, fecha";
	}else{
	    $StringSQL = "SELECT turno, DATE_FORMAT(fecha, '%d-%m-%Y') As fecha, COUNT(*) As Cantidad
						FROM eventos
						WHERE DATE_FORMAT(fecha, '%d-%m-%Y')  > 10-09-2018
						GROUP BY turno, fecha";
	}

    $result = mysql_query($StringSQL) or die(mysql_error());


	while($row = mysql_fetch_assoc($result))
	{
		$output[]=$row;
  	}

	mysql_close($con);

	print(json_encode($output));

?>
