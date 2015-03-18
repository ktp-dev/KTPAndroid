package kappathetapi.ktp.classes.eventhandlers;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import kappathetapi.ktp.classes.Member;
import kappathetapi.ktp.dialogs.EmailDialogFragment;

/**
 * Created by sjdallst on 3/8/2015.
 */
public class EmailEventHandler {
    private Activity myActivity;
    private Member receivingMember;

    //Set the handler's activity, and set the member to be receiving the email (receivingMember)
    public static EmailEventHandler newInstance(Activity activity, Member lastClickedMember) {
        EmailEventHandler emailEventHandler = new EmailEventHandler();
        emailEventHandler.setMyActivity(activity);
        emailEventHandler.setReceivingMember(lastClickedMember);
        return emailEventHandler;
    }
    public void handleEvent(View view) {
        showDialog();
    }

    //Displays a dialog asking if the user wants to send an email (leave the app)
    void showDialog() {
        String name = receivingMember.getFirstName();
        String eAddress = receivingMember.getEmail();
        DialogFragment newFragment = EmailDialogFragment.newInstance(eAddress, name, this);
        newFragment.show(myActivity.getFragmentManager(), "dialog");
    }

    //Creates and sends an intent to open an email application
    //Allows user to choose application
    public void doEmail() {
        if(receivingMember.getEmail() == null ||
                receivingMember.getEmail().compareTo("") == 0) {
            Toast.makeText(myActivity.getApplication(), "Email not set", Toast.LENGTH_LONG).show();
        } else {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{receivingMember.getEmail()});
            try {
                myActivity.startActivity(Intent.createChooser(i, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(myActivity, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Called when pressing cancel button in dialog
    //Does nothing because dialog will automatically close.
    public void emailCancel() {

    }

    public void setMyActivity(Activity activity) {
        myActivity = activity;
    }

    public void setReceivingMember(Member member) {
        receivingMember = member;
    }
}
