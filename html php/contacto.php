<?php
	include ("serverID.php");
	header('Content-Type: application/json;  charset=UTF-8');

	$nombre = $_GET['cliente'];
	$email = $_GET['email'];
	$local = $_GET['local'];
	$asunto = "DeliveryCba - Informe de Error";
	$tipoError = $_GET['tipoError'];
	$msje = $_GET['txt'];
	
	$para = "info@inncode.comoj.com"; //"pn_15157995@jose901933.phpnet.us"; //"info@graybox.esy.es"; //"jose_pareja@msn.com";
	
	$header = "From: " . $email ; 	
	$header .= "\nReply-To: " . $email ; 
	$header .= "\nCc: " . "jose_pareja@msn.com " ; 
	
	
	$msjCorreo = "\n\nError detectado en local: " .  $local;
	
	$msjCorreo .= "\n\n Cliente: " . $nombre ; 
	
	$msjCorreo .= "\nE-Mail: " . $email ;
	
	$msjCorreo .= "\n\nTipo de Error: " . $tipoError ;
	
	$msjCorreo .= "\n\nMensaje: " . $msje ;
	
	$msjCorreo .= "\n\n\nMensaje enviado: " . date('d/m/Y', time()); 
	
	
	if (mail($para, $asunto, utf8_decode($msjCorreo), $email ) or die ("Lo sentimos, Falló el envio \nIntentelo nuevamente.!")){
		print(json_encode(array('estadoFinal' => 'OK'))); 
	}else {		
		print(json_encode(array('estadoFinal' => 'NOK')));
	}
	

?>