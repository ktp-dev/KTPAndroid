package kappathetapi.ktp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import kappathetapi.ktp.R;
import kappathetapi.ktp.tasks.MembersRequest;


public class LoginActivity extends Activity {
    EditText username, password;
    String usernameString, passwordString, accountString = "";
    List<NameValuePair> params;
    MembersRequest membersRequest;
    private JSONArray jsonArray = new JSONArray();
    public final static String JSON_ARRAY = "com.kappathetapi.KTP.JSONArray";
    public final static String MEMBER_UNIQNAME = "com.kappathetapi.KTP.MemberUniqname";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText)findViewById(R.id.username_text);
        password = (EditText)findViewById(R.id.password_text);
        membersRequest = new MembersRequest();
        try {
            //Check to see if there is a saved string of members
            //There will be a saved string of members if returning to LoginActivity from
            //another Activity within the app
            if (savedInstanceState != null &&
                    savedInstanceState.getString(JSON_ARRAY, "").compareTo("") != 0) {
                jsonArray = new JSONArray(savedInstanceState.getString(JSON_ARRAY));
            } else {
                //Get members from server
                jsonArray = new JSONArray(membersRequest.getResponse(getString(R.string.server_address),
                        MembersRequest.RequestPath.MEMBERS, MembersRequest.RequestType.GET, params));
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
        //Need to initialize error text, so it can be modified in switch statement
        TextView errorText = (TextView)(findViewById(R.id.failed_login_text));

        //Attempt to login. Set an error message if necessary
        switch(attemptLogin(view)) {
            case 0:
                //Switch to HomePageActivity on successful login
                Intent homePageIntent = new Intent(LoginActivity.this,HomePageActivity.class);
                homePageIntent.putExtra(JSON_ARRAY, jsonArray.toString());
                homePageIntent.putExtra(MEMBER_UNIQNAME, usernameString);
                startActivity(homePageIntent);
                finish();
                break;
            case 1:
                errorText.setText("Account not found. You in the right place, bruh?");
                break;
            case 2:
                errorText.setText("Invalid password. What time is it? Bad O'Clock!");
            default:
                errorText.setText("Idk what's going on...\nContact sjdallst@umich.edu");
        }
    }

    public ArrayList<NameValuePair> getCredentials(View view) throws WrongUniqnameException {
        ArrayList<NameValuePair> params = new ArrayList<>();

        //Get username and password from User
        usernameString = username.getText().toString().trim();
        passwordString = password.getText().toString().trim();

        //Get accountID by searching through uniqnames
        //accountID is used for POST request to server
        boolean found = false;
        try {
            for (int i = 0; i < jsonArray.length() && !found; ++i) {
                if (((String) jsonArray.getJSONObject(i).get("uniqname")).compareTo(usernameString) == 0) {
                    accountString = ((String) jsonArray.getJSONObject(i).get("account"));
                    found = true;
                }
            }
        } catch(JSONException e) {
            e.printStackTrace();
        }

        if(!found) {
            throw new WrongUniqnameException();
        }

        //Set paramaters for server request
        params.add(new BasicNameValuePair("account", accountString));
        params.add(new BasicNameValuePair("password", passwordString));
        return params;
    }

    public int attemptLogin(View view) {
        //Attempts to match credentials to member, returns 1 if account is not found
        try {
            params = getCredentials(view);
        } catch(WrongUniqnameException e) {
            return 1;
        }
        //Attempt a login. Return based on response.
        String response = membersRequest.getResponse(getString(R.string.server_address), MembersRequest.RequestPath.LOGIN,
                MembersRequest.RequestType.POST, params);
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

    //Empty exception class made to help deal with incorrect username(uniqname) input
    private class WrongUniqnameException extends Throwable {

    }

}
