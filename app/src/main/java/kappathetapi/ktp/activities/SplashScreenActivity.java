package kappathetapi.ktp.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import kappathetapi.ktp.R;
import kappathetapi.ktp.tasks.MembersRequest;

public class SplashScreenActivity extends HorizontalSlideActivity {

    boolean arrayGrabbed = false;
    int grabAttempts = 0;
    ReentrantLock reentrantLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        arrayGrabbed = false;
        grabAttempts = 0;
        reentrantLock = new ReentrantLock();

        Thread startTimer = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                reentrantLock.lock();
                while(!arrayGrabbed && grabAttempts < 100) {
                    reentrantLock.unlock();
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    ++grabAttempts;
                    reentrantLock.lock();
                }

                if(arrayGrabbed) {
                    Intent intent = new Intent(SplashScreenActivity.this,
                            LoginActivity.class);
                    startActivity(intent);
                } else {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "TIMED OUT", Toast.LENGTH_LONG);
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                finish();

            }
        };
        startTimer.start();

        Thread arrayGrabber = new Thread() {
            @Override
            public void run() {
                JSONArray jsonArray = new JSONArray();
                MembersRequest membersRequest = new MembersRequest();
                JSONObject params = new JSONObject();

                try {
                    jsonArray = new JSONArray(membersRequest.getResponse(getString(R.string.server_address),
                            MembersRequest.RequestPath.MEMBERS, MembersRequest.RequestType.GET, params));
                } catch(JSONException e) {
                    e.printStackTrace();
                }

                SharedPreferences prefs = getSharedPreferences(getString(R.string.app_identifier), MODE_PRIVATE);

                prefs.edit().putString(LoginActivity.JSON_ARRAY, jsonArray.toString()).apply();
                reentrantLock.lock();
                arrayGrabbed = true;
                reentrantLock.unlock();
            }
        };

        arrayGrabber.start();
    }

}
