package kappathetapi.ktp.tasks;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Movie;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import kappathetapi.ktp.R;
import kappathetapi.ktp.classes.Member;
import kappathetapi.ktp.classes.gifhelpers.ImageViewGIF;

/**
 * Created by sjdallst on 5/6/2015.
 */
public class PhotoRequest {
    private Activity activity;
    private ImageViewGIF imageView;
    private Bitmap bmp;
    private Movie movie;
    private Member member;

    public void getPicModifyView(String url, Activity activity, ImageViewGIF view) {
        this.activity = activity;
        this.imageView = view;

        Requester requester = new Requester();
        requester.execute(url);
    }

    public void uploadPic(String url, Bitmap bmp, Activity activity, ImageViewGIF view, Member member) {
        this.bmp = bmp;
        this.activity = activity;
        this.imageView = view;
        this.member = member;

        Uploader uploader = new Uploader();
        uploader.execute(url);
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
                BufferedInputStream bis = new BufferedInputStream(url.openConnection().getInputStream(), 4096);
                bis.mark(16*4096);
                movie = Movie.decodeStream(bis);
                System.out.println("JAJAJAJJAJA " + (movie == null));
                applyGIF();
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
                applyImage();
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
        applyImage();
    }

    private class Uploader extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... arg) {
            String response = "";
            try {
                ByteArrayOutputStream bOut = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, bOut);
                ByteArrayInputStream bIn = new ByteArrayInputStream(bOut.toByteArray());
                URL url = new URL(arg[0]);
                HttpPost request = new HttpPost();
                request.setURI(url.toURI());
                request.setHeader("x-access-token", "5af9a24515589a73d0fa687e69cbaaa15918f833");
                request.setHeader("Content-Type", "image/*");
                request.setHeader("Accept", "application/json");
                InputStreamEntity body = new InputStreamEntity(bIn, bOut.toByteArray().length);
                request.setEntity(body);

                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpResponse httpResponse = httpClient.execute(request);
                HttpEntity httpEntity = httpResponse.getEntity();
                InputStream is = httpEntity.getContent();

                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                response = sb.toString();
                Log.e("JSON", response);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }


            try {
                JSONObject json = new JSONObject(response);
                member.setProfPicUrl(json.getString("url"));
                applyImage();
            } catch (JSONException e) {

            }
            return response;
        }
    }

    private void applyImage() {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                imageView.setImageBitmap(bmp);
            }
        });
    }

    private void applyGIF() {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                imageView.setMovie(movie);
            }
        });
    }
}
