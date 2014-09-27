geowifiscanner
==============

GPL v3 license. Enjoy! http://www.gnu.org/copyleft/gpl.html

Super primitive app to teach about data collection on Android. At its most basic, the code sends data over 'lat/long/visible SSIDs/associated wifi strength' to a php file called coordreceiver.php. That file looks something like this (it is also available at: https://github.com/NOVAGIS/novapublic/blob/master/coordreceiver.php ):


<?php
  $newlat = $_POST["lat"];
  $newlon = $_POST["lon"];
  $newacc = $_POST["acc"];
  $newatt = $_POST["att"];
  $newuse = $_POST["use"];  
  $newstr = $_POST["str"];
  
  $File = "wifistrength.txt"; 
  $Data = time()." ".$newlat." ".$newlon." ".$newacc." ".$newatt." ".$newuse." ".$newstr."\r\r"; 
  file_put_contents($File, $Data, FILE_APPEND | LOCK_EX); 

?>
