package kappathetapi.ktp.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import kappathetapi.ktp.R;
import kappathetapi.ktp.classes.interfaces.PhotoGetter;

/**
 * Created by sjdallst on 5/12/2015.
 */
public class PhotoDialogFragment extends DialogFragment {
    private PhotoGetter getter;

    public static PhotoDialogFragment newInstance(PhotoGetter getter) {
        PhotoDialogFragment frag = new PhotoDialogFragment();
        frag.setPhotoGetter(getter);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return new AlertDialog.Builder(getActivity())
                .setTitle("How would you like to get your photo?")
                .setPositiveButton("Camera",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                getter.getFromCamera();
                            }
                        }
                )
                .setNeutralButton("Phone",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getter.getFromPhone();
                            }
                        })
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        }
                )
                .create();
    }

    private void setPhotoGetter(PhotoGetter getter) {
        this.getter = getter;
    }
}
