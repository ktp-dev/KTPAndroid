package kappathetapi.ktp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;


public class LoginActivity extends Activity {
    EditText username, password, resetEmail, code, newPassword;
    String usernameString, passwordString, resetEmailString,codeString,
            newPasswordString;
    List<NameValuePair> params;
    SharedPreferences preferences;
    Dialog resetDialog;
    ServerRequest serverRequest;
    private JSONArray jsonArray = new JSONArray();
    public final static String JSON_ARRAY = "com.kappathetapi.KTP.JSONArray";

    private int loginAttempts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText)findViewById(R.id.username_text);
        password = (EditText)findViewById(R.id.password_text);
        serverRequest = new ServerRequest();
        try {
            if (savedInstanceState != null &&
                    savedInstanceState.getString(JSON_ARRAY, "").compareTo("") != 0) {
                jsonArray = new JSONArray(savedInstanceState.getString(JSON_ARRAY));
            } else {
                jsonArray = new JSONArray(serverRequest.getResponse(getString(R.string.server_address),
                        ServerRequest.RequestPath.MEMBERS, ServerRequest.RequestType.GET, params));
            }
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void performLogin(View view) {
        TextView text = (TextView)(findViewById(R.id.failed_login_text));
        switch(attemptLogin(view)) {
            case 0:
                Intent homePageIntent = new Intent(LoginActivity.this,HomePageActivity.class);
                homePageIntent.putExtra(JSON_ARRAY, jsonArray.toString());
                startActivity(homePageIntent);
                finish();
                break;
            case 1:
                text.setText("Account not found. You in the right place, bruh?");
                break;
            case 2:
                text.setText("Invalid password. What time is it? Bad O'Clock!");
            default:
                text.setText("Idk what's going on...\nContact sjdallst@umich.edu");
        }
    }

    public ArrayList<NameValuePair> getCredentials(View view) throws WrongPasswordException{
        ArrayList<NameValuePair> params = new ArrayList<>();
        /*FOR DEVELOPMENT PURPOSES
        usernameString = "sjdallst";
        passwordString = "dollabillz";//password.getText().toString();
        */
        usernameString = username.getText().toString().trim();
        passwordString = password.getText().toString().trim();
        boolean found = false;
        try {
            for (int i = 0; i < jsonArray.length() && !found; ++i) {
                if (((String) jsonArray.getJSONObject(i).get("uniqname")).compareTo(usernameString) == 0) {
                    usernameString = ((String) jsonArray.getJSONObject(i).get("account"));
                    found = true;
                }
            }
        } catch(JSONException e) {
            e.printStackTrace();
        }

        if(!found) {
            throw new WrongPasswordException();
        }
        params.add(new BasicNameValuePair("account", usernameString));
        params.add(new BasicNameValuePair("password", passwordString));
        return params;
    }

    public int attemptLogin(View view) {
        try {
            params = getCredentials(view);
        } catch(WrongPasswordException e) {
            return 1;
        }
        String response = serverRequest.getResponse(getString(R.string.server_address), ServerRequest.RequestPath.LOGIN,
                ServerRequest.RequestType.POST, params);
        if(response.trim().compareTo("success") == 0) {
            return 0;
        }
        if(response.trim().compareTo("account not found") == 0) {
            return 1;
        }
        if(response.trim().compareTo("invalid password") == 0) {
            return 2;
        }

        Log.e("Unrecognized response", response);
        return 3;
    }

    private class WrongPasswordException extends Throwable {

    }

    public JSONArray getMemberArray() {
        return jsonArray;
    }
}
