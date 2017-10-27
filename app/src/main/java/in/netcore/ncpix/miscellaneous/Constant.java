package in.netcore.ncpix.miscellaneous;

/**
 * Created by vrajesh on 10/24/17.
 */

public class Constant {
    /*
    * This class contains all the constants fields required in the project
    * */

    //Debug Tag
    public static final String DEBUG_TAG = "NCPix";
    public static final String URI_TAG = "fileUri";
    public static final String FILE_PATH_TAG = "filePath";
    public static final String FILE_TYPE_TAG = "fileType";
    public static final String IMG_TAG = "image";

    //Fragment Tags
    public static final String FRAG_UPLOAD = "Upload";
    public static final String FRAG_GALLERY = "Gallery";

    //Activity request codes
    public static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final int PERMISSION_RECORD_AUDIO_CODE = 123;
    public static final int PERMISSION_WRITE_STORAGE_CODE = 567;

    //Directory name and url to store images & videos
//    public static final String FILE_UPLOAD_URL = "http://192.168.1.14/ncpix/upload_file.php";
    public static final String FILE_UPLOAD_URL = "http://192.168.50.166:/opt/product1/papi/ncpix/upload_file.php";
    public static final String IMAGE_DIRECTORY_NAME = "NCPix";
}
