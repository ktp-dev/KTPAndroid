package kappathetapi.ktp.tasks;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by sjdallst on 3/8/2015.
 */
public class FacebookIDRequest {
    static InputStream is = null;
    static JSONObject jObj = null;
    static String response = "";
    private HttpRequestBase httpRequest;
    public FacebookIDRequest() {
        httpRequest = new HttpPut("");//default value so httpRequest is not empty.
    }

    public String getResponse(String url) {
        String response = null;
        Request myTask = new Request();
        try{
            response = myTask.execute(url).get();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (ExecutionException e){
            e.printStackTrace();
        }

        return response;
    }

    public String getResponseFromUrl(String urlString) {
        String fullUrl = urlString;

        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            httpRequest = new HttpGet(fullUrl);
            httpRequest.setHeader("Content-Type", "application/json");
            httpRequest.setHeader("Accept", "application/json");

            HttpResponse httpResponse = httpClient.execute(httpRequest);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            getStringFromInputStream();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        return response;
    }

    private void getStringFromInputStream() throws UnsupportedEncodingException, IOException{
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
    }
    private class Request extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... args) {
            FacebookIDRequest request = new FacebookIDRequest();
            String response = null;
            response = request.getResponseFromUrl(args[0]);
            try {
                JSONObject jsonObject = new JSONObject(response);
                return jsonObject.getString("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return response;
        }
        @Override
        protected void onPostExecute(String json) {

        }
    }
}
