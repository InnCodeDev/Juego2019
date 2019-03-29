<?php

	include ("serverID.php");
  $conexion = mysqli("localhost", DBuser, DBpass, DBname);
  //$conexion = mysqli_connect("localhost", "id5253621_deliverycba", "901933", "id5253621_deliverycba"); //or die("Error " . mysqli_error($conexion))

  if($conexion->connect_error){
     echo " No conecto!!";
     die("Connection failed.. ". $conexion->connect_error);
  }else{
     echo "conectoooo...";}

  $query = "SELECT version FROM DB" ; //or die("Error in the consult.." . mysqli_error($conexion));

  $resultado = $conexion->query($query);

  if ($resulto->num_rows > 0) {
    // output data of each row
    while($row = $resultado->fetch_assoc()){
      echo "Version: " . $row["version"] . "<br>";
  }
  echo $resultado;

  print(json_encode($resultado));
?>
