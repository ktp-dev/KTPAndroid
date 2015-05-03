package kappathetapi.ktp.tasks;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MembersRequest {
    public enum RequestType{POST, GET, PUT, DELETE} //Used to set request type later
    public enum RequestPath{MEMBERS, LOGIN, PITCHES, CHANGE_PASSWORD} //Used to access some default routes
    static InputStream is = null;
    static String response = "";
    private HttpRequestBase httpRequest;
    public MembersRequest() {
        httpRequest = new HttpPut("");//default value so httpRequest is not empty.
    }
    public String getResponseFromUrl(String urlString, JSONObject params,
                                     RequestType requestType) {

        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            setRequestType(requestType, urlString);
            setDefaultHeaders();

            //Only put and post have bodies, because they're the only commands that provide info to the server
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

        return response;
    }

    public String getResponse(String url, RequestPath requestPath, RequestType requestType,
                              JSONObject params) {
        Params param = new Params(url, requestPath, requestType, params);
        return getResponse(param);
    }

    public String getResponse(String url, RequestPath requestPath, RequestType requestType,
                              JSONObject params, String accountId) {
        Params param = new Params(url, requestPath, requestType, params, accountId);
        return getResponse(param);
    }
    private String getResponse(Params param) {
        String response = null;
        Request myTask = new Request();
        try{
            response = myTask.execute(param).get();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (ExecutionException e){
            e.printStackTrace();
        }
        return response;
    }
    private static class Params {
        String url;
        RequestPath requestPath;
        RequestType requestType;
        JSONObject params;
        String accountId;

        Params(String url, RequestPath requestPath, RequestType requestType,
               JSONObject params) {
            this.url = url;
            this.requestPath = requestPath;
            this.requestType = requestType;
            this.params = params;
            this.accountId = "";
        }

        Params(String url, RequestPath requestPath, RequestType requestType,
               JSONObject params, String accountId) {
            this.url = url;
            this.requestPath = requestPath;
            this.requestType = requestType;
            this.params = params;
            this.accountId = accountId;
        }
    }
    private class Request extends AsyncTask<Params, String, String> {
        @Override
        protected String doInBackground(Params... args) {
            MembersRequest request = new MembersRequest();
            String response = null;
            String fullUrl = args[0].url + pathToString(args[0].requestPath) + args[0].accountId;
            response = request.getResponseFromUrl(fullUrl, args[0].params, args[0].requestType);
            return response;
        }
        @Override
        protected void onPostExecute(String json) {

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
            case CHANGE_PASSWORD:
                return "/api/changePassword";
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
    private void setBody(JSONObject params, RequestType requestType) throws UnsupportedEncodingException{
        StringEntity stringEntity = new StringEntity(params.toString());
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
        response = sb.toString();
        Log.e("JSON", response);
    }
}
