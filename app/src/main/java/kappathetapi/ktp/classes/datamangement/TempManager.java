package kappathetapi.ktp.classes.datamangement;

import android.app.Activity;

import org.json.JSONArray;

import kappathetapi.ktp.classes.Member;

/**
 * Created by sjdallst on 5/20/2015.
 */
public class TempManager {
    private UniqnameManager uniqnameManager = new UniqnameManager();
    private MemberArrayManager memberArrayManager = new MemberArrayManager();

    public void save(String uniqname, Member[] members, Activity activity) {
        memberArrayManager.saveArray(members, activity);
        uniqnameManager.saveUniqname(uniqname, activity);
    }

    public void save(String uniqname, JSONArray members, Activity activity) {
        memberArrayManager.saveArray(members, activity);
        uniqnameManager.saveUniqname(uniqname, activity);
    }

    public Member[] loadMemberArray(String uniqname, Member member, Activity activity) {
        return memberArrayManager.loadArray(uniqname, member, activity);
    }

    public String loadUniqname(Activity activity) {
        return uniqnameManager.loadUniqname(activity);
    }
}
