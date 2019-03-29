<?php
	include ("serverID.php");
	header('Content-Type: application/json;  charset=UTF-8');

	$con = mysql_connect(DBserver, DBuser, DBpass);

	if (!$con){
		die('Could not connect: ' + mysql_error());
	}

	mysql_select_db(DBname, $con);

	$fechaEv = $_GET['fecha'];
	$user = $_GET['user'];

//	if (strcmp($user, "") != 0){
	    $StringSQL = "SELECT turno, DATE_FORMAT(fecha, '%d-%m-%Y') As fecha
                    FROM eventos
                    WHERE fecha > STR_TO_DATE('". $fechaEv . "', '%d/%m/%Y') and usuario = '". $user ."'";

		$result = mysql_query($StringSQL) or die(mysql_error());


    	while($row = mysql_fetch_assoc($result))
    	{
    		$output[]=$row;
      	}

    	mysql_close($con);

    	print(json_encode($output));

//	}


?>
