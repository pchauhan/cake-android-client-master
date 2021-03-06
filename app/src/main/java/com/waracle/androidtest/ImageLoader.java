package com.waracle.androidtest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.HashMap;

/**
 * Created by Riad on 20/05/2015.
 */
public class ImageLoader {

    private static final String TAG = ImageLoader.class.getSimpleName();
    HashMap<String,Bitmap> hashMap = new HashMap<>();
    public ImageLoader() { /**/ }

    /**
     * Simple function for loading a bitmap image from the web
     *
     * @param url       image url
     * @param imageView view to set image too.
     */
    public void load(String url, ImageView imageView) {
        if (TextUtils.isEmpty(url)) {
            throw new InvalidParameterException("URL is empty!");
        }

        // Can you think of a way to improve loading of bitmaps
        // that have already been loaded previously??

        try {
           // setImageView(imageView, convertToBitmap(loadImageData(url)));
           // setImageView(imageView, new loadImageDataAsynch().execute(url));
            if(hashMap.containsKey(url)){
                imageView.setImageBitmap(hashMap.get(url));
            }else{
                new loadImageDataAsynch(imageView).execute(url);
            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
    public class loadImageDataAsynch extends AsyncTask<String,Void,byte[]> {
        ImageView imageView;
        String url;
        public loadImageDataAsynch(ImageView imgView){
            imageView = imgView ;
        }
        @Override
        protected byte[] doInBackground(String... voids) {
            byte[] byteArray = null;
            try{
                url = voids[0];
                byteArray = loadImageData(url);
            }catch (Exception e){
            }
            return byteArray;
        }

        @Override
        protected void onPostExecute(byte[] bytes) {
            super.onPostExecute(bytes);
            if(bytes!=null){
                hashMap.put(url,convertToBitmap(bytes));
                imageView.setImageBitmap(convertToBitmap(bytes));
            }
        }
    }
    private static byte[] loadImageData(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        InputStream inputStream = null;
        try {
            try {
                // Read data from workstation
                inputStream = connection.getInputStream();
            } catch (IOException e) {
                // Read the error from the workstation
                inputStream = connection.getErrorStream();
            }

            // Can you think of a way to make the entire
            // HTTP more efficient using HTTP headers??

            return StreamUtils.readUnknownFully(inputStream);
        } finally {
            // Close the input stream if it exists.
            StreamUtils.close(inputStream);

            // Disconnect the connection
            connection.disconnect();
        }
    }

    private static Bitmap convertToBitmap(byte[] data) {
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    private static void setImageView(ImageView imageView, Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }
}
