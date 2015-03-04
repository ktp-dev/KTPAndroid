package kappathetapi.ktp.dialogs;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Dialog;

import kappathetapi.ktp.activities.HomePageActivity;
import kappathetapi.ktp.R;

public class PhoneDialogFragment extends DialogFragment {

    public static PhoneDialogFragment newInstance(String number, String name) {
        PhoneDialogFragment frag = new PhoneDialogFragment();
        Bundle args = new Bundle();
        args.putString("phoneNumber", number);
        args.putString("name", name);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String phoneNumber = getArguments().getString("phoneNumber");
        String name = getArguments().getString("name");
        CharSequence call = getString(R.string.call);

        return new AlertDialog.Builder(getActivity())
                .setTitle("Whata do?")
                .setPositiveButton(R.string.call,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((HomePageActivity)getActivity()).doCall();
                            }
                        }
                )
                .setNeutralButton(R.string.text,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((HomePageActivity)getActivity()).doText();
                            }
                        }
                )
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((HomePageActivity)getActivity()).phoneCancel();
                            }
                        }
                )
                .create();
    }

}
