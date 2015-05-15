package kappathetapi.ktp.classes.datamangement;

import android.app.Activity;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;

import kappathetapi.ktp.R;
import kappathetapi.ktp.classes.Member;

/**
 * Created by sjdallst on 5/14/2015.
 */
public class MemberArrayManager {
    private static String ARRAY_LOC = "com.kappathetapi.ktp.ARRAY_LOC";

    public void saveArray(Member[] members, Activity activity) {
        JSONArray jArray = new JSONArray();
        for(int i = 0; i < members.length; ++i) {
            jArray.put(members[i].toJSON());
        }
        saveArray(jArray, activity);
    }

    public void saveArray(JSONArray jsonArray, Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences(activity.getString(R.string.app_identifier), Activity.MODE_PRIVATE);
        prefs.edit().putString(ARRAY_LOC, jsonArray.toString()).apply();
    }

    public Member[] loadArray(String uniqname, Member member, Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences(activity.getString(R.string.app_identifier), Activity.MODE_PRIVATE);
        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray = new JSONArray(prefs.getString(ARRAY_LOC, null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Member[] members = null;
        try {
            members = new Member[jsonArray.length()];
            for (int i = 0; i < members.length; ++i) {
                members[i] = new Member();
                if (Member.createInstance(jsonArray.getJSONObject(i)) != null) {
                    members[i] = Member.createInstance(jsonArray.getJSONObject(i));
                    if (members[i].getUniqname().equals(uniqname)) {
                        member.copy(members[i]);
                    }
                }
            }
        } catch(JSONException e) {
            e.printStackTrace();
        }

        return members;
    }
}
