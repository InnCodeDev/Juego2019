<?php
	function ejecutarSQLComand ($commando){
echo $commando;
	$mysqli = new mysqli("sql113.phpnet.us", "pn_15157995", "901933", "pn_15157995_deliverycba");

	/*Check Connection*/

	if ($mysqli->connect_errno){

		printf ("Connection Failed: %s\n", $mysqli->connect_error);

		exit();
	}

	if ($mysqli->multi_query($commando)){

		if ($resultset = $mysqli->store_result()){

			while ($row = $resultset->fetch_array (MYSQLI_BOTH)){
				
			}

			$resultset->free();
		
		}
		
	}

echo $commando;
	$mysqli->close();

}

function getSQLResultSet ($commando){

	$mysqli = new mysqli("sql113.phpnet.us", "pn_15157995", "901933", "pn_15157995_deliverycba");
echo $commando;
	/*Check Connection*/
	if ($mysqli->connect_errno){

		printf ("Connection Failed: %s\n", $mysqli->connect_error);

		exit();
	}

	if ($mysqli->multi_query($commando)){

		return $mysqli->store_result();

	}

}

?>