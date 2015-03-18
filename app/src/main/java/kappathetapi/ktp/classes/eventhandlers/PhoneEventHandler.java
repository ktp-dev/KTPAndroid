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
import kappathetapi.ktp.dialogs.PhoneDialogFragment;

/**
 * Created by sjdallst on 3/7/2015.
 */
public class PhoneEventHandler {
    private Activity myActivity;
    private Member receivingMember;

    //Set myActivity and receivingMember
    public static PhoneEventHandler newInstance(Activity activity, Member lastClickedMember) {
        PhoneEventHandler phoneEventHandler = new PhoneEventHandler();
        phoneEventHandler.setMyActivity(activity);
        phoneEventHandler.setReceivingMember(lastClickedMember);
        return phoneEventHandler;
    }

    //Called when button is pressed, creates dialog for User to choose whether to call text or cancel
    public void handleEvent(View view) {
        Uri number = Uri.parse("tel:0000000000");
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
        PackageManager packageManager = myActivity.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(callIntent, 0);
        boolean isIntentSafe = activities.size() > 0;
        showDialog();
    }

    void showDialog() {
        String name = receivingMember.getFirstName();
        String number = receivingMember.getPhoneNumber();
        DialogFragment newFragment = PhoneDialogFragment.newInstance(number, name, this);
        newFragment.show(myActivity.getFragmentManager(), "dialog");
    }

    public void doCall() {
        if(receivingMember.getPhoneNumber() == null ||
                receivingMember.getPhoneNumber().compareTo("") == 0) {
            Toast.makeText(myActivity.getApplication(), "Number not set", Toast.LENGTH_LONG).show();
        } else {
            Uri number = Uri.parse("tel:" + receivingMember.getPhoneNumber());
            Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
            myActivity.startActivity(callIntent);
        }
    }

    public void doText() {
        if(receivingMember.getPhoneNumber() == null ||
                receivingMember.getPhoneNumber().compareTo("") == 0) {
            Toast.makeText(myActivity.getApplication(), "Number not set", Toast.LENGTH_LONG).show();
        } else {
            Uri number = Uri.parse("sms:" + receivingMember.getPhoneNumber());
            Intent callIntent = new Intent(Intent.ACTION_VIEW, number);
            myActivity.startActivity(callIntent);
        }
    }

    public void phoneCancel() {

    }

    public void setMyActivity(Activity activity) {
        myActivity = activity;
    }

    public void setReceivingMember(Member member) {
        receivingMember = member;
    }
}
