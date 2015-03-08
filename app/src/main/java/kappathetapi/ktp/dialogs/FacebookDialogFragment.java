package kappathetapi.ktp.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import kappathetapi.ktp.R;
import kappathetapi.ktp.classes.eventhandlers.FacebookEventHandler;

/**
 * Created by sjdallst on 3/8/2015.
 */
public class FacebookDialogFragment extends DialogFragment{
    private FacebookEventHandler facebookEventHandler;

    public static FacebookDialogFragment newInstance(String username, String name,
                                                    FacebookEventHandler facebookEventHandler) {
        FacebookDialogFragment frag = new FacebookDialogFragment();
        frag.setFacebookEventHandler(facebookEventHandler);
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
                .setTitle("Facebook is for old people?")
                .setPositiveButton(R.string.facebook,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                facebookEventHandler.doFacebook();
                            }
                        }
                )
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                facebookEventHandler.facebookCancel();
                            }
                        }
                )
                .create();
    }

    public void setFacebookEventHandler(FacebookEventHandler facebookEventHandler) {
        this.facebookEventHandler = facebookEventHandler;
    }
}
