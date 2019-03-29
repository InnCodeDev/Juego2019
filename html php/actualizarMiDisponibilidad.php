<?php
//$str = "4-1*3-2*4-2*3-3*6-5*6-10*6-11*";
	include ("serverID.php");
//	header('Content-Type: application/json;  charset=UTF-8');

	echo "INICIANDO PHP \n";

	$Usuario = $_GET['user'];
	$Disponibilidad = $_GET['disponibilidad'];

	$con = mysql_connect(DBserver, DBuser, DBpass);

	if (!$con){
		die('Could not connect: ' + mysql_error());
	}

	mysql_select_db(DBname, $con);

	$sqlDel = "DELETE FROM disponibilidad WHERE usuario = '" . $Usuario ."'" ;

    $retval = mysql_query( $sqlDel, $con );
    if(! $retval ) {
        die('Could not delete data: ' . mysql_error());
    }

// 	FORMATO: 4-1*3-2*4-2*3-3*6-5*6-10*6-11*
	if (strcmp($Disponibilidad, "") != 0){

		$str = $Disponibilidad; //"4-1*3-2*4-2*3-3*6-5*6-10*6-11*";
		$str_array = explode("*", $str);
		print "<pre>";
		//print_r($str_array);
		$long = count($str_array);

		for($i=0;$i<$long-1;$i++){
			list($turno,$dia)=split("-",$str_array[$i] );
		  echo "Turno: " . $turno . " - Dia: " . $dia ."\n";
			$sql = mysql_query( "INSERT INTO disponibilidad (usuario, turno, dia) VALUES ('Jose','".$turno."','".$dia."')");
			mysql_query($sql);
		}

	}


//	$salida = $output;
//	print(json_encode($salida));
//	$sql2 = "INSERT INTO UbicacionesXLocal (nomLocal, Latitud, Longitud) VALUES ('".$Nombre."', )";

//	mysqli_query($con, $sql2);

//	echo $sql2;
//    echo '<br />';

	mysql_close($con); // Cerramos la conexion con la base de datos
?>
