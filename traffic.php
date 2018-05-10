<?php
   
 $con = mysqli_connect("localhost", "id2263671_sherlock", "sherlock", "id2263671_sherlock");
   
 
 
    $traffic_id = $_POST["traffic_id"];
  
  $source = $_POST["source"];
  
  $destination = $_POST["destination"];
  
  $current = $_POST["current"];
    
   
 
   
   
    $statement = mysqli_prepare($con, "INSERT INTO ambulance (traffic_id,source,destination,current) VALUES (?, ?, ?,?)");
  
  mysqli_stmt_bind_param($statement, "ssss",$traffic_id,$source,$deistination,$current);
   
 mysqli_stmt_execute($statement);
  
  
    $response = array();
 
   $response["success"] = true;  
    
 
   echo json_encode($response);
?>