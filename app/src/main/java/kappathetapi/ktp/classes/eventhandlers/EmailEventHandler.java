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
import kappathetapi.ktp.dialogs.EmailDialogFragment;

/**
 * Created by sjdallst on 3/8/2015.
 */
public class EmailEventHandler {
    private Activity myActivity;
    private Member lastClickedMember;

    public static EmailEventHandler newInstance(Activity activity, Member lastClickedMember) {
        EmailEventHandler emailEventHandler = new EmailEventHandler();
        emailEventHandler.setMyActivity(activity);
        emailEventHandler.setLastClickedMember(lastClickedMember);
        return emailEventHandler;
    }
    public void handleEvent(View view) {
        showDialog();
    }

    void showDialog() {
        String name = lastClickedMember.getFirstName();
        String eAddress = lastClickedMember.getEmail();
        DialogFragment newFragment = EmailDialogFragment.newInstance(eAddress, name, this);
        newFragment.show(myActivity.getFragmentManager(), "dialog");
    }

    public void doEmail() {
        if(lastClickedMember.getEmail() == null ||
                lastClickedMember.getEmail().compareTo("") == 0) {
            Toast.makeText(myActivity.getApplication(), "Email not set", Toast.LENGTH_LONG).show();
        } else {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{lastClickedMember.getEmail()});
            try {
                myActivity.startActivity(Intent.createChooser(i, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(myActivity, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void emailCancel() {

    }

    public void setMyActivity(Activity activity) {
        myActivity = activity;
    }

    public void setLastClickedMember(Member member) {
        lastClickedMember = member;
    }
}
