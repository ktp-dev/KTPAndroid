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

import kappathetapi.ktp.classes.datamangement.MemberArrayManager;
import kappathetapi.ktp.classes.eventhandlers.DeleteEventHandler;
import kappathetapi.ktp.classes.eventhandlers.EmailEventHandler;
import kappathetapi.ktp.classes.eventhandlers.FacebookEventHandler;
import kappathetapi.ktp.classes.eventhandlers.PhoneEventHandler;
import kappathetapi.ktp.classes.eventhandlers.TwitterEventHandler;
import kappathetapi.ktp.fragments.EditProfileFragment;
import kappathetapi.ktp.fragments.MemberProfileFragment;
import kappathetapi.ktp.fragments.NavigationDrawerFragment;
import kappathetapi.ktp.fragments.ProfileListFragment;
import kappathetapi.ktp.R;
import kappathetapi.ktp.classes.Member;


public class HomePageActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        ProfileListFragment.OnSelectionListener,
        MemberProfileFragment.OnFragmentInteractionListener,
        EditProfileFragment.OnSaveListener{

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle; //Title displayed at top left of screen (ex: Members, HomePageActivity, etc.)
    public static JSONArray jsonArray = null;
    public static Member[] memberArray;
    public static String HOME_PAGE_JSON = "com.kappathetapi.app.HOME_PAGE_JSON";
    public static String HOME_PAGE_UNIQNAME = "com.kappathetapi.app.HOME_PAGE_UNIQNAME";
    public static String HOME_PAGE_LAST_CLICKED_MEMBER = "com.kappathetapi.app.HOME_PAGE_LAST_CLICKED_MEMBER";
    public static String HOME_PAGE_SELECTION = "com.kappathetapi.app.HOME_PAGE_SELECTION";
    private String uniqname = "";//Currently logged in member's uniqname
    public Member currentMember = new Member(); //Currently logged in member
    public Member lastClickedMember; //The last member that was picked from member list under members

    private MemberArrayManager memberArrayManager = new MemberArrayManager();
    private int navSelection = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Intent intent = getIntent();
        SharedPreferences prefs = getSharedPreferences(getString(R.string.app_identifier), MODE_PRIVATE);
        //Either get the user's uniqname from Login activity, or get it from storage
        if(intent.hasExtra(LoginActivity.MEMBER_UNIQNAME)) {
            uniqname = intent.getStringExtra(LoginActivity.MEMBER_UNIQNAME);
        } else {
            uniqname = prefs.getString(HOME_PAGE_UNIQNAME, "");
        }

        //Set which navigation drawer item to select
        navSelection = 1;
        if(intent.hasExtra(HOME_PAGE_SELECTION)) {
            navSelection = intent.getIntExtra(HOME_PAGE_SELECTION, 1);
        } else {
            navSelection = prefs.getInt(HOME_PAGE_SELECTION, 1);
        }

        currentMember = new Member();
        memberArray = memberArrayManager.loadArray(uniqname, currentMember, this);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout), navSelection);

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
        Intent intent;
        switch(position) {
            case 0:
                navSelection = position;
                fragmentManager.beginTransaction()
                        .replace(R.id.container, EditProfileFragment.createInstance(currentMember))
                        .commit();
                break;
            case 1:
                navSelection = position;
                fragmentManager.beginTransaction()
                        .replace(R.id.container, ProfileListFragment.newInstance(position))
                        .commit();
                break;
            case 2:
                memberArrayManager.saveArray(memberArray, this);
                intent = new Intent(getApplicationContext(), PledgingActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    //Use this in any fragment that can be accessed directly from the navigation drawer
    //use ((HomePageActivity) activity).onSectionAttached(getString(R.string.YOUR_FRAG_TITLE));
    //in your fragment's OnAttach method
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

    //Method called when a member's name is clicked from the list of members.
    //Sets lastClickedMember and switches to a MemberProfileFragment for that member
    @Override
    public void onSelection(int pos) {
        FragmentManager fragmentManager = getFragmentManager();

        lastClickedMember = memberArray[pos];
        fragmentManager.beginTransaction()
                .replace(R.id.container, MemberProfileFragment.newInstance(lastClickedMember))
                .commit();
    }

    @Override
    public void onDeletePressed() {
        DeleteEventHandler deleteHandler = DeleteEventHandler.newInstance(this, lastClickedMember);
        if(deleteHandler.handleDelete()) {
            deleteMember(lastClickedMember);
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, ProfileListFragment.newInstance(1))
                    .commit();
        }
    }

    private void deleteMember(Member member) {
        Member[] mems = new Member[memberArray.length - 1];
        int j = 0;
        for(int i = 0; i < memberArray.length; ++i) {
            if(!memberArray[i].getId().equals(member.getId())) {
                mems[j] = memberArray[i];
                ++j;
            }
        }
        memberArray = mems;
    }

    @Override
    public void onSavePressed(Member member) {
        updateArray(member);
    }
    //ALL OF THESE BUTTON PRESSES LEAVE THE APP
    //Called from MemberProfileFragment when the phone button is pressed.
    //Creates a dialog that asks if you want to call n stuff
    public void phoneButtonPressed(View view) {
        PhoneEventHandler phoneEventHandler = PhoneEventHandler.newInstance(this, lastClickedMember);
        phoneEventHandler.handleEvent(view);
    }

    //Called from MemberProfileFragment when the email button is pressed.
    //Creates a dialog that asks if you want to email
    public void emailButtonPressed(View view) {
        EmailEventHandler emailEventHandler = EmailEventHandler.newInstance(this, lastClickedMember);
        emailEventHandler.handleEvent(view);
    }

    //Called from MemberProfileFragment when the twitter button is pressed.
    //Creates a dialog that asks if you want to twitter n stuff
    public void twitterButtonPressed(View view) {
        TwitterEventHandler twitterEventHandler = TwitterEventHandler.newInstance(this, lastClickedMember);
        twitterEventHandler.handleEvent(view);
    }

    //Called from MemberProfileFragment when the facebook button is pressed.
    //Creates a dialog that asks if you want to facebook n stuff
    public void facebookButtonPressed(View view) {
        FacebookEventHandler facebookEventHandler = FacebookEventHandler.newInstance(this, lastClickedMember);
        facebookEventHandler.handleEvent(view);
    }

    @Override
    public void onStop() {
        SharedPreferences prefs = this.getSharedPreferences(getString(R.string.app_identifier), MODE_PRIVATE);

        memberArrayManager.saveArray(memberArray, this);

        if(uniqname != null) {
            prefs.edit().putString(HOME_PAGE_UNIQNAME, uniqname).apply();
        }
        if(lastClickedMember != null) {
            prefs.edit().putString(HOME_PAGE_LAST_CLICKED_MEMBER, lastClickedMember.toJSON().toString()).apply();
        }

        prefs.edit().putInt(HOME_PAGE_SELECTION, navSelection);
        super.onStop();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        SharedPreferences prefs = this.getSharedPreferences(getString(R.string.app_identifier), MODE_PRIVATE);

        uniqname = prefs.getString(HOME_PAGE_UNIQNAME, "");

        currentMember = new Member();
        memberArray = memberArrayManager.loadArray(uniqname, currentMember, this);
    }

    @Override
    public void updateThrough(Member member) {
        updateArray(member);
    }

    private void updateArray(Member member) {
        int size = memberArray.length;
        boolean memberFound = false;
        for(int i = 0; i < size && !memberFound; ++i) {
            if(memberArray[i].getUniqname().equals(member.getUniqname())) {
                memberFound = true;
                memberArray[i] = member;
            }
        }
    }
}
