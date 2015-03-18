package kappathetapi.ktp.classes.eventhandlers;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import kappathetapi.ktp.classes.Member;
import kappathetapi.ktp.dialogs.FacebookDialogFragment;
import kappathetapi.ktp.tasks.FacebookIDRequest;

/**
 * Created by sjdallst on 3/8/2015.
 */
public class FacebookEventHandler {
    private Activity myActivity;
    private Member lastClickedMember;

    //Set myActivity and lastClicked member
    public static FacebookEventHandler newInstance(Activity activity, Member lastClickedMember) {
        FacebookEventHandler facebookEventHandler = new FacebookEventHandler();
        facebookEventHandler.setMyActivity(activity);
        facebookEventHandler.setLastClickedMember(lastClickedMember);
        return facebookEventHandler;
    }
    public void handleEvent(View view) {
        showDialog();
    }

    void showDialog() {
        String name = lastClickedMember.getFirstName();
        String username = lastClickedMember.getFacebook();
        DialogFragment newFragment = FacebookDialogFragment.newInstance(username, name, this);
        newFragment.show(myActivity.getFragmentManager(), "dialog");
    }

    //Attemps to open Facebook app.
    //If the app is not found, then we open up the facbook page with a browser.
    public void doFacebook() {
        if(lastClickedMember.getEmail() == null ||
                lastClickedMember.getEmail().compareTo("") == 0) {
            Toast.makeText(myActivity.getApplication(), "Facebook not set", Toast.LENGTH_LONG).show();
        } else {
            Intent i = null;
            try {
                myActivity.getPackageManager().getPackageInfo("com.facebook.katana", 0);
                FacebookIDRequest facebookIDRequest = new FacebookIDRequest();
                i = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/" +
                        facebookIDRequest.getResponse(
                                "http://graph.facebook.com/" + lastClickedMember.getFacebook())));
            } catch (Exception e) {
                e.printStackTrace();
                i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" +
                        lastClickedMember.getFacebook()));
            }
            myActivity.startActivity(i);
        }
    }

    //Dialog will close itself, so do nothing.
    public void facebookCancel() {

    }

    public void setMyActivity(Activity activity) {
        myActivity = activity;
    }

    public void setLastClickedMember(Member member) {
        lastClickedMember = member;
    }
}
