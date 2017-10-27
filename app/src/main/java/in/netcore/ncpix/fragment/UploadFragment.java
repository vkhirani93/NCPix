package in.netcore.ncpix.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import in.netcore.ncpix.R;
import in.netcore.ncpix.activity.UploadActivity;
import in.netcore.ncpix.miscellaneous.Constant;

import static android.app.Activity.RESULT_OK;

/**
 * Created by vrajesh on 10/25/17.
 */

public class UploadFragment extends Fragment implements View.OnClickListener {
    private View view;
    private Uri fileUri;
    private ImageButton imgBtnPicture;
    private ImageButton imgBtnVideo;
    private Intent intent;
    private AlertDialog alertDialog;
    private AlertDialog.Builder alertDialogBuilder;

    private boolean imageCapture;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_upload, container, false);

        imgBtnPicture = (ImageButton) view.findViewById(R.id.fragment_upload_img_btn_picture);
        imgBtnVideo = (ImageButton) view.findViewById(R.id.fragment_upload_img_btn_video);

        checkCameraFeature();

        imgBtnPicture.setOnClickListener(this);
        imgBtnVideo.setOnClickListener(this);

        return view;
    }

    @SuppressLint("NewApi")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        boolean permissionExists = true;

        for (String permission : permissions) {
            if (getActivity().checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionExists = false;
            }
        }

        if(permissionExists){
            if(imageCapture){
                captureImage();
            }
            else{
                recordVideo();
            }
        }
    }

    /*
    * Here we store the file url as it will be null after returning from camera app
    * */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constant.URI_TAG, fileUri);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore last state for checked position.
            fileUri = savedInstanceState.getParcelable(Constant.URI_TAG);
        }
    }

    /*
    * Receiving activity result method will be called after closing the camera
    * */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            launchUploadActivity();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fragment_upload_img_btn_picture:
                imageCapture = true;
                checkAllPermissions();
                break;

            case R.id.fragment_upload_img_btn_video:
                imageCapture = false;
                checkAllPermissions();
                break;
        }
    }

    private void captureImage() {
        fileUri = getOutputMediaFileUri(Constant.MEDIA_TYPE_IMAGE);

        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, Constant.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    private void recordVideo() {
        fileUri = getOutputMediaFileUri(Constant.MEDIA_TYPE_VIDEO);

        intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, Constant.CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
    }

    private void checkAllPermissions(){
        boolean permissionExists = true;
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissions != null) {
            for (String permission : permissions) {
                if (getActivity().checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionExists = false;
                }
            }
        }

        if(permissionExists){
            if(imageCapture){
                captureImage();
            }
            else{
                recordVideo();
            }
        }
        else{
            requestPermissions(permissions, Constant.PERMISSION_RECORD_AUDIO_CODE + Constant.PERMISSION_WRITE_STORAGE_CODE);
        }
    }

    /*
    * The function checks whether the phone has camera feature or not.
    * */
    private void checkCameraFeature(){
        if(!getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
               alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.CustomDialogStyle)
                       .setTitle(getString(R.string.alert_title_feature_missing))
                       .setMessage(getString(R.string.alert_message_camera_missing))
                       .setCancelable(false)
                       .setPositiveButton(getString(R.string.alert_btn_ok), new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               getActivity().finish();
                           }
                       });
               alertDialog = alertDialogBuilder.create();
               alertDialog.show();
        }
    }

    private void launchUploadActivity(){
        intent = new Intent(getActivity(), UploadActivity.class);
        intent.putExtra(Constant.FILE_PATH_TAG, fileUri.toString());
        intent.putExtra(Constant.FILE_TYPE_TAG, imageCapture);
        startActivity(intent);
    }

    /*
    * Helper Methods are written here
    * */

    /*
    * Creating URI to store image/video
    * */
    public Uri getOutputMediaFileUri(int type) {
        return FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".miscellaneous.GeneralFileProvider", getOutputMediaFile(type));
    }

    /*
    * Returning Image/Video
    * */
    private static File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Constant.IMAGE_DIRECTORY_NAME);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(Constant.DEBUG_TAG, "Oops! Failed create " + Constant.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;

        if (type == Constant.MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        } else if (type == Constant.MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }
}
