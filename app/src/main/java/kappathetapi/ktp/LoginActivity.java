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

    private int loginAttempts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText)findViewById(R.id.username_text);
        password = (EditText)findViewById(R.id.password_text);
        serverRequest = new ServerRequest();

        loginAttempts = 0;
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
        if(loginAttempts > 1){ //shortcut for development
            Intent homePageActivity = new Intent(LoginActivity.this,HomePageActivity.class);
            startActivity(homePageActivity);
            finish();
        }
        if(attemptLogin(view) == 0) {
            Intent homePageActivity = new Intent(LoginActivity.this,HomePageActivity.class);
            startActivity(homePageActivity);
            finish();
        }
        else {
            TextView text = (TextView)(findViewById(R.id.failed_login_text));
            text.setText("Login failed. Get your shit together.");
        }
    }

    public ArrayList<NameValuePair> getCredentials(View view) {
        ArrayList<NameValuePair> params = new ArrayList<>();
        usernameString = username.getText().toString();
        passwordString = "dollabillz";//password.getText().toString();
        params.add(new BasicNameValuePair("account", usernameString));
        params.add(new BasicNameValuePair("password", passwordString));
        return params;
    }

    public int attemptLogin(View view) {
        loginAttempts++;
        params = getCredentials(view);
        JSONObject json = serverRequest.getJSON(getString(R.string.server_address), ServerRequest.RequestPath.LOGIN,
                ServerRequest.RequestType.POST, params);
        if(json != null){
            Toast.makeText(getApplication(),json.toString(),Toast.LENGTH_LONG).show();
        }
        return 1;
    }


}
