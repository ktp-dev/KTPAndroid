package kappathetapi.ktp.classes.eventhandlers;

import android.app.Activity;
import android.app.DialogFragment;

import kappathetapi.ktp.classes.Member;
import kappathetapi.ktp.dialogs.DeleteDialogFragment;

/**
 * Created by sjdallst on 5/7/2015.
 */
public class DeleteEventHandler {
    private Activity mActivity;
    private Member member;
    private boolean deleted;

    public static DeleteEventHandler newInstance(Activity activity, Member member) {
        DeleteEventHandler deleteEventHandler = new DeleteEventHandler();
        deleteEventHandler.setActivity(activity);
        deleteEventHandler.setMember(member);
        return deleteEventHandler;
    }

    private void setActivity(Activity activity) {
        mActivity = activity;
    }

    private void setMember(Member member) {
        this.member = member;
    }

    public boolean handleDelete() {
        showDialog();
        return deleted;
    }

    private void showDialog() {
        String firstName = member.getFirstName();
        String lastName = member.getLastName();
        DialogFragment newFragment = DeleteDialogFragment.newInstance(firstName, lastName, this);
        newFragment.show(mActivity.getFragmentManager(), "dialog");
    }

    public void doDelete() {
        member.delete(mActivity);
        deleted = true;
    }

    public void deleteCancel() {
        deleted = false;
    }
}
