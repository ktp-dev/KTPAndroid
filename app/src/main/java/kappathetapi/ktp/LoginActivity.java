package kappathetapi.ktp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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

    public void loginPressed(View view) {
        if(attemptLogin(view) == 0) {
            Intent intent = new Intent(this, HomePageActivity.class);
            startActivity(intent);
        }
        else {
            TextView text = (TextView)view.findViewById(R.id.failed_login_text);
            text.setText("Login failed. Get your shit together.");
        }
    }

    public String getCredentials(View view) {
        StringBuffer buffer = new StringBuffer();
        EditText username = (EditText)findViewById(R.id.username_text);
        EditText password = (EditText)findViewById(R.id.password_text);
        buffer.append(username.getText());
        buffer.append(' ');
        buffer.append(password.getText());
        return buffer.toString();
    }

    public int attemptLogin(View view) {
        String credentials = getCredentials(view);
        //TODO: do server stuff
        return 0;
    }


}
