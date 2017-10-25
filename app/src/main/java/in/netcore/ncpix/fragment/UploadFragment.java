package in.netcore.ncpix.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import in.netcore.ncpix.R;
import in.netcore.ncpix.miscellaneous.Constant;
import in.netcore.ncpix.miscellaneous.GeneralMethods;

/**
 * Created by vrajesh on 10/25/17.
 */

public class UploadFragment extends Fragment implements View.OnClickListener {
    private View view;
    private Uri fileUri;
    private ImageButton imgBtnPicture;
    private ImageButton imgBtnVideo;
    private Intent intent;

    GeneralMethods generalMethods;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_upload, container, false);

        imgBtnPicture = (ImageButton) view.findViewById(R.id.fragment_upload_img_btn_picture);
        imgBtnVideo = (ImageButton) view.findViewById(R.id.fragment_upload_img_btn_video);

        generalMethods = new GeneralMethods();

        imgBtnPicture.setOnClickListener(this);
        imgBtnVideo.setOnClickListener(this);

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        if(requestCode == Constant.PERMISSION_RECORD_AUDIO){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                captureImage();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fragment_upload_img_btn_picture:
                if(deviceHasCamera()){
                    if(checkCameraPermission()){
                        captureImage();
                    }
                }
                else{
                    Toast.makeText(getActivity(), R.string.camera_feature_missing, Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.fragment_upload_img_btn_video:
                if(deviceHasCamera()){
                    if(checkCameraPermission()){
                        recordVideo();
                    }
                }
                else{
                    Toast.makeText(getActivity(), R.string.camera_feature_missing, Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    /*
    * Checking device has camera hardware or not
    * */
    private boolean deviceHasCamera() {
        return getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA) ? true : false;
    }

    private void captureImage() {
        fileUri = getOutputMediaFileUri(Constant.MEDIA_TYPE_IMAGE);

        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, Constant.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    private void recordVideo() {
        fileUri = getOutputMediaFileUri(Constant.MEDIA_TYPE_VIDEO);

        intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, Constant.CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
    }

    private boolean checkCameraPermission(){
        if(generalMethods.hasRequestedPermission(getActivity(), Constant.PERMISSION_RECORD_AUDIO)){
            return true;
        }
        else{
            String permissionType = generalMethods.getPermissionType(Constant.PERMISSION_RECORD_AUDIO);
            requestPermissions(new String []{permissionType}, Constant.PERMISSION_RECORD_AUDIO);
            return false;
        }
    }
}
