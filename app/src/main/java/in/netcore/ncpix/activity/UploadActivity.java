package in.netcore.ncpix.activity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import in.netcore.ncpix.R;
import in.netcore.ncpix.miscellaneous.Constant;
import in.netcore.ncpix.miscellaneous.RequestHandler;

/**
 * Created by vrajesh on 10/27/17.
 */

public class UploadActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private ImageView ivPreview;
    private VideoView vvPreview;
    private ProgressBar pbProgress;
    private TextView tvProgress;
    private Button btnUpload;
    private Bitmap bitmapImage;

    private String filePath;
    private boolean isImage;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        toolbar = (Toolbar) findViewById(R.id.layout_toolbar_tb);
        ivPreview = (ImageView) findViewById(R.id.activity_server_upload_iv_preview);
        vvPreview = (VideoView) findViewById(R.id.activity_server_upload_vv_preview);
        pbProgress = (ProgressBar) findViewById(R.id.activity_server_upload_pb_progress);
        tvProgress = (TextView) findViewById(R.id.activity_server_upload_tv_progress);
        btnUpload = (Button) findViewById(R.id.activity_server_upload_btn_upload);

        filePath = getIntent().getStringExtra(Constant.FILE_PATH_TAG);
        isImage = getIntent().getBooleanExtra(Constant.FILE_TYPE_TAG, false);

        setUpToolbar();
        setUpPreview();

        btnUpload.setOnClickListener(this);
    }

    /*
    * Upload file to server
    * */
    private class UploadFileToServer extends AsyncTask<Bitmap, Integer, String> {
        RequestHandler requestHandler = new RequestHandler();

        @Override
        protected void onPreExecute() {
            pbProgress.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            pbProgress.setVisibility(View.VISIBLE);
            tvProgress.setVisibility(View.VISIBLE);

            pbProgress.setProgress(progress[0]);
            tvProgress.setText(String.valueOf(progress[0] + "%"));
        }

        @Override
        protected String doInBackground(Bitmap... params) {
            Bitmap bitmap = params[0];
            String uploadImage = getStringImage(bitmap);

            HashMap<String,String> data = new HashMap<>();

            data.put(Constant.IMG_TAG, uploadImage);
            return requestHandler.sendPostRequest(Constant.FILE_UPLOAD_URL, data);
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(Constant.DEBUG_TAG, "Response from server: " + result);

            //showResponse(result);

            super.onPostExecute(result);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_server_upload_btn_upload:
                new UploadFileToServer().execute(bitmapImage);
                break;
        }
    }

    private void setUpToolbar(){
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpPreview(){
        if (isImage) {
            ivPreview.setVisibility(View.VISIBLE);
            vvPreview.setVisibility(View.GONE);

            try{
                bitmapImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(filePath));
                ivPreview.setImageBitmap(bitmapImage);
            }
            catch (Exception e){
                Log.d(Constant.DEBUG_TAG, e.toString());
            }
        } else {
            ivPreview.setVisibility(View.GONE);
            vvPreview.setVisibility(View.VISIBLE);

            vvPreview.setVideoPath(filePath);
            vvPreview.start();
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
}
