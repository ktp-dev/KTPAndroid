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
    static String response = "";
    private HttpRequestBase httpRequest;
    public FacebookIDRequest() {
        httpRequest = new HttpPut("");//default value so httpRequest is not empty.
    }

    public String getResponse(String url) {
        String response = null;
        Request myTask = new Request();
        try{
            response = myTask.execute(url).get(); //run async task to get response from facebook
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
            DefaultHttpClient httpClient = new DefaultHttpClient(); //set up client
            httpRequest = new HttpGet(fullUrl); //set up get request
            //These are pretty standard headers, the other standard header is "x-token" or something
            //there's an example in MembersRequest
            httpRequest.setHeader("Content-Type", "application/json");
            httpRequest.setHeader("Accept", "application/json");

            //Could be done in one line, but this is a little more clear
            HttpResponse httpResponse = httpClient.execute(httpRequest); //Do magic, get response
            HttpEntity httpEntity = httpResponse.getEntity(); //Actual response
            is = httpEntity.getContent(); //get input stream, which is what we actually read from

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            getStringFromInputStream(); //Get string from "is"
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        return response;
    }

    //Parses input stream from server into a format easily read by JSON
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
