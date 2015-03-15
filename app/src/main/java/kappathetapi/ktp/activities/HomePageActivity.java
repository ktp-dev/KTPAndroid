package kappathetapi.ktp.activities;

import android.app.Activity;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.widget.DrawerLayout;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Arrays;

import kappathetapi.ktp.classes.eventhandlers.EmailEventHandler;
import kappathetapi.ktp.classes.eventhandlers.FacebookEventHandler;
import kappathetapi.ktp.classes.eventhandlers.PhoneEventHandler;
import kappathetapi.ktp.classes.eventhandlers.TwitterEventHandler;
import kappathetapi.ktp.fragments.MemberProfileFragment;
import kappathetapi.ktp.fragments.NavigationDrawerFragment;
import kappathetapi.ktp.fragments.ProfileListFragment;
import kappathetapi.ktp.R;
import kappathetapi.ktp.classes.Member;


public class HomePageActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        ProfileListFragment.OnSelectionListener,
        MemberProfileFragment.OnFragmentInteractionListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private ProfileListFragment mProfileListFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    public static JSONArray jsonArray = null;
    public static Member[] memberArray;
    public static String HOME_PAGE_JSON = "com.kappathetapi.app.HOME_PAGE_JSON";
    public static String HOME_PAGE_UNIQNAME = "com.kappathetapi.app.HOME_PAGE_UNIQNAME";
    public static String HOME_PAGE_LAST_CLICKED_MEMBER = "com.kappathetapi.app.HOME_PAGE_LAST_CLICKED_MEMBER";
    private String uniqname = "";
    private String lastClickedUniqname = "";
    public Member currentMember = new Member();
    public Member lastClickedMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //Get jsonArray of members from intent passed by LoginActivity
        Intent intent = getIntent();
        SharedPreferences prefs = getSharedPreferences(getString(R.string.app_identifier), MODE_PRIVATE);
        if(intent.hasExtra(LoginActivity.MEMBER_UNIQNAME)) {
            uniqname = intent.getStringExtra(LoginActivity.MEMBER_UNIQNAME);
        } else {
            uniqname = prefs.getString(HOME_PAGE_UNIQNAME, "");
        }
        try {
            if(intent.hasExtra(LoginActivity.JSON_ARRAY)) {
                jsonArray = new JSONArray(intent.getStringExtra(LoginActivity.JSON_ARRAY));
            } else {
                jsonArray = new JSONArray(prefs.getString(HOME_PAGE_JSON, null));
            }
            makeMemberArrayFromJSON();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        //Set initial sorting of member list by name
        Arrays.sort(memberArray, new Member.MemberNameComparator());
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        switch(position) {
            case 0:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, ProfileListFragment.newInstance(position))
                        .commit();
                break;
        }
    }

    public void onSectionAttached(String fragmentName) {
        mTitle = fragmentName;
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.home_page, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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
    public void onSelection(int pos) {
        FragmentManager fragmentManager = getFragmentManager();

        lastClickedMember = memberArray[pos];
        lastClickedUniqname = lastClickedMember.getUniqname();
        fragmentManager.beginTransaction()
                .replace(R.id.container, MemberProfileFragment.newInstance(lastClickedMember))
                .commit();


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void phoneButtonPressed(View view) {
        PhoneEventHandler phoneEventHandler = PhoneEventHandler.newInstance(this, lastClickedMember);
        phoneEventHandler.handleEvent(view);
    }

    public void emailButtonPressed(View view) {
        EmailEventHandler emailEventHandler = EmailEventHandler.newInstance(this, lastClickedMember);
        emailEventHandler.handleEvent(view);
    }

    public void twitterButtonPressed(View view) {
        TwitterEventHandler twitterEventHandler = TwitterEventHandler.newInstance(this, lastClickedMember);
        twitterEventHandler.handleEvent(view);
    }

    public void facebookButtonPressed(View view) {
        FacebookEventHandler facebookEventHandler = FacebookEventHandler.newInstance(this, lastClickedMember);
        facebookEventHandler.handleEvent(view);
    }

    @Override
    public void onStop() {
        SharedPreferences prefs = this.getSharedPreferences(getString(R.string.app_identifier), MODE_PRIVATE);
        JSONArray jArray = new JSONArray();
        for(int i = 0; i < memberArray.length; ++i) {
            jArray.put(memberArray[i].toJSON());
        }
        prefs.edit().putString(HOME_PAGE_JSON, jArray.toString()).apply();
        prefs.edit().putString(HOME_PAGE_UNIQNAME, uniqname).apply();
        prefs.edit().putString(HOME_PAGE_LAST_CLICKED_MEMBER, lastClickedMember.toJSON().toString()).apply();
        super.onStop();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        SharedPreferences prefs = this.getSharedPreferences(getString(R.string.app_identifier), MODE_PRIVATE);
        try {
            jsonArray = new JSONArray(prefs.getString(HOME_PAGE_JSON, null));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        uniqname = prefs.getString(HOME_PAGE_UNIQNAME, "");
        makeMemberArrayFromJSON();
    }

    private void makeMemberArrayFromJSON() {
        try {
            memberArray = new Member[jsonArray.length()];
            for (int i = 0; i < memberArray.length; ++i) {
                memberArray[i] = new Member();
                if (Member.createInstance(jsonArray.getJSONObject(i)) != null) {
                    memberArray[i] = Member.createInstance(jsonArray.getJSONObject(i));
                    if (memberArray[i].getUniqname().equals(uniqname)) {
                        currentMember = memberArray[i];
                    }
                }
            }
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }
}
