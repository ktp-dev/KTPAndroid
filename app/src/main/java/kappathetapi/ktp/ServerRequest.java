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
import org.apache.http.client.methods.HttpPost;
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
    public ServerRequest() {
    }
    public JSONObject getJSONFromUrl(String urlString, List<NameValuePair> params, RequestPath requestPath,
                                     RequestType requestType) {
        String fullUrl = "http://35.2.181.254:8080";//urlString + pathToString(requestPath);
        //URL url = new URL(fullUrl);

        try {
            /*HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("x-access-token", String.valueOf(R.string.server_token));
            con.setRequestProperty("Content Type", "application/json");
            ArrayList<String> stringList = new ArrayList<>();
            for(NameValuePair a : params) {
                stringList.add(a.getValue());
            }
            con.setRequestProperty(stringList.get(0), stringList.get(1));
            con.setDoInput(true);
            con.setDoOutput(true);
            con.getOutputStream().flush();
            con.getOutputStream().close();*/
            DefaultHttpClient httpClient = new DefaultHttpClient();

            HttpPost httpRequest = new HttpPost(fullUrl);
            //Header[] headers = new Header[1];
            //headers[0] = new BasicHeader("x-access-token", String.valueOf(R.string.server_token));
            //headers[1] = new BasicHeader("Content Type", "application/json");
            //httpRequest.setHeaders(headers);
            httpRequest.setHeader("x-access-token", "5af9a24515589a73d0fa687e69cbaaa15918f833");
            httpRequest.setHeader("Content-Type", "application/json");
            httpRequest.setHeader("Accept", "application/json");
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
            httpRequest.setEntity(stringEntity);
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
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        //jObj = new JSONObject();

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
}
