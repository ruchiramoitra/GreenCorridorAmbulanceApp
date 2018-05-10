<?php
$con = mysqli_connect("localhost", "id2263671_sherlock", "sherlock", "id2263671_sherlock");

$traffic_id = $_POST["traffic_id"]

$statement = mysqli_prepare($con, "DELETE FROM ambulance WHERE traffic_id="1");
mysqli_stmt_execute($statement);
 $response = array();
    $response["success"] = false; 
 while(mysqli_stmt_fetch($statement)){
      
  $response["success"] = true;
   }
    
    
echo json_encode($response);

echo json_encode(array("result"=>$response));
?>