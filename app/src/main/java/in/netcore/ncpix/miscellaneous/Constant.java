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

    //Fragment Tags
    public static final String FRAG_UPLOAD = "Upload";
    public static final String FRAG_GALLERY = "Gallery";

    //Activity request codes
    public static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final int PERMISSION_RECORD_AUDIO = 1234;
    public static final int PERMISSION_WRITE_STORAGE = 5678;

    //Directory name and url to store images & videos
    public static final String FILE_UPLOAD_URL = "http://192.168.50.166/AndroidFileUpload/fileUpload.php";
    public static final String IMAGE_DIRECTORY_NAME = "NCPix";
}
