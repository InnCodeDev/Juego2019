<?php 
	echo "INICIANDO PHP ";

	//$id = $_GET['id'];
	$Nombre = $_GET['nombre'];
	$Latitud = $_GET['latitud'];
	$Longitud = $_GET['longitud'];
	
	echo "Nombre: ".$Nombre;
    echo '<br />';
	echo "Latitud: ".$Latitud;
    echo '<br />';
	echo "Longitud: ".$Longitud;
    echo '<br />';
	
	$con = mysqli_connect("sql113.phpnet.us", "pn_15157995", "901933") or die("Error " . mysqli_error($con));

	if (!$con){
		die('Could not connect: ' + mysqli_error());
	}

	mysqli_select_db($con, "pn_15157995_deliverycba");

	$sql = "INSERT INTO UbicacionesXLocal (Nombre, Latitud, Longitud) VALUES ('".$Nombre."', '".$Latitud."', '".$Longitud."')";
    echo $sql;
    echo '<br />';
	mysqli_query($con, $sql);
	
	mysqli_close($con); // Cerramos la conexion con la base de datos
?>