package kappathetapi.ktp.classes.eventhandlers;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import kappathetapi.ktp.classes.Member;
import kappathetapi.ktp.dialogs.TwitterDialogFragment;

/**
 * Created by sjdallst on 3/8/2015.
 */
public class TwitterEventHandler {
    private Activity myActivity;
    private Member lastClickedMember;

    public static TwitterEventHandler newInstance(Activity activity, Member lastClickedMember) {
        TwitterEventHandler twitterEventHandler = new TwitterEventHandler();
        twitterEventHandler.setMyActivity(activity);
        twitterEventHandler.setLastClickedMember(lastClickedMember);
        return twitterEventHandler;
    }
    public void handleEvent(View view) {
        showDialog();
    }

    void showDialog() {
        String name = lastClickedMember.getFirstName();
        String username = lastClickedMember.getPhoneNumber();
        DialogFragment newFragment = TwitterDialogFragment.newInstance(username, name, this);
        newFragment.show(myActivity.getFragmentManager(), "dialog");
    }

    public void doTwitter() {
        if(lastClickedMember.getTwitter() == null ||
                lastClickedMember.getTwitter().compareTo("") == 0) {
            Toast.makeText(myActivity.getApplication(), "Number not set", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = null;
            try {
                // get the Twitter app if possible
                myActivity.getPackageManager().getPackageInfo("com.twitter.android", 0);
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" +
                        lastClickedMember.getTwitter()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            } catch (Exception e) {
                // no Twitter app, revert to browser
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/#!/" +
                        lastClickedMember.getTwitter()));
            }
            myActivity.startActivity(intent);
        }
    }

    public void twitterCancel() {

    }

    public void setMyActivity(Activity activity) {
        myActivity = activity;
    }

    public void setLastClickedMember(Member member) {
        lastClickedMember = member;
    }
}
