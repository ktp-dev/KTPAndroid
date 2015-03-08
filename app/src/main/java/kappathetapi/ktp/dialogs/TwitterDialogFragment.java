package kappathetapi.ktp.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import kappathetapi.ktp.R;
import kappathetapi.ktp.classes.eventhandlers.TwitterEventHandler;

/**
 * Created by sjdallst on 3/8/2015.
 */
public class TwitterDialogFragment extends DialogFragment{

    private TwitterEventHandler twitterEventHandler;

    public static TwitterDialogFragment newInstance(String username, String name,
                                                  TwitterEventHandler twitterEventHandler) {
        TwitterDialogFragment frag = new TwitterDialogFragment();
        frag.setTwitterEventHandler(twitterEventHandler);
        Bundle args = new Bundle();
        args.putString("username", username);
        args.putString("name", name);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String username = getArguments().getString("username");
        String name = getArguments().getString("name");

        return new AlertDialog.Builder(getActivity())
                .setTitle("So, you like da tweets?")
                .setPositiveButton(R.string.twitter,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                twitterEventHandler.doTwitter();
                            }
                        }
                )
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                twitterEventHandler.twitterCancel();
                            }
                        }
                )
                .create();
    }

    public void setTwitterEventHandler(TwitterEventHandler twitterEventHandler) {
        this.twitterEventHandler = twitterEventHandler;
    }
}
