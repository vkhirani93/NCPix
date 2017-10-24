package in.netcore.ncpix.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import in.netcore.ncpix.R;
import in.netcore.ncpix.miscellaneous.Constant;

/**
 * Created by vrajesh on 10/24/17.
 */

public class SplashActivity extends AppCompatActivity{
    Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new LongOperation().execute();
    }

    private class LongOperation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Log.d(Constant.DEBUG_TAG, e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        @Override
        protected void onPreExecute() {/*Do Nothing*/}

        @Override
        protected void onProgressUpdate(Void... values) {/*Do Nothing*/}
    }
}
