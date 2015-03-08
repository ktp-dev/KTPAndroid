package kappathetapi.ktp.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import kappathetapi.ktp.R;
import kappathetapi.ktp.classes.eventhandlers.EmailEventHandler;

/**
 * Created by sjdallst on 3/8/2015.
 */
public class EmailDialogFragment extends DialogFragment {
    private EmailEventHandler emailEventHandler;

    public static EmailDialogFragment newInstance(String eAddress, String name,
                                                  EmailEventHandler emailEventHandler) {
        EmailDialogFragment frag = new EmailDialogFragment();
        frag.setEmailEventHandler(emailEventHandler);
        Bundle args = new Bundle();
        args.putString("eAddress", eAddress);
        args.putString("name", name);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String eAddress = getArguments().getString("eAddress");
        String name = getArguments().getString("name");
        CharSequence call = getString(R.string.call);

        return new AlertDialog.Builder(getActivity())
                .setTitle("EMAIL? HELL YEAH!")
                .setPositiveButton(R.string.email,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                emailEventHandler.doEmail();
                            }
                        }
                )
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                emailEventHandler.emailCancel();
                            }
                        }
                )
                .create();
    }

    public void setEmailEventHandler(EmailEventHandler emailEventHandler) {
        this.emailEventHandler = emailEventHandler;
    }
}
