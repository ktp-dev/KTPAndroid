package kappathetapi.ktp.tasks;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Movie;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import kappathetapi.ktp.R;

/**
 * Created by sjdallst on 5/6/2015.
 */
public class PhotoRequest {
    private Activity activity;
    private ImageView imageView;
    private Bitmap bmp;
    private Movie movie;

    public void getPicModifyView(String url, Activity activity, ImageView view) {
        this.activity = activity;
        this.imageView = view;

        Requester requester = new Requester();
        requester.execute(url);
    }

    private class Requester extends AsyncTask<String, String, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... arg) {

            if(isPicSet(arg[0])) {
                if(isGIF(arg[0])) {
                    handleGIF(arg[0]);
                } else {
                    handleImage(arg[0]);
                }
            } else {
                useDefaultImage();
            }

            return bmp;
        }

        private void handleGIF(String urlString) {
            movie = null;
            try {
                URL url = new URL(urlString);
                movie = Movie.decodeStream(url.openConnection().getInputStream());
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        activity.addContentView(new ImageView(activity.getApplicationContext()), imageView.getLayoutParams());
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void handleImage(String urlString) {
            bmp = null;
            try {
                URL url = new URL(urlString);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        imageView.setImageBitmap(bmp);
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
                useDefaultImage();
            } catch (IOException e) {
                e.printStackTrace();
                useDefaultImage();
            }
        }
        @Override
        protected void onPostExecute(Bitmap bmp) {

        }

        private boolean isPicSet(String url) {
            return (!url.equals(activity.getString(R.string.server_address)));
        }

        private boolean isGIF(String url) {
            return (url.substring(url.length() - 3, url.length()).equals("gif"));
        }
    }

    private void useDefaultImage() {
        bmp = BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_user);
        activity.runOnUiThread(new Runnable() {
            public void run() {
                imageView.setImageBitmap(bmp);
            }
        });
    }
}
