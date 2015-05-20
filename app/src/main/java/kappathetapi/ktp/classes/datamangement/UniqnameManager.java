package kappathetapi.ktp.classes.datamangement;

import android.app.Activity;
import android.content.SharedPreferences;

import kappathetapi.ktp.R;

/**
 * Created by sjdallst on 5/20/2015.
 */
public class UniqnameManager {
    private static String UNIQNAME_LOC = "com.kappathetapi.ktp.UNIQNAME_LOC";

    public void saveUniqname(String uniqname, Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences(activity.getString(R.string.app_identifier), Activity.MODE_PRIVATE);
        prefs.edit().putString(UNIQNAME_LOC, uniqname).apply();
    }

    public String loadUniqname(Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences(activity.getString(R.string.app_identifier), Activity.MODE_PRIVATE);
        return prefs.getString(UNIQNAME_LOC, null);
    }
}
