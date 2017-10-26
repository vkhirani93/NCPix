package in.netcore.ncpix.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;

import in.netcore.ncpix.R;
import in.netcore.ncpix.miscellaneous.AndroidMultipartEntity;
import in.netcore.ncpix.miscellaneous.Constant;

/**
 * Created by vrajesh on 10/26/17.
 */

public class ServerUploadFragment extends Fragment implements View.OnClickListener {
    private View view;
    private ImageView ivPreview;
    private VideoView vvPreview;
    private ProgressBar pbProgress;
    private TextView tvProgress;
    private Button btnUpload;

    private String filePath;
    private boolean isImage;
    private long totalSize = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_server_upload, container, false);

        ivPreview = (ImageView) view.findViewById(R.id.fragment_server_upload_iv_preview);
        vvPreview = (VideoView) view.findViewById(R.id.fragment_server_upload_vv_preview);
        pbProgress = (ProgressBar) view.findViewById(R.id.fragment_server_upload_pb_progress);
        tvProgress = (TextView) view.findViewById(R.id.fragment_server_upload_tv_progress);
        btnUpload = (Button) view.findViewById(R.id.fragment_server_upload_btn_upload);

        filePath = getArguments().getString(Constant.FILE_PATH_TAG);
        isImage = getArguments().getBoolean(Constant.FILE_TYPE_TAG);

        setUpPreview();

        btnUpload.setOnClickListener(this);

        return view;
    }

    /*
    * Upload file to server
    * */
    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
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
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(Constant.FILE_UPLOAD_URL);

            try {
                AndroidMultipartEntity entity = new AndroidMultipartEntity(
                        new AndroidMultipartEntity.ProgressListener() {
                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                File sourceFile = new File(filePath);
                entity.addPart(Constant.IMG_TAG, new FileBody(sourceFile));

                totalSize = entity.getContentLength();
                httpPost.setEntity(entity);

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();

                int statusCode = httpResponse.getStatusLine().getStatusCode();

                if (statusCode == 200) {
                    responseString = EntityUtils.toString(httpEntity);
                } else {
                    responseString = "Error occurred! Http Status Code: " + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(Constant.DEBUG_TAG, "Response from server: " + result);

            showResponse(result);

            super.onPostExecute(result);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fragment_server_upload_btn_upload:
                new UploadFileToServer().execute();
                break;
        }
    }

    private void setUpPreview(){
        if (isImage) {
            ivPreview.setVisibility(View.VISIBLE);
            vvPreview.setVisibility(View.GONE);

            try{
                ivPreview.setImageBitmap(MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Uri.parse(filePath)));
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

    private void showResponse(String response){
        Log.d(Constant.DEBUG_TAG, response);
    }
}
