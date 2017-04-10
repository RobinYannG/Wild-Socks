package fr.wcs.wildcommunitysocks;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * Created by robingoudy on 06/04/2017.
 */

public class ModifyMySocksActivity extends AppCompatActivity implements View.OnClickListener {

    private static Chaussette result;
    private ImageView thisSock;
    private String url;
    private String caption;
    private TextView legend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_my_sock);

        thisSock = (ImageView) findViewById(R.id.sockImage);
        legend = (TextView) findViewById(R.id.textViewLegend);

        Intent onStart = getIntent();
        result = onStart.getParcelableExtra("sock");

        url = result.getmImgChaussette();
        caption = result.getmLegende();
        legend.setText(caption);


        new ModifyMySocksActivity.SockImage(thisSock).execute(url);

    }
    public Bitmap loadImageFromNetwork(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) { return null; }
    }


    private class SockImage extends AsyncTask<String, Void, Bitmap> {

        private ImageView miMv;

        public SockImage (ImageView iMv){
            miMv = iMv;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return loadImageFromNetwork(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            thisSock.setImageBitmap(bitmap);
        }
    }
    @Override
    public void onClick(View v) {

    }
}
