package kappathetapi.ktp.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import kappathetapi.ktp.R;
import kappathetapi.ktp.classes.eventhandlers.DeleteEventHandler;

/**
 * Created by sjdallst on 5/7/2015.
 */
public class DeleteDialogFragment extends DialogFragment{
    private DeleteEventHandler deleteEventHandler;

    public static DeleteDialogFragment newInstance(String firstName, String lastName,
                                                  DeleteEventHandler deleteEventHandler) {
        DeleteDialogFragment frag = new DeleteDialogFragment();
        frag.setDeleteEventHandler(deleteEventHandler);
        Bundle args = new Bundle();
        args.putString("firstName", firstName);
        args.putString("lastName", lastName);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String firstName = getArguments().getString("firstName");
        String lastName = getArguments().getString("lastName");
        CharSequence call = getString(R.string.call);

        return new AlertDialog.Builder(getActivity())
                .setTitle("Delete " + firstName + " " + lastName + "?")
                .setPositiveButton(R.string.yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                deleteEventHandler.doDelete();
                            }
                        }
                )
                .setNegativeButton(R.string.no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                deleteEventHandler.deleteCancel();
                            }
                        }
                )
                .create();
    }

    public void setDeleteEventHandler(DeleteEventHandler deleteEventHandler) {
        this.deleteEventHandler = deleteEventHandler;
    }
}
