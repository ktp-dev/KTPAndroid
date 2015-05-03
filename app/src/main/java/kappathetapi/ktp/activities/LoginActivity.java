package kappathetapi.ktp.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import kappathetapi.ktp.R;
import kappathetapi.ktp.fragments.ChangePasswordFragment;
import kappathetapi.ktp.fragments.LoginFragment;
import kappathetapi.ktp.tasks.MembersRequest;


public class LoginActivity extends Activity implements ChangePasswordFragment.OnFragmentInteractionListener,
                                                        LoginFragment.OnFragmentInteractionListener{
    EditText username, password;
    String usernameString, passwordString, accountString = "";
    JSONObject params = null;
    MembersRequest membersRequest;
    private JSONArray jsonArray = new JSONArray();
    public final static String JSON_ARRAY = "com.kappathetapi.KTP.JSONArray";
    public final static String MEMBER_UNIQNAME = "com.kappathetapi.KTP.MemberUniqname";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.login_container, LoginFragment.newInstance())
                .commit();

        username = (EditText)findViewById(R.id.username_text);
        password = (EditText)findViewById(R.id.password_text);
        membersRequest = new MembersRequest();

        SharedPreferences prefs = this.getSharedPreferences(getString(R.string.app_identifier), MODE_PRIVATE);
        try {
            //Check to see if there is a saved string of members
            //There will be a saved string of members if returning to LoginActivity from
            //another Activity within the app
            if (prefs != null &&
                    prefs.getString(JSON_ARRAY, "").compareTo("") != 0) {
                jsonArray = new JSONArray(prefs.getString(JSON_ARRAY, ""));
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

    @Override
    public int performLogin(String username, String password) {

        usernameString = username;
        passwordString = password;

        int canLogin = 0;
        //Attempts to match credentials to member, returns 1 if account is not found
        try {
            findAndSetAccount();
            //if findAndSetAccount throws an error, don't attempt to login
            canLogin = attemptLogin();
        } catch(WrongUniqnameException e) {
            canLogin = 1;
        }

        if(canLogin == 0) {
            Intent homePageIntent = new Intent(LoginActivity.this,HomePageActivity.class);
            homePageIntent.putExtra(JSON_ARRAY, jsonArray.toString());
            homePageIntent.putExtra(MEMBER_UNIQNAME, usernameString);
            startActivity(homePageIntent);
            finish();
        }

        return canLogin;
    }

    public int attemptLogin() {
        membersRequest = new MembersRequest();
        params = new JSONObject();
        try {
            params.put("account", accountString);
            params.put("password", passwordString);
        } catch(JSONException e) {
            e.printStackTrace();
        }
        //Attempt a login. Return based on response.
        String response = membersRequest.getResponse(getString(R.string.server_address), MembersRequest.RequestPath.LOGIN,
                MembersRequest.RequestType.POST, params);
        return loginResponseToInt(response);
    }

    private int attemptPasswordChange(String newPassword, String newPassword1) {
        membersRequest = new MembersRequest();
        params = new JSONObject();
        try {
            params.put("account", accountString);
            params.put("oldPassword", passwordString);
            params.put("newPassword", newPassword);
            params.put("confirmPassword", newPassword1);
        } catch(JSONException e) {
            e.printStackTrace();
        }

        String response = membersRequest.getResponse(getString(R.string.server_address),
                MembersRequest.RequestPath.CHANGE_PASSWORD, MembersRequest.RequestType.POST, params);

        return passwordChangeResponseToInt(response);
    }

    private int passwordChangeResponseToInt(String response) {
        if(response.trim().compareTo("password changed") == 0) {
            return 0;
        }
        if(response.trim().compareTo("account not found") == 0) {
            return 1;
        }
        if(response.trim().compareTo("invalid password") == 0) {
            return 2;
        }
        if(response.trim().compareTo("passwords dont match") == 0) {
            return 4;
        }

        Log.e("Unrecognized response", response);
        return 3;
    }

    @Override
    public int performPasswordChange(String username, String oldPassword, String newPassword, String newPassword1) {
        usernameString = username;
        passwordString = oldPassword;
        int canChangePassword = 0;
        try {
            findAndSetAccount();
        } catch (WrongUniqnameException e) {
            canChangePassword = 1;
        }

        if(canChangePassword == 0) {
            canChangePassword = attemptLogin();
        }

        if(canChangePassword == 0) {
            canChangePassword = attemptPasswordChange(newPassword, oldPassword);
        }

        return canChangePassword;
    }

    @Override
    public void backToLogin() {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
        .replace(R.id.login_container, LoginFragment.newInstance())
        .commit();
    }


    //Empty exception class made to help deal with incorrect username(uniqname) input
    private class WrongUniqnameException extends Throwable {

    }

    @Override
    public void startChangePasswordFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.login_container, ChangePasswordFragment.newInstance())
                .commit();
    }

    private int loginResponseToInt(String response) {
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

    private void findAndSetAccount() throws WrongUniqnameException {
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
    }
}
