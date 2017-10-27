<?php
    require_once('db_connect.php');

    $response = array();

    $server_ip = gethostbyname(gethostname());
    $upload_path = 'media/';
    $upload_url = 'http://'.$server_ip.'/opt/product1/papi/ncpix/'.$upload_path;

    if($_SERVER['REQUEST_METHOD']=='POST'){

        //checking the required parameters from the request 
        if(isset($_POST['name']) and isset($_FILES['image']['name'])){

 	    //getting name from the request 
	    $name = $_POST['name'];
 
	    //getting file info from the request 
	    $fileinfo = pathinfo($_FILES['image']['name']);
 
 	    //getting the file extension 
	    $extension = $fileinfo['extension'];
 
	    //file url to store in the database 
	    $file_url = $upload_url . getFileName($pdo) . '.' . $extension;
 
	    //file path to upload in the server 
	    $file_path = $upload_path . getFileName($pdo) . '.'. $extension; 
 
	    //trying to save the file in the directory 
	    try{
		//saving the file 
		move_uploaded_file($_FILES['image']['tmp_name'],$file_path);

		$stmt_insert_media = $pdo -> prepare('INSERT INTO `test`.`ncpix` (`img_url`) VALUES (:image_url)');

		$stmt_insert_media -> bindParam(':image_url', $file_url, PDO::PARAM_STR);

		if($stmt_insert_media -> execute()){
		    $response['error'] = false; 
		    $response['url'] = $file_url; 
		    $response['name'] = $name;
		}
	    }
	    catch(Exception $e){
		$response['error']=true;
		$response['message']=$e->getMessage();
	    } 
 
	    //closing the connection 
	    unset($pdo);
	}
	else{
 	    $response['error']=true;
	    $response['message']='Please choose a file';
	}
    
    echo json_encode($response, JSON_PRETTY_PRINT);
    }

    /*
    We are generating the file name 
    so this method will return a file name for the image to be upload 
    */
    function getFileName($pdo){
	$stmt_fetch_id = $pdo -> prepare('SELECT MAX(id) AS id FROM test.ncpix');

	$stmt_fetch_id -> execute();

	$res_set_id = $stmt_fetch_id -> fetch(PDO::FETCH_ASSOC);
	
	if(empty($res_set_id)){
	    return 1;
	}
	else{
	    return $res_set_id['id'];	
	}
    }
?>
