package kappathetapi.ktp;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.*;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.jar.Attributes;

import javax.net.ssl.HttpsURLConnection;

public class ServerRequest {
    public enum RequestType{POST, GET, PUT, DELETE}
    public enum RequestPath{MEMBERS, LOGIN, PITCHES}
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    private HttpRequestBase httpRequest;
    public ServerRequest() {
        httpRequest = new HttpPut("");//default value so httpRequest is not empty.
    }
    public JSONObject getJSONFromUrl(String urlString, List<NameValuePair> params, RequestPath requestPath,
                                     RequestType requestType) {
        String fullUrl = urlString + pathToString(requestPath);
        //"http://35.2.181.254:8080"

        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            setRequestType(requestType, fullUrl);
            setDefaultHeaders();

            if(requestType == RequestType.PUT || requestType == RequestType.POST) {
                setBody(params, requestType);
            }

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

        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        return jObj;
    }

    public JSONObject getJSON(String url, RequestPath requestPath, RequestType requestType,
                              List<NameValuePair> params) {
        JSONObject jobj = null;
        Params param = new Params(url, requestPath, requestType, params);
        Request myTask = new Request();
        try{
            jobj= myTask.execute(param).get();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (ExecutionException e){
            e.printStackTrace();
        }

        return jobj;
    }
    private static class Params {
        String url;
        RequestPath requestPath;
        RequestType requestType;
        List<NameValuePair> params;

        Params(String url, RequestPath requestPath, RequestType requestType,
               List<NameValuePair> params) {
            this.url = url;
            this.requestPath = requestPath;
            this.requestType = requestType;
            this.params = params;
        }
    }
    private class Request extends AsyncTask<Params, String, JSONObject> {
        @Override
        protected JSONObject doInBackground(Params... args) {
            ServerRequest request = new ServerRequest();
            JSONObject json = null;
            json = request.getJSONFromUrl(args[0].url, args[0].params, args[0].requestPath,
                    args[0].requestType);
            return json;
        }
        @Override
        protected void onPostExecute(JSONObject json) {

        }
    }
    private String pathToString(RequestPath requestPath) {
        switch (requestPath) {
            case MEMBERS:
                return "/api/members/";
            case LOGIN:
                return "/api/login/";
            case PITCHES:
                return "/api/pitches/";
        }
        return "";
    }
    private void setRequestType(RequestType requestType, String url) {
        switch(requestType) {
            case POST:
                httpRequest = new HttpPost(url);
                break;
            case GET:
                httpRequest = new HttpGet(url);
                break;
            case PUT:
                httpRequest = new HttpPut(url);
                break;
            case DELETE:
                httpRequest = new HttpDelete(url);
                break;
        }
    }
    private void setDefaultHeaders() {
        httpRequest.setHeader("x-access-token", "5af9a24515589a73d0fa687e69cbaaa15918f833");
        httpRequest.setHeader("Content-Type", "application/json");
        httpRequest.setHeader("Accept", "application/json");
    }
    private void setBody(List<NameValuePair> params, RequestType requestType) throws UnsupportedEncodingException{
        JSONObject jsonObject = new JSONObject();
        try {
            for (NameValuePair p : params) {
                jsonObject.put(p.getName(), p.getValue());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringEntity stringEntity = new StringEntity(jsonObject.toString());
        stringEntity.setContentType("application/json");
        stringEntity.setContentEncoding("UTF-8");
        if(requestType == RequestType.PUT) {
            ((HttpPut)httpRequest).setEntity(stringEntity);
        }
        else if(requestType == RequestType.POST) {
            ((HttpPost)httpRequest).setEntity(stringEntity);
        }
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
        json = sb.toString();
        Log.e("JSON", json);
    }
}
