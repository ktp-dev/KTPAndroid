package kappathetapi.ktp.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;

import kappathetapi.ktp.R;
import kappathetapi.ktp.classes.Member;
import kappathetapi.ktp.fragments.meetings.MeetingBoxFragment;
import kappathetapi.ktp.fragments.NavigationDrawerFragment;

/**
 * Created by sjdallst on 5/13/2015.
 */
public class PledgingActivity extends Activity implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        MeetingBoxFragment.OnFragmentInteractionListener{

    private NavigationDrawerFragment mNavigationDrawerFragment;

    private CharSequence mTitle; //Title displayed at top left of screen (ex: Members, HomePageActivity, etc.)
    private JSONArray jsonArray = null;
    private Member[] memberArray;
    private String uniqname = "";//Currently logged in member's uniqname
    private Member currentMember = new Member(); //Currently logged in member
    private Member lastClickedMember; //The last member that was picked from member list under members

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pledging);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout), 2);
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Intent intent;
        FragmentManager fragmentManager = getFragmentManager();
        Fragment frag = MeetingBoxFragment.newInstance();

        switch(position) {
            case 0:
                intent = new Intent(getApplicationContext(), HomePageActivity.class);
                intent.putExtra(HomePageActivity.HOME_PAGE_SELECTION, position);
                startActivity(intent);
                finish();
                break;
            case 1:
                intent = new Intent(getApplicationContext(), HomePageActivity.class);
                intent.putExtra(HomePageActivity.HOME_PAGE_SELECTION, position);
                startActivity(intent);
                finish();
                break;
            case 2:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, frag)
                        .commit();
                break;
        }
    }

    //Use this in any fragment that can be accessed directly from the navigation drawer
    //use ((PledgingActivity) activity).onSectionAttached(getString(R.string.YOUR_FRAG_TITLE));
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

    @Override
    public void onMeetingSelection(String id) {

    }
}
