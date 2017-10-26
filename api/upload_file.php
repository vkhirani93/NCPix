<?php
    $response = array();

    $file_upload_url = 'http://127.0.0.1/ncpix/media/';

    if(isset($_FILES['image']['name'])){
	$target_path = $target_path . basename($_FILES['image']['name']);

        $response['file_name'] = basename($_FILES['image']['name']);
 
        try {
            // Throws exception incase file is not being moved
            if (!move_uploaded_file($_FILES['image']['tmp_name'], $target_path)) {
                // make error flag true
                $response['error'] = true;
                $response['message'] = 'Could not move the file!';
            }
 
            // File successfully uploaded
            $response['message'] = 'File uploaded successfully!';
            $response['error'] = false;
            $response['file_path'] = $file_upload_url . basename($_FILES['image']['name']);
        } catch (Exception $e) {
            // Exception occurred. Make error flag true
            $response['error'] = true;
            $response['message'] = $e->getMessage();
        }
    }
    else{
	$response['error'] = true;
        $response['message'] = 'Not received any file!F';
    }
    
    echo json_encode($response, JSON_PRETTY_PRINT);
?>
