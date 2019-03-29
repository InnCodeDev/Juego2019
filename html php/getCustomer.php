<?php
	include ("serverID.php");
	header('Content-Type: application/json;  charset=UTF-8');

    if(isset($_REQUEST['id']))
       {

		$con = mysql_connect(DBserver, DBuser, DBpass);
	
		if (!$con){
			die('Could not connect: ' + mysql_error());
		}
		
		mysql_select_db(DBname, $con);
	

		$Id = $_REQUEST['id'];  
	
		$result = mysql_query("SELECT * FROM Categoria WHERE id = '$Id' ") or die('Errant query:');

		while($row = mysql_fetch_assoc($result))  {
			$output[]=$row;
		}

		print(json_encode($output));

		mysql_close($con);

}
    else{

		$output = "not found";

       	print(json_encode($output));

	}

?>